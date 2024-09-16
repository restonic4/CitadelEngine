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

in vec4 vFragPos;

uniform sampler2D shadowMap[NUM_CASCADES];
uniform struct CascadeShadow {
    mat4 projViewMatrix;
    float splitDistance;
} cascadeShadows[NUM_CASCADES];

const int DEBUG_SHADOWS = 1;

out vec4 color;

float textureProj(vec4 shadowCoord, int idx) {
    float shadow = 1.0;

    if (shadowCoord.z > -1.0 && shadowCoord.z < 1.0) {
        float dist = texture(shadowMap[idx], shadowCoord.xy).r;
        if (dist < shadowCoord.z - BIAS) {
            shadow = SHADOW_FACTOR;
        }
    }
    return shadow;
}

float calcShadow(vec4 worldPosition, int cascadeIndex) {
    vec4 shadowMapPosition = cascadeShadows[cascadeIndex].projViewMatrix * worldPosition;
    vec4 shadowCoord = (shadowMapPosition / shadowMapPosition.w) * 0.5 + 0.5;
    return textureProj(shadowCoord, cascadeIndex);
}

void main() {
    // Determina el índice de la cascada en función de la distancia
    int cascadeIndex = 0;
    for (int i = 0; i < NUM_CASCADES - 1; i++) {
        if (vFragPos.z < cascadeShadows[i].splitDistance) {
            cascadeIndex = i + 1;
        }
    }

    // Calcula el factor de sombra
    float shadowFactor = calcShadow(vFragPos, cascadeIndex);

    // Asigna el color final
    color = vec4(vec3(shadowFactor), 1.0);

    // Depuración de sombras por cascada (opcional)
    if (DEBUG_SHADOWS == 1) {
        if (cascadeIndex == 0) {
            color.rgb *= vec3(1.0f, 0.25f, 0.25f);
        } else if (cascadeIndex == 1) {
            color.rgb *= vec3(0.25f, 1.0f, 0.25f);
        } else {
            color.rgb *= vec3(0.25f, 0.25f, 1.0f);
        }
    }
}