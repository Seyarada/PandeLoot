package net.seyarada.pandeloot.schedulers;

import net.seyarada.pandeloot.PandeLoot;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LockedHologram {

    private int id;
    private List<ArmorStand> holograms = new ArrayList<>();

    // This generates a hologram(s) that locks into an  entity/item

    public LockedHologram(Entity toTrack, List<String> toDisplay, Player player) {

        Collections.reverse(toDisplay);
        Plugin plugin = PandeLoot.getInstance();
        generateHolograms(toTrack.getLocation(), toDisplay, player);

        id = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {


            if (toTrack.isValid()) {
                updateLocation(toTrack.getLocation(), holograms);
            } else {
                for(ArmorStand i : holograms) {
                    if(i!=null && i.isValid())
                        i.remove();
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

            new HideEntity(armorStand, player);
            holograms.add(armorStand);
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
