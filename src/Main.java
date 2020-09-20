import com.hedgemen.fx.Rectangle;
import com.hedgemen.fx.app.GameConfiguration;
import com.hedgemen.fx.app.GameInitializer;
import com.hedgemen.fx.graphics.*;
import com.hedgemen.fx.input.DefaultInputHandler;
import com.hedgemen.fx.input.Keys;
import com.hedgemen.fx.io.files.FileHandle;
import com.hedgemen.fx.io.files.FileType;
import com.hedgemen.fx.math.Matrix4;
import com.hedgemen.fx.platform.GraphicsAPI;
import com.hedgemen.fx.platform.HedgeClassLoader;
import com.hedgemen.fx.util.Sync;
import com.hedgemen.fx.util.logging.GameAppLogger;
import com.hedgemen.fx.util.logging.ILogger;
import org.lwjgl.system.Configuration;
import org.lwjgl.system.Library;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Random;

import static org.lwjgl.bgfx.BGFX.*;
import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;

public class Main {

    private static HedgeClassLoader classLoader;
    
    public static void main(String[] args) {
        
        classLoader = new HedgeClassLoader();
        
        try {
            //URLClassLoader urlClassLoader = new URLClassLoader(new URL[] { Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().toURL() });
            //Thread.currentThread().setContextClassLoader(urlClassLoader);
        } catch(Exception e) {
            e.printStackTrace();
        }
        
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
        
        graphicsDevice.setTitle("Hedgemen");
        graphicsDevice.applyChanges();
        
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
        //FileHandle ttf = new FileHandle("Seagram tfb.ttf", FileType.Local);
        FileHandle ttf = new FileHandle("PIXELADE.TTF", FileType.Local);
        double start = 0, end = 0;
        start = glfwGetTime();
        try {
            font = new Font(ttf.read(), 24, false);
        } catch(Exception e) {
            e.printStackTrace();
        }
        end = glfwGetTime();
        
        System.out.println("Time taken: " + (end - start) + " MS");
        
        //font = new Font(new java.awt.Font("Helvetica", java.awt.Font.PLAIN, 16));
        
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
    
    Random prng = new Random();
    
    short startFrameBuffer() {
        short frameBuffer = bgfx_create_frame_buffer(1280, 720, BGFX_TEXTURE_FORMAT_RGBA8, BGFX_SAMPLER_U_CLAMP |
                                                                                                   BGFX_SAMPLER_V_CLAMP |
                                                                                                   BGFX_SAMPLER_MIN_POINT |
                                                                                                   BGFX_SAMPLER_MAG_POINT);
        bgfx_set_view_frame_buffer(0, frameBuffer);
        return frameBuffer;
    }
    
    Texture endFrameBuffer(short frameBuffer) {
        bgfx_set_view_frame_buffer(0, BGFX_INVALID_HANDLE);
        return new Texture(frameBuffer, 1280, 720);
    }
    
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
        
        double start = 0, end = 0;
        double elapsed = 0;
        
        float offset = 0;
    
        System.out.println(ClassLoader.getSystemClassLoader());
        
        try {
            classLoader.addURL(new File("Test.jar").toURI().toURL());
            Class<?> clz = classLoader.loadClass("Test");
            Object main = clz.getDeclaredConstructor().newInstance();
            Method test = clz.getMethod("test");
            test.invoke(main);
            
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        int mx = 0;
        int my = 0;
        
        boolean mr = true;
        boolean ml = false;
        boolean mu = false;
        boolean md = false;
    
        ArrayList<Color> colors = new ArrayList<>();
    
        DefaultInputHandler inputHandler = new DefaultInputHandler();
        graphicsDevice.addListener(inputHandler);
        graphicsDevice.show();
        
        while(graphicsDevice.isRunning()) {
            
            if(mx == 200 && my == 0) {
                mr = false;
                ml = false;
                mu = false;
                md = true;
            }
    
            if(mx == 200 && my == 200) {
                mr = false;
                ml = true;
                mu = false;
                md = false;
            }
    
            if(mx == 0 && my == 200) {
                mr = false;
                ml = false;
                mu = true;
                md = false;
            }
    
            if(mx == 0 && my == 0) {
                mr = true;
                ml = false;
                mu = false;
                md = false;
            }
            
            if(mr) mx++;
            if(ml) mx--;
            if(mu) my--;
            if(md) my++;
            
            x += 1.0f;
            graphicsDevice.pollEvents();
    
            if(inputHandler.getTypedChars().length() > 0)
                System.out.println(inputHandler.getTypedChars());
    
            inputHandler.update();
            
            start = glfwGetTime();
    
            Rasterizer rasterizer = new Rasterizer(graphicsDevice);
    
            rasterizer.setScissor(new Rectangle(0, 0, graphicsDevice.getBackBufferWidth(), graphicsDevice.getBackBufferHeight()));
            spriteBatch.begin(rasterizer);
            int width = 256, height = 256;
            int ps = 16;
            int s = 16;
            int sw = font.getTexture().getWidth();
            int sh = font.getTexture().getHeight();
            int subtractAmount = (height % 2 == 0) ? 1 : 1;
            for(int x = 0; x < width; ++x) {
                for(int y = 0; y < height; ++y) {
                    if(x == y || x == height - y - subtractAmount) spriteBatch.draw(texture3, x * ps + mx, y * ps + my, s, s, color(prng));
                    else
                        spriteBatch.draw(texture4, x * ps + mx, y * ps + my, s, s, color(prng));
                }
            }
            spriteBatch.end();
            
            rasterizer.setScissor(new Rectangle(0, 0, 150, 150));
            spriteBatch.begin(rasterizer);
            spriteBatch.draw(durga, 25, 25, 175, 175, Color.white());
            spriteBatch.end();
            
            rasterizer.setScissor(new Rectangle(400, 400, 400, 400));
            spriteBatch.begin(rasterizer);
            spriteBatch.draw(texture4, 350, 350, 450, 450, Color.white());
            spriteBatch.end();
    
            rasterizer.setScissor(new Rectangle(0, 0, graphicsDevice.getBackBufferWidth(), graphicsDevice.getBackBufferHeight()));
            spriteBatch.begin(rasterizer);
            spriteBatch.draw(texture4, inputHandler.cursorPosX(), inputHandler.cursorPosY(), 75, 75, Color.white());
            spriteBatch.end();
            
            graphicsDevice.frame();
    
            end = glfwGetTime();
    
            elapsed += end - start;
            frames++;
            
            sync.sync(60);
    
            if(elapsed >= 1) {
                elapsed = 0;
                System.out.println("FPS: " + frames);
                frames = 0;
                //System.gc();
            }
        }
        
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            program.dispose();
            spriteBatch.dispose();
            graphicsDevice.dispose();
        }));
    }
    
    private static Color staticColor = new Color(1, 1, 1, 1);
    
    private static Color color(Random prng) {
        float a = 1.0f;
        staticColor.r = prng.nextFloat();
        staticColor.g = prng.nextFloat();
        staticColor.b = prng.nextFloat();
        staticColor.a = a;
        staticColor.hex = Float.intBitsToFloat(((int)(255 * staticColor.a) << 24) | ((int)(255 * staticColor.b) << 16) | ((int)(255 * staticColor.g) << 8) | ((int)(255 * staticColor.r)));
        return staticColor;
    }
}
