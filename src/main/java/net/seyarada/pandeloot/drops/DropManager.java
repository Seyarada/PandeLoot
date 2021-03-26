package net.seyarada.pandeloot.drops;

import net.seyarada.pandeloot.damage.DamageUtil;
import net.seyarada.pandeloot.items.ItemUtils;
import net.seyarada.pandeloot.rewards.RewardLine;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DropManager {

    private final List<Player> players;
    private List<RewardLine> rewards;
    private DamageUtil damageUtil;
    private Location location;

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

    public void setDamageUtil(DamageUtil util) {
        this.damageUtil = util;
    }

    public void initDrops() {
        System.out.println("+");
        List<RewardLine> playerDrops = new ArrayList<>();
        for(Player i : players) {
            System.out.println("++");
            playerDrops.clear();
            ItemUtils.collectRewards(rewards, playerDrops, players.size(), damageUtil, i);
            DropConditions.filter(playerDrops, i, damageUtil);

            /*
            double n = playerDrops.size();

            for (double x = 1; x < n+1; x++) {
                double angle = 2 * Math.PI / n;
                double cos = Math.cos(angle * x);
                double sin = Math.sin(angle * x);
                double iX = damageUtil.getLocation().getX() + 3 * cos;
                double iZ = damageUtil.getLocation().getZ() + 3 * sin;

                Location loc = damageUtil.getLocation().clone();
                loc.setX(iX);
                loc.setZ(iZ);

                Bukkit.getScheduler().scheduleSyncRepeatingTask(PandeLoot.getInstance(), () -> {

                    Color rgb = ColorUtil.getRGB("GOLD");
                    Particle.DustOptions dustOptions = new Particle.DustOptions(rgb, 1);
                    damageUtil.getLocation().getWorld().spawnParticle(Particle.REDSTONE, loc, 1, dustOptions);

                }, 0, 1);
            }
            */

            System.out.println("+++");

            for(RewardLine j : playerDrops) {

                System.out.println("++++");

                System.out.println(4 + j.getLine());

                System.out.println("Chance:"+ j.getChance());

                if(damageUtil!=null)
                    new DropItem(j, damageUtil.getLocation(), i);
                else
                    new DropItem(j, location, i);


                if(j.isShared())
                    rewards.remove(j);
            }

        }
    }

}
