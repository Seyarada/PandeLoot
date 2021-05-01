package net.seyarada.pandeloot;

import net.seyarada.pandeloot.rewards.RewardLine;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class StringLib {

    public static final String root = "PandeLoot";
    public static final String playOnPickup = root+".playOnPickup";
    public static final String preventPickup = root+".preventPickUp";
    public static final String preventStack = root+".preventStack";
    public static final String onUse = root+".onUse";
    public static final String bag = root+".LootBag";

    public static final String prefix = ChatColor.YELLOW+"["+root+"] "+ ChatColor.RED;

    public static void badItemStack(RewardLine reward) {
        System.err.println(prefix+"Couldn't create the item -"+reward.item+"- from origin "+reward.origin);
    }

    public static void badItem(String item) {
        System.err.println(prefix+"Couldn't find the item -"+item+"- , this probably means that are trying to drop an item from another " +
                "plugin but you haven't specified the origin plugin? " +
                "You can take a look at all the origins here https://github.com/Seyarada/PandeLoot/wiki/Compatibility");
    }

    public static void badLootTable(String lootTable) {
        if(lootTable==null) return;
        System.err.println(prefix+"Couldn't find the LootTable -"+lootTable+"- !");
    }

    public static void badMIGen(String item) {
        if(item==null) return;
        System.err.println(prefix+"Couldn't use the MI Generator for the item -"+item+"- !");
    }

    public static void badLootBag(String lootBag) {
        System.err.println(prefix+" Couldn't find the LootBag -"+lootBag+"- !");
    }

    public static void badOption(String option) {
        System.err.println(prefix + "Can't find the option -" + option + "- !");
    }

    public static void badPlayer(String player, CommandSender sender) {
        System.err.println(prefix + "Can't find the player -" + player + "- !");
        sender.sendMessage(prefix + "Can't find the player -" + player + "- !");
    }

}
