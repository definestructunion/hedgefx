$input a_position, a_color0, a_texcoord0
$output v_color0, v_texcoord0

#include "bgfx_shader.bsh"

//layout (location = 0) in vec3 a_position; // the position variable has attribute position 0
//attribute vec3 a_position;
//attribute vec4 a_color0;

//varying vec4 v_color0; // specify a color output to the fragment shader

void main()
{
    //mat4 projViewWorld = mul(mul(u_proj, u_view), u_model[0]); // TODO work it out after https://github.com/bkaradzic/bgfx/issues/983
	//gl_Position = vec4(a_position, 1.0);//mul(projViewWorld, vec4(a_position, 1.0));
	//v_color0 = a_color0;

	mat4 projViewWorld = mul(mul(u_proj, u_view), u_model[0]); // TODO work it out after https://github.com/bkaradzic/bgfx/issues/983
	gl_Position = mul(projViewWorld, vec4(a_position, 1.0));
	v_color0 = a_color0;
	v_texcoord0 = a_texcoord0;
}