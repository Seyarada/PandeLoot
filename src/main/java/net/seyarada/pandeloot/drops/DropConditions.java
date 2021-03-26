package net.seyarada.pandeloot.drops;

import net.seyarada.pandeloot.rewards.RewardLine;
import net.seyarada.pandeloot.damage.DamageUtil;
import org.bukkit.entity.Player;

import java.util.List;

public class DropConditions {

    public static void filter(List<RewardLine> rewards, Player player, DamageUtil damageUtil) {
        if(damageUtil==null||player==null) {
            System.out.println("a");
            runConditionsSimple(rewards);
            return;
        }

        System.out.println("b");
        runConditions(rewards, player, damageUtil);
    }

    public static void runConditionsSimple(List<RewardLine> rewards) {
        // true -> false -> no pass aka no remove
        // false -> true -> pass if chance is too aka remove
        for(RewardLine l : rewards) {
            System.out.println("Chance item:" + l.getItem());
            System.out.println("Chance item:" + l.getChance());
        }
        rewards.removeIf(i -> !chance(i) && !i.getSkip());
    }

    public static void runConditions(List<RewardLine> rewards, Player p, DamageUtil u) {
        rewards.removeIf(i -> (!chance(i) || !damage(i,p,u) || !top(i,p,u)) && !i.getSkip());
    }

    public static boolean chance(RewardLine i) {
        double r = Math.random();
        return r<=i.getChance();
    }

    public static boolean damage(RewardLine i, Player player, DamageUtil damageUtil) {
        System.out.println(i);
        System.out.println(player);
        System.out.println(damageUtil);
        double requiredDamage = Double.parseDouble(i.getOption("0", "damage"));
        double playerDamage = damageUtil.getPlayerDamage(player);

        if(playerDamage<requiredDamage)
            return false;

        return true;
    }

    public static boolean top(RewardLine i, Player player, DamageUtil damageUtil) {
        int top = Integer.parseInt(i.getOption("-1", "top"))-1;
        if(top>=0) {
            if(damageUtil.getRankedPlayers().size()>top)
                if (damageUtil.getRankedPlayers().get(top).getKey().equals(player))
                    return true;

            return false;
        }
        return true;
    }

}
