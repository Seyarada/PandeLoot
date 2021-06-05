package net.seyarada.pandeloot.nms;

import net.minecraft.server.v1_16_R3.*;
import net.seyarada.pandeloot.Config;
import net.seyarada.pandeloot.PandeLoot;
import net.seyarada.pandeloot.StringLib;
import net.seyarada.pandeloot.options.Options;
import net.seyarada.pandeloot.options.Reward;
import net.seyarada.pandeloot.utils.MathUtil;
import net.seyarada.pandeloot.utils.PlaceholderUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftChatMessage;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PacketLockedHologram {

    private int id;
    private EntityArmorStand abandonEntity;
    private int abandonTime;
    private String abandonText;
    private String timeText;

    public void spawnLockedHologram(Entity toTrack, Location location, List<String> toDisplay, List<Player> players, int aT, Reward reward) {

        double y = 0.45;
        abandonTime = 20*aT;

        if(players==null || players.isEmpty() || players.contains(null)) {
            players = new ArrayList<>();for (Entity entity : location.getWorld().getNearbyEntities(location, 42, 42, 42)) {
                if(entity instanceof Player) {
                    players.add((Player)entity);
                }
            }
        }

        List<EntityArmorStand> armorStands = new ArrayList<>();

        if(abandonTime>0) {
            abandonText = PlaceholderUtil.parse(Config.getAbandonText(), null, null, false);
            List<String> strs = Arrays.asList(abandonText.split(","));
            Collections.reverse(strs);
            toDisplay.add("");
            toDisplay.addAll(strs);
        }

        for(String i : toDisplay) {
            Location newLoc = location.clone();
            newLoc.setY(location.getY() + y);
            y += 0.2;

            final EntityArmorStand armorStand = spawnPacketHologram(newLoc, i);
            armorStands.add(armorStand);
            if(armorStand==null) continue;
            PacketPlayOutSpawnEntity packetPlayOutSpawnEntity = new PacketPlayOutSpawnEntity(armorStand, 1);
            PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(armorStand.getId(), armorStand.getDataWatcher(), true);

            for(Player player : players) {
                if (player != null && player.isOnline() && ((CraftPlayer) player).getHandle() != null) {
                    final PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
                    connection.sendPacket(packetPlayOutSpawnEntity);
                    connection.sendPacket(metadata);
                }
            }
        }

        List<Player> finalPlayers = players;
        id = Bukkit.getScheduler().scheduleSyncRepeatingTask(PandeLoot.getInstance(), () -> {
            if (toTrack.isValid()) {
                updateLocation(toTrack.getLocation(), armorStands, finalPlayers);

                if(abandonTime>0) {
                    abandonTime--;
                    String text = timeText.replace("%time%", MathUtil.getDurationAsTime(abandonTime / 20));
                    abandonEntity.setCustomName(CraftChatMessage.fromStringOrNull(text));
                }

                if(abandonTime==0 && aT>0) {
                    Location aaLoc = toTrack.getLocation().clone();
                    ItemStack itemStack = ((org.bukkit.entity.Item) toTrack).getItemStack();
                    itemStack = NMSManager.removeNBT(itemStack, StringLib.root);

                    toTrack.remove();

                    Item newItem = aaLoc.getWorld().dropItem(aaLoc, itemStack);
                    newItem.setVelocity(new Vector());
                    reward.item = newItem;
                    reward.options.put("abandontime", "0");
                    reward.options.put("canview", "everyone");
                    reward.player = null;
                    reward.isAbandoned = true;
                    Options.callOptions(reward);
                }

            }
            else {
                for(EntityArmorStand i : armorStands) {
                    if(i==null) continue;
                    if(i instanceof Item) continue;
                    for(Player player : finalPlayers) {
                        if(player==null) continue;
                        final PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
                        connection.sendPacket(new PacketPlayOutEntityDestroy(i.getId()));
                    }
                }
                Bukkit.getScheduler().cancelTask(id);
            }
        }, 0, 1);

    }

    public void updateLocation(Location location, List<EntityArmorStand> holograms, List<Player> players) {
        double y = 0.25;
        for(EntityArmorStand i : holograms) {
            y += 0.2;
            if(i==null) continue;

            Location newLoc = location.clone();
            newLoc.setY(location.getY()+y);
            //i.setCustomName(CraftChatMessage.fromStringOrNull(location.getBlock().toString()));
            i.setLocation(newLoc.getX(), newLoc.getY(), newLoc.getZ(), 0 ,0);
            PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(i.getId(), i.getDataWatcher(), true);
            for(Player player : players) {
                if(player==null) continue;
                final PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
                connection.sendPacket(new PacketPlayOutEntityTeleport(i));
                connection.sendPacket(metadata);
            }
            //i.teleportAndSync(location.getX(), location.getY(), location.getZ());
        }

    }

    private EntityArmorStand spawnPacketHologram(Location location, String display) {
        if(display==null || display.isEmpty()) return null;
        WorldServer wS = ((CraftWorld) location.getWorld()).getHandle();
        final EntityArmorStand armorStand = new EntityArmorStand(EntityTypes.ARMOR_STAND, wS);
        armorStand.setPosition(location.getX(), location.getY(), location.getZ());
        armorStand.setCustomName(CraftChatMessage.fromStringOrNull(display));
        armorStand.setCustomNameVisible(true);
        armorStand.setInvisible(true);
        armorStand.setMarker(true);
        armorStand.setFireTicks(999999);

        if(display.contains("%time")) {
            abandonEntity = armorStand;
            timeText = display;
        }

        return armorStand;
    }

}
