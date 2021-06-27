package gg.valgo.luacraft.api;

import org.luaj.vm2.LuaValue;

public interface LuaSetter<ValueType extends LuaValue> {
    void set(ValueType valueType, LuaValue value);
}