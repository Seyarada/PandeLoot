package net.seyarada.pandeloot.nms.v1_17_R1;

import com.google.gson.JsonObject;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementDisplay;
import net.minecraft.advancements.AdvancementFrameType;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.CriterionInstance;
import net.minecraft.advancements.critereon.LootSerializationContext;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.chat.ChatMessage;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.PacketPlayOutAdvancements;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.minecraft.world.item.ItemStack;
import net.seyarada.pandeloot.Config;
import net.seyarada.pandeloot.PandeLoot;
import net.seyarada.pandeloot.StringLib;
import net.seyarada.pandeloot.damage.DamageTracker;
import net.seyarada.pandeloot.damage.DamageUtil;
import net.seyarada.pandeloot.nms.NMSManager;
import net.seyarada.pandeloot.options.Options;
import net.seyarada.pandeloot.rewards.Reward;
import net.seyarada.pandeloot.utils.MathUtil;
import net.seyarada.pandeloot.utils.PlaceholderUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_17_R1.util.CraftChatMessage;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class V1_17_R1 {

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
        if (hasNBT.hasTag()) {
            return !hasNBT.getTag().getString(tag).isEmpty();
        }
        return false;
    }

    public static String getTag(org.bukkit.inventory.ItemStack item, String tag) {
        ItemStack hasNBT = CraftItemStack.asNMSCopy(item);
        if (hasNBT.hasTag()) {
            return hasNBT.getTag().getString(tag);
        }
        return null;
    }

    public static void destroyEntity(int toBeDestroyed, Entity entity) {
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(toBeDestroyed);
        ((CraftPlayer) entity).getHandle().b.sendPacket(packet);
    }

    public static void spawnHologram(DamageUtil damageUtil) {
        final UUID uuid = damageUtil.getUUID();
        for (UUID playerUUID : DamageTracker.get(uuid).keySet()) {
            final Player player = Bukkit.getPlayer(playerUUID);
            if(player == null) continue;

            final Location location = Bukkit.getEntity(uuid).getLocation();
            final WorldServer wS = ((CraftWorld) location.getWorld()).getHandle();
            double lX = location.getX();
            double lY = location.getY() + 1.2;
            double lZ = location.getZ();

            List<String> messages = Config.getScoreHologram();
            Collections.reverse(messages);

            for (String msg : messages) {
                msg = PlaceholderUtil.parse(msg, damageUtil, player, false);
                if (msg != null) lY += 0.2;
                if (msg == null || msg.isEmpty()) continue;


                final EntityArmorStand armorStand = new EntityArmorStand(EntityTypes.c, wS);
                armorStand.setPosition(lX, lY, lZ);
                armorStand.setCustomName(CraftChatMessage.fromStringOrNull(msg));
                armorStand.setCustomNameVisible(true);
                armorStand.setInvisible(true);
                armorStand.setMarker(true);
                armorStand.setFireTicks(999999);
                PacketPlayOutSpawnEntity packetPlayOutSpawnEntity = new PacketPlayOutSpawnEntity(armorStand, 1);
                PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(armorStand.getId(), armorStand.getDataWatcher(), true);

                if (player.isOnline() && ((CraftPlayer) player).getHandle() != null) {
                    final PlayerConnection connection = ((CraftPlayer) player).getHandle().b;
                    connection.sendPacket(packetPlayOutSpawnEntity);
                    connection.sendPacket(metadata);
                }

                Bukkit.getScheduler().runTaskLater(PandeLoot.getInstance(), () -> {
                    if (player.isOnline()) {
                        PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(armorStand.getId());
                        ((CraftPlayer) player).getHandle().b.sendPacket(destroy);
                    }
                }, 300);
            }

        }
    }

    private int id;
    private EntityArmorStand abandonEntity;
    private int abandonTime;
    private String abandonText;
    private String timeText;

    public void spawnLockedHologram(Entity toTrack, Location location, List<String> toDisplay, List<Player> players, int aT, Reward reward) {

        double y = 0.45;
        abandonTime = 20 * aT;

        if (players == null || players.isEmpty() || players.contains(null)) {
            players = new ArrayList<>();
            for (Entity entity : location.getWorld().getNearbyEntities(location, 42, 42, 42)) {
                if (entity instanceof Player) {
                    players.add((Player) entity);
                }
            }
        }

        List<EntityArmorStand> armorStands = new ArrayList<>();

        if (abandonTime > 0) {
            abandonText = PlaceholderUtil.parse(Config.getAbandonText(), null, null, false);
            List<String> strs = Arrays.asList(abandonText.split(","));
            Collections.reverse(strs);
            toDisplay.add("");
            toDisplay.addAll(strs);
        }

        for (String i : toDisplay) {
            Location newLoc = location.clone();
            newLoc.setY(location.getY() + y);
            y += 0.2;

            final EntityArmorStand armorStand = spawnPacketHologram(newLoc, i);
            armorStands.add(armorStand);
            if (armorStand == null) continue;
            PacketPlayOutSpawnEntity packetPlayOutSpawnEntity = new PacketPlayOutSpawnEntity(armorStand, 1);
            PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(armorStand.getId(), armorStand.getDataWatcher(), true);

            for (Player player : players) {
                if (player != null && player.isOnline() && ((CraftPlayer) player).getHandle() != null) {
                    final PlayerConnection connection = ((CraftPlayer) player).getHandle().b;
                    connection.sendPacket(packetPlayOutSpawnEntity);
                    connection.sendPacket(metadata);
                }
            }
        }

        List<Player> finalPlayers = players;
        id = Bukkit.getScheduler().scheduleSyncRepeatingTask(PandeLoot.getInstance(), () -> {
            if (toTrack.isValid()) {
                updateLocation(toTrack.getLocation(), armorStands, finalPlayers);

                if (abandonTime > 0) {
                    abandonTime--;
                    String text = timeText.replace("%time%", MathUtil.getDurationAsTime(abandonTime / 20));
                    abandonEntity.setCustomName(CraftChatMessage.fromStringOrNull(text));
                }

                if (abandonTime == 0 && aT > 0) {
                    Location aaLoc = toTrack.getLocation().clone();
                    org.bukkit.inventory.ItemStack itemStack = ((org.bukkit.entity.Item) toTrack).getItemStack();
                    itemStack = NMSManager.removeNBT(itemStack, StringLib.root);

                    toTrack.remove();

                    org.bukkit.entity.Item newItem = aaLoc.getWorld().dropItem(aaLoc, itemStack);
                    newItem.setVelocity(new Vector());
                    reward.item = newItem;
                    reward.options.put("abandontime", "0");
                    reward.options.put("canview", "everyone");
                    reward.player = null;
                    reward.isAbandoned = true;
                    Options.callOptions(reward);
                }

            } else {
                for (EntityArmorStand i : armorStands) {
                    if (i == null) continue;
                    for (Player player : finalPlayers) {
                        if (player == null) continue;
                        final PlayerConnection connection = ((CraftPlayer) player).getHandle().b;
                        connection.sendPacket(new PacketPlayOutEntityDestroy(i.getId()));
                    }
                }
                Bukkit.getScheduler().cancelTask(id);
            }
        }, 0, 1);

    }

    public void updateLocation(Location location, List<EntityArmorStand> holograms, List<Player> players) {
        double y = 0.25;
        for (EntityArmorStand i : holograms) {
            y += 0.2;
            if (i == null) continue;

            Location newLoc = location.clone();
            newLoc.setY(location.getY() + y);
            //i.setCustomName(CraftChatMessage.fromStringOrNull(location.getBlock().toString()));
            i.setLocation(newLoc.getX(), newLoc.getY(), newLoc.getZ(), 0, 0);
            PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(i.getId(), i.getDataWatcher(), true);
            for (Player player : players) {
                if (player == null) continue;
                final PlayerConnection connection = ((CraftPlayer) player).getHandle().b;
                connection.sendPacket(new PacketPlayOutEntityTeleport(i));
                connection.sendPacket(metadata);
            }
            //i.teleportAndSync(location.getX(), location.getY(), location.getZ());
        }

    }

    private EntityArmorStand spawnPacketHologram(Location location, String display) {
        if (display == null || display.isEmpty()) return null;
        WorldServer wS = ((CraftWorld) location.getWorld()).getHandle();
        final EntityArmorStand armorStand = new EntityArmorStand(EntityTypes.c, wS);
        armorStand.setPosition(location.getX(), location.getY(), location.getZ());
        armorStand.setCustomName(CraftChatMessage.fromStringOrNull(display));
        armorStand.setCustomNameVisible(true);
        armorStand.setInvisible(true);
        armorStand.setMarker(true);
        armorStand.setFireTicks(999999);

        if (display.contains("%time")) {
            abandonEntity = armorStand;
            timeText = display;
        }

        return armorStand;
    }

    public static void displayToast(Player player, String title, String frame, org.bukkit.inventory.ItemStack icon) {
        MinecraftKey minecraftKey = new MinecraftKey("pandeloot", "notification");
        HashMap<String, Criterion> criteria = new HashMap<>();

        criteria.put("for_free", new Criterion(new CriterionInstance() {
            public MinecraftKey a() {
                return new MinecraftKey("minecraft", "impossible");
            }

            @Override // Not needed
            public JsonObject a(LootSerializationContext lootSerializationContext) {
                return null;
            }
        }));

        ArrayList<String[]> fixed = new ArrayList<>();
        fixed.add(new String[]{"for_free"});

        String[][] requirements = fixed.toArray(new String[fixed.size()][]);

        IChatBaseComponent chatTitle = new ChatMessage(title);
        //IChatBaseComponent chatDescription = new ChatMessage(description);
        AdvancementFrameType advancementFrame = AdvancementFrameType.valueOf(frame.toUpperCase());
        ItemStack craftIcon = CraftItemStack.asNMSCopy(icon);

        AdvancementDisplay display = new AdvancementDisplay(craftIcon, chatTitle, null, null, advancementFrame, true, true, true);
        AdvancementRewards reward = new AdvancementRewards(0, new MinecraftKey[0], new MinecraftKey[0], null);
        Advancement advancement = new Advancement(minecraftKey, null, display, reward, criteria, requirements);

        HashMap<MinecraftKey, AdvancementProgress> progressMap = new HashMap<>();
        AdvancementProgress progress = new AdvancementProgress();
        progress.a(criteria, requirements);
        progress.getCriterionProgress("for_free").b();
        progressMap.put(minecraftKey, progress);

        PacketPlayOutAdvancements packet
                = new PacketPlayOutAdvancements(false, Collections.singletonList(advancement), new HashSet<>(), progressMap);
        ((CraftPlayer) player).getHandle().b.sendPacket(packet);

        // Remove the advancement
        HashSet<MinecraftKey> remove = new HashSet<>();
        remove.add(minecraftKey);
        progressMap.clear();
        packet = new PacketPlayOutAdvancements(false, new ArrayList<>(), remove, progressMap);
        ((CraftPlayer) player).getHandle().b.sendPacket(packet);
    }

    public static void injectPlayer(Player player) {
        EntityPlayer ply = ((CraftPlayer) player).getHandle();
        PandeLootChannelHandler_v1_17_R1 cdh = new PandeLootChannelHandler_v1_17_R1(ply);

        ChannelPipeline pipeline = ply.b.a.k.pipeline();
        for (String name : pipeline.toMap().keySet()) {
            if (pipeline.get(name) instanceof NetworkManager) {
                pipeline.addBefore(name, "pande_loot_packet_handler", cdh);
                break;
            }
        }
    }

    public static void removePlayer(Player player) {
        Channel channel = ((CraftPlayer) player).getHandle().b.a.k;
        channel.eventLoop().submit(() -> {
            channel.pipeline().remove("pande_loot_packet_handler");
            return null;
        });
    }

}
