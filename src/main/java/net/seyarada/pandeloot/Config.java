package net.seyarada.pandeloot;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Config {

    private static File dataFolder;

    private static File lootBagFile;
    private static File lootTableFile;
    private static File configFile;

    public static FileConfiguration lootBagConfig;
    public static FileConfiguration lootTableConfig;
    public static FileConfiguration config;

    public Config() {
        dataFolder = PandeLoot.getInstance().getDataFolder();

        generateConfigFile();
        generateLootTableFile();
        generateLootBagFile();

        reload();
    }

    public static void reload() {
        loadConfigFile();
        loadLootTableFile();
        loadLootBagFile();
    }

    public void generateConfigFile() {
        configFile = new File(dataFolder, "Config.yml");
        config = new YamlConfiguration();
        if (!configFile.exists()) {
            try {
                // Load default
                InputStreamReader iSR = new InputStreamReader(PandeLoot.getInstance().getResource("Config.yml"));
                FileConfiguration internalConfig = YamlConfiguration.loadConfiguration(iSR);
                internalConfig.options().copyDefaults(true);
                internalConfig.save(configFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void loadConfigFile() {
        try {
            config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }


    public void generateLootTableFile() {
        lootTableFile = new File(dataFolder, "LootTables.yml");
        lootTableConfig = new YamlConfiguration();
        if (!lootTableFile.exists()) {
            try {
                // Load default
                InputStreamReader iSR = new InputStreamReader(PandeLoot.getInstance().getResource("LootTables.yml"));
                FileConfiguration internalConfig = YamlConfiguration.loadConfiguration(iSR);
                internalConfig.options().copyDefaults(true);
                internalConfig.save(lootTableFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void loadLootTableFile() {
        try {
            lootTableConfig.load(lootTableFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void generateLootBagFile() {
        lootBagFile = new File(dataFolder, "LootBags.yml");
        lootBagConfig = new YamlConfiguration();
        if (!lootBagFile.exists()) {
            try {
                // Load default
                InputStreamReader iSR = new InputStreamReader(PandeLoot.getInstance().getResource("LootBags.yml"));
                FileConfiguration internalConfig = YamlConfiguration.loadConfiguration(iSR);
                internalConfig.options().copyDefaults(true);
                internalConfig.save(lootBagFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void loadLootBagFile() {
        try {
            lootBagConfig.load(lootBagFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static ConfigurationSection getLootTableRaw(String lootTable) {
        return lootTableConfig.getConfigurationSection(lootTable);
    }

    public static ConfigurationSection getLootBagRaw(String lootBag) {
        return lootBagConfig.getConfigurationSection(lootBag);
    }

    public static List<String> getLootTable(String loottable) {
        return lootTableConfig.getStringList(loottable+".Rewards");
    }
}
