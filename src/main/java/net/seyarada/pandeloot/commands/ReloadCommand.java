package net.seyarada.pandeloot.commands;

import net.seyarada.pandeloot.Config;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ReloadCommand {

    public ReloadCommand() {
        AutoComplete.add(1, "reload");
    }

    public static boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Config.reload();
        sender.sendMessage("Reloaded!");

        return true;
    }
}