package net.seyarada.pandeloot;

import net.milkbowl.vault.economy.Economy;
import net.seyarada.pandeloot.commands.*;
import net.seyarada.pandeloot.compatibility.mythicmobs.MythicMobsCompatibility;
import net.seyarada.pandeloot.damage.DamageTracker;
import net.seyarada.pandeloot.rewards.RewardsListener;
import org.bukkit.entity.ArmorStand;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class PandeLoot extends JavaPlugin {

    private static PandeLoot instance;
    private static Economy econ = null;
    public static final List<ArmorStand> totalHolograms = new ArrayList<>();

    @Override
    public void onEnable() {

        instance = this;

        new Config();
        if(getServer().getPluginManager().getPlugin("MythicMobs")!=null) {
            MythicMobsCompatibility mmComp = new MythicMobsCompatibility();
            this.getServer().getPluginManager().registerEvents(mmComp, this);
        }
        this.getServer().getPluginManager().registerEvents(new DamageTracker(), this);
        this.getServer().getPluginManager().registerEvents(new RewardsListener(), this);

        this.getCommand("pandeloot").setExecutor(new CommandManager());

        // Register auto completion

        new BoosterCommand();
        new GiveCommand();
        new ReloadCommand();

        this.getCommand("pandeloot").setTabCompleter(new AutoComplete());

        setupEconomy();

    }

    @Override
    public void onDisable() {
        // Removes possible holograms that may be alive at the time of reload, so they don't get stuck
        // in a "frozen" state.
        try {
            for(ArmorStand i : totalHolograms) {
                if(i!=null && i.isValid()) {
                    i.remove();
                }
            }
        } catch (Exception ignored) { }
    }

    private void setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return;
        }
        econ = rsp.getProvider();
    }

    public static Economy getEconomy() {
        return econ;
    }

    public static PandeLoot getInstance() {
        return instance;
    }

}
