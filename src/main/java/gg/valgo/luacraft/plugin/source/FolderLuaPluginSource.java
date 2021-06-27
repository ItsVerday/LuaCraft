package gg.valgo.luacraft.plugin.source;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class FolderLuaPluginSource implements LuaPluginSource {
    private String name;
    private File folder;

    public FolderLuaPluginSource(File folder, String name) {
        this.folder = folder;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public File getFolder() {
        return folder;
    }

    private String readFile(String path) throws IOException {
        File file = new File(folder, path);
        Scanner scanner = new Scanner(file);
        String data = "";
        while (scanner.hasNextLine()) {
            data = data.concat(scanner.nextLine()) + "\n";
        }

        scanner.close();
        return data;
    }

    @Override
    public String getPluginYML() {
        try {
            return readFile("plugin.yml");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<String> getScriptList() {
        return new ArrayList<>(Arrays.asList(folder.list((dir, name) -> name.endsWith(".lua"))));
    }

    @Override
    public String getScript(String path) {
        try {
            return readFile(path);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<String> getResourcesList() {
        File resourcesFolder = new File(folder, "resources");
        if (!resourcesFolder.exists()) {
            return new ArrayList<>();
        }

        return new ArrayList<>(Arrays.asList(resourcesFolder.list()));
    }

    @Override
    public String getResource(String path) {
        try {
            return readFile(path);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}