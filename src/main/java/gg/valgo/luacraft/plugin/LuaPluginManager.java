package gg.valgo.luacraft.plugin;

import gg.valgo.luacraft.Main;

import java.io.File;
import java.util.HashMap;

public class LuaPluginManager {
    private static LuaPluginLoader loader;
    private static HashMap<String, LuaPlugin> plugins = new HashMap<>();

    public static void setLoader(LuaPluginLoader loader) {
        if (LuaPluginManager.loader == null) {
            LuaPluginManager.loader = loader;
        }
    }

    public static LuaPlugin getPlugin(String name) {
        return plugins.get(name);
    }

    public static void loadPlugin(String name) {
        try {
            if (getPlugin(name) != null) {
                return;
            }

            LuaPlugin plugin = (LuaPlugin) loader.loadPlugin(new File(Main.getMain().getDataFolder(), "plugins/" + name));
            plugins.put(name, plugin);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void enablePlugin(String name) {
        try {
            if (getPlugin(name) == null) {
                loadPlugin(name);
            }

            if (!getPlugin(name).isEnabled()) {
                getPlugin(name).setEnabled(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void disablePlugin(String name) {
        try {
            if (getPlugin(name) == null) {
                return;
            }

            loader.disablePlugin(getPlugin(name));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void unloadPlugin(String name) {
        if (getPlugin(name) == null) {
            return;
        }

        if (getPlugin(name).isEnabled()) {
            disablePlugin(name);
        }

        plugins.remove(name);
    }
}