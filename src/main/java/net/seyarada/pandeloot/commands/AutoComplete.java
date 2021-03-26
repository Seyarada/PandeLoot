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

                    int k = 0;
                    int success = args.length;


                    for(String j : i.split("\\.")) {

                        if(j.equals(args[k])||j.equals("null")) {
                            success--;
                        }
                        else if(j.equals("players")) {
                            for(Player n : Bukkit.getOnlinePlayers()) {
                                autoCompletes.add(n.getName());
                            }
                        }
                        k++;

                    }

                    if(success==1) {
                        String entry = args[args.length - 1];
                        String suggest = i.split("\\.")[i.split("\\.").length - 1];
                        if(!suggest.equals("null") && suggest.startsWith(entry) && !suggest.equals("players")) {
                            autoCompletes.add(suggest);
                        }
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