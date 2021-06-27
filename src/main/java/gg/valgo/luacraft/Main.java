package gg.valgo.luacraft;

import gg.valgo.luacraft.command.LuaCommand;
import gg.valgo.luacraft.plugin.LuaPluginLoader;
import gg.valgo.luacraft.plugin.LuaPluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private static Main main;

    @Override
    public void onEnable() {
        main = this;
        LuaPluginManager.setLoader(new LuaPluginLoader(getServer()));
        getServer().getPluginCommand("lua").setExecutor(new LuaCommand());

        LuaPluginManager.enablePlugin("test");
    }

    @Override
    public void onDisable() {
    }

    public static Main getMain() {
        return main;
    }
}