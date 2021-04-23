package net.seyarada.pandeloot;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Config {

    private static File dataFolder;

    private static File mobFile;
    private static File lootBagFile;
    private static File lootTableFile;
    private static File configFile;

    private static FileConfiguration mobConfig;
    public static FileConfiguration lootBagConfig;
    public static FileConfiguration lootTableConfig;
    public static FileConfiguration config;

    public Config() {
        dataFolder = PandeLoot.getInstance().getDataFolder();

        generateConfigFile();
        generateLootTableFile();
        generateLootBagFile();
        generateMobFile();

        reload();
    }

    public static void reload() {
        loadConfigFile();
        loadLootTableFile();
        loadLootBagFile();
        loadMobFile();
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

        updateConfig();

    }

    public void updateConfig() {
        InputStreamReader iSR = new InputStreamReader(PandeLoot.getInstance().getResource("Config.yml"));
        FileConfiguration internalConfig = YamlConfiguration.loadConfiguration(iSR);

        for(String i : internalConfig.getKeys(true)) {
            if(!config.contains(i) && config.getStringList(i).size()==0) {
                config.set(i, internalConfig.get(i));
            }
        }
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
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

    public static void loadMobFile() {
        try {
            mobConfig.load(mobFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void generateMobFile() {
        mobFile = new File(dataFolder, "Mobs.yml");
        mobConfig = new YamlConfiguration();
        if (!mobFile.exists()) {
            try {
                // Load default
                InputStreamReader iSR = new InputStreamReader(PandeLoot.getInstance().getResource("Mobs.yml"));
                FileConfiguration internalConfig = YamlConfiguration.loadConfiguration(iSR);
                internalConfig.options().copyDefaults(true);
                internalConfig.save(mobFile);
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

    public static List<String> getScoreHologram() {
        return config.getStringList("Announcements.ScoreHologram");
    }
    public static List<String> getScoreMessage() {
        return config.getStringList("Announcements.ScoreMessage");
    }
    public static int getRainbowFrequency() { return config.getInt("Settings.RainbowFrequency"); }
    public static boolean getPlayArm() { return config.getBoolean("Settings.PlayArmWhenOpeningLootBag"); }
    public static boolean getPlayArmEmpty() { return config.getBoolean("Settings.OnlyPlayArmIfBothEmpty"); }

    public static ConfigurationSection getMob(Entity entity) {

        String entityType = entity.getType().toString();
        String display = entity.getCustomName();
        String world = entity.getWorld().getName();

        for(String i : mobConfig.getKeys(false)) {

            ConfigurationSection subConfig = mobConfig.getConfigurationSection(i);
            final String subType = subConfig.getString("Type");
            if(subType!=null && !subType.equalsIgnoreCase(entityType)) continue;

            final String subDisplay = subConfig.getString("Display");
            if(subDisplay!=null && !subDisplay.equalsIgnoreCase(display)) continue;

            final String subWorld = subConfig.getString("World");
            if(subWorld!=null && !subWorld.equalsIgnoreCase(world)) continue;

            return subConfig;

        }
        return null;
    }

    public static String getDefault(String str) {
        return config.getString("DefaultValues."+str);
    }

    public static String getAbandonText() {
        return config.getString("Settings.AbandonText");
    }

    public static String getSecondsText() {
        return config.getString("Settings.TimeFormatSeconds");
    }

    public static String getMinutesText() {
        return config.getString("Settings.TimeFormatMinutes");
    }

    public static String getHoursText() {
        return config.getString("Settings.TimeFormatHours");
    }

}
