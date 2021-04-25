package net.seyarada.pandeloot.schedulers;

import net.seyarada.pandeloot.Config;
import net.seyarada.pandeloot.PandeLoot;
import net.seyarada.pandeloot.StringLib;
import net.seyarada.pandeloot.drops.DropEffects;
import net.seyarada.pandeloot.nms.NMSManager;
import net.seyarada.pandeloot.utils.MathUtil;
import net.seyarada.pandeloot.utils.PlaceholderUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.util.*;

public class LockedHologram {

    private int id;
    private List<ArmorStand> holograms = new ArrayList<>();

    private ArmorStand abandonEntity;

    private int abandonTime;
    private boolean isAbandoned;
    private String abandonText;
    private String timeText;

    // This generates a hologram(s) that locks into an  entity/item

    public LockedHologram(Entity toTrack, List<String> toDisplay, Player player, int aT, boolean isAbandoned) {
        this.abandonTime = 20*aT; // Field   ===-------- Longinus fork
        this.isAbandoned = isAbandoned;
        if(toTrack==null) return;
        Collections.reverse(toDisplay);

        if(abandonTime>0) {
            abandonText = PlaceholderUtil.parse(Config.getAbandonText(), null, player);
            List<String> strs = Arrays.asList(abandonText.split(","));
            Collections.reverse(strs);
            toDisplay.add("");
            toDisplay.addAll(strs);
        }

        Plugin plugin = PandeLoot.getInstance();
        generateHolograms(toTrack.getLocation(), toDisplay, player);

        id = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {


            if (toTrack.isValid()) {
                updateLocation(toTrack.getLocation(), holograms);

                if(abandonTime>0) {
                    abandonTime--;
                    abandonEntity.setCustomName(timeText.replace("%time%", MathUtil.getDurationAsTime(abandonTime/20)));
                }

                if(abandonTime==0 && aT>0) {
                    Location location = toTrack.getLocation();
                    ItemStack itemStack = ((Item) toTrack).getItemStack();
                    itemStack = NMSManager.removeNBT(itemStack, StringLib.root);
                    UUID uuid = toTrack.getUniqueId();

                    toTrack.remove();

                    Item newItem = location.getWorld().dropItem(location, itemStack);
                    newItem.setVelocity(new Vector());
                    new DropEffects(uuid, newItem);

                }

            } else {
                for(ArmorStand i : holograms) {
                    if(i!=null && i.isValid()) {
                        PandeLoot.totalHolograms.remove(i);
                        i.remove();
                    }
                }
                Bukkit.getScheduler().cancelTask(id);
            }

        }, 0, 1);
    }

    public void generateHolograms(Location location, List<String> toDisplay, Player player) {
        World world = location.getWorld();
        double y = 0.45;

        for(String i : toDisplay) {
            Location newLoc = location.clone();
            newLoc.setY(location.getY()+y);
            y += 0.2;

            if(i.isEmpty()) {
                holograms.add(null);
                continue;
            }

            ArmorStand armorStand = (ArmorStand) world.spawnEntity(newLoc, EntityType.ARMOR_STAND);
            armorStand.setCustomName(i);
            armorStand.setCustomNameVisible(true);
            armorStand.setInvisible(true);
            armorStand.setMarker(true);
            armorStand.setFireTicks(999999);

            if(i.contains("%time")) {
                abandonEntity = armorStand;
                timeText = i;
            }

            if(!isAbandoned)
                new HideEntity(armorStand, player);

            holograms.add(armorStand);
            PandeLoot.totalHolograms.add(armorStand);
        }

    }

    public void updateLocation(Location location, List<ArmorStand> holograms) {
        double y = 0.25;
        for(ArmorStand i : holograms) {
            y += 0.2;
            if(i==null) continue;

            Location newLoc = location.clone();
            newLoc.setY(location.getY()+y);
            i.teleport(newLoc);
        }

    }

}
