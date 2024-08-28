package me.restonic4.citadel.render;

import me.restonic4.citadel.exceptions.RenderException;
import me.restonic4.citadel.world.Scene;
import me.restonic4.citadel.world.SceneManager;
import me.restonic4.citadel.world.object.GameObject;
import me.restonic4.citadel.world.object.components.ModelRendererComponent;
import me.restonic4.shared.SharedConstants;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {
    private List<RenderBatch> staticBatches, dynamicBatches;

    // This is just a stat
    private int drawCallsConsumed = 0;
    private int dirtyModifiedTotal = 0;
    private int dirtySkippedTotal = 0;

    public Renderer() {
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
        int maxBatchSize = modelRenderer.gameObject.isStatic() ? SharedConstants.MAX_STATIC_BATCH_VERTEX_SIZE : SharedConstants.MAX_DYNAMIC_BATCH_VERTEX_SIZE;

        // Trying adding it to an existing one
        for (RenderBatch batch : batches) {
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
        dirtyModifiedTotal = 0;
        dirtySkippedTotal = 0;

        //Background, blue :D
        glClearColor(0.267f, 0.741f, 1, 1.0f);

        // Clear the color and depth buffers
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        Scene scene = SceneManager.getCurrentScene();
        if (scene == null) {
            return;
        }

        // Updating the frustum culling
        FrustumCullingFilter.getInstance().updateFrustum(scene.getCamera().projectionMatrix, scene.getCamera().viewMatrix);
        FrustumCullingFilter.getInstance().filter(scene.getGameObjects(), 1);

        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);

        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LESS);

        // Render batches
        renderBatches(this.staticBatches);
        renderBatches(this.dynamicBatches);
    }

    private <T extends RenderBatch> void renderBatches(List<T> batches) {
        for (RenderBatch batch : batches) {
            batch.render();

            drawCallsConsumed++;
            dirtyModifiedTotal += batch.getDirtyModified();
            dirtySkippedTotal += batch.getDirtySkipped();
        }
    }

    public int getDrawCalls() {
        return this.drawCallsConsumed;
    }

    public int getDirtyModified() {
        return dirtyModifiedTotal;
    }

    public int getDirtySkipped() {
        return dirtySkippedTotal;
    }
}


