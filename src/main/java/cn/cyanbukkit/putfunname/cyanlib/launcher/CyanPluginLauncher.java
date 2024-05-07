package cn.cyanbukkit.putfunname.cyanlib.launcher;

import cn.cyanbukkit.putfunname.cyanlib.loader.KotlinBootstrap;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;

/**
 * 嵌套框架
 */

public class CyanPluginLauncher extends JavaPlugin {

    public static CyanPluginLauncher cyanPlugin;

    public CyanPluginLauncher() {
        cyanPlugin = this;
        KotlinBootstrap.init();
    }


    public void registerCommand(Command command) {
        Class<?> pluginManagerClass = cyanPlugin.getServer().getPluginManager().getClass();
        try {
            Field field = pluginManagerClass.getDeclaredField("commandMap");
            field.setAccessible(true);
            SimpleCommandMap commandMap = (SimpleCommandMap) field.get(cyanPlugin.getServer().getPluginManager());
            commandMap.register(cyanPlugin.getName(), command);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoad() {
    }


    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }



}