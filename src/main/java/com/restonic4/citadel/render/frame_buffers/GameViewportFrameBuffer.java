package com.restonic4.citadel.render.frame_buffers;

import com.restonic4.citadel.registries.built_in.types.FrameBuffer;

public class GameViewportFrameBuffer extends FrameBuffer {
    private int depthBufferId;

    public GameViewportFrameBuffer(int width, int height, boolean useDepthBuffer) {
        super(width, height, useDepthBuffer);
    }
}
