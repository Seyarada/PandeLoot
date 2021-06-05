package net.seyarada.pandeloot.nms;

import net.minecraft.server.v1_16_R1.*;
import net.seyarada.pandeloot.Config;
import net.seyarada.pandeloot.PandeLoot;
import net.seyarada.pandeloot.damage.DamageTracker;
import net.seyarada.pandeloot.damage.DamageUtil;
import net.seyarada.pandeloot.utils.PlaceholderUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_16_R1.util.CraftChatMessage;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class V1_16_R1 {

    public static org.bukkit.inventory.ItemStack removeNBT(org.bukkit.inventory.ItemStack item, String key) {
        ItemStack removeNBT = CraftItemStack.asNMSCopy(item);
        removeNBT.removeTag(key);
        return (CraftItemStack.asBukkitCopy(removeNBT));
    }

    public static org.bukkit.inventory.ItemStack addNBT(org.bukkit.inventory.ItemStack item, String key, String value) {
        ItemStack addNBT = CraftItemStack.asNMSCopy(item);
        NBTTagCompound addNBTCompound = (addNBT.hasTag()) ? addNBT.getTag() : new NBTTagCompound();
        addNBTCompound.set(key, NBTTagString.a(value));
        addNBT.setTag(addNBTCompound);
        return (CraftItemStack.asBukkitCopy(addNBT));
    }

    public static boolean hasTag(org.bukkit.inventory.ItemStack item, String tag) {
        ItemStack hasNBT = CraftItemStack.asNMSCopy(item);
        if(hasNBT.hasTag()) {
            return !hasNBT.getTag().getString(tag).isEmpty();
        }
        return false;
    }

    public static String getTag(org.bukkit.inventory.ItemStack item, String tag) {
        ItemStack hasNBT = CraftItemStack.asNMSCopy(item);
        if(hasNBT.hasTag()) {
            return hasNBT.getTag().getString(tag);
        }
        return null;
    }

    public static void destroyEntity(int toBeDestroyed, Entity entity) {
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(toBeDestroyed);
        ((CraftPlayer) entity).getHandle().playerConnection.sendPacket(packet);
    }

    public static void spawnHologram(DamageUtil damageUtil) {
        UUID uuid = damageUtil.getUUID();
        for(Player player : DamageTracker.get(uuid).keySet()) {

            Location location = Bukkit.getEntity(uuid).getLocation();
            WorldServer wS = ((CraftWorld) location.getWorld()).getHandle();
            double lX = location.getX();
            double lY = location.getY() + 1.2;
            double lZ = location.getZ();

            List<String> messages = Config.getScoreHologram();
            Collections.reverse(messages);

            for (String msg : messages) {
                lY += 0.2;
                if(msg==null || msg.isEmpty()) continue;

                msg = PlaceholderUtil.parse(msg, damageUtil, player, false);

                final EntityArmorStand armorStand = new EntityArmorStand(EntityTypes.ARMOR_STAND, wS);
                armorStand.setPosition(lX, lY, lZ);
                armorStand.setCustomName(CraftChatMessage.fromStringOrNull(msg));
                armorStand.setCustomNameVisible(true);
                armorStand.setInvisible(true);
                armorStand.setMarker(true);
                PacketPlayOutSpawnEntity packetPlayOutSpawnEntity = new PacketPlayOutSpawnEntity(armorStand, 1);
                PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(armorStand.getId(), armorStand.getDataWatcher(), true);

                if (player != null && player.isOnline() && ((CraftPlayer) player).getHandle() != null) {
                    final PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
                    connection.sendPacket(packetPlayOutSpawnEntity);
                    connection.sendPacket(metadata);
                }

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (player.isOnline()) {
                            PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(armorStand.getId());
                            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(destroy);
                        }
                    }
                }.runTaskLater(PandeLoot.getInstance(), 300);
            }

        }
    }

}
