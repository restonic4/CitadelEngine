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
    int id = int(fTextureId);

    switch (id) {
        case 0:
        color = fColor;
        break;
        case 1:
        color = fColor * texture(uTextures[1], fUV);
        break;
        case 2:
        color = fColor * texture(uTextures[2], fUV);
        break;
        case 3:
        color = fColor * texture(uTextures[3], fUV);
        break;
        case 4:
        color = fColor * texture(uTextures[4], fUV);
        break;
        case 5:
        color = fColor * texture(uTextures[5], fUV);
        break;
        case 6:
        color = fColor * texture(uTextures[6], fUV);
        break;
        case 7:
        color = fColor * texture(uTextures[7], fUV);
        break;
    }
}