package net.seyarada.pandeloot.items;

import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.drops.Drop;
import io.lumine.xikage.mythicmobs.drops.DropMetadata;
import io.lumine.xikage.mythicmobs.drops.IItemDrop;
import net.seyarada.pandeloot.Config;
import net.seyarada.pandeloot.compatibility.mythicmobs.MythicMobsCompatibility;
import net.seyarada.pandeloot.damage.DamageUtil;
import net.seyarada.pandeloot.drops.DropConditions;
import net.seyarada.pandeloot.rewards.RewardContainer;
import net.seyarada.pandeloot.rewards.RewardLine;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ItemUtils {

    public static void collectRewards(List<RewardLine> rewardsToCollect, List<RewardLine> store, int playerSize, DamageUtil damageUtil, Player player) {
        for(RewardLine i : rewardsToCollect) {

            i.damageUtil = damageUtil;
            i.player = player;
            i.build();

            switch(i.origin) {
                case "loottable":

                    if(DropConditions.validate(i, player, damageUtil)) break;

                    LootTable lootTable = LootTable.of(i);
                    lootTable.setDamageUtil(damageUtil);
                    lootTable.setPlayer(player);
                    List<RewardLine> j = lootTable.getDrops();
                    collectRewards(j, store, playerSize, damageUtil, player);
                    break;
                case "droptable":

                    if(DropConditions.validate(i, player, damageUtil)) break;

                    Map.Entry<Collection<Drop>, DropMetadata> pair = MythicMobsCompatibility.getDropTableDrops(i, player, damageUtil);
                    if(pair==null) break;

                    for (Drop drop : pair.getKey()) {

                        RewardLine reward = new RewardLine(drop.getLine());
                        if (drop instanceof IItemDrop) {
                            reward.itemStack = BukkitAdapter.adapt(((IItemDrop) drop).getDrop(pair.getValue()));
                            reward.amount = reward.itemStack.getAmount();
                            reward.setInsideOptions(i.line);
                        }

                        store.add(reward);
                    }
                    break;
                case "lootbag":
                    if(i.asloottable) {
                        lootTable = new LootTable(Config.getLootBagRaw(i.item), i);
                        lootTable.setDamageUtil(damageUtil);
                        lootTable.setPlayer(player);
                        j = lootTable.getDrops();
                        collectRewards(j, store, playerSize, damageUtil, player);
                        break;
                    }
                    else RewardContainer.setParentTable(i, Config.getLootBagRaw(i.item));
                default:
                    if(i.shared) {
                        i.chance = String.valueOf(1d/playerSize);
                    }
                    store.add(i);
            }
        }
    }

}
