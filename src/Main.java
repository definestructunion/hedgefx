import hedge.fx.app.GameConfiguration;
import hedge.fx.app.GameInitializer;
import hedge.fx.graphics.Color;
import hedge.fx.graphics.GraphicsDevice;
import hedge.fx.graphics.ShaderProgram;
import hedge.fx.graphics.SpriteBatch;
import hedge.fx.graphics.buffers.*;
import hedge.fx.io.files.FileHandle;
import hedge.fx.io.files.FileType;
import hedge.fx.math.Matrix4;
import hedge.fx.platform.GraphicsAPI;
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
import java.util.Random;

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
        config.setBackBufferWidth(1280);
        config.setBackBufferHeight(720);
        config.setTitle("");
        config.setPreferredBackend(GraphicsAPI.DirectX11);
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
    
    Color cornflowerBlue = new Color(0x6495EDFF);
    
    Color clearColor = Color.lavender();
    
    Color col1 = new Color(1, 0, 0, 1);
    Color col2 = new Color(0, 0, 1, 1);
    Color col3 = new Color(0, 1, 0, 1);
    Color col4 = new Color(1, 1, 0, 1);
    
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
    
        // 2d texture arrays are supported, but im not sure how to implement them
        
        texture2 = bgfx_create_texture_2d(width2[0], height2[0], false, 1, BGFX_TEXTURE_FORMAT_RGBA8,
                BGFX_SAMPLER_U_CLAMP | BGFX_SAMPLER_V_CLAMP, tex2Mem);
        
        stbi_image_free(textureBuffer);
        stbi_image_free(textureBuffer2);
    }
    
    ShaderProgram program;
    
    short textureUniform;
    
    BGFXMemory vboMem;
    
    VertexAttributes vertexAttributes;
    VertexLayout vertexLayout;
    VertexBuffer vertexBuffer;
    IndexBuffer indexBuffer;
    
    VertexBuffer vertexBuffer2;
    IndexBuffer indexBuffer2;
    
    SpriteBatch spriteBatch;
    
    private void init() {
        
        System.out.println("Renderer: " + GraphicsAPI.values()[bgfx_get_renderer_type()]);
        
        if(api.equals(GraphicsAPI.OpenGL)) {
            program = new ShaderProgram(
                    new FileHandle("shaders/gl_sandbox.vs.bin", FileType.Local),
                    new FileHandle("shaders/gl_sandbox.fs.bin", FileType.Local)
            );
        }
    
        else if(api.equals(GraphicsAPI.DirectX11) || api.equals(GraphicsAPI.DirectX12)) {
            program = new ShaderProgram(
                    new FileHandle("shaders/dx_sandbox.vs.bin", FileType.Local),
                    new FileHandle("shaders/dx_sandbox.fs.bin", FileType.Local)
            );
        }
    
        if(api.equals(GraphicsAPI.Vulkan)) {
            program = new ShaderProgram(
                    new FileHandle("shaders/vk_sandbox.vs.bin", FileType.Local),
                    new FileHandle("shaders/vk_sandbox.fs.bin", FileType.Local)
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
        
        spriteBatch = new SpriteBatch(graphicsDevice, vertexLayout, program);
    }
    
    short frameBuffer;
    
    Random prng = new Random();
    
    float x = 0.0f;
    int frames = 0;
    public void run() {
        //graphicsDevice.setClearColor(clearColor);
        init();
        
        Sync sync = new Sync();
        
        bgfx_set_view_transform(0, view.getBuffer(), proj.getBuffer());
        bgfx_set_view_rect(0, 0, 0, graphicsDevice.getBackBufferWidth(), graphicsDevice.getBackBufferHeight());
        bgfx_set_view_scissor(0, 0, 0, graphicsDevice.getBackBufferWidth(), graphicsDevice.getBackBufferHeight());
        bgfx_set_view_clear(0, BGFX_CLEAR_COLOR | BGFX_CLEAR_DEPTH, cornflowerBlue.hexInt(), 1.0f, 0);
        bgfx_reset(graphicsDevice.getBackBufferWidth(), graphicsDevice.getBackBufferHeight(), config.useVSync() ? BGFX_RESET_VSYNC : BGFX_RESET_NONE, BGFX_TEXTURE_FORMAT_RGBA8);
    
        graphicsDevice.show();
        
        double start = 0, end = 0;
        double elapsed = 0;
        
        while(graphicsDevice.isRunning()) {
            x += 1.0f;
            glfwPollEvents();
            
            start = glfwGetTime();
            
            /*bgfx_encoder_set_state(encoder, BGFX_STATE_WRITE_RGB
													| BGFX_STATE_WRITE_A
													| BGFX_STATE_WRITE_Z
													| BGFX_STATE_DEPTH_TEST_LESS
													| BGFX_STATE_MSAA, 0);*/
            
            spriteBatch.begin();
            
            int width = 12, height = 12;
            int ps = 12;
            int s = 12;
            int subtractAmount = (height % 2 == 0) ? 1 : 1;
            for(int x = 0; x < width; ++x) {
                for(int y = 0; y < height; ++y) {
                    //if(x == y || x == height - y - subtractAmount) spriteBatch.draw(texture2, x * ps, y * ps, s, s, col2.hex);
                    //else spriteBatch.draw(texture, x * ps, y * ps, s, s, col4.hex);
                    if(x == y || x == height - y - subtractAmount) spriteBatch.draw(texture2, x * ps, y * ps, s, s, color(prng));
                    else spriteBatch.draw(texture, x * ps, y * ps, s, s, color(prng));
                }
            }
            
            spriteBatch.end();
            graphicsDevice.frame();
    
            sync.sync(60);
            
            end = glfwGetTime();
    
            elapsed += end - start;
            frames++;
    
            if(elapsed >= 1) {
                elapsed = 0;
                System.out.println("FPS: " + frames + " drawing: " + width * height + " squares");
                spriteBatch.setTotalDrawCalls(0L);
                frames = 0;
            }
        }
        
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("hello");
            program.dispose(); // prints Disposing
        }));
    }
    
    private static float color(Random prng) {
        return Float.floatToIntBits(prng.nextInt(2147483647));
    }
}