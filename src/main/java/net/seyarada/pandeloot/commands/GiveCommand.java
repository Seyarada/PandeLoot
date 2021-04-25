package net.seyarada.pandeloot.commands;

import net.seyarada.pandeloot.StringLib;
import net.seyarada.pandeloot.drops.DropManager;
import net.seyarada.pandeloot.rewards.RewardLine;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class GiveCommand {

    public GiveCommand() {
        AutoComplete.add(1, "drop");
        AutoComplete.add(2, "drop.players");
        AutoComplete.add(3, "drop.null.item");
    }

    public static boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = Bukkit.getPlayer(args[1]);

        if(player==null) {
            StringLib.badPlayer(args[1], sender);
            return false;
        }

        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < args.length-2; i++) {
            if (i > 0) sb.append(" ");
            sb.append(args[2+i]);
        }
        RewardLine line = new RewardLine(sb.toString());
        new DropManager(player, player.getLocation(), Collections.singletonList(line)).initDrops();

        return true;
    }
}
