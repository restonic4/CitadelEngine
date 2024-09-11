#type vertex
#version 330 core
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
uniform int uLightAmount;

out vec4 fColor;
out vec2 fUV;
flat out uvec2 fTextureHandle;
out vec3 normalizedNormal;
out vec3 toLightVector[4];
out vec3 toCameraVector;
out vec3 reflectedLightVector[4];
out vec2 fReflectivity;
flat out int fLightAmount;
flat out vec3 fLightColors[4];

void main()
{
    // Variables

    fColor = aColor;
    fUV = aUV;
    fTextureHandle = uvec2(floatBitsToUint(aTextureHandle.x), floatBitsToUint(aTextureHandle.y));
    fReflectivity = aReflectivity;
    fLightAmount = uLightAmount;
    fLightColors = uLightColors;

    // Lighting

    vec3 cameraPos = (inverse(uView) * vec4(0, 0, 0, 1)).xyz;

    normalizedNormal = normalize(aNormal);

    for (int i = 0; i < uLightAmount; i++) {
        toLightVector[i] = normalize(uLightPos[i] - aPos);

        vec3 lightDirection = -toLightVector[i];
        reflectedLightVector[i] = reflect(lightDirection, normalizedNormal);
    }

    toCameraVector = normalize(cameraPos - aPos);

    // Positioning

    gl_Position = uProjection * uView * vec4(aPos, 1);
}

#type fragment
#version 330 core
#extension GL_NV_gpu_shader5 : enable
#extension GL_ARB_bindless_texture : require

in vec4 fColor;
in vec2 fUV;
flat in uvec2 fTextureHandle;
in vec3 normalizedNormal;
in vec3 toLightVector[4];
in vec3 toCameraVector;
in vec3 reflectedLightVector[4];
in vec2 fReflectivity;
flat in int fLightAmount;
flat in vec3 fLightColors[4];

out vec4 color;

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

    float minLight = 0.1;

    vec3 lightResult = vec3(0.0);
    vec3 lightSpecularResult = vec3(0.0);

    for (int i = 0; i < fLightAmount; i++) {
        float lightBrightnessFactor = max(0, dot(normalizedNormal, toLightVector[i]));
        float lightSpecularFactor = max(0, dot(reflectedLightVector[i], toCameraVector));
        float lightDampedFactor = pow(lightSpecularFactor, fReflectivity.y);

        lightResult += lightBrightnessFactor * fLightColors[i];
        lightSpecularResult += lightDampedFactor * fReflectivity.x * fLightColors[i];
    }

    lightResult = max(lightResult, minLight);

    // Result

    color = vec4(lightResult, 1.0) * baseColor + vec4(lightSpecularResult, 1.0);
}