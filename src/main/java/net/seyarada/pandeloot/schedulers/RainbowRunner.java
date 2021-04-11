package net.seyarada.pandeloot.schedulers;

import net.seyarada.pandeloot.Config;
import net.seyarada.pandeloot.PandeLoot;
import net.seyarada.pandeloot.utils.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.atomic.AtomicInteger;

public class RainbowRunner {

    private static final String[] rainbowColors = new String[]{"DARK_RED", "RED", "GOLD", "YELLOW", "GREEN", "DARK_GREEN", "AQUA", "DARK_AQUA", "DARK_PURPLE", "LIGHT_PURPLE" };
    private int id;

    public RainbowRunner(Item item, Location location, double beam) {

        Plugin plugin = PandeLoot.getInstance();
        AtomicInteger tick = new AtomicInteger(-1);

        id = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {

            tick.addAndGet(1);
            if(tick.get()>=rainbowColors.length) tick.set(0);

            String color = rainbowColors[tick.get()];
            Color rgb = ColorUtil.getRGB(color);

            if(item.isValid()) {

                ColorUtil.setItemColor(item, color, null);

                if(item.isOnGround()) { // Beam
                    double modHeight = beam;
                    while(modHeight>0) {
                        Particle.DustOptions dustOptions = new Particle.DustOptions(rgb, 1);
                        location.getWorld().spawnParticle(Particle.REDSTONE, item.getLocation().add(0, modHeight, 0), 1, dustOptions);
                        modHeight = modHeight - 0.1;
                    }
                } else {                // Particle Trail
                    Particle.DustOptions dustOptions = new Particle.DustOptions(rgb, 1);
                    location.getWorld().spawnParticle(Particle.REDSTONE, item.getLocation(), 1, dustOptions);
                }

            } else {
                Bukkit.getScheduler().cancelTask(id);
            }
        }, 0, 3);
    }

    public RainbowRunner(Item item, Player player, double beam) {

        Plugin plugin = PandeLoot.getInstance();
        AtomicInteger tick = new AtomicInteger(-1);
        int frequency = Config.getRainbowFrequency();

        id = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {

            tick.addAndGet(1);
            if(tick.get()>=rainbowColors.length) tick.set(0);

            String color = rainbowColors[tick.get()];
            Color rgb = ColorUtil.getRGB(color);

            if(item.isValid()) {

                ColorUtil.setItemColor(item, color, player);

                if(item.isOnGround()) { // Beam
                    double modHeight = beam;
                    while(modHeight>0) {
                        Particle.DustOptions dustOptions = new Particle.DustOptions(rgb, 1);
                        player.spawnParticle(Particle.REDSTONE, item.getLocation().add(0, modHeight, 0), 1, dustOptions);
                        modHeight = modHeight - 0.1;
                    }
                } else {                // Particle Trail
                    Particle.DustOptions dustOptions = new Particle.DustOptions(rgb, 1);
                    player.spawnParticle(Particle.REDSTONE, item.getLocation(), 1, dustOptions);
                }

            } else {
                Bukkit.getScheduler().cancelTask(id);
            }
        }, 0, frequency);
    }

}
