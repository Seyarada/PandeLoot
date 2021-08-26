package net.seyarada.pandeloot.rewards;

import net.seyarada.pandeloot.Config;
import net.seyarada.pandeloot.StringLib;
import net.seyarada.pandeloot.options.Conditions;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RewardContainer {

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

    public RewardContainer(String internalName, Reward reward) {
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
        return Math.max(totalItems, Math.max(minItems, maxItems));
    }

    public List<RewardLine> getDrops() { ;
        final List<RewardLine> drops = new ArrayList<>();

        if(goalAmount>0) {
            final List<RewardLine> items = getRawDropsParsingConditions(false);
            final List<RewardLine> itemsIgnoreChance = getRawDropsParsingConditions(true);
            final List<String> recurrentBaseLines = itemsIgnoreChance.stream().map(rL -> rL.baseLine).collect(Collectors.toList());
            drops.addAll(items);

            StringLib.warn("+ Getting drops of goal amount");
            while(minItems>0 && drops.size()<minItems) {
                RewardLine item = doRoll(itemsIgnoreChance);
                List<String> lS = new ArrayList<>();
                for (RewardLine m : drops)
                    lS.add(m.baseLine);
                if(item!=null && Collections.frequency(lS, item.baseLine)<Collections.frequency(recurrentBaseLines, item.baseLine))
                    drops.add(item);
            }
            while(maxItems>0 && drops.size()>maxItems) {
                drops.remove((int)(Math.random() * drops.size()));
            }
            while(totalItems>0 && drops.size()!=totalItems) {
                if(drops.size()<totalItems) {
                    RewardLine item = doRoll(itemsIgnoreChance);
                    List<String> lS = new ArrayList<>();
                    for (RewardLine m : drops)
                        lS.add(m.baseLine);
                    if(item!=null && Collections.frequency(lS, item.baseLine)<Collections.frequency(recurrentBaseLines, item.baseLine))
                        drops.add(item);
                }
                else {
                    drops.remove((int)(Math.random() * drops.size()));
                }
            }
        } else if(guaranteedItems>0) {
            final List<RewardLine> items = getRawDropsParsingConditions(false);
            final List<RewardLine> itemsIgnoreChance = getRawDropsParsingConditions(true);
            drops.addAll(items);
            StringLib.warn("+ Getting drops of guaranteed");
            while(drops.size()<guaranteedItems && itemsIgnoreChance.size()>0) {
                RewardLine item = doRoll(itemsIgnoreChance);
                List<String> lS = new ArrayList<>();
                for (RewardLine m : drops) {
                    lS.add(m.baseLine);
                }
                if(item!=null&&!lS.contains(item.baseLine))
                    drops.add(item);
            }
        } else {
            final List<RewardLine> items = getRawDropsParsingConditions(false);
            StringLib.warn("+ Getting all drops");
            drops.addAll(items);
        }
        return drops;
    }

    public static void addRewardContainerOptions(RewardLine rewardLine, ConfigurationSection rewardContainer) {
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

    private RewardLine doRoll(List<RewardLine> items) {
        if(items.size()==0) return null;

        // Compute the total weight of all items together.
        RewardLine[] rollItems = items.toArray(new RewardLine[0]);
        double totalWeight = 0.0;
        for (RewardLine i : items) {
            i.build(reward.player, reward.damageUtil);
            StringLib.warn("+ Weight of "+i.baseLine+" is "+i.getChance(reward.damageUtil, reward.player));
            totalWeight += i.getChance(reward.damageUtil, reward.player);
        }

        // Now choose a random item.
        int idx = 0;
        for (double r = Math.random() * totalWeight; idx < rollItems.length - 1; ++idx) {
            r -= rollItems[idx].getChance(reward.damageUtil, reward.player);
            if (r <= 0.0) break;
        }

        items.remove(idx); // Makes sure there isn't any repeated

        RewardLine item = rollItems[idx];
        item.skipConditions = true;
        return item;
    }


    private List<RewardLine> getRawDrops() {
        List<RewardLine> drops = new ArrayList<>();
        for (String rewardString : rewards) {
            RewardLine rewardLine = new RewardLine(rewardString);
            rewardLine.build(reward.player, reward.damageUtil);
            addRewardContainerOptions(rewardLine, rewardContainer);
            drops.add(rewardLine);
        }
        return drops;
    }

    private List<RewardLine> getRawDropsParsingConditions(boolean ignoreChance) {
        List<RewardLine> drops = new ArrayList<>();
        List<RewardLine> rewardLines = getRawDrops();

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
