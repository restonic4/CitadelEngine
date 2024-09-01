#type vertex
#version 330 core
layout (location=0) in vec3 aPos;
layout (location=1) in vec4 aColor;
layout (location=2) in vec2 aUV;
layout (location=3) in float aTextureId;

uniform mat4 uProjection;
uniform mat4 uView;

out vec4 fColor;
out vec2 fUV;
flat out float fTextureId;

void main()
{
    fColor = aColor;
    fUV = aUV;
    fTextureId = aTextureId;

    gl_Position = uProjection * uView * vec4(aPos, 1.0);
}

#type fragment
#version 330 core

uniform sampler2D uTextures[8];

in vec4 fColor;
in vec2 fUV;
flat in float fTextureId;

out vec4 color;

void main()
{
    int id = int((fTextureId));

    switch (id) {
        case 0:
        color = fColor;
        break;
        case 1:
        color = vec4(1, 0, 0, 1);
        break;
        case 2:
        color = vec4(0, 1, 0, 1);
        break;
        case 3:
        color = vec4(0, 0, 1, 1);
        break;
        case 4:
        color = vec4(1, 0, 1, 1);
        break;
        case 5:
        color = vec4(1, 1, 0, 1);
        break;
        case 6:
        color = vec4(1, 1, 1, 1);
        break;
        case 7:
        color = vec4(0.639, 0.412, 0.161, 1);
        break;
    }
}