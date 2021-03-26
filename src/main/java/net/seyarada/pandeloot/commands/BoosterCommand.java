package net.seyarada.pandeloot.commands;

import net.seyarada.pandeloot.Boosts;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class BoosterCommand {

    public BoosterCommand() {
        AutoComplete.add(1, "boost");
        AutoComplete.add(2, "boost.set");
        AutoComplete.add(3, "boost.set.time in seconds");
        AutoComplete.add(4, "boost.set.null.boost power");
        AutoComplete.add(5, "boost.set.null.null.players");
        AutoComplete.add(5, "boost.set.null.null.-");
    }

    public static boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // Set
        if(args[1].equals("set")) {
            if(args.length==5) {
                Boosts.setPlayerBoost(Integer.parseInt(args[3]), System.currentTimeMillis()+Long.parseLong(args[2])*1000, args[4]);
                sender.sendMessage("Boost of x"+args[3]+" power set for "+args[2]+" seconds for "+args[4]);
                return true;
            }
            Boosts.setGlobalBoost(Integer.parseInt(args[3]), System.currentTimeMillis()+Long.parseLong(args[2])*1000);
            sender.sendMessage("Boost of x"+args[3]+" power set for "+args[2]+" seconds");
        }

        return true;
    }
}