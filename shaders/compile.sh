#!/bin/bash

for file in ./*; do
    #name="${filename%.*}"
    extension="${file##*.}"
    if [ "$extension" == "vs" ]; then
        ./shaderc.exe -f $file -o "$file"".bin" --type v --varyingdef varying.def.sc -i shaders/bgfx_shader.sh
    fi
    if [ "$extension" == "fs" ]; then
        ./shaderc.exe -f $file -o "$file"".bin" --type f --varyingdef varying.def.sc -i shaders/bgfx_shader.sh
    fi
done

#read  -n 1 -p "Done"

#.\shaderc.exe -f sandbox.vs -o sandbox.vs.bin --type v

#.\shaderc.exe --platform windows -f sandbox.vs -o dx11_sandbox.vs.bin --type v -p vs_5_0
#.\shaderc.exe --platform windows -f sandbox.fs -o dx11_sandbox.fs.bin --type f -p ps_5_0