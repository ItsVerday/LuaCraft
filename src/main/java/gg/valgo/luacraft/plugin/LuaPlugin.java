package gg.valgo.luacraft.plugin;

import com.google.common.base.Charsets;
import gg.valgo.luacraft.Main;
import gg.valgo.luacraft.execution.LuaPluginExecutor;
import gg.valgo.luacraft.plugin.source.LuaPluginSource;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.PluginBase;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.PluginLogger;

import java.io.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LuaPlugin extends PluginBase {
    private LuaPluginSource source;
    private LuaPluginExecutor executor;

    private boolean isEnabled = false;
    private boolean isNaggable = true;
    private File dataFolder;
    private File configFile;
    private FileConfiguration config = null;
    private PluginDescriptionFile pluginDescriptionFile;

    private Logger logger;

    public LuaPlugin(LuaPluginSource source, PluginDescriptionFile pluginDescriptionFile) {
        this.source = source;
        this.pluginDescriptionFile = pluginDescriptionFile;

        logger = new PluginLogger(this);
        dataFolder = new File(Main.getMain().getDataFolder(), "data/" + getName());
        configFile = new File(dataFolder, "config.yml");
    }

    public LuaPluginSource getSource() {
        return source;
    }

    @Override
    public File getDataFolder() {
        return dataFolder;
    }

    @Override
    public PluginDescriptionFile getDescription() {
        return pluginDescriptionFile;
    }

    @Override
    public FileConfiguration getConfig() {
        if (config == null) {
            reloadConfig();
        }

        return config;
    }

    @Override
    public InputStream getResource(String filename) {
        return new ByteArrayInputStream(source.getResource(filename).getBytes());
    }

    @Override
    public void saveConfig() {
        try {
            getConfig().save(configFile);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Could not save config to " + configFile, ex);
        }
    }

    @Override
    public void saveDefaultConfig() {
        if (!configFile.exists()) {
            saveResource("config.yml", false);
        }
    }

    @Override
    public void saveResource(String resourcePath, boolean replace) {
        if (resourcePath == null || resourcePath.equals("")) {
            throw new IllegalArgumentException("ResourcePath cannot be null or empty");
        }

        resourcePath = resourcePath.replace('\\', '/');
        InputStream in = getResource(resourcePath);
        if (in == null) {
            throw new IllegalArgumentException("The embedded resource '" + resourcePath + "' cannot be found in the plugin.");
        }

        File outFile = new File(dataFolder, resourcePath);
        int lastIndex = resourcePath.lastIndexOf('/');
        File outDir = new File(dataFolder, resourcePath.substring(0, lastIndex >= 0 ? lastIndex : 0));

        if (!outDir.exists()) {
            outDir.mkdirs();
        }

        try {
            if (!outFile.exists() || replace) {
                OutputStream out = new FileOutputStream(outFile);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.close();
                in.close();
            } else {
                logger.log(Level.WARNING, "Could not save " + outFile.getName() + " to " + outFile + " because " + outFile.getName() + " already exists.");
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Could not save " + outFile.getName() + " to " + outFile, ex);
        }
    }


    @Override
    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);

        InputStream defConfigStream = getResource("config.yml");
        if (defConfigStream == null) {
            return;
        }

        config.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8)));
    }

    @Override
    public PluginLoader getPluginLoader() {
        return Main.getMain().getPluginLoader();
    }

    @Override
    public Server getServer() {
        return Main.getMain().getServer();
    }

    public void setEnabled(boolean enabled) {
        if (isEnabled != enabled) {
            isEnabled = enabled;

            if (isEnabled) {
                onEnable();
            } else {
                onDisable();
            }
        }
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public void onLoad() {
    }

    @Override
    public void onEnable() {
        executor = new LuaPluginExecutor(this);
    }

    @Override
    public void onDisable() {
        executor.onDisable();
        executor = null;
    }

    @Override
    public boolean isNaggable() {
        return isNaggable;
    }

    @Override
    public void setNaggable(boolean naggable) {
        isNaggable = naggable;
    }

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return null;
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return false;
    }
}