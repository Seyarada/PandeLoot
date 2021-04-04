package net.seyarada.pandeloot.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;

public class AutoComplete implements TabCompleter {

    public static Map<Integer, List<String>> autoComplete = new HashMap<>();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equals("pandeloot")) {

            if (sender instanceof Player && autoComplete.get(args.length) != null) {
                List<String> autoCompletes = new ArrayList<>();
                for(String i : autoComplete.get(args.length)) {

                    int match = 0;
                    int tick = 0;
                    String[] iVal = i.split("\\.");

                    for (String o : args) {
                        if (iVal.length <= tick) continue;
                        if (iVal[tick].equals("null")) continue;
                        if (o.isEmpty()) match++;

                        if (o.equals(iVal[tick])) {
                            match++;
                        } else {
                            match--;
                        }
                        tick++;
                    }
                    if (match + 1 >= tick) {
                        String toAdd = iVal[iVal.length - 1];

                        if(toAdd.equals("players")) {
                            for(Player n : Bukkit.getOnlinePlayers()) {
                                    autoCompletes.add(n.getName());
                            }
                        }
                        else
                            autoCompletes.add(iVal[iVal.length - 1]);
                    }
                }

                return autoCompletes;
            }
        }
        return null;
    }

    public static void add(int position, String string) {
        List<String> list = autoComplete.get(position);
        if(list!=null && !list.isEmpty()) {
            list.add(string);
            autoComplete.put(position, list);
        } else {
            List<String> l = new ArrayList<>();
            l.add(string);
            autoComplete.put(position , l);
        }
    }

}