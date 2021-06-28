package net.seyarada.pandeloot.compatibility.mythicmobs;

import io.lumine.xikage.mythicmobs.adapters.AbstractPlayer;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.drops.Drop;
import io.lumine.xikage.mythicmobs.drops.DropMetadata;
import io.lumine.xikage.mythicmobs.drops.IIntangibleDrop;
import io.lumine.xikage.mythicmobs.drops.IItemDrop;
import net.seyarada.pandeloot.StringLib;
import net.seyarada.pandeloot.rewards.Reward;
import net.seyarada.pandeloot.rewards.RewardLine;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class UnpackDropTable {

    public UnpackDropTable(Reward i, List<Reward> store) {
        StringLib.warn("++++++ Unpacking droptable");
        StringLib.warn("++++++++++ ================");
        StringLib.depthBonus++;
        Map.Entry<Collection<Drop>, DropMetadata> pair = MythicMobsCompatibility.getDropTableDrops(i);
        if(pair==null) return;

        for (Drop drop : pair.getKey()) {
            final RewardLine rewardLine = new RewardLine(drop.getLine());
            rewardLine.generateInsideOptions(i.rewardLine.baseLine);
            rewardLine.chance = "1"; // MM already does the chance check on their side

            final Reward reward = new Reward(rewardLine, i.player, i.damageUtil);

            if (drop instanceof IItemDrop) {
                reward.itemStack = BukkitAdapter.adapt(((IItemDrop) drop).getDrop(pair.getValue()));
                reward.rewardLine.amount = reward.itemStack.getAmount();
            } else if(drop instanceof IIntangibleDrop) {
                final AbstractPlayer abstractPlayer = BukkitAdapter.adapt(i.player);
                ((IIntangibleDrop) drop).giveDrop(abstractPlayer, new DropMetadata(null, abstractPlayer));
                continue;
            }
            store.add(reward);
        }
    }

}
