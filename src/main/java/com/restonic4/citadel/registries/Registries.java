package com.restonic4.citadel.registries;

import com.restonic4.citadel.registries.built_in.types.*;
import com.restonic4.citadel.render.Shader;
import com.restonic4.citadel.world.object.Component;

import java.util.ArrayList;
import java.util.List;

public class Registries {
    // List of built-in keys for you to use, yippie!

    public static final RegistryKey<Sound> SOUND = new RegistryKey<>("sound", "assets/sounds", "ogg");
    public static final RegistryKey<Sound> MUSIC = new RegistryKey<>("music", "assets/sounds/music", "ogg");
    public static final RegistryKey<Shader> SHADER = new RegistryKey<>("shader", "assets/shaders", "glsl");
    public static final RegistryKey<Icon> ICON = new RegistryKey<>("icon", "assets/textures/icons", "png");

    public static final RegistryKey<ProfilerStat> PROFILER_STAT = new RegistryKey<>("profiler_stat");
    public static final RegistryKey<Locale> LOCALE = new RegistryKey<>("locale");
    public static final RegistryKey<KeyBind> KEY_BIND = new RegistryKey<>("key_bind");
    public static final RegistryKey<ImGuiScreen> IM_GUI_SCREEN = new RegistryKey<>("im_gui_screen");
    public static final RegistryKey<Packet> PACKET = new RegistryKey<>("packet");
    public static final RegistryKey<PacketDataType<?>> PACKET_DATA_TYPE = new RegistryKey<>("packet_data_type");
    public static final RegistryKey<FrameBuffer> FRAME_BUFFER = new RegistryKey<>("frame_buffer");
    public static final RegistryKey<Component> COMPONENT = new RegistryKey<>("component");

    // Custom key system, where you can register your own keys

    private static final List<RegistryKey<?>> customKeys = new ArrayList<>();

    public static <T extends RegistryObject> RegistryKey<T> registerCustomKey(String id) {
        return registerCustomKey(id, null, null);
    }

    public static <T extends RegistryObject> RegistryKey<T> registerCustomKey(String id, String rootDirectory, String assetFileExtension) {
        RegistryKey<T> customKey = new RegistryKey<>(id, rootDirectory, assetFileExtension);
        customKeys.add(customKey);

        return customKey;
    }

    public static <T extends RegistryObject> RegistryKey<T> getCustomKey(String id) {
        for (RegistryKey<?> key : customKeys) {
            if (key.getKey().equals(id)) {
                RegistryKey<T> foundKey = (RegistryKey<T>) key;
                return foundKey;
            }
        }

        return null;
    }
}
