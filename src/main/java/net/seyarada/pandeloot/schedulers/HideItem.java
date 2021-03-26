package net.seyarada.pandeloot.schedulers;

import net.seyarada.pandeloot.PandeLoot;
import net.seyarada.pandeloot.nms.NMSManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class HideItem {

    private int id;

    // This leaves a trail of particles of a specific color behind the item

    public HideItem(Item item, Player player) {

        Plugin plugin = PandeLoot.getInstance();

        id = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {

            if (item.isValid()) {

                for (Entity entity : item.getNearbyEntities(36, 36, 36)) {
                    if (entity instanceof Player && !entity.getName().equals(player.getName())) {
                        NMSManager.destroyEntity(item, entity);
                    }
                }

            } else {
                Bukkit.getScheduler().cancelTask(id);
            }

        }, 0, 10);
    }

}
