package net.seyarada.pandeloot.drops;

import net.seyarada.pandeloot.PandeLoot;
import net.seyarada.pandeloot.StringLib;
import net.seyarada.pandeloot.damage.DamageUtil;
import net.seyarada.pandeloot.items.ItemUtils;
import net.seyarada.pandeloot.options.Options;
import net.seyarada.pandeloot.rewards.Reward;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StartDrops {

    public StartDrops(List<Player> players, List<String> rewardStrings, DamageUtil damageUtil, Location location) {
        StringLib.warn("+++ Started drop starter");

        if(players==null) {
            final List<Reward> rewards = Reward.rewardsFromStringList(rewardStrings, null, damageUtil);
            perEntryDrop(rewards, location, rewardStrings);
            return;
        }

        for(Player player : players) {
            StringLib.warn("++++ Doing drop for: "+player.getName());
            final List<Reward> rewards = Reward.rewardsFromStringList(rewardStrings, player, damageUtil);

            StringLib.warn("++++ Pre-collected rewards:");
            for(Reward reward : rewards) {
                StringLib.warn("+++++ "+reward.rewardLine.baseLine);
            }

            final List<Reward> outputRewards = new ArrayList<>();

            ItemUtils.collectRewards(rewards, outputRewards, players.size(), true);

            StringLib.warn("++++ Post-collected rewards:");
            for(Reward reward : outputRewards) {
                StringLib.warn("+++++ "+reward.rewardLine.baseLine);
            }

            if(location==null)
                perEntryDrop(outputRewards, player.getLocation(), rewardStrings);
            else
                perEntryDrop(outputRewards, location, rewardStrings);
        }
    }

    private void perEntryDrop(List<Reward> rewards, Location location, List<String> rewardSource) {
        final Map<Double, Integer> radialCounter = new HashMap<>();
        int playerDelay = 0;
        int skip = 0;

        StringLib.warn("+++ Starting good drop loop");

        for(Reward reward : rewards) {

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
                rewardSource.remove(reward.rewardLine.baseLine);
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
            StringLib.warn("++++ Assigning explode radius order");
            final double explodeRadius = Double.parseDouble(explodeRadiusString);

            radialCounter.put(explodeRadius, radialCounter.containsKey(explodeRadius)
                    ? radialCounter.get(explodeRadius) + 1
                    : 1);

            reward.radialDropInformation = radialCounter;
            reward.radialOrder = radialCounter.get(explodeRadius)+1;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                StringLib.warn("++++ Spawning the reward and calling the options for "+reward.rewardLine.baseLine);
                new SpawnReward(reward, location);
                Options.callOptions(reward);
            }
        }.runTaskLater(PandeLoot.getInstance(), playerDelay);
    }

}
