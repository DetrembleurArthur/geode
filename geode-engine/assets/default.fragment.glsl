#version 330 core

in vec2 fUV;
uniform sampler2D TEX_SAMPLER;
uniform int isTextured;

uniform vec4 fColor;

out vec4 color;

void main()
{
    if(isTextured == 1)
    {
        color = texture(TEX_SAMPLER, fUV) * fColor;
    }
    else
    {
        color = fColor;
    }
}
