package gg.valgo.luacraft.execution;

import gg.valgo.luacraft.api.LuaAPI;
import gg.valgo.luacraft.plugin.LuaPlugin;
import gg.valgo.luacraft.plugin.source.LuaPluginSource;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.util.HashMap;

public class LuaPluginExecutor {
    private HashMap<String, LuaValue> modules = new HashMap<>();
    private LuaValue api;
    private LuaValue mainChunk;
    private LuaFunction importFunction = new OneArgFunction() {
        @Override
        public LuaValue call(LuaValue arg) {
            String name = arg.checkjstring();
            if (!modules.containsKey(name)) {
                LuaPluginExecutor.this.load(name);
            }

            return modules.get(name);
        }
    };

    private static Globals globals = JsePlatform.standardGlobals();

    private LuaPlugin plugin;

    public LuaPluginExecutor(LuaPlugin plugin) {
        this.plugin = plugin;
        onEnable();
    }

    public LuaPlugin getPlugin() {
        return plugin;
    }

    public void onEnable() {
        String main = plugin.getDescription().getMain();
        LuaPluginSource source = plugin.getSource();

        api = LuaAPI.load(this);
        globals.rawset("plugin", api);
        globals.rawset("import", importFunction);
        mainChunk = globals.load(source.getScript(main), main);
        setModule(main, mainChunk.call());
    }

    public void load(String name) {
        LuaPluginSource source = plugin.getSource();
        LuaValue chunk = globals.load(source.getScript(name), name);

        setModule(name, chunk.call());
    }

    public void setModule(String name, LuaValue chunk) {
        modules.put(name, chunk);
    }

    public void onDisable() {
    }
}