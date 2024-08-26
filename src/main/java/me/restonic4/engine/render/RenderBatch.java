package me.restonic4.engine.render;

import me.restonic4.engine.world.Scene;
import me.restonic4.engine.world.SceneManager;
import me.restonic4.engine.world.object.GameObject;
import me.restonic4.engine.world.object.components.ModelRendererComponent;
import me.restonic4.engine.util.debug.DebugManager;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class RenderBatch {
    // Pos                    Color
    // float, float, float    float, float, float, float

    private final int POS_SIZE = 3;
    private final int COLOR_SIZE = 4;

    private final int POS_OFFSET = 0;
    private final int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
    private final int VERTEX_SIZE = 7;
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private List<ModelRendererComponent> models;
    private boolean isStatic;
    private float[] vertices;
    private int[] indices;
    private int currentVertexCount = 0;

    private int vaoID, vboID, eboID;
    private int maxBatchSize; // Max vertices allowed per batch
    private Shader shader;

    // This is just a stat
    private int dirtyModified = 0;
    private int dirtySkipped = 0;
    private boolean areIndicesDirty;

    public RenderBatch(int maxBatchSize, boolean isStatic) {
        shader = new Shader("assets/shaders/default.glsl");
        shader.compile();

        this.models = new ArrayList<>();
        this.maxBatchSize = maxBatchSize;

        vertices = new float[maxBatchSize * VERTEX_SIZE];

        this.isStatic = isStatic;
        this.areIndicesDirty = true;
    }

    public boolean isStatic() {
        return this.isStatic;
    }

    public void start() {
        // Generate and bind a Vertex Array Object
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Allocate space for vertices
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

        // Create and upload indices buffer
        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, updateIndices(), GL_DYNAMIC_DRAW);

        // Enable the buffer attribute pointers
        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
        glEnableVertexAttribArray(1);

        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);

        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LESS);
    }

    public int getModelVertexOffset(ModelRendererComponent modelRenderer) {
        int offset = 0;

        for (ModelRendererComponent mrc : models) {
            if (mrc == modelRenderer) {
                break;
            }

            offset += mrc.getMesh().getVertices().length;
        }

        return offset * VERTEX_SIZE;
    }

    public AddFailureTypes addModel(ModelRendererComponent modelRenderer) {
        if ((this.isStatic() && !modelRenderer.gameObject.isStatic()) || (!this.isStatic() && modelRenderer.gameObject.isStatic())) {
            return AddFailureTypes.WRONG_TYPE;
        }

        int modelVertexCount = modelRenderer.getMesh().getVertices().length;

        if (currentVertexCount + modelVertexCount > this.maxBatchSize) {
            return AddFailureTypes.FULL;
        }

        // Add the model to the list
        this.models.add(modelRenderer);
        currentVertexCount += modelVertexCount;

        // Add properties to local vertices array
        loadVertexProperties(modelRenderer);
        this.areIndicesDirty = true;

        return AddFailureTypes.PASS;
    }

    public void updateDirtyModels() {
        if (isStatic()) {
            return;
        }

        for (ModelRendererComponent modelRendererComponent : models) {
            GameObject gameObject = modelRendererComponent.gameObject;

            if (!gameObject.isInsideFrustum()) {
                dirtySkipped++;
                continue;
            }

            if (gameObject.transform.isDirty()) {
                loadVertexProperties(modelRendererComponent);

                gameObject.transform.setClean();

                dirtyModified++; // Stat
            }
        }
    }

    public void render() {
        dirtyModified = 0; // Stat
        dirtySkipped = 0; // Stat

        Scene scene = SceneManager.getCurrentScene();

        updateDirtyModels();

        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

        updateIndices();

        // Use shader
        shader.use();
        shader.uploadMat4f("uProjection", scene.getCamera().getProjectionMatrix());
        shader.uploadMat4f("uView", scene.getCamera().getViewMatrix());

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        // Wireframe mode
        if (DebugManager.isWireframeMode()) {
            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        }

        //glDrawElements(GL_TRIANGLES, this.indices.length, GL_UNSIGNED_INT, 0);
        glDrawElements(GL_TRIANGLES, this.indices.length, GL_UNSIGNED_INT, 0);

        // Debug
        if (DebugManager.isVerticesMode()) {
            glDrawArrays(GL_POINTS, 0, this.currentVertexCount);
        }

        // Cleanup
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        shader.detach();
    }

    private void loadVertexProperties(ModelRendererComponent modelRenderer) {
        int offset = getModelVertexOffset(modelRenderer);

        Vector3f[] vertexPositions = modelRenderer.getMesh().getVertices();
        Vector4f[] vertexColors = modelRenderer.getMesh().getVerticesColors();
        Vector4f tint = modelRenderer.getMesh().getTint();

        for (int i = 0; i < vertexPositions.length; i++) {
            Vector3f currentPos = new Vector3f(vertexPositions[i]);

            // Apply scale
            currentPos.mul(modelRenderer.gameObject.transform.getScale());

            // Apply rotation
            currentPos.rotate(modelRenderer.gameObject.transform.getRotation());

            // Apply position
            currentPos.add(modelRenderer.gameObject.transform.getPosition());

            // Load position into the vertices array
            vertices[offset] = currentPos.x;
            vertices[offset + 1] = currentPos.y;
            vertices[offset + 2] = currentPos.z;

            // Gets the vertex color and applies the tint
            Vector4f color;
            if (vertexColors != null && i < vertexColors.length) {
                color = vertexColors[i];
            } else {
                color = tint; // Use tint if vertexColors is null or does not have color for this vertex
            }

            // Load color
            vertices[offset + 3] = color.x;
            vertices[offset + 4] = color.y;
            vertices[offset + 5] = color.z;
            vertices[offset + 6] = color.w;

            offset += VERTEX_SIZE;
        }
    }

    private int[] updateIndices() {
        if (!this.areIndicesDirty) {
            return this.indices;
        }

        this.areIndicesDirty = false;

        List<Integer> indicesList = new ArrayList<>();
        int vertexOffset = 0;

        for (ModelRendererComponent model : models) {
            int[] modelIndices = model.getMesh().getIndices();

            for (int index : modelIndices) {
                indicesList.add(index + vertexOffset);
            }

            vertexOffset += model.getMesh().getVertices().length;
        }

        this.indices = indicesList.stream().mapToInt(Integer::intValue).toArray();

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, this.indices, GL_DYNAMIC_DRAW);
        //glBufferSubData(GL_ELEMENT_ARRAY_BUFFER, 0, this.indices);

        return this.indices;
    }

    public boolean hasRoom() {
        return currentVertexCount < maxBatchSize;
    }

    public int getDirtyModified() {
        return dirtyModified;
    }

    public int getDirtySkipped() {
        return dirtySkipped;
    }

    public void pointer() {
        //Logger.log("This should be used with debug mode to check the variables and all of that");
    }

    public enum AddFailureTypes {
        WRONG_TYPE("The batch type incorrect. (Static/Dynamic)"),
        FULL("The batch is full."),
        PASS("The operation was successful.");

        private final String message;

        AddFailureTypes(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
