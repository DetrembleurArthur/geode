#version 330 core

/*
uniform int isTextured;
*/
//uniform vec4 fColor;

out vec4 color;
in vec4 fColor;

in vec2 fUV;
uniform sampler2D TEX_SAMPLER;

void main()
{/*
    if(isTextured == 1)
    {
        color = texture(TEX_SAMPLER, fUV) * fColor;
    }
    else
    {
        color = fColor;
    }*/
    color = texture(TEX_SAMPLER, fUV) * fColor;
}
