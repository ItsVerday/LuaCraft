package gg.valgo.luacraft.api;

import org.luaj.vm2.LuaValue;

public interface LuaGetter<ValueType extends LuaValue> {
    LuaValue get(ValueType valueType);
}