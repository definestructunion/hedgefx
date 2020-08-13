import hedge.fx.app.GameConfiguration;
import hedge.fx.app.GameInitializer;
import hedge.fx.graphics.Color;
import hedge.fx.graphics.GraphicsDevice;
import hedge.fx.graphics.ShaderProgram;
import hedge.fx.graphics.buffers.*;
import hedge.fx.io.files.FileHandle;
import hedge.fx.io.files.FileType;
import hedge.fx.math.Matrix4;
import hedge.fx.platform.GraphicsAPI;
import hedge.fx.util.BufferUtils;
import hedge.fx.util.Sync;
import hedge.fx.util.logging.GameAppLogger;
import hedge.fx.util.logging.ILogger;
import org.lwjgl.bgfx.BGFXMemory;
import org.lwjgl.bgfx.BGFXVertexLayout;
import org.lwjgl.system.Configuration;
import org.lwjgl.system.Library;

import java.awt.*;
import java.io.File;
import java.nio.ByteBuffer;

import static org.lwjgl.bgfx.BGFX.*;
import static org.lwjgl.bgfx.BGFX.BGFX_ATTRIB_TYPE_UINT8;
import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;

import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load_from_memory;

public class Main {

    public static void main(String[] args) {
        
        //Configuration.LIBRARY_PATH.set(new File("runtime/hedgefx").getAbsolutePath());
        Configuration.SHARED_LIBRARY_EXTRACT_DIRECTORY.set(new File("runtime/hedgefx").getAbsolutePath());
        Configuration.SHARED_LIBRARY_EXTRACT_PATH.set(new File("runtime/hedgefx").getAbsolutePath());
        Library.initialize();
        
        GameConfiguration config = new GameConfiguration();
        config.setPreferredBackend(GraphicsAPI.OpenGL);
        config.setUseVSync(false);
        
        ILogger logger = new GameAppLogger();
        GameInitializer initializer = new GameInitializer(config, logger);
    
        GraphicsDevice graphicsDevice = new GraphicsDevice(initializer);
        
        Main mainObj = new Main(graphicsDevice, config);
        mainObj.run();
    }
    
    public Main(GraphicsDevice graphicsDevice, GameConfiguration config) {
        this.graphicsDevice = graphicsDevice;
        api = graphicsDevice.getApi();
        this.config = config;
    }
    
    GameConfiguration config;
    GraphicsDevice graphicsDevice;
    GraphicsAPI api;
    
    Color clearColor = Color.lavender();
    int clearColorHex = clearColor.hexInt();
    
    short fragmentShader;
    short vertexShader;
    
    BGFXVertexLayout layout;
    short layoutHandle;
    short vbo, vbo2;
    short ibo;
    
    Color col1 = new Color(1, 0, 0, 1);
    Color col2 = new Color(0, 0, 1, 1);
    Color col3 = new Color(0, 1, 0, 1);
    Color col4 = new Color(1, 1, 0, 1);
    
    //Color col1 = new Color(1, 0, 1, 1);
    
    int spriteCount = 1;
    
    //float[] bigVertices = new float[Short.MAX_VALUE - 1024];
    //short[] bigIndices = new short[Short.MAX_VALUE - 1024];
    
    //float[] bigVertices = new float[24 * spriteCount];
    //short[] bigIndices = new short[12 * spriteCount];
    
    //float[] bigVertices2 = new float[24 * spriteCount];
    //short[] bigIndices2 = new short[12 * spriteCount];
    
    float[] vertices = {
            10, 10, 0, 0, 0,     col1.hex,
            10, 100, 0, 0, 1,    col2.hex,
            100, 100, 0, 1, 1,   col3.hex,
            100, 10, 0, 1, 0,    col4.hex,
    };
    
    float[] vertices2 = {
            100, 100, 0, 0, 0,    col1.hex,
            100, 190, 0, 0, 1,    col2.hex,
            190, 190, 0, 1, 1,    col3.hex,
            190, 100, 0, 1, 0,    col4.hex,
    };
    
    private void setVertices(ByteBuffer vertices, float x, float y, float w, float h) {
        //vertices.rewind();
        vertices.putFloat(0*4, x);
        vertices.putFloat(1*4, y);
    
        vertices.putFloat(6*4, x);
        vertices.putFloat(7*4, y+h);
    
        vertices.putFloat(12*4, x+w);
        vertices.putFloat(13*4, y+h);
    
        vertices.putFloat(18*4, x+w);
        vertices.putFloat(19*4, y);
        
        /*System.out.println(vertices.getFloat(0));
        System.out.println(vertices.getFloat(1));
    
        System.out.println(vertices.getFloat(6));
        System.out.println(vertices.getFloat(7));
    
        System.out.println(vertices.getFloat(12));
        System.out.println(vertices.getFloat(13));
    
        System.out.println(vertices.getFloat(18));
        System.out.println(vertices.getFloat(19));
        
        System.out.println();
        System.out.println();
        System.out.println();*/
    }
    
    private void setVertices(float[] vertices, float x, float y, float w, float h) {
        vertices[0] = x;
        vertices[1] = y;
        
        vertices[6] = x;
        vertices[7] = y+h;
        
        vertices[12] = x+w;
        vertices[13] = y+h;
        
        vertices[18] = x+w;
        vertices[19] = y;
    }
    
    short[] indices = {
            0, 1, 2,
            2, 3, 0,
    };
    
    short[] indices2 = {
            0, 1, 2,
            2, 3, 0,
    };
    
    Matrix4 proj;
    Matrix4 view;
    
    short texture;
    short texture2;
    
    //short colorUniform;
    //float[] color = { Color.blue().hex };
    //ByteBuffer colorBuffer;
    
    private void loadTexture() {
        int[] width = new int[1];
        int[] height = new int[1];
        int[] bitsPerPixel = new int[1];
    
        int[] width2 = new int[1];
        int[] height2 = new int[1];
        int[] bitsPerPixel2 = new int[1];
        
        ByteBuffer textureBuffer = stbi_load_from_memory(new FileHandle("116.jpg", FileType.Local).readByteBuffer(), width, height, bitsPerPixel, 4);
        ByteBuffer textureBuffer2 = stbi_load_from_memory(new FileHandle("bunny.jpg", FileType.Local).readByteBuffer(), width2, height2, bitsPerPixel2, 4);
        //ByteBuffer textureBuffer = stbi_load("116.jpg", width, height, bitsPerPixel, 4);
        
        BGFXMemory texMem = bgfx_copy(textureBuffer);
        BGFXMemory tex2Mem = bgfx_copy(textureBuffer2);
        
        texture = bgfx_create_texture_2d(width[0], height[0], false, 1, BGFX_TEXTURE_FORMAT_RGBA8,
                BGFX_SAMPLER_U_CLAMP | BGFX_SAMPLER_V_CLAMP, texMem);
    
        texture2 = bgfx_create_texture_2d(width2[0], height2[0], false, 1, BGFX_TEXTURE_FORMAT_RGBA8,
                BGFX_SAMPLER_U_CLAMP | BGFX_SAMPLER_V_CLAMP, tex2Mem);
        
        stbi_image_free(textureBuffer);
        stbi_image_free(textureBuffer2);
    }
    
    ShaderProgram program;
    
    short textureUniform;
    short textureUniform2;
    
    BGFXMemory vboMem;
    
    VertexAttributes vertexAttributes;
    VertexLayout vertexLayout;
    VertexBuffer vertexBuffer;
    IndexBuffer indexBuffer;
    
    VertexBuffer vertexBuffer2;
    IndexBuffer indexBuffer2;
    
    //ByteBuffer bigVerticesBuffer;
    //ByteBuffer bigIndicesBuffer;
    
    //ByteBuffer bigVertices2Buffer;
    //ByteBuffer bigIndices2Buffer;
    
    private void init() {
        
        /*for(int i = 0; i < bigVertices.length; i += 24) {
            
            int x = 0, y = 0, w = 0, h = 0;
    
            x = 10;
            y = 10;
            w = 310;
            h = 310;
            
            bigVertices[i + 0] = x;
            bigVertices[i + 1] = y;
            bigVertices[i + 2] = 0;
            bigVertices[i + 3] = col1.hex;
            bigVertices[i + 4] = 0;
            bigVertices[i + 5] = 0;
    
            bigVertices[i + 6] = x;
            bigVertices[i + 7] = h;
            bigVertices[i + 8] = 0;
            bigVertices[i + 9] = col2.hex;
            bigVertices[i + 10] = 0;
            bigVertices[i + 11] = 1;
    
            bigVertices[i + 12] = w;
            bigVertices[i + 13] = h;
            bigVertices[i + 14] = 0;
            bigVertices[i + 15] = col3.hex;
            bigVertices[i + 16] = 1;
            bigVertices[i + 17] = 1;
    
            bigVertices[i + 18] = w;
            bigVertices[i + 19] = y;
            bigVertices[i + 20] = 0;
            bigVertices[i + 21] = col4.hex;
            bigVertices[i + 22] = 1;
            bigVertices[i + 23] = 0;
        }
        
        for(short i = 0; i < bigIndices.length; i += 12) {
            bigIndices[i + 0] = (short)(i + 0);
            bigIndices[i + 1] = (short)(i + 1);
            bigIndices[i + 2] = (short)(i + 2);
            bigIndices[i + 3] = (short)(i + 2);
            bigIndices[i + 4] = (short)(i + 3);
            bigIndices[i + 5] = (short)(i + 0);
    
            bigIndices[i + 6] = (short)(i + 4);
            bigIndices[i + 7] = (short)(i + 5);
            bigIndices[i + 8] = (short)(i + 6);
            bigIndices[i + 9] = (short)(i + 6);
            bigIndices[i + 10] = (short)(i + 7);
            bigIndices[i + 11] = (short)(i + 4);
        }
    
        for(int i = 0; i < bigVertices2.length; i += 24) {
        
            int x = 0, y = 0, w = 0, h = 0;
    
            x = 320;
            y = 320;
            w = 620;
            h = 620;
        
            bigVertices2[i + 0] = x;
            bigVertices2[i + 1] = y;
            bigVertices2[i + 2] = 0;
            bigVertices2[i + 3] = col1.hex;
            bigVertices2[i + 4] = 0;
            bigVertices2[i + 5] = 0;
            
            bigVertices2[i + 6] = x;
            bigVertices2[i + 7] = h;
            bigVertices2[i + 8] = 0;
            bigVertices2[i + 9] = col2.hex;
            bigVertices2[i + 10] = 0;
            bigVertices2[i + 11] = 1;
            
            bigVertices2[i + 12] = w;
            bigVertices2[i + 13] = h;
            bigVertices2[i + 14] = 0;
            bigVertices2[i + 15] = col3.hex;
            bigVertices2[i + 16] = 1;
            bigVertices2[i + 17] = 1;
            
            bigVertices2[i + 18] = w;
            bigVertices2[i + 19] = y;
            bigVertices2[i + 20] = 0;
            bigVertices2[i + 21] = col4.hex;
            bigVertices2[i + 22] = 1;
            bigVertices2[i + 23] = 0;
        }
    
        for(short i = 0; i < bigIndices2.length; i += 12) {
            bigIndices2[i + 0] = (short)(i + 0);
            bigIndices2[i + 1] = (short)(i + 1);
            bigIndices2[i + 2] = (short)(i + 2);
            bigIndices2[i + 3] = (short)(i + 2);
            bigIndices2[i + 4] = (short)(i + 3);
            bigIndices2[i + 5] = (short)(i + 0);
            
            bigIndices2[i + 6] = (short)(i + 4);
            bigIndices2[i + 7] = (short)(i + 5);
            bigIndices2[i + 8] = (short)(i + 6);
            bigIndices2[i + 9] = (short)(i + 6);
            bigIndices2[i + 10] = (short)(i + 7);
            bigIndices2[i + 11] = (short)(i + 4);
        }*/
    
        /*bigVerticesBuffer = BufferUtils.toByteBuffer(bigVertices);
        bigIndicesBuffer = BufferUtils.toByteBuffer(bigIndices);
        
        bigVertices2Buffer = BufferUtils.toByteBuffer(bigVertices2);
        bigIndices2Buffer = BufferUtils.toByteBuffer(bigIndices2);*/
        
        System.out.println("Renderer: " + GraphicsAPI.values()[bgfx_get_renderer_type()]);
        
        if(api.equals(GraphicsAPI.OpenGL)) {
            program = new ShaderProgram(
                    new FileHandle("shaders/sandbox.vs.bin", FileType.Local),
                    new FileHandle("shaders/sandbox.fs.bin", FileType.Local)
            );
        }
    
        else if(api.equals(GraphicsAPI.DirectX11)) {
            program = new ShaderProgram(
                    new FileHandle("shaders/dx11_sandbox.vs.bin", FileType.Local),
                    new FileHandle("shaders/dx11_sandbox.fs.bin", FileType.Local)
            );
        }
        
        //System.out.println(program.getHandle());
        
        textureUniform = bgfx_create_uniform("texture", BGFX_UNIFORM_TYPE_VEC4, 1);
        
        vertexAttributes = new VertexAttributes(
                new VertexAttribute(BGFX_ATTRIB_POSITION, 3, BGFX_ATTRIB_TYPE_FLOAT, false, false),
                new VertexAttribute(BGFX_ATTRIB_TEXCOORD0, 2, BGFX_ATTRIB_TYPE_FLOAT, true, false),
                new VertexAttribute(BGFX_ATTRIB_COLOR0, 4, BGFX_ATTRIB_TYPE_UINT8, true, false)
        );
        
        vertexLayout = new VertexLayout(vertexAttributes, graphicsDevice);
        
        vertexBuffer = new VertexBuffer(vertices, vertexLayout, graphicsDevice);
        indexBuffer = new IndexBuffer(indices, graphicsDevice);
    
        vertexBuffer2 = new VertexBuffer(vertices2, vertexLayout, graphicsDevice);
        indexBuffer2 = new IndexBuffer(indices2, graphicsDevice);
        
        loadTexture();
        
        proj = Matrix4.ortho(0, graphicsDevice.getBackBufferWidth(), 0, graphicsDevice.getBackBufferHeight(), -1.0f, 1.0f);
        view = Matrix4.identity();
    
        try {
            //BufferedImage.TYPE_INT_ARGB
            Graphics2D graphics2D = null;
            Font font = Font.createFont(Font.TRUETYPE_FONT, new FileHandle("PIXELADE.TTF", FileType.Local).read());
            System.out.println(font.getNumGlyphs());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //loadTexture();
    }
    
    short frameBuffer;
    
    float x = 0.0f;
    int frames = 0;
    public void run() {
        //graphicsDevice.setClearColor(clearColor);
        init();
        
        Sync sync = new Sync();
        
        bgfx_set_view_transform(0, view.getBuffer(), proj.getBuffer());
        bgfx_set_view_rect(0, 0, 0, graphicsDevice.getBackBufferWidth(), graphicsDevice.getBackBufferHeight());
        bgfx_set_view_scissor(0, 0, 0, graphicsDevice.getBackBufferWidth(), graphicsDevice.getBackBufferHeight());
        bgfx_set_view_clear(0, BGFX_CLEAR_COLOR | BGFX_CLEAR_DEPTH, clearColorHex, 1.0f, 0);
        bgfx_reset(graphicsDevice.getBackBufferWidth(), graphicsDevice.getBackBufferHeight(), config.useVSync() ? BGFX_RESET_VSYNC : BGFX_RESET_NONE, BGFX_TEXTURE_FORMAT_RGBA8);
        
        graphicsDevice.show();
        
        double start = 0, end = 0;
        double elapsed = 0;
        
        while(graphicsDevice.isRunning()) {
            x += 1.0f;
            glfwPollEvents();
            
            start = glfwGetTime();
    
            setVertices(vertices, 25 + x, 25 + x, 150.0f, 150.0f);
            ByteBuffer buffer = BufferUtils.toByteBuffer(vertices);
            setVertices(buffer, 25 + x, 25 + x, 150, 150);
            vertexBuffer.setVertices(buffer);
            
            frameBuffer = bgfx_create_frame_buffer(graphicsDevice.getBackBufferWidth(), graphicsDevice.getBackBufferHeight(), BGFX_TEXTURE_FORMAT_RGBA8,
                    BGFX_SAMPLER_MIN_POINT | BGFX_SAMPLER_MAG_POINT);
            
            int forCalls = 100;
            for(int i = 0; i < forCalls; ++i) {
                
                //bgfx_set_view_frame_buffer(0, frameBuffer);
    
                long encoder = bgfx_encoder_begin(false);
                bgfx_encoder_set_texture(encoder, 0, textureUniform, texture2, 0xFFFFFFFF);
                bgfx_encoder_set_dynamic_index_buffer(encoder, indexBuffer.getHandle(), 0, indices.length);
                bgfx_encoder_set_dynamic_vertex_buffer(encoder, 0, vertexBuffer.getHandle(), 0, vertices.length / 24, vertexLayout.getHandle());
                bgfx_encoder_submit(encoder, 0, program.getHandle(), 0, true);
			
			    /*bgfx_encoder_set_state(encoder, BGFX_STATE_WRITE_RGB
													| BGFX_STATE_WRITE_A
													| BGFX_STATE_WRITE_Z
													| BGFX_STATE_DEPTH_TEST_LESS
													| BGFX_STATE_MSAA, 0);*/
    
                bgfx_encoder_set_texture(encoder, 0, textureUniform2, texture, 0xFFFFFFFF);
                bgfx_encoder_set_dynamic_index_buffer(encoder, indexBuffer2.getHandle(), 0, indices2.length);
                bgfx_encoder_set_dynamic_vertex_buffer(encoder, 0, vertexBuffer2.getHandle(), 0, vertices2.length / 24, vertexLayout.getHandle());
                bgfx_encoder_submit(encoder, 0, program.getHandle(), 0, true);
                bgfx_encoder_end(encoder);
    
                //bgfx_set_view_frame_buffer(0, frameBuffer);
            }
    
            //bgfx_update_texture_2d();
    
            //bgfx_frame(false);
            //sync.sync(60);
            bgfx_frame(false);
            
            //if(i % 60 == 0) System.out.println(i / 60 + " seconds");
            //if(i % 1000 == 0) System.out.println(i);
            
            end = glfwGetTime();
            
            elapsed += end - start;
            
            frames++;
    
            if(elapsed >= 1) {
                elapsed = 0;
                System.out.println("FPS: " + frames + " drawing: " + spriteCount * frames * forCalls + 2 + " squares");
                frames = 0;
            }
        }
        
        program.dispose();
    }
}
