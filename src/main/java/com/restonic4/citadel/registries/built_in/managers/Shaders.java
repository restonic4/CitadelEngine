package com.restonic4.citadel.registries.built_in.managers;

import com.restonic4.citadel.core.CitadelLauncher;
import com.restonic4.citadel.registries.AbstractRegistryInitializer;
import com.restonic4.citadel.registries.AssetLocation;
import com.restonic4.citadel.registries.Registries;
import com.restonic4.citadel.registries.Registry;
import com.restonic4.citadel.render.shadows.CascadeShadow;
import com.restonic4.citadel.render.Shader;
import com.restonic4.citadel.render.UniformsMap;
import com.restonic4.citadel.util.CitadelConstants;
import com.restonic4.citadel.util.debug.diagnosis.Logger;

public class Shaders extends AbstractRegistryInitializer {
    public static Shader MAIN;
    public static Shader SHADOWS;

    @Override
    public void register() {
        // TODO: Optimize this shader, it's using a ton of memory or something like, i cant pass new variables.
        MAIN = Registry.register(Registries.SHADER, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "main"),
                new Shader(new String[]{ "uProjection", "uView", "uLightPos", "uLightAmount", "uLightColors", "uLightAttenuationFactors" }) {
                    @Override
                    public void compile() {
                        if (CitadelLauncher.getInstance().getSettings().shouldGenerateBindlessTextures()) {
                            super.compile();
                        } else {
                            Logger.log("Main shader compilation avoided due to incompatibilities.");
                        }
                    }

                    @Override
                    public void generateUniforms() {
                        super.generateUniforms();

                        for (int i = 0; i < CascadeShadow.SHADOW_MAP_CASCADE_COUNT; i++) {
                            UniformsMap uniformsMap = getUniformsMap();
                            uniformsMap.createUniform("shadowMap[" + i + "]");
                            uniformsMap.createUniform("cascadeShadows[" + i + "]" + ".projViewMatrix");
                            uniformsMap.createUniform("cascadeShadows[" + i + "]" + ".splitDistance");
                        }
                    }
                }
        );
        SHADOWS = Registry.register(Registries.SHADER, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "shadows"),
                new Shader(new String[]{ "uProjViewMatrix" })
        );
    }
}
