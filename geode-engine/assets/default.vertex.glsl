#version 330 core
layout (location=0) in vec3 aPos;
layout (location=1) in vec4 aColor;

uniform mat4 uProjection;
uniform mat4 uView;
uniform mat4 uModel;

out vec4 fColor;

out vec2 fUV;

void main()
{
    fColor = aColor;
    gl_Position = uProjection * uView * uModel * vec4(aPos, 1.0);
}
