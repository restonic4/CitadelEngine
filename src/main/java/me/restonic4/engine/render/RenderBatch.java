package me.restonic4.engine.render;

import me.restonic4.engine.Scene;
import me.restonic4.engine.SceneManager;
import me.restonic4.engine.Window;
import me.restonic4.engine.object.GameObject;
import me.restonic4.engine.object.components.ModelRendererComponent;
import me.restonic4.engine.util.debug.Logger;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.nio.FloatBuffer;
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
    private boolean areIndicesDirty;

    public RenderBatch(int maxBatchSize, boolean isStatic) {
        shader = new Shader("shaders/default.glsl");
        shader.compile();

        this.models = new ArrayList<>();
        this.maxBatchSize = maxBatchSize;

        vertices = new float[maxBatchSize * VERTEX_SIZE];

        this.isStatic = isStatic;
        this.areIndicesDirty = false;
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
        int[] indices = updateIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_DYNAMIC_DRAW);

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

            if (gameObject.transform.isDirty()) {
                loadVertexProperties(modelRendererComponent);

                gameObject.transform.setClean();

                dirtyModified++; // Stat
            }
        }
    }

    public void render() {
        dirtyModified = 0; // Stat

        Scene scene = SceneManager.getInstance().getCurrentScene();
        if (scene == null) {
            return;
        }

        updateDirtyModels();

        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

        if (this.areIndicesDirty) {
            this.areIndicesDirty = false;

            updateIndices();
        }

        // Use shader
        shader.use();
        shader.uploadMat4f("uProjection", scene.getCamera().getProjectionMatrix());
        shader.uploadMat4f("uView", scene.getCamera().getViewMatrix());

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);

        // Debug
        glDrawArrays(GL_POINTS, 0, currentVertexCount);
        //glDrawArrays(GL_LINES, 0, currentVertexCount);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        shader.detach();
    }

    private void loadVertexProperties(ModelRendererComponent modelRenderer) {
        int offset = getModelVertexOffset(modelRenderer);

        Logger.log(offset);

        Vector3f[] vertexPositions = modelRenderer.getMesh().getVertices();
        Vector4f[] vertexColors = modelRenderer.getMesh().getVerticesColors();
        Vector4f tint = modelRenderer.getMesh().getTint();

        for (int i = 0; i < vertexPositions.length; i++) {
            Vector3f currentPos = vertexPositions[i];

            // Apply scale
            //currentPos.mul(modelRenderer.gameObject.transform.getScale());

            // Apply rotation
            //currentPos.rotate(modelRenderer.gameObject.transform.getRotation());

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
                color = tint;  // Use tint if vertexColors is null or does not have color for this vertex
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
        List<Integer> indicesList = new ArrayList<>();

        for (ModelRendererComponent model : models) {
            int[] modelIndices = model.getMesh().getIndices();
            int offset = indicesList.size();

            for (int index : modelIndices) {
                indicesList.add(index + offset);
            }
        }

        this.indices = indicesList.stream().mapToInt(Integer::intValue).toArray();

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferSubData(GL_ELEMENT_ARRAY_BUFFER, 0, this.indices);
        //glBufferData(GL_ELEMENT_ARRAY_BUFFER, this.indices, GL_DYNAMIC_DRAW);

        return this.indices;
    }

    public boolean hasRoom() {
        return currentVertexCount < maxBatchSize;
    }

    public int getDirtyModified() {
        return dirtyModified;
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
