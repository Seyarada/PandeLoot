package net.seyarada.pandeloot.nms;

import net.seyarada.pandeloot.damage.DamageUtil;
import net.seyarada.pandeloot.nms.v1_13_R2.V1_13_R2;
import net.seyarada.pandeloot.nms.v1_14_R1.V1_14_R1;
import net.seyarada.pandeloot.nms.v1_15_R1.V1_15_R1;
import net.seyarada.pandeloot.nms.v1_16_R1.V1_16_R1;
import net.seyarada.pandeloot.nms.v1_16_R2.V1_16_R2;
import net.seyarada.pandeloot.nms.v1_16_R3.V1_16_R3;
import net.seyarada.pandeloot.nms.v1_17_R1.V1_17_R1;
import net.seyarada.pandeloot.rewards.Reward;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NMSManager {

    public static final Map<Integer, List<Player>> hideItemFromPlayerMap = new ConcurrentHashMap<>();
    public static final String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

    public static ItemStack removeNBT(ItemStack item, String entity) {
        // String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        switch (version) {
            case "v1_17_R1":
                return V1_17_R1.removeNBT(item, entity);
            case "v1_16_R3":
                return V1_16_R3.removeNBT(item, entity);
            case "v1_16_R2":
                return V1_16_R2.removeNBT(item, entity);
            case "v1_16_R1":
                return V1_16_R1.removeNBT(item, entity);
            case "v1_15_R1":
                return V1_15_R1.removeNBT(item, entity);
            case "v1_14_R1":
                return V1_14_R1.removeNBT(item, entity);
            case "v1_13_R2":
                return V1_13_R2.removeNBT(item, entity);
        }
        return item;
    }

    public static ItemStack addNBT(ItemStack item, String key, String value) {
        // String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        switch (version) {
            case "v1_17_R1":
                return V1_17_R1.addNBT(item, key, value);
            case "v1_16_R3":
                return V1_16_R3.addNBT(item, key, value);
            case "v1_16_R2":
                return V1_16_R2.addNBT(item, key, value);
            case "v1_16_R1":
                return V1_16_R1.addNBT(item, key, value);
            case "v1_15_R1":
                return V1_15_R1.addNBT(item, key, value);
            case "v1_14_R1":
                return V1_14_R1.addNBT(item, key, value);
            case "v1_13_R2":
                return V1_13_R2.addNBT(item, key, value);
        }
        return item;
    }

    public static boolean hasTag(ItemStack item, String tag) {
        // String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        switch (version) {
            case "v1_17_R1":
                return V1_17_R1.hasTag(item, tag);
            case "v1_16_R3":
                return V1_16_R3.hasTag(item, tag);
            case "v1_16_R2":
                return V1_16_R2.hasTag(item, tag);
            case "v1_16_R1":
                return V1_16_R1.hasTag(item, tag);
            case "v1_15_R1":
                return V1_15_R1.hasTag(item, tag);
            case "v1_14_R1":
                return V1_14_R1.hasTag(item, tag);
            case "v1_13_R2":
                return V1_13_R2.hasTag(item, tag);
        }
        return false;
    }

    public static String getTag(ItemStack item, String tag) {
        // String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        switch (version) {
            case "v1_17_R1":
                return V1_17_R1.getTag(item, tag);
            case "v1_16_R3":
                return V1_16_R3.getTag(item, tag);
            case "v1_16_R2":
                return V1_16_R2.getTag(item, tag);
            case "v1_16_R1":
                return V1_16_R1.getTag(item, tag);
            case "v1_15_R1":
                return V1_15_R1.getTag(item, tag);
            case "v1_14_R1":
                return V1_14_R1.getTag(item, tag);
            case "v1_13_R2":
                return V1_13_R2.getTag(item, tag);
        }
        return null;
    }

    public static void destroyEntity(int toDestroy, Entity entity) {
        // String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        switch (version) {
            case "v1_17_R1":
                V1_17_R1.destroyEntity(toDestroy, entity); break;
            case "v1_16_R3":
                V1_16_R3.destroyEntity(toDestroy, entity); break;
            case "v1_16_R2":
                V1_16_R2.destroyEntity(toDestroy, entity); break;
            case "v1_16_R1":
                V1_16_R1.destroyEntity(toDestroy, entity); break;
            case "v1_15_R1":
                V1_15_R1.destroyEntity(toDestroy, entity); break;
            case "v1_14_R1":
                V1_14_R1.destroyEntity(toDestroy, entity); break;
            case "v1_13_R2":
                V1_13_R2.destroyEntity(toDestroy, entity); break;
        }
    }

    public static void spawnHologram(DamageUtil damageUtil) {
        // String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        switch (version) {
            case "v1_17_R1":
                V1_17_R1.spawnHologram(damageUtil); break;
            case "v1_16_R3":
                V1_16_R3.spawnHologram(damageUtil); break;
            case "v1_16_R2":
                V1_16_R2.spawnHologram(damageUtil); break;
            case "v1_16_R1":
                V1_16_R1.spawnHologram(damageUtil); break;
            case "v1_15_R1":
                V1_15_R1.spawnHologram(damageUtil); break;
            case "v1_14_R1":
                V1_14_R1.spawnHologram(damageUtil); break;
            case "v1_13_R2":
                V1_13_R2.spawnHologram(damageUtil); break;
        }
    }

    public static void spawnLockedHologram(Entity toTrack, Location location, List<String> toDisplay, List<Player> players, int aT, Reward reward) {
        // String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        switch (version) {
            case "v1_17_R1":
                new V1_17_R1().spawnLockedHologram(toTrack, location, toDisplay, players, aT, reward); break;
            case "v1_16_R3":
                new V1_16_R3().spawnLockedHologram(toTrack, location, toDisplay, players, aT, reward); break;
            case "v1_16_R2":
                new V1_16_R2().spawnLockedHologram(toTrack, location, toDisplay, players, aT, reward); break;
            case "v1_16_R1":
                new V1_16_R1().spawnLockedHologram(toTrack, location, toDisplay, players, aT, reward); break;
            case "v1_15_R1":
                new V1_15_R1().spawnLockedHologram(toTrack, location, toDisplay, players, aT, reward); break;
            case "v1_14_R1":
                new V1_14_R1().spawnLockedHologram(toTrack, location, toDisplay, players, aT, reward); break;
            case "v1_13_R2":
                new V1_13_R2().spawnLockedHologram(toTrack, location, toDisplay, players, aT, reward); break;
        }
    }

    public static void toast(Player player, String title, String frame, ItemStack icon) {
        // String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        switch (version) {
            case "v1_17_R1":
                V1_17_R1.displayToast(player, title, frame, icon); break;
            case "v1_16_R3":
                V1_16_R3.displayToast(player, title, frame, icon); break;
            case "v1_16_R2":
                V1_16_R2.displayToast(player, title, frame, icon); break;
            case "v1_16_R1":
                V1_16_R1.displayToast(player, title, frame, icon); break;
        }
    }

    public static void injectPlayer(Player player) {
        switch (version) {
            case "v1_17_R1":
                V1_17_R1.injectPlayer(player); break;
            case "v1_16_R3":
                V1_16_R3.injectPlayer(player); break;
            case "v1_16_R2":
                V1_16_R2.injectPlayer(player); break;
            case "v1_16_R1":
                V1_16_R1.injectPlayer(player); break;
            case "v1_15_R1":
                V1_15_R1.injectPlayer(player); break;
            case "v1_14_R1":
                V1_14_R1.injectPlayer(player); break;
            case "v1_13_R2":
                V1_13_R2.injectPlayer(player); break;
        }
    }

    public static void removePlayer(Player player) {
        switch (version) {
            case "v1_17_R1":
                V1_17_R1.removePlayer(player); break;
            case "v1_16_R3":
                V1_16_R3.removePlayer(player); break;
            case "v1_16_R2":
                V1_16_R2.removePlayer(player); break;
            case "v1_16_R1":
                V1_16_R1.removePlayer(player); break;
            case "v1_15_R1":
                V1_15_R1.removePlayer(player); break;
            case "v1_14_R1":
                V1_14_R1.removePlayer(player); break;
            case "v1_13_R2":
                V1_13_R2.removePlayer(player); break;
        }
    }

}
