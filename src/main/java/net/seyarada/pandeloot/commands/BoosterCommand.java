package net.seyarada.pandeloot.commands;

import net.seyarada.pandeloot.Boosts;
import net.seyarada.pandeloot.utils.MathUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class BoosterCommand {

    public BoosterCommand() {
        AutoComplete.add(1, "boost");
        AutoComplete.add(2, "boost.list");
        AutoComplete.add(2, "boost.set");
        AutoComplete.add(2, "boost.terminate");
        AutoComplete.add(3, "boost.terminate.players");
        AutoComplete.add(3, "boost.terminate.Leave this empty for global boost");
        AutoComplete.add(3, "boost.set.time in seconds");
        AutoComplete.add(4, "boost.set.null.boost power");
        AutoComplete.add(5, "boost.set.null.null.players");
        AutoComplete.add(5, "boost.set.null.null.Leave this empty for a global boost");
    }

    public static boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // Set
        if(args[1].equals("set")) {
            if(args.length==5) {
                Boosts.setPlayerBoost(Integer.parseInt(args[3]), System.currentTimeMillis()+Long.parseLong(args[2])*1000, args[4]);
                String text = "Boost of &bx" + args[3] + "&a power set for &b" + args[2] + " seconds &afor &b" + args[4];
                CommandText.sendTitle(text, sender);
                return true;
            }
            Boosts.setGlobalBoost(Integer.parseInt(args[3]), System.currentTimeMillis()+Long.parseLong(args[2])*1000);
            String text = "Boost of &bx" + args[3] + "&a power set for &b" + args[2] + " seconds";
            CommandText.sendTitle(text, sender);
        }
        else if(args[1].equals("list")) {
            long globalTime = Boosts.getGlobalDuration()/1000;


            CommandText.sendTitle("List of active boosts:", sender);

            if(globalTime<0)
                CommandText.sendText("&7- &fGlobal boost: &aNot Active", sender);
            else
                CommandText.sendText("&7- &fGlobal boost: &ax"+Boosts.getGlobalBoost()+", "+ MathUtil.getDurationAsTime(globalTime), sender);

            for(String i : Boosts.playerPower.keySet()) {
                if(Boosts.getPlayerBoost(i)>1) {
                    long time = ( Boosts.playerDuration.get(i)-System.currentTimeMillis() )/1000;
                    CommandText.sendText("&7- &f"+i+"'s boost: &ax"+Boosts.getPlayerBoost(i)+", "+MathUtil.getDurationAsTime(time), sender);
                }
            }

        } else if(args[1].equals("terminate")) {
            if(args.length==2) {
                Boosts.terminateGlobal();
                CommandText.sendTitle("Terminated global boost", sender);
            }
            if(args.length==3) {
                Boosts.terminatePlayer(args[2]);
                CommandText.sendTitle("Terminated "+args[2]+"'s boost", sender);
            }
        }
        return true;
    }
}