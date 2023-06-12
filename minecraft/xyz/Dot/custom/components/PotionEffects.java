package xyz.Dot.custom.components;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import xyz.Dot.custom.Component;
import xyz.Dot.module.Client.CustomColor;
import xyz.Dot.module.Client.HUD;
import xyz.Dot.ui.FontLoaders;
import xyz.Dot.utils.RenderUtils;
import xyz.Dot.utils.Translator;
import xyz.Dot.utils.shader.ShaderManager;

import java.awt.*;
import java.util.Collection;

public class PotionEffects extends Component {
    public PotionEffects() {
        super(64, 56, "Potion");
    }

    private final ResourceLocation inventoryBackground = new ResourceLocation("textures/gui/container/inventory.png");

    @Override
    public void drawHUD(float x, float y, float partialTicks) {
        int StartX = (int) x;
        int StartY = (int) y;
        int finalStartY = StartY;
        int finalStartY1 = StartY;
        ShaderManager.addBlurTask(() -> RenderUtils.drawRoundRect(StartX, finalStartY, StartX + width, finalStartY1 + height,4, new Color(0, 0, 0, 64)));
        RenderUtils.drawRoundRect(StartX, finalStartY, StartX + width, finalStartY1 + height,4, new Color(0, 0, 0, 64));

        RenderUtils.drawHalfRoundRect(StartX, StartY, StartX + width, StartY + 12, 4,  HUD.transparent.isToggle() ? new Color(0,0,0,100) : CustomColor.getColor());
        FontLoaders.normalfont16.drawString(Translator.getInstance().m("Potion Effects"), (int) x + 5, (int) y + 4, new Color(255, 255, 255).getRGB());
        setWidth(120);
        setHeight(mc.thePlayer.getActivePotionEffects().size() * 33 + 14);
        this.drawExamplePotionEffects(mc.thePlayer.getActivePotionEffects(), (int) x, (int) y + 14);
    }

    private void drawExamplePotionEffects(Collection<PotionEffect> collection, int x, int y) {
        if (!collection.isEmpty()) {
            double scale = 1;
            double scale_back = 1.0 / scale;
            GlStateManager.pushMatrix();
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.disableLighting();
            int l = this.scale();
/*            if (collection.size() > 5) {
                collection = collection.stream().limit(5L).collect(Collectors.toList());
            }*/
            for (PotionEffect potioneffect : collection) {
                GlStateManager.scale(scale, scale, scale);
                Potion effects = Potion.potionTypes[potioneffect.getPotionID()];
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                mc.getTextureManager().bindTexture(this.inventoryBackground);
                if (effects.hasStatusIcon()) {
                    int i1 = effects.getStatusIconIndex();
                    this.draw((int) (scale_back * (double) x + 6.0), (int) (scale_back * (double) y + 7.0), i1 % 8 * 18, 198 + i1 / 8 * 18);
                }
                String name = "";
                if (potioneffect.getDuration() < 600 && potioneffect.getDuration() > 300) {
                    name = name + "§6" + Potion.getDurationString(potioneffect);
                } else if (potioneffect.getDuration() < 300) {
                    name = name + "§c" + Potion.getDurationString(potioneffect);
                } else if (potioneffect.getDuration() > 600) {
                    name = name + "§7" + Potion.getDurationString(potioneffect);
                }
                String PType = I18n.format(potioneffect.getEffectName());
                switch (potioneffect.getAmplifier()) {
                    case 0: {
                        break;
                    }
                    case 1: {
                        PType = PType + " II";
                        break;
                    }
                    case 2: {
                        PType = PType + " III";
                        break;
                    }
                    case 3: {
                        PType = PType + " IV";
                        break;
                    }
                    default: {
                        PType = PType + potioneffect.getAmplifier();
                    }
                }
                mc.fontRendererObj.drawStringWithShadow(PType, (float) scale_back * (float) x + 10.0f + 18.0f, (float) scale_back * (float) y + 6.0f, 0xFFFFFF);
                int offset = mc.fontRendererObj.FONT_HEIGHT + 1;
                mc.fontRendererObj.drawStringWithShadow(name, (float) scale_back * (float) x + 10.0f + 18.0f, (float) scale_back * (float) y + 6.0f + (float) offset, 0x7F7F7F);
                GlStateManager.scale(scale_back, scale_back, scale_back);
                y += l;
            }
            GlStateManager.popMatrix();
        }
    }

    private void draw(int x, int y, int textureX, int textureY) {
        float f = 0.00390625f;
        float f1 = 0.00390625f;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(x, y + 18, 0.0).tex((float) (textureX) * f, (float) (textureY + 18) * f1).endVertex();
        worldrenderer.pos(x + 18, y + 18, 0.0).tex((float) (textureX + 18) * f, (float) (textureY + 18) * f1).endVertex();
        worldrenderer.pos(x + 18, y, 0.0).tex((float) (textureX + 18) * f, (float) (textureY) * f1).endVertex();
        worldrenderer.pos(x, y, 0.0).tex((float) (textureX) * f, (float) (textureY) * f1).endVertex();
        tessellator.draw();
    }

    private int scale() {
        return (int) (1 * (double) 33);
    }

}