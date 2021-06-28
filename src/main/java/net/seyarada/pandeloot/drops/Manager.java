package net.seyarada.pandeloot.drops;

import net.seyarada.pandeloot.PandeLoot;
import net.seyarada.pandeloot.StringLib;
import net.seyarada.pandeloot.damage.DamageUtil;
import net.seyarada.pandeloot.items.ItemUtils;
import net.seyarada.pandeloot.options.Options;
import net.seyarada.pandeloot.rewards.Reward;
import net.seyarada.pandeloot.rewards.RewardLine;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Manager {

    private List<Player> players;
    public int delay;

    public void fromString(List<Player> players, List<String> rewardStrings, DamageUtil damageUtil, Location location) {
        this.players = players;

        StringLib.warn("++ Started drop manager");

        for(Player player : players) {
            StringLib.warn("++ Doing drop for: "+player.getName());
            final List<Reward> rewards = Reward.rewardsFromStringList(rewardStrings, player, damageUtil);
            if(location==null)
                perEntryDrop(rewards, player.getLocation(), rewardStrings);
            else
                perEntryDrop(rewards, location, rewardStrings);
        }
    }

    public void fromRewardLine(List<Player> players, List<RewardLine> rewardLines, DamageUtil damageUtil, Location location) {
        this.players = players;

        for(Player player : players) {
            final List<Reward> rewards = Reward.createNewRewards(rewardLines, player, damageUtil);
            if(location==null)
                perEntryDrop(rewards, player.getLocation(), rewardLines);
            else
                perEntryDrop(rewards, location, rewardLines);
        }
    }

    public void fromReward(List<Player> players, List<Reward> rewards, Location location) {
        this.players = players;

        for(Player player : players) {
            if(location==null)
                perEntryDrop(rewards, player.getLocation(), rewards);
            else
                perEntryDrop(rewards, location, rewards);
        }
    }

    private void perEntryDrop(List<Reward> rewards, Location location, List<?> rewardSource) {
        final List<Reward> playerDrops = new ArrayList<>();
        final Map<Double, Integer> radialCounter = new HashMap<>();
        int playerDelay = 0;
        int skip = 0;

        ItemUtils.collectRewards(rewards, playerDrops, players.size(), true);

        StringLib.warn("+++ Starting wrong drop loop");

        // This solution is bad and ugly
        for(Reward reward : playerDrops) {
            final String explodeRadiusString = reward.get("exploderadius");
            if(explodeRadiusString!=null) {
                StringLib.warn("++++ Assigning explode radius order");
                final double explodeRadius = Double.parseDouble(explodeRadiusString);
                radialCounter.put(explodeRadius, radialCounter.getOrDefault(explodeRadius,0)+1);
                reward.radialOrder = radialCounter.get(explodeRadius);
            }
        }

        for(Reward reward : playerDrops) {

            playerDelay += Integer.parseInt(reward.options.get("delay"));

            StringLib.warn("+++ Doing drop for "+reward.rewardLine.baseLine);

            if(skip>0) {
                skip--;
                StringLib.warn("+++ Reward skipped");
                continue;
            }

            StringLib.warn("+++ Running reward drop");
            runRewardDrop(reward, radialCounter, location, playerDelay);

            if(Boolean.parseBoolean(reward.get("shared"))) {
                StringLib.warn("+++ Reward was shared, removing it from reward list...");
                if      (rewardSource.get(0) instanceof String) rewardSource.remove(reward.rewardLine.baseLine);
                else if (rewardSource.get(0) instanceof Reward) rewardSource.remove(reward);
            }
            if(Boolean.parseBoolean(reward.get("stop"))) {
                StringLib.warn("+++ Reward had stop, stopping loop...");
                break;
            }

            skip = reward.skip;

        }
    }

    private void runRewardDrop(Reward reward, Map<Double, Integer> radialCounter, Location location, int playerDelay) {

        final String explodeRadiusString = reward.get("exploderadius");
        if(explodeRadiusString!=null) {
            reward.radialDropInformation = radialCounter;
        }

        StringLib.warn("++++ Delay is "+playerDelay);

        if(playerDelay<=0) {
            StringLib.warn("++++ Spawning the reward and calling the options...");
            new SpawnReward(reward, location);
            Options.callOptions(reward);
        } else {
            new BukkitRunnable() {
                @Override
                public void run() {
                    StringLib.warn("++++ Spawning the reward and calling the options...");
                    new SpawnReward(reward, location);
                    Options.callOptions(reward);
                }
            }.runTaskLater(PandeLoot.getInstance(), playerDelay);
        }
    }

}
