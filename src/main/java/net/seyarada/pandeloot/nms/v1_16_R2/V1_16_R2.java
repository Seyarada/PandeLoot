package net.seyarada.pandeloot.nms.v1_16_R2;

import com.google.gson.JsonObject;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import net.minecraft.server.v1_16_R2.*;
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
import org.bukkit.craftbukkit.v1_16_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R2.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_16_R2.util.CraftChatMessage;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class V1_16_R2 {

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
        for(UUID playerUUID : DamageTracker.get(uuid).keySet()) {
            Player player = Bukkit.getPlayer(playerUUID);

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
                    org.bukkit.inventory.ItemStack itemStack = ((Item) toTrack).getItemStack();
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

            }
            else {
                for(EntityArmorStand i : armorStands) {
                    if(i==null) continue;
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

    public static void displayToast(Player player, String title, String frame, org.bukkit.inventory.ItemStack icon) {
        MinecraftKey minecraftKey = new MinecraftKey("pandeloot", "notification");
        HashMap<String, Criterion> criteria = new HashMap<>();

        criteria.put("for_free",new Criterion(new CriterionInstance() {
            public MinecraftKey a() {
                return new MinecraftKey("minecraft","impossible");
            }
            @Override // Not needed
            public JsonObject a(LootSerializationContext lootSerializationContext) { return null; }
        }));

        ArrayList<String[]> fixed = new ArrayList<>();
        fixed.add(new String[]{"for_free"});

        String[][] requirements = Arrays.stream(fixed.toArray()).toArray(String[][]::new);

        IChatBaseComponent chatTitle = new ChatMessage(title);
        //IChatBaseComponent chatDescription = new ChatMessage(description);
        AdvancementFrameType advancementFrame = AdvancementFrameType.valueOf(frame.toUpperCase());
        net.minecraft.server.v1_16_R2.ItemStack craftIcon = CraftItemStack.asNMSCopy(icon);

        AdvancementDisplay display = new AdvancementDisplay(craftIcon, chatTitle, null, null, advancementFrame, true,true,true);
        AdvancementRewards reward = new AdvancementRewards(0, new MinecraftKey[0], new MinecraftKey[0], null);
        Advancement advancement = new Advancement(minecraftKey, null, display, reward, criteria, requirements);

        HashMap<MinecraftKey,AdvancementProgress> progressMap = new HashMap<>();
        AdvancementProgress progress = new AdvancementProgress();
        progress.a(criteria,requirements);
        progress.getCriterionProgress("for_free").b();
        progressMap.put(minecraftKey,progress);

        PacketPlayOutAdvancements packet
                = new PacketPlayOutAdvancements(false, Collections.singletonList(advancement), new HashSet<>(), progressMap);
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);

        // Remove the advancement
        HashSet<MinecraftKey> remove = new HashSet<>();
        remove.add(minecraftKey);
        progressMap.clear();
        packet = new PacketPlayOutAdvancements(false, new ArrayList<>(), remove, progressMap);
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
    }

    public static void injectPlayer(Player player) {
        EntityPlayer ply = ((CraftPlayer) player).getHandle();
        PandeLootChannelHandler_v1_16_R2 cdh = new PandeLootChannelHandler_v1_16_R2(ply);

        ChannelPipeline pipeline = ply.playerConnection.networkManager.channel.pipeline();
        for(String name : pipeline.toMap().keySet()) {
            if(pipeline.get(name) instanceof NetworkManager) {
                pipeline.addBefore(name, "pande_loot_packet_handler", cdh);
                break;
            }
        }
    }

    public static void removePlayer(Player player) {
        Channel channel = ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel;
        channel.eventLoop().submit(() -> {
            channel.pipeline().remove("pande_loot_packet_handler");
            return null;
        });
    }

}
