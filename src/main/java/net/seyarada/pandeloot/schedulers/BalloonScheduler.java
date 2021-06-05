package net.seyarada.pandeloot.schedulers;

import net.seyarada.pandeloot.PandeLoot;
import net.seyarada.pandeloot.items.LootBalloon;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

public class BalloonScheduler {

    private int id;

    public BalloonScheduler(Entity balloon, Entity anchor) {

        Plugin plugin = PandeLoot.getInstance();

        id = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {

            if (anchor.isValid() && balloon.isValid()) {
                balloon.teleport(anchor.getLocation().clone().add(0,4.5,0));
                if(anchor.getFallDistance()>0) {
                    LootBalloon.clear(balloon.getUniqueId());
                }
            } else {
                Bukkit.getScheduler().cancelTask(id);
            }

        }, 0, 1);
    }

}
