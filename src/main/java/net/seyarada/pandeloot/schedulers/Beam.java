package net.seyarada.pandeloot.schedulers;

import net.seyarada.pandeloot.PandeLoot;
import net.seyarada.pandeloot.utils.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Beam {

    private int id;

    // Beam effect

    public Beam(Item item, String color, Location location, double height) {

        String fColor = ColorUtil.getColor(item, color);
        Color rgb = ColorUtil.getRGB(fColor);

        Plugin plugin = PandeLoot.getInstance();

        id = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {

            if (item.isValid()) {
                if(item.isOnGround()) {
                    double modHeight = height;
                    while(modHeight>0) {
                        Particle.DustOptions dustOptions = new Particle.DustOptions(rgb, 1);
                        location.getWorld().spawnParticle(Particle.REDSTONE, item.getLocation().add(0, 0.15+modHeight, 0), 1, dustOptions);
                        modHeight = modHeight - 0.1;
                    }
                }
            } else {
                Bukkit.getScheduler().cancelTask(id);
            }

        }, 0, 1);
    }

    public Beam(Item item, String color, Player player, double height) {

        String fColor = ColorUtil.getColor(item, color);
        Color rgb = ColorUtil.getRGB(fColor);

        Plugin plugin = PandeLoot.getInstance();

        id = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {

            if (item.isValid()) {
                if(item.isOnGround()) {
                    double modHeight = height;
                    while(modHeight>0) {
                        Particle.DustOptions dustOptions = new Particle.DustOptions(rgb, 1);
                        player.spawnParticle(Particle.REDSTONE, item.getLocation().add(0, modHeight, 0), 1, dustOptions);
                        modHeight = modHeight - 0.1;
                    }
                }
            } else {
                Bukkit.getScheduler().cancelTask(id);
            }

        }, 0, 1);
    }

}
