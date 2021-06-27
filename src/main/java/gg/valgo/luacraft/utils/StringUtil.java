package gg.valgo.luacraft.utils;

import org.bukkit.ChatColor;

public class StringUtil {
    public static String formatColors(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}