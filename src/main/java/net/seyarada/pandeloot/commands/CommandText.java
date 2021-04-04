package net.seyarada.pandeloot.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CommandText {

    public static final String name = ChatColor.YELLOW+"[PandeLoot] "+ChatColor.GREEN;

    public static void sendTitle(String titleName, CommandSender player) {
        titleName = ChatColor.translateAlternateColorCodes('&', titleName);
        player.sendMessage(name+titleName);
    }

    public static void sendText(String text, CommandSender player) {
        text = ChatColor.translateAlternateColorCodes('&', text);
        player.sendMessage(text);
    }

}
