package xyz.Dot.utils.shader;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glUniform1;

public class ShaderManager {

    public static ShaderUtil blurShader = new ShaderUtil("dot/shaders/gaussian.frag");

    public static Framebuffer framebuffer = new Framebuffer(1, 1, false);

    private static final ArrayList<Runnable> tasks = new ArrayList<>();
    private static final ArrayList<Runnable> bloomTasks = new ArrayList<>();

    public static ArrayList<Runnable> getTasks() {
        return tasks;
    }

    public static ArrayList<Runnable> getBloomTasks() {
        return bloomTasks;
    }

    public static void setupUniforms(float dir1, float dir2, float radius) {
        blurShader.setUniformi("textureIn", 0);
        blurShader.setUniformf("texelSize", 1.0F / (float) Minecraft.getMinecraft().displayWidth, 1.0F / (float) Minecraft.getMinecraft().displayHeight);
        blurShader.setUniformf("direction", dir1, dir2);
        blurShader.setUniformf("radius", radius);

        final FloatBuffer weightBuffer = BufferUtils.createFloatBuffer(256);
        for (int i = 0; i <= radius; i++) {
            weightBuffer.put(calculateGaussianValue(i, radius / 2));
        }

        weightBuffer.rewind();
        glUniform1(blurShader.getUniform("weights"), weightBuffer);
    }


    public static float calculateGaussianValue(float x, float sigma) {
        double PI = 3.141592653;
        double output = 1.0 / Math.sqrt(2.0 * PI * (sigma * sigma));
        return (float) (output * Math.exp(-(x * x) / (2.0 * (sigma * sigma))));
    }


    public static void renderBlur(float radius) {
        GlStateManager.enableBlend();
        GlStateManager.color(1, 1, 1, 1);
        OpenGlHelper.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ZERO);


        framebuffer = createFrameBuffer(framebuffer);

        framebuffer.framebufferClear();
        framebuffer.bindFramebuffer(true);
        blurShader.init();
        setupUniforms(1, 0, radius);

        bindTexture(Minecraft.getMinecraft().getFramebuffer().framebufferTexture);

        ShaderUtil.drawQuads();
        framebuffer.unbindFramebuffer();
        blurShader.unload();

        Minecraft.getMinecraft().getFramebuffer().bindFramebuffer(true);
        blurShader.init();
        setupUniforms(0, 1, radius);

        bindTexture(framebuffer.framebufferTexture);
        ShaderUtil.drawQuads();
        blurShader.unload();

        GlStateManager.resetColor();
        GlStateManager.bindTexture(0);
    }

    public static void addBlurTask(Runnable context){

        if(Minecraft.getMinecraft().gameSettings.ofFastRender){
            return;
        }
        tasks.add(context);

    }

    public static void addBloomTask(Runnable context){
        bloomTasks.add(context);

    }

    public static void bindTexture(int texture) {
        glBindTexture(GL_TEXTURE_2D, texture);
    }


    public static Framebuffer createFrameBuffer(Framebuffer framebuffer) {
        if (framebuffer == null || framebuffer.framebufferWidth != Minecraft.getMinecraft().displayWidth || framebuffer.framebufferHeight != Minecraft.getMinecraft().displayHeight) {
            if (framebuffer != null) {
                framebuffer.deleteFramebuffer();
            }
            return new Framebuffer(Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight, true);
        }
        return framebuffer;
    }
}
