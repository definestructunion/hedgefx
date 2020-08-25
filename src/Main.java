import com.hgm.fx.app.GameConfiguration;
import com.hgm.fx.app.GameInitializer;
import com.hgm.fx.graphics.*;
import com.hgm.fx.graphics.renderers.SpriteBatch;
import com.hgm.fx.io.files.FileHandle;
import com.hgm.fx.io.files.FileType;
import com.hgm.fx.math.Matrix4;
import com.hgm.fx.platform.GraphicsAPI;
import com.hgm.fx.util.Sync;
import com.hgm.fx.util.logging.GameAppLogger;
import com.hgm.fx.util.logging.ILogger;
import org.lwjgl.system.Configuration;
import org.lwjgl.system.Library;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Random;

import static org.lwjgl.bgfx.BGFX.*;
import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;

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
        config.setUseVSync(false);
        config.setPreferredBackend(GraphicsAPI.OpenGL);
        
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
    
    Color clearColor = new Color(0x6495EDFF);//Color.lavender();
    
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
    
    Texture texture;
    Texture texture2;
    Texture durga;
    Texture texture3;
    Texture texture4;
    
    private void loadTexture() {
        texture3 = new Texture(new FileHandle("bunny.jpg", FileType.Local));
        texture = new Texture(new FileHandle("smile.png", FileType.Local));
        texture2 = new Texture(new FileHandle("heart.png", FileType.Local));
        durga = new Texture(new FileHandle("durga.png", FileType.Local));
        texture4 = new Texture(new FileHandle("116.jpg", FileType.Local));
    }
    
    ShaderProgram program;
    
    short textureUniform;
    
    SpriteBatch spriteBatch;
    
    Font font;
    
    private void init() {
        FileHandle ttf = new FileHandle("Seagram tfb.ttf", FileType.Local);
        //FileHandle ttf = new FileHandle("PIXELADE.TTF", FileType.Local);
        double start = 0, end = 0;
        start = glfwGetTime();
        try {
            font = new Font(ttf.read(), 24, false);
        } catch(Exception e) {
            e.printStackTrace();
        }
        end = glfwGetTime();
        
        System.out.println("Time taken: " + (end - start) + " MS");
        
        /*String katakana = "アイウエオ";
        for(int i = 0; i < katakana.length(); ++i) {
            System.out.println((int)katakana.charAt(i));
        }*/
        
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
        
        textureUniform = bgfx_create_uniform("texture", BGFX_UNIFORM_TYPE_VEC4, 1);
        
        loadTexture();
        
        proj = Matrix4.ortho(0, graphicsDevice.getBackBufferWidth(), 0, graphicsDevice.getBackBufferHeight(), -1.0f, 1.0f);
        view = Matrix4.identity();
        //loadTexture();
        
        spriteBatch = new SpriteBatch(graphicsDevice, program);
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
        bgfx_set_view_clear(0, BGFX_CLEAR_COLOR | BGFX_CLEAR_DEPTH, clearColor.hexInt(), 1.0f, 0);
        bgfx_reset(graphicsDevice.getBackBufferWidth(), graphicsDevice.getBackBufferHeight(), config.useVSync() ? BGFX_RESET_VSYNC : BGFX_RESET_NONE, BGFX_TEXTURE_FORMAT_RGBA8);
        
        graphicsDevice.show();
        
        double start = 0, end = 0;
        double elapsed = 0;
        
        int offset = 0;
    
        System.out.println(ClassLoader.getSystemClassLoader());
    
        URL url = null;
        try {
            url = new File("Test.jar").toURI().toURL();
            var testClass = Class.forName("Test", true, new URLClassLoader(new URL[] { url }));
            Object obj = testClass.getDeclaredConstructor().newInstance();
            Method method = testClass.getMethod("test");
            method.invoke(obj);
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        while(graphicsDevice.isRunning()) {
            x += 1.0f;
            glfwPollEvents();
            
            start = glfwGetTime();
            
            spriteBatch.begin();
    
            int width = 64, height = 64;
            int ps = 16;
            int s = 16;
            int sw = font.getTexture().getWidth();
            int sh = font.getTexture().getHeight();
            int subtractAmount = (height % 2 == 0) ? 1 : 1;
            for(int x = 0; x < width; ++x) {
                for(int y = 0; y < height; ++y) {
                    if(x == y || x == height - y - subtractAmount) spriteBatch.draw(texture3, x * ps + offset, y * ps + offset, s, s, color(prng));
                    else
                        spriteBatch.draw(texture4, x * ps + offset, y * ps + offset, s, s, color(prng));
                }
            }
            
            //++offset;
            
            spriteBatch.draw(font.getTexture(), 25 + offset, 500, sw, sh, col1);
            
            spriteBatch.drawString(font, "Hello!\nThere!\nMy name is Mattheus. I am a disciple of Durga, and I crush all who would appose Durga.\n" +
                                                 "Convert or be burned at the stake for your sins.", 25, 550, col1);
    
    
            spriteBatch.draw(durga, 700, 2, 500, 500, new Color(0xFFFFFFFF));
            
            spriteBatch.end();
            //spriteBatch.flush();
            graphicsDevice.frame();
            
            //sync.sync(60);
            
            end = glfwGetTime();
    
            elapsed += end - start;
            frames++;
    
            if(elapsed >= 1) {
                elapsed = 0;
                System.out.println("FPS: " + frames + " drawing: " + width * height + " squares");
                frames = 0;
            }
        }
        
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            program.dispose();
            spriteBatch.dispose();
            graphicsDevice.dispose();
        }));
    }
    
    private static Color color(Random prng) {
        //return Float.floatToIntBits(prng.nextInt(2147483647) | (255) << 24);
        //return Float.floatToIntBits(prng.nextInt(2147483647));
        
        float a = 1.0f;
        //float a = (prng.nextFloat() > 0.5f) ? 1.0f : 0.0f;
        //float a = prng.nextFloat();
        
        return new Color(prng.nextFloat(), prng.nextFloat(), prng.nextFloat(), a);
    }
}
