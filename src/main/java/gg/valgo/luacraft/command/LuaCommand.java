package gg.valgo.luacraft.command;

import gg.valgo.luacraft.plugin.LuaPluginManager;
import gg.valgo.luacraft.utils.StringUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class LuaCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            printHelp(sender);
            return true;
        }

        switch (args[0]) {
            case "help": {
                printHelp(sender);
                return true;
            }

            case "reload": {
                if (args.length < 2) {
                    sender.sendMessage(StringUtil.formatColors("&cYou must specify a plugin to reload. Use &4/lua reload <plugin>&c."));
                    return true;
                }

                String pluginName = args[1];
                if (LuaPluginManager.getPlugin(pluginName) != null) {
                    LuaPluginManager.unloadPlugin(pluginName);
                }

                LuaPluginManager.enablePlugin(pluginName);
                sender.sendMessage(StringUtil.formatColors("&aLua Plugin &2" + pluginName + "&a was reloaded!"));
                return true;
            }

            default: {
                sender.sendMessage(StringUtil.formatColors("&cUnrecognized subcommand. Use &4/lua help&c for help."));
                return true;
            }
        }
    }

    public static void printHelp(CommandSender sender) {
        sender.sendMessage(StringUtil.formatColors(
                "&9Help for &1/lua&9:\n" +
                "&1/lua help&9: Shows this help message.\n" +
                "&1/lua reload <plugin>&9: Reloads the Lua plugin called <plugin>."));
    }
}