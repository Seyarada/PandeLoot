package net.seyarada.pandeloot.nms;

import net.seyarada.pandeloot.damage.DamageUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

public class NMSManager {

    public static ItemStack removeNBT(ItemStack item, String entity) {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        switch (version) {
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
            case "v1_13_R1":
                return V1_13_R1.removeNBT(item, entity);
        }
        return item;
    }

    public static ItemStack addNBT(ItemStack item, String key, String value) {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        switch (version) {
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
            case "v1_13_R1":
                return V1_13_R1.addNBT(item, key, value);
        }
        return item;
    }

    public static boolean hasTag(ItemStack item, String tag) {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        switch (version) {
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
            case "v1_13_R1":
                return V1_13_R1.hasTag(item, tag);
        }
        return false;
    }

    public static String getTag(ItemStack item, String tag) {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        switch (version) {
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
            case "v1_13_R1":
                return V1_13_R1.getTag(item, tag);
        }
        return null;
    }

    public static void destroyEntity(int toDestroy, Entity entity) {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        switch (version) {
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
            case "v1_13_R1":
                V1_13_R1.destroyEntity(toDestroy, entity); break;
        }
    }

    public static void spawnHologram(DamageUtil damageUtil) {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        switch (version) {
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
            case "v1_13_R1":
                V1_13_R1.spawnHologram(damageUtil); break;
        }
    }

}
