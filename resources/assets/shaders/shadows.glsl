#type vertex
#version 330 core

layout (location=0) in vec3 aPos;

uniform mat4 uProjViewMatrix;

void main() {
    gl_Position = uProjViewMatrix * vec4(aPos, 1);
}

#type fragment
#version 330 core

out vec4 color;

void main() {
    color = vec4(1, 1, 1, 1);
}