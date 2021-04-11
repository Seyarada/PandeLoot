package net.seyarada.pandeloot.items;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractPlayer;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.drops.LootBag;
import io.lumine.xikage.mythicmobs.drops.*;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import net.seyarada.pandeloot.damage.DamageUtil;
import net.seyarada.pandeloot.rewards.RewardLine;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

public class ItemUtils {

    public static void collectRewards(List<RewardLine> rewardsToCollect, List<RewardLine> store, int playerSize, DamageUtil damageUtil, Player player) {
        for(RewardLine i : rewardsToCollect) {
            switch(i.getOrigin()) {
                case "loottable":
                    LootTable lootTable = LootTable.of(i);
                    lootTable.setDamageUtil(damageUtil);
                    lootTable.setPlayer(player);
                    List<RewardLine> j = lootTable.getDrops();
                    collectRewards(j, store, playerSize, damageUtil, player);
                    break;
                case "droptable":

                    DropTable dropTable;
                    Optional<DropTable> maybeTable = MythicMobs.inst().getDropManager().getDropTable(i.getItem());
                    if(maybeTable.isPresent()) {
                        dropTable = maybeTable.get();

                        DropMetadata dropMeta;
                        if(player==null&&damageUtil==null) {
                            dropMeta = null;
                        } else if (player==null) {
                            AbstractEntity entity = BukkitAdapter.adapt(damageUtil.entity);
                            ActiveMob caster = MythicMobs.inst().getMobManager().getMythicMobInstance(entity);
                            dropMeta = new DropMetadata(caster, null);
                        } else if(damageUtil==null) {
                            AbstractPlayer p = BukkitAdapter.adapt(player);
                            dropMeta = new DropMetadata(null, p);
                        } else {
                            AbstractPlayer p = BukkitAdapter.adapt(player);
                            AbstractEntity entity = BukkitAdapter.adapt(damageUtil.entity);
                            ActiveMob caster = MythicMobs.inst().getMobManager().getMythicMobInstance(entity);
                            dropMeta = new DropMetadata(caster, p);
                        }

                        LootBag loot = dropTable.generate(dropMeta);

                        for (Drop drop : loot.getDrops()) {

                            RewardLine reward = new RewardLine(drop.getLine(), true);
                            if (drop instanceof IItemDrop) {
                                reward.itemStack = BukkitAdapter.adapt(((IItemDrop) drop).getDrop(dropMeta));
                                reward.amount = reward.itemStack.getAmount();
                            }

                            store.add(reward);
                        }
                    }
                    break;
                default:
                    if(i.isShared()) {
                        i.setChance(String.valueOf(1d/playerSize));
                    }
                    store.add(i);
            }
        }
    }

}
