package me.restonic4.citadel.registries.built_in.managers;

import me.restonic4.citadel.registries.AbstractRegistryInitializer;
import me.restonic4.citadel.registries.AssetLocation;
import me.restonic4.citadel.registries.Registries;
import me.restonic4.citadel.registries.Registry;
import me.restonic4.citadel.render.CascadeShadow;
import me.restonic4.citadel.render.Shader;
import me.restonic4.citadel.render.UniformsMap;
import me.restonic4.citadel.util.CitadelConstants;

public class Shaders extends AbstractRegistryInitializer {
    public static Shader MAIN;
    public static Shader SHADOWS;

    @Override
    public void register() {
        // TODO: Optimize this shader, it's using a ton of memory or something like, i cant pass new variables.
        MAIN = Registry.register(Registries.SHADER, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "main"),
                new Shader(new String[]{ "uProjection", "uView", "uLightPos", "uLightAmount", "uLightColors", "uLightAttenuationFactors" }) {
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
