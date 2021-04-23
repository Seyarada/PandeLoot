package net.seyarada.pandeloot.items;

import net.seyarada.pandeloot.Config;
import net.seyarada.pandeloot.rewards.RewardContainer;
import net.seyarada.pandeloot.rewards.RewardLine;
import org.bukkit.configuration.ConfigurationSection;

public class LootTable extends RewardContainer {

    private ConfigurationSection lootTable;

    public LootTable(ConfigurationSection lootTable, RewardLine line) {
        super(lootTable, line);
        this.lootTable = lootTable;
    }

    public static LootTable of(RewardLine line) {
        return new LootTable(Config.getLootTableRaw(line.item), line);
    }

    public ConfigurationSection getLootTable() {
        return lootTable;
    }

}
