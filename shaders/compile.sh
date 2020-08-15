#!/bin/bash


for file in ./*; do
    name="${file:2}"
    extension="${file##*.}"
    if [ "$extension" == "vs" ]; then
        #./shaderc.exe -f "$file" -o "gl_""$name"".bin" --type v :'-p glsl:' --varyingdef varying.def.sc
        #./shaderc.exe  --platform windows -f "$file" -o "dx_""$name"".bin" --type f -p vs_5_0 --varyingdef varying.def.sc --platform windows
        ./shaderc.exe -f $file -o "gl_""$name"".bin" --type v --varyingdef varying.def.sc
        ./shaderc.exe -f $file -o "dx_""$name"".bin" --type v -p vs_5_0 --varyingdef varying.def.sc --platform windows
    fi
    if [ "$extension" == "fs" ]; then
        #./shaderc.exe -f "$file" -o "gl_""$name"".bin" --type f '-p glsl:' --varyingdef varying.def.sc -i shaders/bgfx_shader.sh
        #./shaderc.exe  --platform windows -f "$file" -o "dx_""$name"".bin" --type f -p ps_5_0 --varyingdef varying.def.sc
        ./shaderc.exe -f $file -o "gl_""$name"".bin" --type f --varyingdef varying.def.sc
        ./shaderc.exe -f $file -o "dx_""$name"".bin" --type f -p ps_5_0 --varyingdef varying.def.sc --platform windows
    fi
done


#read  -n 1 -p "Done"

#.\shaderc.exe -f sandbox.vs -o sandbox.vs.bin --type v

#./shaderc.exe --platform windows -f sandbox.vs -o dx_sandbox.vs.bin --type v -p vs_5_0
#./shaderc.exe --platform windows -f sandbox.fs -o dx_sandbox.fs.bin --type f -p ps_5_0