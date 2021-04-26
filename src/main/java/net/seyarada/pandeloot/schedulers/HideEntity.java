package net.seyarada.pandeloot.schedulers;

import net.seyarada.pandeloot.PandeLoot;
import net.seyarada.pandeloot.nms.NMSManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class HideEntity {

    private int id;
    //private int abandonTime;

    // This leaves a trail of particles of a specific color behind the item

    public HideEntity(Entity toHide, List<Player> canView) {

        Plugin plugin = PandeLoot.getInstance();

        id = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            if (toHide.isValid()) {

                for (Entity entity : toHide.getNearbyEntities(36, 36, 36)) {
                    if (entity instanceof Player && !canView.contains(entity)) {
                        NMSManager.destroyEntity(toHide.getEntityId(), entity);
                    }
                }

            } else {
                Bukkit.getScheduler().cancelTask(id);
            }

        }, 0, 10);
    }

}
