package com.restonic4.citadel.registries.built_in.managers;

import com.restonic4.citadel.core.nodex.Node;
import com.restonic4.citadel.registries.AbstractRegistryInitializer;
import com.restonic4.citadel.registries.AssetLocation;
import com.restonic4.citadel.registries.Registries;
import com.restonic4.citadel.registries.Registry;
import com.restonic4.citadel.registries.built_in.types.NodeType;
import com.restonic4.citadel.util.CitadelConstants;
import com.restonic4.citadel.util.StringBuilderHelper;
import com.restonic4.citadel.util.debug.diagnosis.Logger;
import org.joml.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.util.zip.GZIPOutputStream;

public class NodeTypes extends AbstractRegistryInitializer {
    public static NodeType STRING;
    public static NodeType INTEGER;
    public static NodeType FLOAT;
    public static NodeType BOOLEAN;
    public static NodeType DOUBLE;
    public static NodeType LONG;
    public static NodeType SHORT;
    public static NodeType BYTE;
    public static NodeType CHARACTER;

    public static NodeType VECTOR2I;
    public static NodeType VECTOR3I;
    public static NodeType VECTOR4I;

    public static NodeType VECTOR2F;
    public static NodeType VECTOR3F;
    public static NodeType VECTOR4F;

    public static NodeType QUATERNIONF;

    public void register() {
        STRING = Registry.register(Registries.NODE_TYPE, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "string"), new NodeType() {
            @Override
            public boolean serialize(Object object, DataOutputStream out) {
                Node node = (Node) object;

                try {
                    out.writeUTF((String) node.getValue());
                    return true;
                } catch (Exception exception) {
                    Logger.logError(exception);
                    return false;
                }
            }

            @Override
            public boolean deserialize(Object object, DataInputStream in) {
                Node node = (Node) object;

                try {
                    node.setValue(in.readUTF());
                    return true;
                } catch (Exception exception) {
                    Logger.log(exception);
                    return false;
                }
            }
        });

        INTEGER = Registry.register(Registries.NODE_TYPE, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "integer"), new NodeType() {
            @Override
            public boolean serialize(Object object, DataOutputStream out) {
                Node node = (Node) object;

                try {
                    out.writeInt((Integer) node.getValue());
                    return true;
                } catch (Exception exception) {
                    Logger.logError(exception);
                    return false;
                }
            }

            @Override
            public boolean deserialize(Object object, DataInputStream in) {
                Node node = (Node) object;

                try {
                    node.setValue(in.readInt());
                    return true;
                } catch (Exception exception) {
                    Logger.log(exception);
                    return false;
                }
            }
        });

        FLOAT = Registry.register(Registries.NODE_TYPE, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "float"), new NodeType() {
            @Override
            public boolean serialize(Object object, DataOutputStream out) {
                Node node = (Node) object;

                try {
                    out.writeFloat((Float) node.getValue());
                    return true;
                } catch (Exception exception) {
                    Logger.logError(exception);
                    return false;
                }
            }

            @Override
            public boolean deserialize(Object object, DataInputStream in) {
                Node node = (Node) object;

                try {
                    node.setValue(in.readFloat());
                    return true;
                } catch (Exception exception) {
                    Logger.log(exception);
                    return false;
                }
            }
        });

        BOOLEAN = Registry.register(Registries.NODE_TYPE, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "boolean"), new NodeType() {
            @Override
            public boolean serialize(Object object, DataOutputStream out) {
                Node node = (Node) object;

                try {
                    out.writeBoolean((Boolean) node.getValue());
                    return true;
                } catch (Exception exception) {
                    Logger.logError(exception);
                    return false;
                }
            }

            @Override
            public boolean deserialize(Object object, DataInputStream in) {
                Node node = (Node) object;

                try {
                    node.setValue(in.readBoolean());
                    return true;
                } catch (Exception exception) {
                    Logger.log(exception);
                    return false;
                }
            }
        });

        DOUBLE = Registry.register(Registries.NODE_TYPE, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "double"), new NodeType() {
            @Override
            public boolean serialize(Object object, DataOutputStream out) {
                Node node = (Node) object;

                try {
                    out.writeDouble((Double) node.getValue());
                    return true;
                } catch (Exception exception) {
                    Logger.logError(exception);
                    return false;
                }
            }

            @Override
            public boolean deserialize(Object object, DataInputStream in) {
                Node node = (Node) object;

                try {
                    node.setValue(in.readDouble());
                    return true;
                } catch (Exception exception) {
                    Logger.log(exception);
                    return false;
                }
            }
        });

        LONG = Registry.register(Registries.NODE_TYPE, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "long"), new NodeType() {
            @Override
            public boolean serialize(Object object, DataOutputStream out) {
                Node node = (Node) object;

                try {
                    out.writeLong((Long) node.getValue());
                    return true;
                } catch (Exception exception) {
                    Logger.logError(exception);
                    return false;
                }
            }

            @Override
            public boolean deserialize(Object object, DataInputStream in) {
                Node node = (Node) object;

                try {
                    node.setValue(in.readLong());
                    return true;
                } catch (Exception exception) {
                    Logger.log(exception);
                    return false;
                }
            }
        });

        SHORT = Registry.register(Registries.NODE_TYPE, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "short"), new NodeType() {
            @Override
            public boolean serialize(Object object, DataOutputStream out) {
                Node node = (Node) object;

                try {
                    out.writeShort((Short) node.getValue());
                    return true;
                } catch (Exception exception) {
                    Logger.logError(exception);
                    return false;
                }
            }

            @Override
            public boolean deserialize(Object object, DataInputStream in) {
                Node node = (Node) object;

                try {
                    node.setValue(in.readShort());
                    return true;
                } catch (Exception exception) {
                    Logger.log(exception);
                    return false;
                }
            }
        });

        BYTE = Registry.register(Registries.NODE_TYPE, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "byte"), new NodeType() {
            @Override
            public boolean serialize(Object object, DataOutputStream out) {
                Node node = (Node) object;

                try {
                    out.writeByte((Byte) node.getValue());
                    return true;
                } catch (Exception exception) {
                    Logger.logError(exception);
                    return false;
                }
            }

            @Override
            public boolean deserialize(Object object, DataInputStream in) {
                Node node = (Node) object;

                try {
                    node.setValue(in.readByte());
                    return true;
                } catch (Exception exception) {
                    Logger.log(exception);
                    return false;
                }
            }
        });

        CHARACTER = Registry.register(Registries.NODE_TYPE, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "character"), new NodeType() {
            @Override
            public boolean serialize(Object object, DataOutputStream out) {
                Node node = (Node) object;

                try {
                    out.writeChar((Character) node.getValue());
                    return true;
                } catch (Exception exception) {
                    Logger.logError(exception);
                    return false;
                }
            }

            @Override
            public boolean deserialize(Object object, DataInputStream in) {
                Node node = (Node) object;

                try {
                    node.setValue(in.readChar());
                    return true;
                } catch (Exception exception) {
                    Logger.log(exception);
                    return false;
                }
            }
        });

        VECTOR2I = Registry.register(Registries.NODE_TYPE, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "vec2i"), new NodeType() {
            @Override
            public boolean serialize(Object object, DataOutputStream out) {
                Node node = (Node) object;

                try {
                    Vector2i vector2i = (Vector2i) node.getValue();

                    out.writeInt(vector2i.x);
                    out.writeInt(vector2i.y);

                    return true;
                } catch (Exception exception) {
                    Logger.logError(exception);
                    return false;
                }
            }

            @Override
            public boolean deserialize(Object object, DataInputStream in) {
                Node node = (Node) object;

                try {
                    int x = in.readInt();
                    int y = in.readInt();

                    node.setValue(new Vector2i(x, y));

                    return true;
                } catch (Exception exception) {
                    Logger.log(exception);
                    return false;
                }
            }
        });

        VECTOR3I = Registry.register(Registries.NODE_TYPE, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "vec3i"), new NodeType() {
            @Override
            public boolean serialize(Object object, DataOutputStream out) {
                Node node = (Node) object;

                try {
                    Vector3i vector3i = (Vector3i) node.getValue();

                    out.writeInt(vector3i.x);
                    out.writeInt(vector3i.y);
                    out.writeInt(vector3i.z);

                    return true;
                } catch (Exception exception) {
                    Logger.logError(exception);
                    return false;
                }
            }

            @Override
            public boolean deserialize(Object object, DataInputStream in) {
                Node node = (Node) object;

                try {
                    int x = in.readInt();
                    int y = in.readInt();
                    int z = in.readInt();

                    node.setValue(new Vector3i(x, y, z));

                    return true;
                } catch (Exception exception) {
                    Logger.log(exception);
                    return false;
                }
            }
        });

        VECTOR4I = Registry.register(Registries.NODE_TYPE, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "vec4i"), new NodeType() {
            @Override
            public boolean serialize(Object object, DataOutputStream out) {
                Node node = (Node) object;

                try {
                    Vector4i vector4i = (Vector4i) node.getValue();

                    out.writeInt(vector4i.x);
                    out.writeInt(vector4i.y);
                    out.writeInt(vector4i.z);
                    out.writeInt(vector4i.w);

                    return true;
                } catch (Exception exception) {
                    Logger.logError(exception);
                    return false;
                }
            }

            @Override
            public boolean deserialize(Object object, DataInputStream in) {
                Node node = (Node) object;

                try {
                    int x = in.readInt();
                    int y = in.readInt();
                    int z = in.readInt();
                    int w = in.readInt();

                    node.setValue(new Vector4i(x, y, z, w));

                    return true;
                } catch (Exception exception) {
                    Logger.log(exception);
                    return false;
                }
            }
        });

        VECTOR2F = Registry.register(Registries.NODE_TYPE, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "vec2f"), new NodeType() {
            @Override
            public boolean serialize(Object object, DataOutputStream out) {
                Node node = (Node) object;

                try {
                    Vector2f vector2f = (Vector2f) node.getValue();

                    out.writeFloat(vector2f.x);
                    out.writeFloat(vector2f.y);

                    return true;
                } catch (Exception exception) {
                    Logger.logError(exception);
                    return false;
                }
            }

            @Override
            public boolean deserialize(Object object, DataInputStream in) {
                Node node = (Node) object;

                try {
                    float x = in.readFloat();
                    float y = in.readFloat();

                    node.setValue(new Vector2f(x, y));

                    return true;
                } catch (Exception exception) {
                    Logger.log(exception);
                    return false;
                }
            }
        });

        VECTOR3F = Registry.register(Registries.NODE_TYPE, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "vec3f"), new NodeType() {
            @Override
            public boolean serialize(Object object, DataOutputStream out) {
                Node node = (Node) object;

                try {
                    Vector3f vector3f = (Vector3f) node.getValue();

                    out.writeFloat(vector3f.x);
                    out.writeFloat(vector3f.y);
                    out.writeFloat(vector3f.z);

                    return true;
                } catch (Exception exception) {
                    Logger.logError(exception);
                    return false;
                }
            }

            @Override
            public boolean deserialize(Object object, DataInputStream in) {
                Node node = (Node) object;

                try {
                    float x = in.readFloat();
                    float y = in.readFloat();
                    float z = in.readFloat();

                    node.setValue(new Vector3f(x, y, z));

                    return true;
                } catch (Exception exception) {
                    Logger.log(exception);
                    return false;
                }
            }
        });

        VECTOR4F = Registry.register(Registries.NODE_TYPE, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "vec4f"), new NodeType() {
            @Override
            public boolean serialize(Object object, DataOutputStream out) {
                Node node = (Node) object;

                try {
                    Vector4f vector4f = (Vector4f) node.getValue();

                    out.writeFloat(vector4f.x);
                    out.writeFloat(vector4f.y);
                    out.writeFloat(vector4f.z);
                    out.writeFloat(vector4f.w);

                    return true;
                } catch (Exception exception) {
                    Logger.logError(exception);
                    return false;
                }
            }

            @Override
            public boolean deserialize(Object object, DataInputStream in) {
                Node node = (Node) object;

                try {
                    float x = in.readFloat();
                    float y = in.readFloat();
                    float z = in.readFloat();
                    float w = in.readFloat();

                    node.setValue(new Vector4f(x, y, z, w));

                    return true;
                } catch (Exception exception) {
                    Logger.log(exception);
                    return false;
                }
            }
        });

        QUATERNIONF = Registry.register(Registries.NODE_TYPE, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "quaternionf"), new NodeType() {
            @Override
            public boolean serialize(Object object, DataOutputStream out) {
                Node node = (Node) object;

                try {
                    Quaternionf quaternionf = (Quaternionf) node.getValue();

                    out.writeFloat(quaternionf.x);
                    out.writeFloat(quaternionf.y);
                    out.writeFloat(quaternionf.z);
                    out.writeFloat(quaternionf.w);

                    return true;
                } catch (Exception exception) {
                    Logger.logError(exception);
                    return false;
                }
            }

            @Override
            public boolean deserialize(Object object, DataInputStream in) {
                Node node = (Node) object;

                try {
                    float x = in.readFloat();
                    float y = in.readFloat();
                    float z = in.readFloat();
                    float w = in.readFloat();

                    node.setValue(new Quaternionf(x, y, z, w));

                    return true;
                } catch (Exception exception) {
                    Logger.log(exception);
                    return false;
                }
            }
        });
    }
}
