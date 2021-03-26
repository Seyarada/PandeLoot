package net.seyarada.pandeloot;

import net.seyarada.pandeloot.commands.*;
import net.seyarada.pandeloot.compatibility.mythicmobs.MythicMobsCompatibility;
import net.seyarada.pandeloot.damage.DamageTracker;
import net.seyarada.pandeloot.rewards.RewardsListener;
import org.bukkit.plugin.java.JavaPlugin;

public class PandeLoot extends JavaPlugin {

    private static PandeLoot instance;

    MythicMobsCompatibility mmComp = new MythicMobsCompatibility();

    @Override
    public void onEnable() {

        instance = this;

        new Config();
        this.getServer().getPluginManager().registerEvents(mmComp, this);
        this.getServer().getPluginManager().registerEvents(new DamageTracker(), this);
        this.getServer().getPluginManager().registerEvents(new RewardsListener(), this);

        this.getCommand("pandeloot").setExecutor(new CommandManager());

        // Register auto completion

        new BoosterCommand();
        new GiveCommand();
        new ReloadCommand();

        this.getCommand("pandeloot").setTabCompleter(new AutoComplete());
    }

    @Override
    public void onDisable() {

    }

    public static PandeLoot getInstance() {
        return instance;
    }

}
