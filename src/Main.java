import com.hedgemen.fx.Rectangle;
import com.hedgemen.fx.app.GameConfiguration;
import com.hedgemen.fx.app.GameInitializer;
import com.hedgemen.fx.graphics.*;
import com.hedgemen.fx.graphics.buffers.FrameBuffer;
import com.hedgemen.fx.input.Buttons;
import com.hedgemen.fx.input.DefaultInputHandler;
import com.hedgemen.fx.input.InputHandler;
import com.hedgemen.fx.input.Keys;
import com.hedgemen.fx.io.files.FileHandle;
import com.hedgemen.fx.io.files.FileType;
import com.hedgemen.fx.math.Matrix4;
import com.hedgemen.fx.platform.GraphicsAPI;
import com.hedgemen.fx.platform.HedgeClassLoader;
import com.hedgemen.fx.util.Sync;
import com.hedgemen.fx.util.logging.GameAppLogger;
import com.hedgemen.fx.util.logging.Logger;
import org.lwjgl.system.Configuration;
import org.lwjgl.system.Library;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Random;

import static org.lwjgl.bgfx.BGFX.*;
import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class Main {

    public static class DrawInfo {
        
        public float x, y, width, height;
        public Texture texture;
    }
    
    private static ArrayList<DrawInfo> draws = new ArrayList<>();
    
    private static StringBuilder typed = new StringBuilder();
    
    private static HedgeClassLoader classLoader;
    
    public static void main(String[] args) {
        
        classLoader = new HedgeClassLoader();
        
        //Configuration.LIBRARY_PATH.set(new File("runtime/hedgefx").getAbsolutePath());
        Configuration.SHARED_LIBRARY_EXTRACT_DIRECTORY.set(new File("runtime/hedgefx").getAbsolutePath());
        Configuration.SHARED_LIBRARY_EXTRACT_PATH.set(new File("runtime/hedgefx").getAbsolutePath());
        Library.initialize();
        
        GameConfiguration config = new GameConfiguration();
        config.setBackBufferWidth(1280);
        config.setBackBufferHeight(720);
        config.setTitle("");
        config.setUseVSync(false);
        config.setPreferredBackend(GraphicsAPI.DirectX11);
        
        Logger logger = new GameAppLogger();
        GameInitializer initializer = new GameInitializer(config, logger);
    
        GraphicsDevice graphicsDevice = new GraphicsDevice(initializer);
        
        graphicsDevice.setBackBufferWidth(1280);
        graphicsDevice.setBackBufferHeight(720);
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
    
    Texture[] textures;
    
    private void loadTexture() {
        texture3 = new Texture(new FileHandle("bunny.jpg", FileType.Local));
        texture = new Texture(new FileHandle("smile.png", FileType.Local));
        texture2 = new Texture(new FileHandle("heart.png", FileType.Local));
        durga = new Texture(new FileHandle("durga.png", FileType.Local));
        texture4 = new Texture(new FileHandle("116.jpg", FileType.Local));
        
        textures = new Texture[5];
        textures[0] = texture;
        textures[1] = texture2;
        textures[2] = texture3;
        textures[3] = durga;
        textures[4] = texture4;
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
    
        InputHandler inputHandler = new DefaultInputHandler();
        graphicsDevice.addListener(inputHandler);
        graphicsDevice.show();
    
        FrameBuffer frameBuffer = new FrameBuffer(graphicsDevice);
        
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
    
            if(inputHandler.isButtonDown(Buttons.Left)) {
                DrawInfo drawInfo = new DrawInfo();
                drawInfo.texture = textures[prng.nextInt(5)];
                drawInfo.width = 75;
                drawInfo.height = 75;
                drawInfo.x = inputHandler.cursorPosX() - (drawInfo.width / 2);
                drawInfo.y = inputHandler.cursorPosY() - (drawInfo.height / 2);
                draws.add(drawInfo);
            } else if(inputHandler.isKeyPressed(Keys.Escape)) {
                draws.clear();
            }
            
            typed.append(inputHandler.getTypedChars());
            
            if(inputHandler.isKeyPressed(Keys.Backspace)) {
                typed.delete(Math.max(0, typed.length() - 1), typed.length());
            }
            
            for(var key : inputHandler.getKeysPressed()) {
                System.out.println(key);
            }
            
            inputHandler.update();
            
            start = glfwGetTime();
    
            Rasterizer rasterizer = new Rasterizer(graphicsDevice);
    
            rasterizer.setScissor(new Rectangle(0, 0, graphicsDevice.getBackBufferWidth(), graphicsDevice.getBackBufferHeight()));
            spriteBatch.begin(rasterizer);
            int width = 50, height = 50;
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
    
            long totalMemory = Runtime.getRuntime().totalMemory() / 1000000;
            long usedMemory = (Runtime.getRuntime().totalMemory() -Runtime.getRuntime().freeMemory()) / 1000000;
            
            rasterizer.setScissor(new Rectangle(0, 0, graphicsDevice.getBackBufferWidth(), graphicsDevice.getBackBufferHeight()));
            
            frameBuffer.begin();
            
            spriteBatch.begin(rasterizer);
            
            for(DrawInfo drawInfo : draws) {
                spriteBatch.draw(drawInfo.texture, drawInfo.x, drawInfo.y, drawInfo.width, drawInfo.height, Color.white());
            }
            
            spriteBatch.drawString(font, (usedMemory + "/" + totalMemory + " MB"), 1000, 500, Color.red());
            spriteBatch.drawString(font, typed.toString(), 10, 10, Color.black());
            spriteBatch.end();
            
            graphicsDevice.frame();
            
            Texture frameBufferTexture = frameBuffer.end();
            spriteBatch.begin(rasterizer);
            spriteBatch.draw(frameBufferTexture, 0, 0, graphicsDevice.getBackBufferWidth(), graphicsDevice.getBackBufferHeight(), Color.white());
            spriteBatch.end();
            
            graphicsDevice.frame();
            
            sync.sync(60);
    
            end = glfwGetTime();
            elapsed += end - start;
            frames++;
            
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
