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
    public static final String skin = root+".Skin";

    public static final String prefix = ChatColor.YELLOW+"["+root+"] "+ ChatColor.RED;

    public static int depthBonus;

    public static void badItemStack(RewardLine reward) {
        System.err.println(prefix+" Couldn't create the item -"+reward.item+"- from origin "+reward.origin);
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

    public static void warn(String text) {
        // Rainbow color scheme for debugging
        long depth = text.chars().filter(ch -> ch == '+').count() + depthBonus;
        if(depth<=Config.debug)
            switch ((int) depth) {
                // This cycles trough rainbow colors the deeper a debug is. Useless, I know
                case 1:
                    System.err.println(prefix + ChatColor.WHITE + text); break;
                case 2:
                    System.err.println(prefix + ChatColor.DARK_PURPLE + text); break;
                case 3:
                    System.err.println(prefix + ChatColor.LIGHT_PURPLE + text); break;
                case 4:
                    System.err.println(prefix + ChatColor.BLUE + text); break;
                case 5:
                    System.err.println(prefix + ChatColor.DARK_BLUE + text); break;
                case 6:
                    System.err.println(prefix + ChatColor.DARK_AQUA + text); break;
                case 7:
                    System.err.println(prefix + ChatColor.AQUA + text); break;
                case 8:
                    System.err.println(prefix + ChatColor.GREEN + text); break;
                case 9:
                    System.err.println(prefix + ChatColor.DARK_GREEN + text); break;
                case 10:
                    System.err.println(prefix + ChatColor.YELLOW + text); break;
                case 11:
                    System.err.println(prefix + ChatColor.GOLD + text); break;
                case 12:
                    System.err.println(prefix + ChatColor.RED + text); break;
                case 13:
                    System.err.println(prefix + ChatColor.DARK_RED + text); break;
                default:
                    System.err.println(prefix + text); break;

        }
    }

}
