package gg.valgo.luacraft.api;

import gg.valgo.luacraft.api.spigot.material.LuaMaterial;
import gg.valgo.luacraft.execution.LuaPluginExecutor;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;

import java.util.logging.Level;

public class LuaAPI {
    public static LuaValue load(LuaPluginExecutor executor) {
        LuaTable api = new LuaTable();

        api.rawset("log", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                executor.getPlugin().getLogger().log(Level.INFO, arg.tostring().checkjstring());
                return NIL;
            }
        });

        api.rawset("material", LuaMaterial.createAPI());

        return api;
    }
}