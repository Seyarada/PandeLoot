package net.seyarada.pandeloot.rewards;

import net.seyarada.pandeloot.Errors;
import net.seyarada.pandeloot.damage.DamageUtil;
import net.seyarada.pandeloot.drops.DropConditions;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

public class RewardContainer {

    private final ConfigurationSection rewardContainer;
    private final RewardLine line;

    private int totalItems = 0;
    private int minItems = 0;
    private int maxItems = 0;
    private int amountGoal = 0;
    private int amountDropped = 0;
    private int guaranteed = 0;

    private Player player;
    private DamageUtil damageUtil;

    public RewardContainer(ConfigurationSection rewardContainer, RewardLine line) {
        this.rewardContainer = rewardContainer;
        this.line = line;
        initDropTable();
    }

    private void initDropTable() {
        totalItems = rewardContainer.getInt("TotalItems");
        minItems = rewardContainer.getInt("MinItems");
        maxItems = rewardContainer.getInt("MaxItems");
        guaranteed = rewardContainer.getInt("Guaranteed");


        if(guaranteed>rewardContainer.getStringList("Rewards").size()) {
            guaranteed = rewardContainer.getStringList("Rewards").size();
        }

        if(totalItems>0) {
            amountGoal = totalItems;

        } else if (minItems>0 && maxItems>0) {
            Random r = new Random();
            if(maxItems>rewardContainer.getStringList("Rewards").size()) {
                maxItems = rewardContainer.getStringList("Rewards").size();
            }
            amountGoal = r.nextInt(maxItems-minItems+1) + minItems;
        } else if(minItems>0) {
            Random r = new Random();
            maxItems = rewardContainer.getStringList("Rewards").size();
            amountGoal = r.nextInt(maxItems-minItems+1) + minItems;
        }
    }

    public List<RewardLine> getDrops() {
        List<RewardLine> drops = new ArrayList<>();

        if(amountGoal<=0) {                                             // No special limiters found, so this
            for (RewardLine lineConfig : getRawDropsParsingChance()) {     // sends all the items in then lets// DropItem take care of the chance
                setParentTable(lineConfig, rewardContainer);
                drops.add(lineConfig);
            }
            List<RewardLine> items = getRawDropsParsingConditions();
            while(drops.size()<guaranteed) {
                RewardLine item = doRoll(items);                             // by item until it reaches the desired
                List<String> lS = new ArrayList<>();
                for(RewardLine m : drops) {
                    lS.add(m.getLine());
                }
                if(item!=null&&!lS.contains(item.getLine()))
                    drops.add(item);
            }
        } else {
            List<RewardLine> items;
            if(damageUtil==null||player==null)
                items = getRawDrops();
            else
                items = getRawDropsParsingConditions();
            while(amountDropped<amountGoal) {

                RewardLine item = doRoll(items);                             // by item until it reaches the desired
                if(item!=null)
                    drops.add(item);
            }
        }
        amountDropped = 0; // Resets the amount in case this lootTable gets called again
        return drops;
    }

    private RewardLine doRoll(List<RewardLine> items) {

        if(items.size()==0) return null;

        // Compute the total weight of all items together.
        RewardLine[] rollItems = items.toArray(new RewardLine[0]);
        double totalWeight = 0.0;
        for (RewardLine i : items) {
            totalWeight += i.getChance(damageUtil, player);
        }

        // Now choose a random item.
        int idx = 0;
        for (double r = Math.random() * totalWeight; idx < rollItems.length - 1; ++idx) {
            r -= rollItems[idx].getChance(damageUtil, player);
            if (r <= 0.0) break;
        }
        amountDropped++;

        items.remove(idx); // Makes sure there isn't any repeated

        RewardLine item = rollItems[idx];
        item.setSkip(true);
        return item;
    }

    private List<RewardLine> getRawDrops() {
        List<RewardLine> drops = new ArrayList<>();
        for (String item : rewardContainer.getStringList("Rewards")) {
            RewardLine lineConfig = new RewardLine(item);
            setParentTable(lineConfig, rewardContainer);
            drops.add(lineConfig);
        }
        return drops;
    }

    private List<RewardLine> getRawDropsParsingConditions() {
        List<RewardLine> drops = new ArrayList<>();
        List<RewardLine> rewards = RewardLine.StringListToRewardList(rewardContainer.getStringList("Rewards"));

        DropConditions.filter(rewards, player, damageUtil);

        for (RewardLine item : rewards) {
            item.setChance("1");
            setParentTable(item, rewardContainer);
            drops.add(item);
        }
        return drops;
    }

    private List<RewardLine> getRawDropsParsingChance() {
        List<RewardLine> drops = new ArrayList<>();
        List<RewardLine> rewards = RewardLine.StringListToRewardList(rewardContainer.getStringList("Rewards"));

        DropConditions.runConditionsSimple(rewards);

        for (RewardLine item : rewards) {
            item.setChance("1");
            setParentTable(item, rewardContainer);
            drops.add(item);
        }
        return drops;
    }

    // This adds the LootTable options to the lineConfig.
    // Note: This won't override existing options
    public static RewardLine setParentTable(RewardLine lineConfig, ConfigurationSection rewardContainer) {

        if(rewardContainer==null) {
            Errors.UnableToFindLootTable(lineConfig.getOption(null, "parent"));
            return lineConfig;
        }

        for (String key : rewardContainer.getKeys(false)) {
            String value = rewardContainer.getString(key);
            lineConfig.put(key, value);
        }
        return lineConfig;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setDamageUtil(DamageUtil damageUtil) {
        this.damageUtil = damageUtil;
    }
}
