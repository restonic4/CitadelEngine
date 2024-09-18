#type vertex
#version 430 core
#extension GL_NV_gpu_shader5 : enable
#extension GL_ARB_bindless_texture : require

layout (location=0) in vec3 aPos;
layout (location=1) in vec4 aColor;
layout (location=2) in vec2 aUV;
layout (location=3) in vec2 aTextureHandle;
layout (location=4) in vec3 aNormal;
layout (location=5) in vec2 aReflectivity; // The first float is the reflectance, the second the shine damper

uniform mat4 uProjection;
uniform mat4 uView;
uniform vec3 uLightPos[4]; // Max lights
uniform vec3 uLightColors[4];
uniform vec4 uLightAttenuationFactors[4];
uniform int uLightAmount;
uniform mat4 uProjViewMatrix;

out vec4 fColor;
out vec2 fUV;
flat out uvec2 fTextureHandle;
out vec3 normalizedNormal;
out vec3 toLightVectorNormaliced[4];
out vec3 toCameraVector;
out vec3 fLightPos[4];
out vec3 toLightVector[4];
out vec3 reflectedLightVector[4];
out vec2 fReflectivity;
flat out int fLightAmount;
flat out vec3 fLightColors[4];
flat out vec4 fLightAttenuationFactors[4];
out vec4 vFragPosWorld;

void main()
{
    // Variables

    fColor = aColor;
    fUV = aUV;
    fTextureHandle = uvec2(floatBitsToUint(aTextureHandle.x), floatBitsToUint(aTextureHandle.y));
    fReflectivity = aReflectivity;
    fLightAmount = uLightAmount;
    fLightColors = uLightColors;
    fLightAttenuationFactors = uLightAttenuationFactors;
    fLightPos = uLightPos;

    // Lighting

    vec3 cameraPos = (inverse(uView) * vec4(0, 0, 0, 1)).xyz;

    normalizedNormal = normalize(aNormal);

    for (int i = 0; i < uLightAmount; i++) {
        toLightVector[i] = uLightPos[i] - aPos;
        toLightVectorNormaliced[i] = normalize(toLightVector[i]);

        vec3 lightDirection = -toLightVectorNormaliced[i];
        reflectedLightVector[i] = reflect(lightDirection, normalizedNormal);
    }

    toCameraVector = normalize(cameraPos - aPos);

    // Positioning

    vFragPosWorld = vec4(aPos, 1);
    gl_Position = uProjection * uView * vFragPosWorld;
}

#type fragment
#version 430 core
#extension GL_NV_gpu_shader5 : enable
#extension GL_ARB_bindless_texture : require

const int NUM_CASCADES = 3;
const float BIAS = 0.0005;
const float SHADOW_FACTOR = 0.25;
const int DEBUG_SHADOWS = 0;

struct CascadeShadow {
    mat4 projViewMatrix;
    float splitDistance;
};

uniform sampler2D shadowMap[NUM_CASCADES];
uniform CascadeShadow cascadeShadows[NUM_CASCADES];
uniform mat4 uView;

in vec4 fColor;
in vec2 fUV;
flat in uvec2 fTextureHandle;
in vec3 normalizedNormal;
in vec3 fLightPos[4];
in vec3 toLightVector[4];
in vec3 toLightVectorNormaliced[4];
in vec3 toCameraVector;
in vec3 reflectedLightVector[4];
in vec2 fReflectivity;
flat in int fLightAmount;
flat in vec3 fLightColors[4];
flat in vec4 fLightAttenuationFactors[4]; // id, x, y, z
in vec4 vFragPosWorld;

out vec4 color;

float textureProj(vec4 shadowCoord, vec2 offset, int cascadeIndex) {
    float shadow = 1.0;

    if (shadowCoord.z > -1.0 && shadowCoord.z < 1.0) {
        float dist = 0.0;
        dist = texture(shadowMap[cascadeIndex], vec2(shadowCoord.xy + offset)).r;
        if (shadowCoord.w > 0 && dist < shadowCoord.z - BIAS) {
            shadow = SHADOW_FACTOR;
        }
    }

    return shadow;
}

float calcShadow(vec4 worldPosition, int cascadeIndex) {
    vec4 shadowMapPosition = cascadeShadows[cascadeIndex].projViewMatrix * worldPosition;
    vec4 shadowCoord = (shadowMapPosition / shadowMapPosition.w) * 0.5 + 0.5;
    return textureProj(shadowCoord, vec2(0, 0), cascadeIndex);
}

void main()
{
    // Texture

    uint64_t handle = (uint64_t(fTextureHandle.y) << 32) | uint64_t(fTextureHandle.x);

    vec4 baseColor;

    if (handle == 0) {
        baseColor = fColor;
    } else {
        sampler2D tex = sampler2D(handle);
        baseColor = fColor * texture(tex, fUV);
    }

    // Lights

    float minLight = 0.05;
    float minAttenuationConstantFactor = 0.5;

    vec3 lightResult = vec3(0.0);
    vec3 lightSpecularResult = vec3(0.0);

    for (int i = 0; i < fLightAmount; i++) {
        float attenuationFactor = 1.0;
        vec3 lightDir;

        if (fLightAttenuationFactors[i].x == 0) { // Directional light
            lightDir = normalize(fLightPos[i]);
        } else if (fLightAttenuationFactors[i].x == 1) { // Point light
            float distance = length(toLightVector[i]);
            attenuationFactor = max(fLightAttenuationFactors[i].y, minAttenuationConstantFactor) + (fLightAttenuationFactors[i].z * distance) + (fLightAttenuationFactors[i].w * distance * distance); // Attenuation formula
            lightDir = toLightVectorNormaliced[i];
        }

        float lightBrightnessFactor = max(0, dot(normalizedNormal, lightDir));
        float lightSpecularFactor = max(0, dot(reflectedLightVector[i], toCameraVector));
        float lightDampedFactor = pow(lightSpecularFactor, fReflectivity.y);

        lightResult += (lightBrightnessFactor * fLightColors[i]) / attenuationFactor;
        lightSpecularResult += (lightDampedFactor * fReflectivity.x * fLightColors[i]) / attenuationFactor;
    }

    lightResult = max(lightResult, minLight);

    // Shadows

    vec3 fragPosView = (uView * vFragPosWorld).xyz;

    int cascadeIndex = 0;
    for (int i = 0; i < NUM_CASCADES - 1; i++) {
        if (fragPosView.z < cascadeShadows[i].splitDistance) {
            cascadeIndex = i + 1;
        }
    }

    float shadowFactor = calcShadow(vFragPosWorld, cascadeIndex);

    // Result

    color = vec4(lightResult, 1.0) * baseColor + vec4(lightSpecularResult, 1.0);
    color.rgb = color.rgb * shadowFactor;

    if (DEBUG_SHADOWS == 1) {
        switch (cascadeIndex) {
            case 0:
            color.rgb *= vec3(1, 0, 0);
            break;
            case 1:
            color.rgb *= vec3(0, 1, 0);
            break;
            case 2:
            color.rgb *= vec3(0, 0, 1);
            break;
            default :
            color.rgb *= vec3(1, 1, 0); // yellow
            break;
        }
    }
}