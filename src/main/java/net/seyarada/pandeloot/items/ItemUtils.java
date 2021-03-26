package net.seyarada.pandeloot.items;

import net.seyarada.pandeloot.damage.DamageUtil;
import net.seyarada.pandeloot.rewards.RewardLine;
import org.bukkit.entity.Player;

import java.util.List;

public class ItemUtils {

    public static void collectRewards(List<RewardLine> rewardsToCollect, List<RewardLine> store, int playerSize, DamageUtil damageUtil, Player player) {
        for(RewardLine i : rewardsToCollect) {
            System.out.println(i.getOrigin());
            switch(i.getOrigin()) {
                case "loottable":
                    LootTable lootTable = LootTable.of(i);
                    lootTable.setDamageUtil(damageUtil);
                    lootTable.setPlayer(player);
                    List<RewardLine> j = lootTable.getDrops();
                    collectRewards(j, store, playerSize, damageUtil, player);
                    break;
                default:
                    if(i.isShared()) {
                        i.setChance(1d/playerSize);
                    }
                    store.add(i);
            }
        }
        System.out.println("Store "+store.size());
    }

}
