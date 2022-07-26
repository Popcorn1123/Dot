package xyz.Dot;

import org.apache.logging.log4j.core.appender.FileManager;
import org.lwjgl.opengl.Display;
import xyz.Dot.command.CommandManager;
import xyz.Dot.file.CustomFileManager;
import xyz.Dot.log.Log_Dot;
import xyz.Dot.module.ModuleManager;
import xyz.Dot.event.EventBus;
import xyz.Dot.setting.Setting;
import xyz.Dot.setting.SettingManager;

public enum Client {
    instance;
    public String client_name = "Dot";
    public String client_version = "0.1";
    public boolean inDevelopment = true;
    public EventBus eventmanger;
    public ModuleManager modulemanager;
    public SettingManager settingmanager;

    public CommandManager commandmanager;
    public CustomFileManager customfilemanager;

    public void run(){

        Log_Dot.info("客户端启动");
        eventmanger = new EventBus();
        modulemanager = new ModuleManager();
        settingmanager = new SettingManager();
        commandmanager = new CommandManager();
        customfilemanager = new CustomFileManager();
        String title = client_name + " " + client_version + " " + getDevMode() + "- Minecraft 1.8.8";
        Display.setTitle(title);
        modulemanager.loadModule();
        commandmanager.run();
        customfilemanager.loadFiles();
    }

    public void stop(){

        Log_Dot.info("客户端关闭");
        Log_Dot.sava_Log();
        customfilemanager.saveFiles();

    }

    public String getDevMode(){

        if(inDevelopment){
            return "Dev ";
        }else{
            return "";
        }

    }

    public ModuleManager getModuleManager() {
        return this.modulemanager;
    }
}
