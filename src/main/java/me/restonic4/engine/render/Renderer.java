package me.restonic4.engine.render;

import me.restonic4.engine.object.GameObject;
import me.restonic4.engine.object.components.ModelRendererComponent;
import me.restonic4.engine.util.debug.Logger;

import java.util.ArrayList;
import java.util.List;

public class Renderer {
    private final int MAX_BATCH_SIZE = 1000;
    private List<RenderBatch> batches;

    // This is just a stat
    private int drawCallsConsumed = 0;
    private int dirtyModifiedTotal = 0;

    public Renderer() {
        this.batches = new ArrayList<>();
    }

    public void add(GameObject gameObject) {
        ModelRendererComponent modelRenderer = gameObject.getComponent(ModelRendererComponent.class);
        if (modelRenderer != null) {
            add(modelRenderer);
        }
    }

    private void add(ModelRendererComponent modelRenderer) {
        boolean added = false;
        for (RenderBatch batch : this.batches) {
            if (batch.hasRoom()) {
                batch.addSprite(modelRenderer);
                added = true;
                break;
            }
        }

        if (!added) {
            RenderBatch newBatch = new RenderBatch(MAX_BATCH_SIZE);
            newBatch.start();
            this.batches.add(newBatch);
            newBatch.addSprite(modelRenderer);
        }
    }

    public void render() {
        // Stats
        drawCallsConsumed = 0;
        dirtyModifiedTotal = 0;

        for (RenderBatch batch : this.batches) {
            batch.render();

            drawCallsConsumed++;
            dirtyModifiedTotal += batch.getDirtyModified();
        }
    }

    public int getDrawCalls() {
        return this.drawCallsConsumed;
    }

    public int getDirtyModified() {
        return dirtyModifiedTotal;
    }
}
