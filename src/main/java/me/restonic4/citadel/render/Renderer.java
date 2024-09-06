package me.restonic4.citadel.render;

import me.restonic4.citadel.exceptions.RenderException;
import me.restonic4.citadel.util.debug.DebugManager;
import me.restonic4.citadel.world.Scene;
import me.restonic4.citadel.world.SceneManager;
import me.restonic4.citadel.world.object.GameObject;
import me.restonic4.citadel.world.object.components.ModelRendererComponent;
import me.restonic4.citadel.util.CitadelConstants;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {
    private List<RenderBatch> staticBatches, dynamicBatches;

    private static Shader currentShader;

    // This is just a stat
    private int drawCallsConsumed = 0;
    private int drawCallsSkipped = 0;
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
        int maxBatchSize = modelRenderer.gameObject.isStatic() ? CitadelConstants.MAX_STATIC_BATCH_VERTEX_SIZE : CitadelConstants.MAX_DYNAMIC_BATCH_VERTEX_SIZE;

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
        drawCallsSkipped = 0;
        dirtyModifiedTotal = 0;
        dirtySkippedTotal = 0;

        currentShader.use();

        //Background, blue :D
        glClearColor(0.267f, 0.741f, 1, 1.0f);

        // Clear the color and depth buffers
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        Scene scene = SceneManager.getCurrentScene();
        if (scene == null) {
            return;
        }

        Camera camera = scene.getCamera();

        // Updating the frustum culling

        Matrix4f projection = camera.projectionMatrix;
        Matrix4f view = camera.viewMatrix;

        if (camera.isSimulated()) {
            projection = camera.fakeProjectionMatrix;
            view = camera.fakeViewMatrix;

            Vector3f[] vertex = FrustumRenderer.extractFrustumCorners(projection, view);
            FrustumRenderer.renderFrustum(vertex);
        }

        FrustumCullingFilter.getInstance().updateFrustum(projection, view);
        FrustumCullingFilter.getInstance().filter(scene.getGameObjects(), 1);

        // Render batches
        renderBatches(this.staticBatches);
        renderBatches(this.dynamicBatches);
    }

    private <T extends RenderBatch> void renderBatches(List<T> batches) {
        for (RenderBatch batch : batches) {
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

    public static void setShader(Shader shader) {
        currentShader = shader;
    }

    public static Shader getCurrentShader() {
        return currentShader;
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

        for (RenderBatch renderBatch : staticBatches) {
            bytes += renderBatch.getByteSize();
        }

        for (RenderBatch renderBatch : dynamicBatches) {
            bytes += renderBatch.getByteSize();
        }

        return bytes;
    }

    public void cleanup() {
        for (RenderBatch batch : this.staticBatches) {
            batch.cleanup();
        }

        for (RenderBatch batch : this.dynamicBatches) {
            batch.cleanup();
        }
    }
}


