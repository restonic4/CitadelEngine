package com.restonic4.citadel.render;

import com.restonic4.ClientSide;
import com.restonic4.citadel.core.CitadelLauncher;
import com.restonic4.citadel.core.CitadelSettings;
import com.restonic4.citadel.core.editor.LevelEditor;
import com.restonic4.citadel.exceptions.RenderException;
import com.restonic4.citadel.registries.built_in.managers.FrameBuffers;
import com.restonic4.citadel.registries.built_in.managers.Shaders;
import com.restonic4.citadel.registries.built_in.types.FrameBuffer;
import com.restonic4.citadel.render.shadows.CascadeShadow;
import com.restonic4.citadel.world.Scene;
import com.restonic4.citadel.world.SceneManager;
import com.restonic4.citadel.world.object.GameObject;
import com.restonic4.citadel.world.object.components.CameraComponent;
import com.restonic4.citadel.world.object.components.ModelRendererComponent;
import com.restonic4.citadel.util.CitadelConstants;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

@ClientSide
public class Renderer {
    private List<RenderBatch> staticBatches, dynamicBatches;

    private Scene scene;
    private CitadelSettings citadelSettings;

    private CameraComponent cameraBeingUsed;

    // This is just stats
    private int drawCallsConsumed = 0;
    private int drawCallsSkipped = 0;
    private int dirtyModifiedTotal = 0;
    private int dirtySkippedTotal = 0;

    public Renderer(Scene scene) {
        this.scene = scene;
        this.staticBatches = new ArrayList<>();
        this.dynamicBatches = new ArrayList<>();
        this.citadelSettings = CitadelLauncher.getInstance().getSettings();
    }

    public void add(GameObject gameObject) {
        ModelRendererComponent modelRenderer = gameObject.getComponent(ModelRendererComponent.class);
        if (modelRenderer != null) {
            add(modelRenderer);
        }
    }

    private void add(ModelRendererComponent modelRenderer) {
        List<RenderBatch> batches = (modelRenderer.gameObject.isStatic() ? this.staticBatches : this.dynamicBatches);
        int maxBatchSize = modelRenderer.gameObject.isStatic() ? CitadelConstants.MAX_STATIC_BATCH_VERTEX_SIZE : CitadelConstants.MAX_DYNAMIC_BATCH_VERTEX_SIZE;

        // Trying adding it to an existing one
        for (int i = 0; i < batches.size(); i++) {
            RenderBatch batch = batches.get(i);

            if (batch.hasRoom()) {
                RenderBatch.AddFailureTypes addFailureTypes = batch.addModel(modelRenderer);

                if (addFailureTypes == RenderBatch.AddFailureTypes.PASS) {
                    return;
                }
            }
        }

        // Creating a new batch
        RenderBatch newBatch = new RenderBatch(maxBatchSize, modelRenderer.gameObject.isStatic());
        newBatch.start();
        batches.add(newBatch);

        RenderBatch.AddFailureTypes addFailureTypes = newBatch.addModel(modelRenderer);

        if (addFailureTypes != RenderBatch.AddFailureTypes.PASS) {
            throw new RenderException("Failed adding a game object to a render batch (" + addFailureTypes.getMessage() + ")");
        }
    }

    public void render() {
        if (!citadelSettings.shouldGenerateBindlessTextures()) {
            return;
        }

        // Stats
        drawCallsConsumed = 0;
        drawCallsSkipped = 0;
        dirtyModifiedTotal = 0;
        dirtySkippedTotal = 0;

        if (scene == null) {
            return;
        }

        CameraComponent camera = scene.getMainCamera();

        if (camera == null) {
            if (citadelSettings.isEditorMode()) {
                FrameBuffers.GAME_VIEWPORT.bind();
            }

            renderBlack();

            FrameBufferManager.unbindCurrentFrameBuffer();

            return;
        }

        if (citadelSettings.isEditorMode()) {
            updateFrustum(LevelEditor.getHardcodedCamera());
            renderAll(FrameBuffers.SCENE_VIEWPORT);

            updateFrustum(camera);
            renderAll(FrameBuffers.GAME_VIEWPORT);
        }
        else {
            renderAll(null);
        }
    }

    private void updateFrustum(CameraComponent camera) {
        cameraBeingUsed = camera;

        Matrix4f projection = camera.getProjectionMatrix();
        Matrix4f view = camera.getViewMatrix();

        if (camera.isSimulated()) {
            projection = camera.getFakeProjectionMatrix();
            view = camera.getFakeViewMatrix();

            Vector3f[] vertex = camera.getFrustumCorners();
            //FrustumRenderer.renderFrustum(vertex);
        }

        FrustumCullingFilter.getInstance().updateFrustum(projection, view);
        FrustumCullingFilter.getInstance().filter(scene.getGameObjects(), CitadelConstants.FRUSTUM_BOUNDING_SPHERE_RADIUS);
    }

    private void renderAll(FrameBuffer frameBuffer) {
        glClearColor(0.267f, 0.741f, 1, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        renderShadowsCascades();

        if (frameBuffer != null) {
            frameBuffer.bind();
        }

        renderGeometry();
    }

    private void renderBlack() {
        glClearColor(0, 0, 0, 1);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    private void renderShadowsCascades() {
        FrameBuffers.SHADOWS.bind();
        Shaders.SHADOWS.use();

        for (int i = 0; i < CascadeShadow.SHADOW_MAP_CASCADE_COUNT; i++) {
            glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, FrameBuffers.SHADOWS.getDepthMapTexture().getIds()[i], 0);
            glClear(GL_DEPTH_BUFFER_BIT);

            renderShadowBatches(this.staticBatches, i);
            renderShadowBatches(this.dynamicBatches, i);
        }

        Shaders.SHADOWS.detach();
        FrameBufferManager.unbindCurrentFrameBuffer();
    }

    private void renderGeometry() {
        Shaders.MAIN.use();

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        renderMainBatches(this.staticBatches);
        renderMainBatches(this.dynamicBatches);

        Shaders.MAIN.detach();
        FrameBufferManager.unbindCurrentFrameBuffer();
    }

    private <T extends RenderBatch> void renderShadowBatches(List<T> batches, int cascadeIndex) {
        for (int i = 0; i < batches.size(); i++) {
            RenderBatch batch = batches.get(i);

            if (batch.shouldBeSkipped()) {
                drawCallsSkipped++;
                continue;
            }

            batch.update();

            batch.renderShadowMap(cascadeIndex);

            drawCallsConsumed++;
            dirtyModifiedTotal += batch.getDirtyModified();
            dirtySkippedTotal += batch.getDirtySkipped();
        }
    }

    private <T extends RenderBatch> void renderMainBatches(List<T> batches) {
        for (int i = 0; i < batches.size(); i++) {
            RenderBatch batch = batches.get(i);

            if (batch.shouldBeSkipped()) {
                drawCallsSkipped++;
                continue;
            }

            batch.render(cameraBeingUsed);

            drawCallsConsumed++;
        }
    }

    public int getDrawCalls() {
        return this.drawCallsConsumed;
    }

    public int getDrawCallsSkipped() {
        return this.drawCallsSkipped;
    }

    public int getDirtyModified() {
        return dirtyModifiedTotal;
    }

    public int getDirtySkipped() {
        return dirtySkippedTotal;
    }

    public int getByteSize() {
        int bytes = 0;

        for (int i = 0; i < staticBatches.size(); i++) {
            bytes += staticBatches.get(i).getByteSize();
        }

        for (int i = 0; i < dynamicBatches.size(); i++) {
            bytes += dynamicBatches.get(i).getByteSize();
        }

        return bytes;
    }

    public int getVerticesAmount() {
        int vertices = 0;

        for (int i = 0; i < staticBatches.size(); i++) {
            vertices += staticBatches.get(i).getVerticesAmount();
        }

        for (int i = 0; i < dynamicBatches.size(); i++) {
            vertices += dynamicBatches.get(i).getVerticesAmount();
        }

        return vertices;
    }

    public void cleanup() {
        for (int i = 0; i < staticBatches.size(); i++) {
            staticBatches.get(i).cleanup();
        }

        for (int i = 0; i < dynamicBatches.size(); i++) {
            dynamicBatches.get(i).cleanup();
        }
    }
}


