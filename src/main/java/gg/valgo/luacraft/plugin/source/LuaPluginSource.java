package gg.valgo.luacraft.plugin.source;

import java.util.List;

public interface LuaPluginSource {
    String getName();
    String getPluginYML();

    List<String> getScriptList();
    String getScript(String path);

    List<String> getResourcesList();
    String getResource(String path);
}