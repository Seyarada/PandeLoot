package net.seyarada.pandeloot.drops;

import net.seyarada.pandeloot.PandeLoot;
import net.seyarada.pandeloot.damage.DamageUtil;
import net.seyarada.pandeloot.items.ItemUtils;
import net.seyarada.pandeloot.rewards.RewardLine;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class DropManager {

    private List<Player> players;
    private List<RewardLine> rewards;
    private DamageUtil damageUtil;
    private Location location;

    public int delay;

    public DropManager(List<Player> players, List<RewardLine> rewards) {
        this.players = players;
        this.rewards = rewards;
    }

    public DropManager(Player player, List<RewardLine> rewards) {
        this.players = Collections.singletonList(player);
        this.rewards = rewards;
    }

    public DropManager(Player player, Location location, List<RewardLine> rewards) {
        this.players = Collections.singletonList(player);
        this.location = location;
        this.rewards = rewards;
    }

    public DropManager(Location location, List<RewardLine> rewards) {
        this.location = location;
        this.rewards = rewards;
    }

    public void setDamageUtil(DamageUtil util) {
        this.damageUtil = util;
    }

    public DamageUtil getDamageUtil() { return damageUtil; }

    public void initDrops() {
        List<RewardLine> playerDrops = new ArrayList<>();

        for(Player i : players) {
            playerDrops.clear();
            ItemUtils.collectRewards(rewards, playerDrops, players.size(), damageUtil, i);
            DropConditions.filter(playerDrops, i, damageUtil);

            int playerDelay = 0;
            int skip = 0;
            Map<Double, Integer> advCounter = new HashMap<>();

            for(RewardLine j : playerDrops) {
                if(skip>0) {
                    skip--;
                    continue;
                }

                // 3 Radius
                if(advCounter.containsKey(j.explodeRadius)) {
                    // 3,x+1
                    advCounter.put(j.explodeRadius, advCounter.get(j.explodeRadius)+1);
                } else {
                    // 3,1
                    advCounter.put(j.explodeRadius, 1);
                }

                //j.options.put("RadialDrop", advCounter);
                j.radialDrop = advCounter;
                //j.options.put("ThisItem", advCounter.get(j.explodeRadius)+1);
                j.radialOrder = advCounter.get(j.explodeRadius)+1;

                playerDelay += j.delay;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        dropAction(j,i);
                    }
                }.runTaskLater(PandeLoot.getInstance(), playerDelay);

                if(j.shared)
                    rewards.remove(j);
                if(j.stop)
                    break;

                skip = j.skip;
            }

            delay = playerDelay;

        }
    }

    private void dropAction(RewardLine j, Player i) {
        if(damageUtil!=null)
            new DropItem(j, damageUtil, i);
        else
            new DropItem(j, location, i);
    }

}
