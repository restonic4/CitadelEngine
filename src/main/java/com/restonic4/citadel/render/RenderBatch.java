package com.restonic4.citadel.render;

import com.restonic4.ClientSide;
import com.restonic4.citadel.registries.built_in.managers.FrameBuffers;
import com.restonic4.citadel.registries.built_in.managers.Shaders;
import com.restonic4.citadel.util.ArrayHelper;
import com.restonic4.citadel.util.CitadelConstants;
import com.restonic4.citadel.world.Scene;
import com.restonic4.citadel.world.SceneManager;
import com.restonic4.citadel.world.object.GameObject;
import com.restonic4.citadel.world.object.Transform;
import com.restonic4.citadel.world.object.components.ModelRendererComponent;
import com.restonic4.citadel.util.debug.DebugManager;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

@ClientSide
public class RenderBatch {
    //         Pos                       Color                   UV      Texture handle id        Normals          Reflectivity
    // float, float, float    float, float, float, float    float, float    float, float    float, float, float    float, float

    private final int POS_SIZE = 3;
    private final int COLOR_SIZE = 4;
    private final int UV_SIZE = 2;
    private final int TEXTURE_HANDLER_ID_SIZE = 2;
    private final int NORMALS_SIZE = 3;
    private final int REFLECTIVITY_SIZE = 2;

    private final int POS_OFFSET = 0;
    private final int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
    private final int UV_OFFSET = COLOR_OFFSET + COLOR_SIZE * Float.BYTES;
    private final int TEXTURE_HANDLER_ID_OFFSET = UV_OFFSET + UV_SIZE * Float.BYTES;
    private final int NORMALS_OFFSET = TEXTURE_HANDLER_ID_OFFSET + TEXTURE_HANDLER_ID_SIZE * Float.BYTES;
    private final int REFLECTIVITY_OFFSET = NORMALS_OFFSET + NORMALS_SIZE * Float.BYTES;

    // Change this in case adding new data to the VBO
    private final int VERTEX_SIZE = POS_SIZE + COLOR_SIZE + UV_SIZE + TEXTURE_HANDLER_ID_SIZE + NORMALS_SIZE + REFLECTIVITY_SIZE;
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private List<ModelRendererComponent> models;
    private ArrayList<CascadeShadow> cascadeShadows;

    private float[] vertices;
    private int[] indices;
    private int currentVertexCount = 0;

    private int vaoID, vboID, eboID;
    private int maxBatchSize; // Max vertex allowed per batch
    private boolean isStatic;

    // This is just a stat
    private int dirtyModified = 0;
    private int dirtySkipped = 0;
    private boolean areIndicesDirty;

    // This vec3 is used to avoid huge memory leaks, this used to be inside the for-loop
    Vector3f tempCacheVec = new Vector3f();

    public RenderBatch(int maxBatchSize, boolean isStatic) {
        this.models = new ArrayList<>();

        this.maxBatchSize = maxBatchSize;
        this.isStatic = isStatic;
        this.areIndicesDirty = true;

        this.vertices = new float[maxBatchSize * VERTEX_SIZE];

        this.cascadeShadows = new ArrayList<>();
        for (int i = 0; i < CascadeShadow.SHADOW_MAP_CASCADE_COUNT; i++) {
            CascadeShadow cascadeShadow = new CascadeShadow();
            cascadeShadows.add(cascadeShadow);
        }
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

        glVertexAttribPointer(2, UV_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, UV_OFFSET);
        glEnableVertexAttribArray(2);

        glVertexAttribPointer(3, TEXTURE_HANDLER_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEXTURE_HANDLER_ID_OFFSET);
        glEnableVertexAttribArray(3);

        glVertexAttribPointer(4, NORMALS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, NORMALS_OFFSET);
        glEnableVertexAttribArray(4);

        glVertexAttribPointer(5, REFLECTIVITY_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, REFLECTIVITY_OFFSET);
        glEnableVertexAttribArray(5);
    }

    public int getModelVertexOffset(ModelRendererComponent modelRenderer) {
        int offset = 0;

        // Traditional for-loop, because GC explodes so badly lol
        for (int i = 0; i < models.size(); i++) {
            ModelRendererComponent mrc = models.get(i);

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

        // If max vertex
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

        // Traditional for-loop, because GC explodes so badly lol
        for (int i = 0; i < models.size(); i++) {
            ModelRendererComponent modelRendererComponent = models.get(i);
            GameObject gameObject = modelRendererComponent.gameObject;

            Vector3f cleanPos = gameObject.transform.getCleanPosition();
            if (!gameObject.isInsideFrustum() && !FrustumCullingFilter.getInstance().insideFrustum(cleanPos.x, cleanPos.y, cleanPos.z, CitadelConstants.FRUSTUM_BOUNDING_SPHERE_RADIUS)) {
                if (gameObject.transform.isDirty()) {
                    dirtySkipped++;
                }

                continue;
            }

            if (gameObject.transform.isDirty()) {
                loadVertexProperties(modelRendererComponent);

                gameObject.transform.setClean();

                dirtyModified++; // Stat
            }
        }
    }

    public void update() {
        dirtyModified = 0; // Stat
        dirtySkipped = 0; // Stat

        updateDirtyModels();

        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

        updateIndices();
    }

    public void render() {
        Scene scene = SceneManager.getCurrentScene();

        Shaders.MAIN.use();

        UniformsMap mainShaderUniformMap = Shaders.MAIN.getUniformsMap();

        // Shadows

        int start = 2;
        for (int i = 0; i < CascadeShadow.SHADOW_MAP_CASCADE_COUNT; i++) { // TODO: Use string builder helper for the strings
            mainShaderUniformMap.setUniform("shadowMap[" + i + "]", start + i);
            CascadeShadow cascadeShadow = cascadeShadows.get(i);
            mainShaderUniformMap.setUniform("cascadeShadows[" + i + "]" + ".projViewMatrix", cascadeShadow.getProjViewMatrix());
            mainShaderUniformMap.setUniform("cascadeShadows[" + i + "]" + ".splitDistance", cascadeShadow.getSplitDistance());
        }

        FrameBuffers.SHADOWS.bindTextures(GL_TEXTURE2);

        // Main

        mainShaderUniformMap.setUniform("uProjection", scene.getCamera().getProjectionMatrix());
        mainShaderUniformMap.setUniform("uView", scene.getCamera().getViewMatrix());
        // TODO: I think this should not be updated every frame (Lights)
        mainShaderUniformMap.setUniform("uLightPos", ArrayHelper.nullifyWithFixedSize_GC_Optimized(scene.getLightPositions(), CitadelConstants.MAX_LIGHTS));
        mainShaderUniformMap.setUniform("uLightAmount", scene.getLightsAmount());
        mainShaderUniformMap.setUniform("uLightColors", ArrayHelper.nullifyWithFixedSize_GC_Optimized(scene.getLightColors(), CitadelConstants.MAX_LIGHTS));
        mainShaderUniformMap.setUniform("uLightAttenuationFactors", ArrayHelper.nullifyWithFixedSize_GC_Optimized(scene.getLightAttenuationFactors(), CitadelConstants.MAX_LIGHTS));

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        glEnableVertexAttribArray(3);
        glEnableVertexAttribArray(4);
        glEnableVertexAttribArray(5);

        // Wireframe mode
        if (DebugManager.isWireframeMode()) {
            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        }

        if (!DebugManager.isBatchRenderingDisabled()) {
            glDrawElements(GL_TRIANGLES, this.indices.length, GL_UNSIGNED_INT, 0);
        }

        // Debug
        if (DebugManager.isVerticesMode()) {
            glDrawArrays(GL_POINTS, 0, this.currentVertexCount);
        }

        // Cleanup
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glDisableVertexAttribArray(3);
        glDisableVertexAttribArray(4);
        glDisableVertexAttribArray(5);

        glBindVertexArray(0);

        Shaders.MAIN.detach();
    }

    public void renderShadowMap() {
        CascadeShadow.updateCascadeShadows(cascadeShadows, SceneManager.getCurrentScene());

        /*FrameBuffers.SHADOWS.bind();
        Shaders.SHADOWS.use();*/

        for (int i = 0; i < CascadeShadow.SHADOW_MAP_CASCADE_COUNT; i++) {
            glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, FrameBuffers.SHADOWS.getDepthMapTexture().getIds()[i], 0);
            glClear(GL_DEPTH_BUFFER_BIT);

            CascadeShadow shadowCascade = cascadeShadows.get(i);
            Shaders.SHADOWS.getUniformsMap().setUniform("uProjViewMatrix", shadowCascade.getProjViewMatrix());

            glBindVertexArray(vaoID);
            glEnableVertexAttribArray(0);
            glDrawElements(GL_TRIANGLES, this.indices.length, GL_UNSIGNED_INT, 0);
        }

        /*Shaders.SHADOWS.detach();
        FrameBufferManager.unbindCurrentFrameBuffer();*/
    }

    // TODO: Optimize this, CPU usage and Memory
    private void loadVertexProperties(ModelRendererComponent modelRenderer) {
        int offset = getModelVertexOffset(modelRenderer);

        Vector3f[] vertexPositions = modelRenderer.getMesh().getVertices();
        Vector4f[] vertexColors = modelRenderer.getMesh().getVerticesColors();
        Vector2f[] UVs = modelRenderer.getMesh().getUVs();
        Vector4f tint = modelRenderer.getMesh().getTint();
        Vector3f[] normals = modelRenderer.getMesh().getNormals();

        long textureHandleId = 0L;
        if (modelRenderer.getMesh().getTexture() != null) {
            textureHandleId = modelRenderer.getMesh().getTexture().getBindlessHandle();
        }

        float textureHandleLow = Float.intBitsToFloat((int)(textureHandleId & 0xFFFFFFFF));
        float textureHandleHigh = Float.intBitsToFloat((int)(textureHandleId >>> 32));

        for (int i = 0; i < vertexPositions.length; i++) {
            // Vertex transformation
            transformTempCacheVec3(modelRenderer.gameObject.transform, vertexPositions[i]);

            // Load position into the vertices array
            vertices[offset] = tempCacheVec.x;
            vertices[offset + 1] = tempCacheVec.y;
            vertices[offset + 2] = tempCacheVec.z;

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

            // Texture UV
            vertices[offset + 7] = (UVs[i] != null) ? UVs[i].x : 0;
            vertices[offset + 8] = (UVs[i] != null) ? UVs[i].y : 0;

            // Texture handle id
            vertices[offset + 9] = textureHandleLow;
            vertices[offset + 10] = textureHandleHigh;

            // Normals
            vertices[offset + 11] = normals[i].x;
            vertices[offset + 12] = normals[i].y;
            vertices[offset + 13] = normals[i].z;

            // Reflectivity
            vertices[offset + 14] = modelRenderer.getMaterial().getReflectance();
            vertices[offset + 15] = modelRenderer.getMaterial().getShineDamper();

            offset += VERTEX_SIZE;
        }
    }

    private void transformTempCacheVec3(Transform transform, Vector3f data) {
        tempCacheVec.set(data);

        // Apply scale
        tempCacheVec.mul(transform.getScale());

        // Apply rotation
        tempCacheVec.rotate(transform.getRotation()); // TODO: Optimize, CPU

        // Apply position
        tempCacheVec.add(transform.getPosition());
    }

    List<Integer> cacheIndicesList = new ArrayList<>();
    private int[] updateIndices() {
        if (!this.areIndicesDirty) {
            return this.indices;
        }

        this.areIndicesDirty = false;

        cacheIndicesList.clear();

        int vertexOffset = 0;

        // Traditional for-loop, because GC explodes so badly lol
        for (int i = 0; i < models.size(); i++) {
            ModelRendererComponent model = models.get(i);

            int[] modelIndices = model.getMesh().getIndices();

            for (int index : modelIndices) {
                cacheIndicesList.add(index + vertexOffset);
            }

            vertexOffset += model.getMesh().getVertices().length;
        }

        // TODO: toArray() could be improved and used another method instead on instantiating a new array.
        this.indices = cacheIndicesList.stream().mapToInt(Integer::intValue).toArray();

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, this.indices, GL_DYNAMIC_DRAW);

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

    public int getByteSize() {
        return VERTEX_SIZE_BYTES * maxBatchSize;
    }

    public int getVerticesAmount() {
        return this.vertices.length;
    }

    public List<CascadeShadow> getCascadeShadows() {
        return cascadeShadows;
    }

    public boolean isOutsideFrustum() {
        // Traditional for-loop, because GC explodes so badly lol
        for (int i = 0; i < this.models.size(); i++) {
            if (this.models.get(i).gameObject.isInsideFrustum()) {
                return false;
            }
        }

        return true;
    }

    public boolean shouldBeSkipped() {
        return isOutsideFrustum();
    }

    public void cleanup() {
        for (ModelRendererComponent modelRendererComponent : models) {
            Texture texture = modelRendererComponent.getMesh().getTexture();

            if (texture != null) {
                texture.cleanup();
            }
        }
    }

    @ClientSide
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
