package com.restonic4.citadel.registries.built_in.types.subtypes;

import com.restonic4.citadel.util.FlagFactory;

public enum IconSize {
    SIZE_512(FlagFactory.createFlag(0), 512),
    SIZE_112(FlagFactory.createFlag(1), 112),
    SIZE_100(FlagFactory.createFlag(2), 100),
    SIZE_56(FlagFactory.createFlag(3), 56),
    SIZE_16(FlagFactory.createFlag(4), 16);

    private final int flag;
    private final int pixelSize;

    IconSize(int flag, int pixelSize) {
        this.flag = flag;
        this.pixelSize = pixelSize;
    }

    public int getFlag() {
        return flag;
    }

    public int getPixelSize() {
        return pixelSize;
    }
}