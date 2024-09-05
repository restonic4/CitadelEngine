#type vertex
#version 330 core
#extension GL_NV_gpu_shader5 : enable
#extension GL_ARB_bindless_texture : require

layout (location=0) in vec3 aPos;
layout (location=1) in vec4 aColor;
layout (location=2) in vec2 aUV;
layout (location=3) in vec2 aTextureHandle;

uniform mat4 uProjection;
uniform mat4 uView;

out vec4 fColor;
out vec2 fUV;
flat out uvec2 fTextureHandle;

void main()
{
    fColor = aColor;
    fUV = aUV;
    fTextureHandle = uvec2(floatBitsToUint(aTextureHandle.x), floatBitsToUint(aTextureHandle.y));

    gl_Position = uProjection * uView * vec4(aPos, 1.0);
}

#type fragment
#version 330 core
#extension GL_NV_gpu_shader5 : enable
#extension GL_ARB_bindless_texture : require

uniform sampler2D uTextures[8];

in vec4 fColor;
in vec2 fUV;
flat in uvec2 fTextureHandle;

out vec4 color;

void main()
{
    uint64_t handle = (uint64_t(fTextureHandle.y) << 32) | uint64_t(fTextureHandle.x);

    if (handle == 0) {
        color = fColor;
    } else {
        sampler2D tex = sampler2D(handle);
        color = fColor * texture(tex, fUV);
    }
}