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
uniform vec3 uLightPos;

out vec4 fColor;
out vec2 fUV;
flat out uvec2 fTextureHandle;
out float fLightIntensity;

void main()
{
    fColor = aColor;
    fUV = aUV;
    fTextureHandle = uvec2(floatBitsToUint(aTextureHandle.x), floatBitsToUint(aTextureHandle.y));

    float minLight = 0.1;

    vec3 cameraPos = (inverse(uView) * vec4(0, 0, 0, 1)).xyz;

    vec3 normalizedNormal = normalize(aNormal);

    vec3 toLightVector = normalize(uLightPos - aPos);
    vec3 toCameraVector = normalize(cameraPos - aPos);
    vec3 lightDirection = -toLightVector;
    vec3 reflectedLightVector = reflect(lightDirection, normalizedNormal);

    float lightBrightnessFactor = max(minLight, dot(normalizedNormal, toLightVector));
    float lightSpecularFactor = max(0, dot(reflectedLightVector, toCameraVector));
    float lightDampedFactor = pow(lightSpecularFactor, aReflectivity.y);
    float lightFinalSpecularFactor = lightDampedFactor * aReflectivity.x;

    fLightIntensity = lightBrightnessFactor + lightFinalSpecularFactor;

    gl_Position = uProjection * uView * vec4(aPos, 1);
}

#type fragment
#version 330 core
#extension GL_NV_gpu_shader5 : enable
#extension GL_ARB_bindless_texture : require

in vec4 fColor;
in vec2 fUV;
flat in uvec2 fTextureHandle;
in float fLightIntensity;

out vec4 color;

void main()
{
    uint64_t handle = (uint64_t(fTextureHandle.y) << 32) | uint64_t(fTextureHandle.x);

    vec4 baseColor;

    if (handle == 0) {
        baseColor = fColor;
    } else {
        sampler2D tex = sampler2D(handle);
        baseColor = fColor * texture(tex, fUV);
    }

    color = baseColor * fLightIntensity;
}