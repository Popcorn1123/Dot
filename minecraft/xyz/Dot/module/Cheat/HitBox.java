package xyz.Dot.module.Cheat;

import org.lwjgl.input.Keyboard;
import xyz.Dot.Client;
import xyz.Dot.module.Category;
import xyz.Dot.module.Module;
import xyz.Dot.module.ModuleManager;
import xyz.Dot.setting.Setting;

public class HitBox extends Module {
    public static Setting size = new Setting(ModuleManager.getModuleByName("HitBox"), "Size", 0.1d, 0.1d, 1.0d, 0.05d);
    public HitBox() {
        super("HitBox", Keyboard.KEY_NONE, Category.Cheat);
        this.addValues(size);
    }

    @Override
    public void onEnable() {
        if(!Client.instance.SigmaMode){
            this.setToggle(false);

        }
        super.onEnable();
    }
}