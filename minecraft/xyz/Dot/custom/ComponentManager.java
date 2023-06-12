package xyz.Dot.custom;

import net.minecraft.client.Minecraft;
import xyz.Dot.custom.base.TextComponent;
import xyz.Dot.custom.components.BPSGraph;
import xyz.Dot.custom.components.BetterScoreboard;
import xyz.Dot.custom.components.KeyStrokes;
import xyz.Dot.custom.components.Watermark;
import xyz.Dot.ui.Custom;

import java.util.ArrayList;

public class ComponentManager {
    public final ArrayList<Component> components = new ArrayList<>();
    public boolean init = false;

    public ComponentManager(){
        init();
    }

    public void init() {
        components.add(new Watermark());
        components.add(new BPSGraph());
        components.add(new KeyStrokes());
        components.add(new BetterScoreboard());
        components.add(new TextComponent("FPS", () -> Minecraft.getDebugFPS() + " FPS"));
        components.add(new TextComponent("CPS", () -> KeyStrokes.lastCPS + " CPS"));
        components.add(new TextComponent("ServerAddress",() -> Minecraft.getMinecraft().isIntegratedServerRunning() ? "Singleplayer" : Minecraft.getMinecraft().getCurrentServerData().serverIP));
        components.add(new TextComponent("ItemInfo", () -> {
            if (Minecraft.getMinecraft().currentScreen instanceof Custom) {
                return "ItemInfo";
            } else {
                if (Minecraft.thePlayer.getHeldItem() != null) {
                    return Minecraft.thePlayer.getHeldItem().getDisplayName() + " x" + Minecraft.thePlayer.getHeldItem().stackSize;
                }
            }
            return "";
        }));
    }

    public void drawComponents () {
        for (Component component : components) {
            component.draw(Minecraft.getMinecraft().timer.renderPartialTicks);
        }
    }
}