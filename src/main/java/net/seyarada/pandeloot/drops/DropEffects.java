package net.seyarada.pandeloot.drops;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.economy.Economy;
import net.mystipvp.holobroadcast.holograms.HologramPlayer;
import net.mystipvp.holobroadcast.holograms.HologramPlayersManager;
import net.seyarada.pandeloot.PandeLoot;
import net.seyarada.pandeloot.nms.NMSManager;
import net.seyarada.pandeloot.rewards.NBTNames;
import net.seyarada.pandeloot.schedulers.LockedHologram;
import net.seyarada.pandeloot.utils.ColorUtil;
import net.seyarada.pandeloot.utils.DropUtil;
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

    public static Map<UUID, DropItem> playOnPickupStorage = new HashMap<>();

    public DropEffects(DropItem source) {
        this.src = source;
        this.item = source.item;

        if(runItemEffects()) {
            runPlayerEffects();
            runGeneralEffects();
        }
    }

    public DropEffects(DropItem source, boolean noExecuteItemEffects) {
        this.src = source;
        this.item = source.item;

        if(noExecuteItemEffects) {
            runPlayerEffects();
            runGeneralEffects();
        }
    }

    private boolean runItemEffects() {
        if(item==null) return true;

        if(src.hologram!=null && !src.hologram.isEmpty()) {

            if(src.hologram.equalsIgnoreCase("display")) {
                String text = PlaceholderUtil.parse(item.getItemStack().getItemMeta().getDisplayName(), src.damageUtil, src.player);
                new LockedHologram(item, Collections.singletonList(text), src.player);
            }
            else if(src.hologram.equalsIgnoreCase("lore")) {
                List<String> lore = item.getItemStack().getItemMeta().getLore();
                if(lore!=null && !lore.isEmpty()) {
                    List<String> newLore = new ArrayList<>();
                    for(String i : lore) {
                        newLore.add(PlaceholderUtil.parse(i, src.damageUtil, src.player));
                    }
                    new LockedHologram(item, newLore, src.player);
                }
            }
            else if(src.hologram.equalsIgnoreCase("full")) {
                List<String> lore = item.getItemStack().getItemMeta().getLore();
                String name = PlaceholderUtil.parse(item.getItemStack().getItemMeta().getDisplayName(), src.damageUtil, src.player);
                List<String> newLore = new ArrayList<>();
                newLore.add(name);

                if(lore!=null && !lore.isEmpty()) {
                    for(String i : lore) {
                        newLore.add(PlaceholderUtil.parse(i, src.damageUtil, src.player));
                    }
                }
                new LockedHologram(item, newLore, src.player);
            } else {
                new LockedHologram(item, Arrays.asList(src.hologram.split(",")), src.player);
            }
        }
        //new LockedHologram(item, new String[]{"Volvo", "BMW", "Ford", "", "Mazda", "PandeLoot is Cool!"}, src.player);

        if (src.explode) {
            switch (src.explodeType.toLowerCase()) {
                default:
                case "spread":
                    doSpreadDrop();
                    break;
                case "radial":
                    doRadialDrop();
                    break;
            }
        }

        if (src.glow) {
            item.setGlowing(true);
            ColorUtil.setColorEffects(item, src.color, src.player, src.location, src.beam);
        }

        if (src.playonpickup) {
            UUID uuid = UUID.randomUUID();
            item.setItemStack(NMSManager.addNBT(item.getItemStack(), NBTNames.playOnPickup, uuid.toString()));
            playOnPickupStorage.put(uuid, src);
            src.playonpickup = false;
            return false;
        }

        return true;
    }

    private void runPlayerEffects() {
        if(src.player==null) return;

        if (src.message!=null && !src.message.isEmpty()) {
            src.message = PlaceholderUtil.parse(src.message, src.damageUtil, src.player);
            src.player.sendMessage(src.message);
        }

        if ( (src.title!=null && !src.title.isEmpty()) || (src.subtitle!=null && !src.subtitle.isEmpty()) ) {
            src.title = PlaceholderUtil.parse(src.title, src.damageUtil, src.player);
            src.subtitle = PlaceholderUtil.parse(src.subtitle, src.damageUtil, src.player);
            src.player.sendTitle(src.title, src.subtitle, src.titleFade, src.titleDuration, src.titleFade);
        }

        if (src.sound!=null && !src.sound.isEmpty()) {
            src.sound = PlaceholderUtil.parse(src.sound, src.damageUtil, src.player);
            src.player.playSound(src.player.getLocation(), src.sound, 1, 1);
        }

        if (src.actionbar!=null && !src.actionbar.isEmpty()) {
            src.actionbar = PlaceholderUtil.parse(src.actionbar, src.damageUtil, src.player);
            src.player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(src.actionbar));
        }

        if(src.money>0 && Bukkit.getServer().getPluginManager().getPlugin("Vault") != null) {
            Economy economy = PandeLoot.getEconomy();
            economy.depositPlayer(src.player, src.money);
        }

        if (src.experience > 0)
            src.player.giveExp(src.experience);

        if (src.holoBroadcast != null && Bukkit.getServer().getPluginManager().getPlugin("HoloBroadcast") != null) {
            HologramPlayersManager manager = HologramPlayersManager.getInstance();
            HologramPlayer holoPlayer = manager.getHologramPlayerFromUUID(src.player.getUniqueId());
            holoPlayer.showHUD(src.holoBroadcast, -1);
        }
    }

    private void runGeneralEffects() {
        if (src.broadcast!=null && !src.broadcast.isEmpty()) {
            src.broadcast = PlaceholderUtil.parse(src.broadcast, src.damageUtil, src.player);
            Bukkit.broadcastMessage(src.broadcast);
        }
        if (src.command!=null && !src.command.isEmpty()) {
            src.command = PlaceholderUtil.parse(src.command, src.damageUtil, src.player);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), src.command);
        }
        DropUtil.sendDiscordEmbed(src.player, src.damageUtil, src.dTitle, src.dMessage, src.dChannel, src.dColor, src.dLink, src.dAvatar);
    }

    private void doSpreadDrop() {
        Vector velocity = DropUtil.getVelocity(src.expoffset, src.expheight);
        item.setVelocity(velocity);
    }

    private void doRadialDrop() {
        Map<Double, Integer> advCounter = (Map<Double, Integer>) src.reward.options.get("RadialDrop");

        int n = advCounter.get(src.explodeRadius);
        int x = (int) src.reward.options.get("ThisItem");

        if(n==1) {
            Random r = new Random();
            n = r.nextInt(99) + 1;
            x = r.nextInt(99) + 1;
        }

        double angle = 2 * Math.PI / n;
        double cos = Math.cos(angle * x);
        double sin = Math.sin(angle * x);
        double iX = item.getLocation().getX() + src.explodeRadius * cos;
        double iZ = item.getLocation().getZ() + src.explodeRadius * sin;

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
