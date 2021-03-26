package net.seyarada.pandeloot.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class CommandManager implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if(args.length>0) {
            switch(args[0]) {
                case "boost":
                    BoosterCommand.onCommand(sender, command, s, args);
                    break;
                case "drop":
                    GiveCommand.onCommand(sender, command, s, args);
                    break;
                case "reload":
                    ReloadCommand.onCommand(sender, command, s, args);
                    break;
            }
        }

        return true;
    }
}