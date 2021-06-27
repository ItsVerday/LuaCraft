package gg.valgo.luacraft.api;

import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class CustomLuaValue<ValueType extends CustomLuaValue> extends LuaTable {
    private ArrayList<CustomLuaValuePrototype<ValueType>> prototypes = new ArrayList<>();

    public void usePrototype(CustomLuaValuePrototype<ValueType> prototype) {
        prototypes.add(prototype);
    }

    public ArrayList<CustomLuaValuePrototype<ValueType>> getPrototypes() {
        return prototypes;
    }

    @Override
    public int type() {
        return TUSERDATA;
    }

    public String typename() {
        return prototypes.get(prototypes.size() - 1).getTypeName();
    }

    @Override
    public LuaValue rawget(LuaValue key) {
        String stringKey;
        try {
            stringKey = key.checkjstring().toLowerCase();
        } catch (LuaError e) {
            return super.rawget(key);
        }

        for (int index = prototypes.size() - 1; index >= 0; index--) {
            CustomLuaValuePrototype<ValueType> prototype = prototypes.get(index);
            HashMap<String, LuaGetter<ValueType>> getters = prototype.getGetters();
            if (getters.containsKey(stringKey)) {
                return getters.get(stringKey).get((ValueType) this);
            }
        }

        return super.rawget(key);
    }

    @Override
    public void rawset(LuaValue key, LuaValue value) {
        String stringKey;
        try {
            stringKey = key.checkjstring().toLowerCase();
        } catch (LuaError e) {
            super.rawset(key, value);
            return;
        }

        for (int index = prototypes.size() - 1; index >= 0; index--) {
            CustomLuaValuePrototype<ValueType> prototype = prototypes.get(index);
            HashMap<String, LuaSetter<ValueType>> setters = prototype.getSetters();
            if (setters.containsKey(stringKey)) {
                setters.get(stringKey).set((ValueType) this, value);
                return;
            }
        }

        super.rawset(key, value);
    }

    public static LuaValue argerror(String expected, LuaValue actual) {
        throw new LuaError("bad argument: " + expected + " expected, got " + actual.typename());
    }

    public static LuaValue valueOf(Object object) {
        return LuaTypes.valueOf(object);
    }
}