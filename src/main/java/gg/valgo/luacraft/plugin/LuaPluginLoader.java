package gg.valgo.luacraft.plugin;

import gg.valgo.luacraft.Main;
import gg.valgo.luacraft.plugin.source.FolderLuaPluginSource;
import org.apache.commons.lang.Validate;
import org.bukkit.Server;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.*;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.regex.Pattern;

public class LuaPluginLoader implements PluginLoader {
    private Server server;

    public LuaPluginLoader(Server server) {
        this.server = server;
    }

    @Override
    public Plugin loadPlugin(File file) throws InvalidPluginException, UnknownDependencyException {
        Validate.notNull(file, "File cannot be null");

        if (!file.exists()) {
            throw new InvalidPluginException(new FileNotFoundException(file.getPath() + " does not exist"));
        }

        if (file.isDirectory()) {
            PluginDescriptionFile description;
            try {
                description = getPluginDescription(file);
            } catch (Exception e) {
                throw new InvalidPluginException(e);
            }

            File dataFolder = new File(Main.getMain().getDataFolder(), "data/" + description.getName());

            if (dataFolder.exists() && !dataFolder.isDirectory()) {
                throw new InvalidPluginException(String.format(
                    "Projected datafolder: `%s' for %s (%s) exists and is not a directory",
                    dataFolder,
                    description.getFullName(),
                    file
                ));
            }

            server.getUnsafe().checkSupported(description);
            return new LuaPlugin(new FolderLuaPluginSource(file, description.getName()), description);
        } else {
            // TODO: Add support for zipped plugins;
            throw new InvalidPluginException("Zipped plugins not supported yet");
        }
    }

    @Override
    public PluginDescriptionFile getPluginDescription(File file) throws InvalidDescriptionException {
        Validate.notNull(file, "File cannot be null");

        if (file.isDirectory()) {
            File descriptionFile = new File(file, "plugin.yml");

            try {
                return new PluginDescriptionFile(new FileInputStream(descriptionFile));
            } catch (YAMLException | IOException e) {
                throw new InvalidDescriptionException(e);
            }
        } else {
            // TODO: Add support for zipped plugins;
            throw new InvalidDescriptionException("Zipped plugins not supported yet");
        }
    }

    @Override
    public Pattern[] getPluginFileFilters() {
        return new Pattern[] {};
    }

    @Override
    public Map<Class<? extends Event>, Set<RegisteredListener>> createRegisteredListeners(Listener listener, Plugin plugin) {
        return new HashMap<>();
    }

    @Override
    public void enablePlugin(Plugin plugin) {
        Validate.isTrue(plugin instanceof LuaPlugin, "Plugin is not associated with this PluginLoader");

        if (!plugin.isEnabled()) {
            LuaPlugin luaPlugin = (LuaPlugin) plugin;

            try {
                luaPlugin.setEnabled(true);
            } catch (Throwable ex) {
                server.getLogger().log(Level.SEVERE, "Error occurred while enabling lua plugin " + plugin.getDescription().getFullName(), ex);
            }

            server.getPluginManager().callEvent(new PluginEnableEvent(plugin));
        }
    }

    @Override
    public void disablePlugin(Plugin plugin) {
        Validate.isTrue(plugin instanceof LuaPlugin, "Plugin is not associated with this PluginLoader");

        if (plugin.isEnabled()) {
            LuaPlugin luaPlugin = (LuaPlugin) plugin;

            try {
                luaPlugin.setEnabled(false);
            } catch (Throwable ex) {
                server.getLogger().log(Level.SEVERE, "Error occurred while disabling " + plugin.getDescription().getFullName() + " (Is it up to date?)", ex);
            }
        }
    }
}