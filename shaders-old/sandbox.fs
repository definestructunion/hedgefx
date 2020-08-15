$input v_color0, v_texcoord0

#include "bgfx_shader.bsh"
//#include "bgfx_shader_lib.bsh"

//uniform sampler2D u_texture;
SAMPLER2D(u_texture, 0);
//SAMPLER2DARRAY(u_texture, 31);

//out vec4 FragColor;r
  
//varying vec4 v_color0; // the input variable from the vertex shader (same name and same type)  

void main()
{
    //gl_FragColor = vertexColor;
    //FragColor = vertexColor;
    //FragColor = vec4(0.0f, 1.0f, 0.0f, 1.0f);
    //gl_FragColor = vec4(0.0f, 0.0f, 1.0f, 1.0f);
    gl_FragColor = v_color0 * texture2D(u_texture, v_texcoord0.xy);
} 