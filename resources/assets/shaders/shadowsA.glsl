#type vertex
#version 330 core

layout (location=0) in vec3 aPos;

uniform mat4 uProjViewMatrix;

out vec4 vFragPos;

void main() {
    vFragPos = vec4(aPos, 1);
    gl_Position = uProjViewMatrix * vFragPos;
}

#type fragment
#version 330 core

const int NUM_CASCADES = 3;
const float BIAS = 0.0005;
const float SHADOW_FACTOR = 0.25;
const int DEBUG_SHADOWS = 1;

struct CascadeShadow {
    mat4 projViewMatrix;
    float splitDistance;
};

in vec4 vFragPos;

uniform sampler2D shadowMap[NUM_CASCADES];
uniform CascadeShadow cascadeShadows[NUM_CASCADES];

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

void main() {
    int cascadeIndex = 0;
    for (int i = 0; i < NUM_CASCADES - 1; i++) {
        if (vFragPos.z < cascadeShadows[i].splitDistance) {
            cascadeIndex = i + 1;
        }
    }

    float shadowFactor = calcShadow(vFragPos, cascadeIndex);

    color = vec4(vec3(shadowFactor), 1.0);

    if (DEBUG_SHADOWS == 1) {
        switch (cascadeIndex) {
            case 0:
            color.rgb *= vec3(1.0f, 0.25f, 0.25f);
            break;
            case 1:
            color.rgb *= vec3(0.25f, 1.0f, 0.25f);
            break;
            case 2:
            color.rgb *= vec3(0.25f, 0.25f, 1.0f);
            break;
            default :
            color.rgb *= vec3(1.0f, 1.0f, 0.25f);
            break;
        }
    }
}