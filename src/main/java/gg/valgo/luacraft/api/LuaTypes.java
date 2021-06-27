package gg.valgo.luacraft.api;

import gg.valgo.luacraft.api.spigot.material.LuaMaterial;
import org.bukkit.Material;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import java.util.List;
import java.util.Map;

public class LuaTypes {
    public static LuaValue valueOf(Object obj) {
        if (obj == null) {
            return LuaValue.NIL;
        }

        if (obj instanceof Byte || obj instanceof Short || obj instanceof Integer || obj instanceof Long || obj instanceof Float || obj instanceof Double) {
            return LuaValue.valueOf((double) obj);
        }

        if (obj instanceof CharSequence) {
            return LuaValue.valueOf((String) obj);
        }

        if (obj instanceof Boolean) {
            return LuaValue.valueOf((boolean) obj);
        }

        if (obj instanceof byte[]) {
            return LuaValue.valueOf((byte[]) obj);
        }

        if (obj instanceof List) {
            LuaTable table = LuaValue.tableOf();
            for (Object elt : (List<?>) obj) {
                table.insert(table.keyCount() + 1, valueOf(elt));
            }

            return table;
        }

        if (obj instanceof Map) {
            LuaTable table = LuaValue.tableOf();
            Map<?, ?> map = (Map<?, ?>) obj;
            for (Object key : map.keySet()) {
                table.set(valueOf(key), valueOf(map.get(key)));
            }

            return table;
        }

        return spigotTypes(obj);
    }

    public static LuaValue spigotTypes(Object obj) {
        if (obj instanceof Material) {
            return LuaMaterial.getLuaMaterial((Material) obj);
        }

        return LuaValue.NIL;
    }
}