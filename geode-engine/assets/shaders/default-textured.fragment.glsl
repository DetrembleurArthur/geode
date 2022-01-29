#version 330 core

uniform vec4 fColor;

out vec4 color;

in vec2 fUV;
uniform sampler2D TEX_SAMPLER;

void main()
{
    color = texture(TEX_SAMPLER, fUV) * fColor;
}
