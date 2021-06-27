package gg.valgo.luacraft.api;

import org.luaj.vm2.LuaValue;

import java.util.ArrayList;

public interface LuaMethod<ValueType extends LuaValue> {
    LuaValue run(ValueType valueType, ArrayList<LuaValue> values);
}