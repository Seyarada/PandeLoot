package net.seyarada.pandeloot.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import net.seyarada.pandeloot.Boosts;
import net.seyarada.pandeloot.PandeLoot;
import net.seyarada.pandeloot.damage.DamageUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

public class PlaceholderNew {

    public static String parse(String i, DamageUtil damageUtil, Player player) {

        if (i == null || i.isEmpty()) return i;
        i = PlaceholderUtil.nukeMythicMobsPlaceholders(i);
        i = ChatColor.translateAlternateColorCodes('&', i);
        if (!i.contains("%")) return i;

        int first = i.indexOf("%");
        int second = i.indexOf("%", first + 1);
        int pendingBoost = 0;

        if(player!=null) {
            if(PandeLoot.getInstance().getServer().getPluginManager().getPlugin("PlaceholderAPI")!=null)
                PlaceholderAPI.setPlaceholders(player, i);
            pendingBoost = Boosts.getPlayerBoost(player.getName());
            i = i.replace("%player%", player.getName());
        }

        for (int j = 0; j < 3; j++) {
            int k = j + 1;
            if(damageUtil!=null && damageUtil.getRankedPlayers().size()>=k) {
                i = i.replace("%"+k+".name%", damageUtil.getPlayer(j).getName());
                i = i.replace("%"+k+".dmg%", String.valueOf(damageUtil.getDamage(j)));
            } else if(i.contains("%"+k+".name%") || i.contains("%"+k+".dmg%")) {
                return null;
            }
        }

        StringBuilder placeholderBuilder = new StringBuilder(i);

        while(first>0 && first<i.lastIndexOf("%")) {
            DecimalFormat df = new DecimalFormat("#.##");
            String str;
            second++;
            if(damageUtil!=null) {
                switch(i.substring(first, second)) {
                    case "%player.dmg%":
                        str = df.format(damageUtil.getPlayerDamage(player)).replace(",", ".");
                        placeholderBuilder.replace(first, second, str); break;
                    case "%player.pdmg%":
                        str = df.format(damageUtil.getPercentageDamage(player)).replace(",", ".");
                        placeholderBuilder.replace(first, second, str); break;
                    case "%player.pdmg100%":
                        str = df.format(damageUtil.getPercentageDamage(player)*100).replace(",", ".");
                        placeholderBuilder.replace(first, second, str); break;
                    case "%player.rank%":
                        str = String.valueOf(damageUtil.getPlayerRank(player));
                        placeholderBuilder.replace(first, second, str); break;
                    case "%mob.name%":
                        placeholderBuilder.replace(first, second, damageUtil.getEntityName()); break;
                    case "%mob.hp%":
                        placeholderBuilder.replace(first, second, String.valueOf(damageUtil.getEntityHealth())); break;
                    case "%lasthit%":
                        placeholderBuilder.replace(first, second, damageUtil.getLasthit().getName()); break;
                    case "%lasthit.dmg%":
                        str = df.format(damageUtil.getPlayerDamage(damageUtil.getLasthit())).replace(",", ".");
                        placeholderBuilder.replace(first, second, str); break;
                    case "%lasthit.pdmg%":
                        str = df.format(damageUtil.getPercentageDamage(damageUtil.getLasthit())).replace(",", ".");
                        placeholderBuilder.replace(first, second, str); break;
                    case "%lasthit.pdmg100%":
                        str = df.format(damageUtil.getPercentageDamage(damageUtil.getLasthit())*100).replace(",", ".");
                        placeholderBuilder.replace(first, second, str); break;
                    case "%lasthit.rank%":
                        str = String.valueOf(damageUtil.getPlayerRank(damageUtil.getLasthit()));
                        placeholderBuilder.replace(first, second, str); break;
                    case "%boost%":
                        int globalBoost = Boosts.getGlobalBoost();
                        if(globalBoost<pendingBoost) {
                            placeholderBuilder.replace(first, second, String.valueOf(pendingBoost)); break;
                        } placeholderBuilder.replace(first, second, String.valueOf(globalBoost)); break;
                }
            }
            first = i.indexOf("%", second);
            second = i.indexOf("%", first+1);
        }

        i = placeholderBuilder.toString();

        if(i.contains("%math%") || i.contains("*")) {
            i = i.replace("%math%", "");
            i = String.valueOf(PlaceholderUtil.parseMath(i));
        }

        return i;
    }
}
