package net.seyarada.pandeloot.drops;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.economy.Economy;
import net.mystipvp.holobroadcast.holograms.HologramPlayer;
import net.mystipvp.holobroadcast.holograms.HologramPlayersManager;
import net.seyarada.pandeloot.PandeLoot;
import net.seyarada.pandeloot.StringLib;
import net.seyarada.pandeloot.compatibility.DiscordSRVCompatibility;
import net.seyarada.pandeloot.compatibility.MMOCoreCompatibility;
import net.seyarada.pandeloot.nms.NMSManager;
import net.seyarada.pandeloot.rewards.RewardLine;
import net.seyarada.pandeloot.schedulers.LockedHologram;
import net.seyarada.pandeloot.utils.ColorUtil;
import net.seyarada.pandeloot.utils.MathUtil;
import net.seyarada.pandeloot.utils.PlaceholderUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.util.Vector;

import java.util.*;

public class DropEffects {

    private final DropItem src;
    private final Item item;
    private final RewardLine reward;
    public static Map<UUID, DropItem> playOnPickupStorage = new HashMap<>();

    private boolean isAbandoned;
    public static Map<UUID, DropItem> storeForAbandon = new HashMap<>();

    public DropEffects(DropItem source) {
        this.src = source;
        this.item = source.item;
        this.reward = src.reward;

        if(item==null) {
            runPlayerEffects();
            runGeneralEffects();
            return;
        }

        storeForAbandon.put(item.getUniqueId(), source);

        if(runItemEffects()) {
            runPlayerEffects();
            runGeneralEffects();
        }
    }

    public DropEffects(DropItem source, boolean noExecuteItemEffects) {
        this.src = source;
        this.item = source.item;
        this.reward = src.reward;

        if(noExecuteItemEffects) {
            runPlayerEffects();
            runGeneralEffects();
        }
    }

    public DropEffects(UUID uuid, Item item) {
        this.src = storeForAbandon.get(uuid);
        this.item = item;
        this.reward = src.reward;

        storeForAbandon.remove(uuid);
        isAbandoned = true;
        reward.abandonTime = 0;

        if(runItemEffects()) {
            runPlayerEffects();
            runGeneralEffects();
        }
    }

    private boolean runItemEffects() {
        if(item==null) return true;

        if(reward.abandonTime>0||isAbandoned) reward.playonpickup=true;

        hologramEffect();
        explodeEffect();

        if (reward.glow) {
            item.setGlowing(true);
            ColorUtil.setColorEffects(item, reward.color, src.player, src.location, reward.beam);
        }

        if (reward.playonpickup) {
            UUID uuid = UUID.randomUUID();
            item.setItemStack(NMSManager.addNBT(item.getItemStack(), StringLib.playOnPickup, uuid.toString()));
            playOnPickupStorage.put(uuid, src);
            reward.playonpickup = false;
            return false;
        }

        return true;
    }

    private void runPlayerEffects() {
        if(src.player==null) return;

        if (reward.message!=null && !reward.message.isEmpty()) {
            src.player.sendMessage(reward.message);
        }

        if ( (reward.title!=null && !reward.title.isEmpty()) || (reward.subtitle!=null && !reward.subtitle.isEmpty()) ) {
            src.player.sendTitle(reward.title, reward.subtitle, reward.titleFade, reward.titleDuration, reward.titleFade);
        }

        if (reward.sound!=null && !reward.sound.isEmpty()) {
            src.player.playSound(src.player.getLocation(), reward.sound, 1, 1);
        }

        if (reward.actionbar!=null && !reward.actionbar.isEmpty()) {
            src.player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(reward.actionbar));
        }

        if(reward.money>0 && Bukkit.getServer().getPluginManager().getPlugin("Vault") != null) {
            Economy economy = PandeLoot.getEconomy();
            economy.depositPlayer(src.player, reward.money);
        }

        if (reward.experience > 0)
            src.player.giveExp(reward.experience);

        if (reward.holoBroadcast != null && Bukkit.getServer().getPluginManager().getPlugin("HoloBroadcast") != null) {
            HologramPlayersManager manager = HologramPlayersManager.getInstance();
            HologramPlayer holoPlayer = manager.getHologramPlayerFromUUID(src.player.getUniqueId());
            holoPlayer.showHUD(reward.holoBroadcast, -1);
        }

        if(reward.mmocoreExp > 0)
            MMOCoreCompatibility.exp(src.player, reward.mmocoreExp);
    }

    private void runGeneralEffects() {
        if (reward.broadcast!=null && !reward.broadcast.isEmpty()) {
            Bukkit.broadcastMessage(reward.broadcast);
        }
        if (reward.command!=null && !reward.command.isEmpty()) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), reward.command);
        }
        DiscordSRVCompatibility.embed(src.player, src.damageUtil, reward.dTitle, reward.dMessage, reward.dChannel, reward.dColor, reward.dLink, reward.dAvatar);
    }


    private void hologramEffect() {
        if( reward.hologram!=null && !reward.hologram.isEmpty()) {
            if(reward.hologram.equalsIgnoreCase("display")) {
                String text = PlaceholderUtil.parse(item.getItemStack().getItemMeta().getDisplayName(), src.damageUtil, src.player);
                new LockedHologram(item, Collections.singletonList(text), src.player, reward.abandonTime, isAbandoned);
            }
            else if(reward.hologram.equalsIgnoreCase("lore")) {
                List<String> lore = item.getItemStack().getItemMeta().getLore();
                if(lore!=null && !lore.isEmpty()) {
                    List<String> newLore = new ArrayList<>();
                    for(String i : lore) {
                        newLore.add(PlaceholderUtil.parse(i, src.damageUtil, src.player));
                    }
                    new LockedHologram(item, newLore, src.player, reward.abandonTime, isAbandoned);
                }
            }
            else if(reward.hologram.equalsIgnoreCase("full")) {
                List<String> lore = item.getItemStack().getItemMeta().getLore();
                String name = PlaceholderUtil.parse(item.getItemStack().getItemMeta().getDisplayName(), src.damageUtil, src.player);
                List<String> newLore = new ArrayList<>();
                newLore.add(name);

                if(lore!=null && !lore.isEmpty()) {
                    for(String i : lore) {
                        newLore.add(PlaceholderUtil.parse(i, src.damageUtil, src.player));
                    }
                }
                new LockedHologram(item, newLore, src.player, reward.abandonTime, isAbandoned);
            } else {
                new LockedHologram(item, Arrays.asList(reward.hologram.split(",")), src.player, reward.abandonTime, isAbandoned);
            }
        } else if (reward.abandonTime>0)
            new LockedHologram(item, new ArrayList<>(), src.player, reward.abandonTime, isAbandoned);
    }

    private void explodeEffect() {
        if (reward.explode && !isAbandoned) {
            switch (reward.explodeType.toLowerCase()) {
                default:
                case "spread":
                    doSpreadDrop();
                    break;
                case "radial":
                    doRadialDrop();
                    break;
            }
        }
    }

    private void doSpreadDrop() {
        Vector velocity = MathUtil.getVelocity(reward.expoffset, reward.expheight);
        item.setVelocity(velocity);
    }

    private void doRadialDrop() {

        int n = reward.radialDrop.get(reward.explodeRadius);
        int x = reward.radialOrder;

        if(n==1) {
            doSpreadDrop();
            return;
        }

        double angle = 2 * Math.PI / n;
        double cos = Math.cos(angle * x);
        double sin = Math.sin(angle * x);
        double iX = item.getLocation().getX() + reward.explodeRadius * cos;
        double iZ = item.getLocation().getZ() + reward.explodeRadius * sin;

        Location loc = item.getLocation().clone();
        loc.setX(iX);
        loc.setZ(iZ);

        item.setVelocity(
                MathUtil.calculateVelocity(item.getLocation().toVector(), loc.toVector(), 0.115, 3));

        //Bukkit.getScheduler().scheduleSyncRepeatingTask(PandeLoot.getInstance(), () -> {

        //    Color rgb = ColorUtil.getRGB("GOLD");
        //    Particle.DustOptions dustOptions = new Particle.DustOptions(rgb, 1);
        //    item.getLocation().getWorld().spawnParticle(Particle.REDSTONE, loc, 1, dustOptions);

        //    }, 0, 1);
    }
}
