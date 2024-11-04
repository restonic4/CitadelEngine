package com.restonic4.citadel.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

public class ConfigurableGZIPOutputStream extends GZIPOutputStream {
    public ConfigurableGZIPOutputStream(OutputStream out) throws IOException {
        super(out);
    }

    public ConfigurableGZIPOutputStream setCompressionLevel(int level) {
        this.def.setLevel(level);
        return this;
    }
}
