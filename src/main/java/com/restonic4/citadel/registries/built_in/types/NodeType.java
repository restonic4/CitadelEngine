package com.restonic4.citadel.registries.built_in.types;

import com.restonic4.citadel.registries.RegistryObject;

import java.io.*;
import java.nio.file.Path;
import java.util.zip.GZIPOutputStream;

public class NodeType extends RegistryObject {
    // Should override this
    public boolean serialize(Object object, DataOutputStream out) {
        return true;
    }

    public boolean deserialize(Object object, DataInputStream in) {
        return true;
    }
}
