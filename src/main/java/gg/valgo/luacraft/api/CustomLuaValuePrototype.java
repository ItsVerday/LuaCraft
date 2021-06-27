package gg.valgo.luacraft.api;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomLuaValuePrototype<ValueType extends CustomLuaValue> {
    private String typeName;

    private HashMap<String, LuaGetter<ValueType>> getters = new HashMap<>();
    private HashMap<String, LuaSetter<ValueType>> setters = new HashMap<>();

    public CustomLuaValuePrototype(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void method(LuaMethod<ValueType> method, String... names) {
        getter(value -> new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs args) {
                ArrayList<LuaValue> argsList = new ArrayList<>();
                for (int i = 1; i <= args.narg(); i++) {
                    argsList.add(args.arg(i));
                }

                return method.run(value, argsList);
            }
        }, names);
    }

    public void getterSetter(LuaGetter<ValueType> getter, LuaSetter<ValueType> setter, String... names) {
        getter(getter, names);
        setter(setter, names);
    }

    public void getter(LuaGetter<ValueType> getter, String... names) {
        for (String name : names) {
            addGetter(name, getter);
        }
    }

    protected void setter(LuaSetter<ValueType> setter, String... names) {
        for (String name : names) {
            addSetter(name, setter);
        }
    }

    private void addGetter(String name, LuaGetter<ValueType> getter) {
        getters.put(name.toLowerCase(), getter);
    }

    private void addSetter(String name, LuaSetter<ValueType> setter) {
        setters.put(name.toLowerCase(), setter);
    }

    public HashMap<String, LuaGetter<ValueType>> getGetters() {
        return getters;
    }

    public HashMap<String, LuaSetter<ValueType>> getSetters() {
        return setters;
    }
}