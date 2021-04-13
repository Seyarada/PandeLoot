package net.seyarada.pandeloot.drops;

import net.seyarada.pandeloot.Config;
import net.seyarada.pandeloot.damage.DamageUtil;
import net.seyarada.pandeloot.rewards.RewardLine;
import org.bukkit.entity.Player;

import java.util.List;

public class DropConditions {

    public static void filter(List<RewardLine> rewards, Player player, DamageUtil damageUtil) {
        if(damageUtil==null||player==null) {
            runConditionsSimple(rewards);
            return;
        }

        runConditions(rewards, player, damageUtil);
    }

    public static void runConditionsSimple(List<RewardLine> rewards) {
        // true -> false -> no pass aka no remove
        // false -> true -> pass if chance is too aka remove
        rewards.removeIf(i -> !chance(i, null,null) && !i.getSkip());
    }

    public static void runConditions(List<RewardLine> rewards, Player p, DamageUtil u) {
        rewards.removeIf(i -> (!chance(i,p,u) || !damage(i,p,u) || !top(i,p,u) || !permission(i,p) || !lasthit(i,p,u)) && !i.getSkip());
    }

    public static boolean chance(RewardLine i, Player p, DamageUtil u) {
        double r = Math.random();
        return r<=i.getChance(u,p);
    }

    public static boolean damage(RewardLine i, Player player, DamageUtil damageUtil) {
        String requiredDamage = i.getOption(null, "damage");
        if(requiredDamage==null) return true;

        double totalHP = damageUtil.getTotalHP();
        double playerDamage = damageUtil.getPlayerDamage(player);

        if(requiredDamage.contains("%")){
            requiredDamage = requiredDamage.replace("%", "");
            if (requiredDamage.contains("to")) {
                return damageRanged(requiredDamage, playerDamage, totalHP, true);
            }
            return playerDamage / totalHP * 100 >= Double.parseDouble(requiredDamage.replace("%", ""));
        }

        if (requiredDamage.contains("to")) {
            return damageRanged(requiredDamage, playerDamage, totalHP, false);
        }
        else return playerDamage >= Double.parseDouble(requiredDamage);
    }


    private static boolean damageRanged(String damageString, double playerDamage, double totalHP, boolean isPercentage) {
        String[] values = damageString.split("to");
        if(isPercentage)
            return Double.parseDouble(values[0]) <= playerDamage / totalHP * 100 &&
                Double.parseDouble(values[1]) >= playerDamage / totalHP * 100;
        return Double.parseDouble(values[0]) <= playerDamage && Double.parseDouble(values[1]) >= playerDamage;
    }

    public static boolean top(RewardLine i, Player player, DamageUtil damageUtil) {
        String top = i.getOption("0", "top");

        if(top.contains("to")) {
            String[] values = top.split("to");
            return  Integer.parseInt(values[0]) <= damageUtil.getPlayerRank(player) &&
                    Integer.parseInt(values[1]) >= damageUtil.getPlayerRank(player);
        }

        int intTop = Integer.parseInt(top)-1;
        if(intTop>=0) {
            if(damageUtil.getRankedPlayers().size()>intTop)
                return damageUtil.getRankedPlayers().get(intTop).getKey().equals(player);

            return false;
        }
        return true;
    }

    public static boolean permission(RewardLine reward, Player player) {
        String permission = reward.getOption(null, "permission");
        if(permission==null) return true;

        return player.hasPermission(permission);
    }

    public static boolean lasthit(RewardLine rewardLine, Player player, DamageUtil damageUtil) {
        boolean lastHit = Boolean.parseBoolean(rewardLine.getOption(Config.getDefault("LastHit"), "lasthit"));
        if(lastHit)
            return player == damageUtil.lastHit;
        return true;
    }

}
