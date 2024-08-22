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

    private final int INDICES_PER_QUAD = 6;

    private List<ModelRendererComponent> models;
    private boolean isStatic;
    private float[] vertices;

    private int vaoID, vboID;
    private int maxBatchSize;
    private Shader shader;

    // This is just a stat
    private int dirtyModified = 0;

    public RenderBatch(int maxBatchSize, boolean isStatic) {
        shader = new Shader("shaders/default.glsl");
        shader.compile();

        this.models = new ArrayList<>();
        this.maxBatchSize = maxBatchSize;

        // 4 vertices quads
        vertices = new float[maxBatchSize * VERTEX_SIZE];

        this.isStatic = isStatic;
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
        int eboID = glGenBuffers();
        int[] indices = generateIndices();
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

    public int getCurrentVertexAmount() {
        return this.vertices.length / VERTEX_SIZE;
    }

    public int getModelVertexOffset(ModelRendererComponent modelRenderer) {
        int offset = 0;

        for (ModelRendererComponent mrc : models) {
            if (mrc == modelRenderer) {
                break;
            }

            offset += mrc.getMesh().getVertices().length * VERTEX_SIZE;
        }

        return offset;
    }

    public int getModelIndex(ModelRendererComponent modelRenderer) {
        return models.lastIndexOf(modelRenderer);
    }

    public AddFailureTypes addModel(ModelRendererComponent modelRenderer) {
        if ((this.isStatic() && !modelRenderer.gameObject.isStatic()) || (!this.isStatic() && modelRenderer.gameObject.isStatic())) {
            return AddFailureTypes.WRONG_TYPE;
        }

        if (getCurrentVertexAmount() + modelRenderer.getMesh().getVertices().length > this.maxBatchSize) {
            return AddFailureTypes.FULL;
        }

        // Add the model to the list
        this.models.add(modelRenderer);

        // Add properties to local vertices array
        loadVertexProperties(modelRenderer);

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

        // Use shader
        shader.use();
        shader.uploadMat4f("uProjection", scene.getCamera().getProjectionMatrix());
        shader.uploadMat4f("uView", scene.getCamera().getViewMatrix());

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, this.numModels * INDICES_PER_QUAD, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        shader.detach();
    }

    private void loadVertexProperties(ModelRendererComponent modelRenderer) {
        int offset = getModelVertexOffset(modelRenderer);

        Vector3f[] vertexPositions = modelRenderer.getMesh().getVertices();
        Vector4f color = modelRenderer.getMesh().getColor();

        for (int i = 0; i < 4; i++) {
            Vector3f currentPos = vertexPositions[i];

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

            // Load color
            vertices[offset + 3] = color.x;
            vertices[offset + 4] = color.y;
            vertices[offset + 5] = color.z;
            vertices[offset + 6] = color.w;

            offset += VERTEX_SIZE;
        }
    }

    private int[] generateIndices() {
        // TODO: Support custom models instead of just quads

        // 6 indices per quad (3 per triangle)
        int[] elements = new int[6 * maxBatchSize];

        for (int i=0; i < maxBatchSize; i++) {
            loadElementIndices(elements, i);
        }

        return elements;
    }

    private void loadElementIndices(int[] elements, int index) {
        // TODO: Support custom models instead of just quads

        int offsetArrayIndex = 6 * index;
        int offset = 4 * index;

        // 3, 2, 0, 0, 2, 1        7, 6, 4, 4, 6, 5
        // Triangle 1
        elements[offsetArrayIndex] = offset + 3;
        elements[offsetArrayIndex + 1] = offset + 2;
        elements[offsetArrayIndex + 2] = offset + 0;

        // Triangle 2
        elements[offsetArrayIndex + 3] = offset + 0;
        elements[offsetArrayIndex + 4] = offset + 2;
        elements[offsetArrayIndex + 5] = offset + 1;
    }

    public boolean hasRoom() {
        return getCurrentVertexAmount() < maxBatchSize;
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
