package net.seyarada.pandeloot.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import net.seyarada.pandeloot.Boosts;
import net.seyarada.pandeloot.PandeLoot;
import net.seyarada.pandeloot.damage.DamageUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

public class PlaceholderUtil {

    public static String parse(String i) {
        return parse(i, null, null, true);
    }

    public static String parse(String i, DamageUtil damageUtil, Player player, boolean doMath) {

        if(i==null || i.isEmpty()) return i;

        i = nukeMythicMobsPlaceholders(i);

        i = ChatColor.translateAlternateColorCodes('&', i);
        if(!i.contains("%")) return i;

        int pendingBoost = 0;

        if(player!=null) {

            if(PandeLoot.getInstance().getServer().getPluginManager().getPlugin("PlaceholderAPI")!=null)
                PlaceholderAPI.setPlaceholders(player, i);

            i = fastReplace(i, "%player%", player.getName());
            pendingBoost = Boosts.getPlayerBoost(player.getName());

            if(damageUtil!=null) {

                for (int j = 0; j < 3; j++) {
                    int k = j + 1;
                    if(damageUtil.getRankedPlayers().size()>=k) {
                        i = fastReplace(i, "%"+k+".name%", damageUtil.getPlayer(j).getName());
                        i = fastReplace(i, "%"+k+".dmg%", String.valueOf(damageUtil.getDamage(j)).replace(".0", ""));
                    } else if(i.contains("%"+k+".name%") || i.contains("%"+k+".dmg%")) {
                        return null;
                    }
                }

                if(i.contains("%player.")) {
                    DecimalFormat df = new DecimalFormat("#.##");
                    i = fastReplace(i, "%player.dmg%", df.format(damageUtil.getPlayerDamage(player)).replace(",", ".").replace(".0", ""));
                    i = fastReplace(i, "%player.pdmg%", df.format(damageUtil.getPercentageDamage(player)).replace(",", ".")).replace(".0", "");
                    i = fastReplace(i, "%player.pdmg100%", df.format(damageUtil.getPercentageDamage(player) * 100).replace(",", ".").replace(".0", ""));
                    i = fastReplace(i, "%player.rank%", df.format(damageUtil.getPlayerRank(player)).replace(",", "."));
                }
            }
        }

        if(damageUtil!=null) {
            if(i.contains("%mob.")) {
                i = fastReplace(i, "%mob.name%", damageUtil.getEntityName());
                i = fastReplace(i, "%mob.hp%", String.valueOf(damageUtil.getEntityHealth())).replace(".0", "");
            }

            if(damageUtil.lastHit!=null && i.contains("%lasthit")) {
                DecimalFormat df = new DecimalFormat("#.##");
                i = fastReplace(i, "%lasthit%", damageUtil.getLasthit().getName());
                i = fastReplace(i, "%lasthit.dmg%", df.format(damageUtil.getPlayerDamage(damageUtil.getLasthit())).replace(",", ".").replace(".0", ""));
                i = fastReplace(i, "%lasthit.pdmg%", df.format(damageUtil.getPercentageDamage(damageUtil.getLasthit())).replace(",", ".").replace(".0", ""));
                i = fastReplace(i, "%lasthit.pdmg100%", String.valueOf(df.format(damageUtil.getPercentageDamage(damageUtil.getLasthit()) * 100)).replace(",", ".").replace(".0", ""));
                i = fastReplace(i, "%lasthit.rank%", df.format(damageUtil.getPlayerRank(damageUtil.getLasthit())).replace(",", "."));
            }
        }

        if(i.contains("%boost%")) {
            int globalBoost = Boosts.getGlobalBoost();
            if(globalBoost<pendingBoost)
                i = fastReplace(i, "%boost%", String.valueOf(pendingBoost));
            else
                i = fastReplace(i, "%boost%", String.valueOf(globalBoost));
        }

        if(doMath && (i.contains("%math%") || i.contains("*") || i.contains("+")) ) {
            i = fastReplace(i, "%math%", "");
            i = String.valueOf(parseMath(i));
        }

        return i;
    }

    public static double parseMath(String string) {
        return MathUtil.eval(string);
    }


    public static String nukeMythicMobsPlaceholders(String i) {
        i = fastReplace(i, "%nl%", "\n");       // This replaces MythicMobs string placeholders which are thrown in
        i = fastReplace(i, "%co%", ":");        // forcefully when they create a ConfigLine, god knows why
        i = fastReplace(i, "<pc>", "%");
        i = fastReplace(i, "<&co>", ":");
        i = fastReplace(i, "<&sq>", "'");
        i = fastReplace(i, "<&da>", "-");
        i = fastReplace(i, "<&bs>", "\\");
        i = fastReplace(i, "<&fs>", "/");
        i = fastReplace(i, "<&sp>", " ");
        i = fastReplace(i, "<&csp>", " ");
        i = fastReplace(i, "<&cm>", ",");
        i = fastReplace(i, "<&sc>", ";");
        i = fastReplace(i, "<&sq>", "=");
        i = fastReplace(i, "<&rb>", "]");
        i = fastReplace(i, "<&lb>", "[");
        i = fastReplace(i, "<&rc>", "}");
        i = fastReplace(i, "<&lc>", "{");
        return i;
    }

    public static String fastReplace( String str, String target, String replacement ) {
        int targetLength = target.length();
        if( targetLength == 0 ) {
            return str;
        }
        int idx2 = str.indexOf( target );
        if( idx2 < 0 ) {
            return str;
        }
        StringBuilder buffer = new StringBuilder( targetLength > replacement.length() ? str.length() : str.length() * 2 );
        int idx1 = 0;
        do {
            buffer.append( str, idx1, idx2 );
            buffer.append( replacement );
            idx1 = idx2 + targetLength;
            idx2 = str.indexOf( target, idx1 );
        } while( idx2 > 0 );
        buffer.append( str, idx1, str.length() );
        return buffer.toString();
    }

}
