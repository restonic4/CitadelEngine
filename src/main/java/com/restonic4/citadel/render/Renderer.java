package com.restonic4.citadel.render;

import com.restonic4.ClientSide;
import com.restonic4.citadel.exceptions.RenderException;
import com.restonic4.citadel.render.cameras.Camera;
import com.restonic4.citadel.world.Scene;
import com.restonic4.citadel.world.object.GameObject;
import com.restonic4.citadel.world.object.components.ModelRendererComponent;
import com.restonic4.citadel.util.CitadelConstants;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

@ClientSide
public class Renderer {
    private List<RenderBatch> staticBatches, dynamicBatches;

    private Scene scene;

    // This is just a stat
    private int drawCallsConsumed = 0;
    private int drawCallsSkipped = 0;
    private int dirtyModifiedTotal = 0;
    private int dirtySkippedTotal = 0;

    public Renderer(Scene scene) {
        this.scene = scene;
        this.staticBatches = new ArrayList<>();
        this.dynamicBatches = new ArrayList<>();
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
        // Stats
        drawCallsConsumed = 0;
        drawCallsSkipped = 0;
        dirtyModifiedTotal = 0;
        dirtySkippedTotal = 0;

        if (scene == null) {
            return;
        }

        Camera camera = scene.getCamera();

        // Updating the frustum culling

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

        glClearColor(0.267f, 0.741f, 1, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        // Render batches
        renderShadowBatches(this.staticBatches);
        renderShadowBatches(this.dynamicBatches);

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        renderMainBatches(this.staticBatches);
        renderMainBatches(this.dynamicBatches);
    }

    private <T extends RenderBatch> void renderShadowBatches(List<T> batches) {
        for (int i = 0; i < batches.size(); i++) {
            RenderBatch batch = batches.get(i);

            if (batch.shouldBeSkipped()) {
                drawCallsSkipped++;
                continue;
            }

            batch.update();

            batch.renderShadowMap();

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

            batch.render();

            drawCallsConsumed++;
            dirtyModifiedTotal += batch.getDirtyModified();
            dirtySkippedTotal += batch.getDirtySkipped();
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

