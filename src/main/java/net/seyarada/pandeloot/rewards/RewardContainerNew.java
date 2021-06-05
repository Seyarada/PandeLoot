package net.seyarada.pandeloot.rewards;

import net.seyarada.pandeloot.Config;
import net.seyarada.pandeloot.StringLib;
import net.seyarada.pandeloot.options.Conditions;
import net.seyarada.pandeloot.options.Reward;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class RewardContainerNew {

    public ConfigurationSection rewardContainer;
    public List<String> rewards;
    public Reward reward;
    public String internalName;

    int totalItems;
    int minItems;
    int maxItems;
    int guaranteedItems;

    int rewardSize;

    int goalAmount;
    int droppedAmount;

    public RewardContainerNew(String internalName, Reward reward) {
        if(internalName==null) {
            StringLib.warn("+ NULL REWARD CONTAINER CREATED");
            return;
        }
        this.internalName = internalName;
        this.rewardContainer = Config.getRewardContainer(internalName);
        this.reward = reward;

        rewards = rewardContainer.getStringList("Rewards");
        rewardSize = rewards.size();

        guaranteedItems = Math.min(rewardContainer.getInt("Guaranteed"), rewardSize);
        totalItems = Math.min(rewardContainer.getInt("TotalItems"), rewardSize);
        minItems = Math.min(rewardContainer.getInt("MinItems"), rewardSize);
        maxItems = Math.min(rewardContainer.getInt("MaxItems"), rewardSize);
        goalAmount = generateGoalAmount();

        StringLib.warn("++ Guaranteed: "+guaranteedItems);
        StringLib.warn("++ TotalItems: "+totalItems);
        StringLib.warn("++ MinItems: "+minItems);
        StringLib.warn("++ MaxItems: "+maxItems);
        StringLib.warn("++ GoalAmount: "+goalAmount);

    }

    private int generateGoalAmount() {
        if (totalItems > 0) return totalItems;

        final Random r = new Random();
        if (minItems > 0 && maxItems > 0) return r.nextInt(maxItems - minItems + 1) + minItems;
        if (minItems > 0)                 return r.nextInt(rewardSize - minItems + 1) + minItems;
        return 0;
    }

    public List<RewardLineNew> getDrops() {
        final List<RewardLineNew> drops = new ArrayList<>();

        if(goalAmount>0) {
            final List<RewardLineNew> items = getRawDropsParsingConditions(true);
            StringLib.warn("+ Getting drops of goal amount");
            while(droppedAmount<goalAmount && items.size()>0) {
                RewardLineNew item = doRoll(items);                             // by item until it reaches the desired
                if(item!=null)
                    drops.add(item);
            }
        } else if(guaranteedItems>0) {
            final List<RewardLineNew> items = getRawDropsParsingConditions(true);
            StringLib.warn("+ Getting drops of guaranteed");
            while(drops.size()<guaranteedItems && items.size()>0) {
                RewardLineNew item = doRoll(items);
                List<String> lS = new ArrayList<>();
                for (RewardLineNew m : drops) {
                    lS.add(m.baseLine);
                }
                if(item!=null&&!lS.contains(item.baseLine))
                    drops.add(item);
            }
        } else {
            final List<RewardLineNew> items = getRawDropsParsingConditions(false);
            StringLib.warn("+ Getting all drops");
            drops.addAll(items);
        }
        droppedAmount = 0; // Resets the amount in case this lootTable gets called again
        return drops;
    }

    public static void addRewardContainerOptions(RewardLineNew rewardLine, ConfigurationSection rewardContainer) {
        if(rewardContainer==null) {
            StringLib.warn("++++++ RewardContainer is null, skipping");
            return;
        }

        ConfigurationSection optionsContainer = rewardContainer.getConfigurationSection("Options");

        if(optionsContainer==null) {
            StringLib.warn("++++++ OptionsContainer is null, skipping");
            return;
        }

        for(String str : optionsContainer.getKeys(false)) {
            StringLib.warn("+++++++ Adding "+str+ " with value "+optionsContainer.getString(str)+
                    " to "+rewardLine.baseLine);
            rewardLine.insideOptions.put(str.toLowerCase(), optionsContainer.getString(str));
        }
    }

    public static void addRewardContainerOptions(Reward rewardLine, ConfigurationSection rewardContainer) {
        if(rewardContainer==null) {
            StringLib.warn("++++++ RewardContainer is null, skipping");
            return;
        }

        for(String str : rewardContainer.getKeys(false)) {
            String capitalizedStr = str;
            str = str.toLowerCase();
            if(Config.defaultOptions.containsKey(str)) {
                if(Config.defaultOptions.get(str).equals(rewardLine.options.get(str))) {
                    StringLib.warn("+++++++ Adding "+str+ " with value "+rewardContainer.getString(capitalizedStr)+
                            " to "+rewardLine.rewardLine.baseLine);
                    rewardLine.options.put(str, rewardContainer.getString(capitalizedStr));
                }
            }
        }
    }

    private RewardLineNew doRoll(List<RewardLineNew> items) {
        if(items.size()==0) return null;

        // Compute the total weight of all items together.
        RewardLineNew[] rollItems = items.toArray(new RewardLineNew[0]);
        double totalWeight = 0.0;
        for (RewardLineNew i : items) {
            totalWeight += i.getChance(reward.damageUtil, reward.player);
        }

        // Now choose a random item.
        int idx = 0;
        for (double r = Math.random() * totalWeight; idx < rollItems.length - 1; ++idx) {
            r -= rollItems[idx].getChance(reward.damageUtil, reward.player);
            if (r <= 0.0) break;
        }
        droppedAmount++;

        items.remove(idx); // Makes sure there isn't any repeated

        RewardLineNew item = rollItems[idx];
        item.skipConditions = true;
        return item;
    }


    private List<RewardLineNew> getRawDrops() {
        List<RewardLineNew> drops = new ArrayList<>();
        for (String rewardString : rewards) {
            RewardLineNew rewardLine = new RewardLineNew(rewardString);
            rewardLine.build(reward.player, reward.damageUtil);
            addRewardContainerOptions(rewardLine, rewardContainer);
            drops.add(rewardLine);
        }
        return drops;
    }

    private List<RewardLineNew> getRawDropsParsingConditions(boolean ignoreChance) {
        List<RewardLineNew> drops = new ArrayList<>();
        List<RewardLineNew> rewardLines = getRawDrops();

        List<Reward> rewardsList = reward.createNewRewards(rewardLines);
        if(ignoreChance) {
            for(Reward reward : rewardsList) {
                reward.options.put("ignorechance", "true");
            }
        }
        rewardsList.removeIf(i -> Conditions.getResult(i) && !i.skipConditions);

        for (Reward item : rewardsList) {
            item.rewardLine.chance = "1";
            drops.add(item.rewardLine);
        }
        return drops;
    }




}
