package com.restonic4.citadel.registries;

import com.restonic4.citadel.core.editor.LevelEditor;
import com.restonic4.citadel.registries.built_in.managers.LevelEditorAddTemplates;
import com.restonic4.citadel.registries.built_in.types.LevelEditorAddTemplate;
import com.restonic4.citadel.world.SceneManager;
import imgui.ImGui;

import java.util.ArrayList;
import java.util.List;

public class Collections {
    public static final List<List<LevelEditorAddTemplate>> LEAT = new ArrayList<>();

    public static final List<LevelEditorAddTemplate> LEAT_SHAPES = new ArrayList<>();
    public static final List<LevelEditorAddTemplate> LEAT_LIGHTS = new ArrayList<>();

    static {
        LEAT.add(LEAT_SHAPES);
        LEAT.add(LEAT_LIGHTS);

        LEAT_SHAPES.add(LevelEditorAddTemplates.EMPTY);
        LEAT_SHAPES.add(LevelEditorAddTemplates.CUBE);

        LEAT_LIGHTS.add(LevelEditorAddTemplates.SUN);
    }
}
