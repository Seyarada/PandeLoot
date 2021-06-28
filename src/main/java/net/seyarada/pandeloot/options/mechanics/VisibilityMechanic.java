package net.seyarada.pandeloot.options.mechanics;

import net.seyarada.pandeloot.StringLib;
import net.seyarada.pandeloot.nms.NMSManager;
import net.seyarada.pandeloot.options.MechanicEvent;
import net.seyarada.pandeloot.rewards.Reward;
import net.seyarada.pandeloot.schedulers.HideEntity;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class VisibilityMechanic implements MechanicEvent {
    @Override
    public void onCall(Reward reward, String value) {
        StringLib.warn("++++++ Applying visibility effect with value "+value);
        if(value.equalsIgnoreCase("player") && reward.player!=null) {
            // new HideEntity(reward.item, Collections.singletonList(reward.player));
            List<Player> players = Collections.singletonList(reward.player);
            hideEntity(reward.item, players);
            NMSManager.hideItemFromPlayerMap.put(reward.item.getEntityId(), players);
        } else if(value.equalsIgnoreCase("fight")) {
            List<Player> dropPlayers = new ArrayList<>();
            for(UUID playerUUID : reward.damageUtil.getPlayers()) {
                dropPlayers.add(Bukkit.getPlayer(playerUUID));
            }
            if(dropPlayers.size()>0) {
                // new HideEntity(reward.item, dropPlayers);
                hideEntity(reward.item, dropPlayers);
                NMSManager.hideItemFromPlayerMap.put(reward.item.getEntityId(), dropPlayers);
            }
        }
    }

    private void hideEntity(Entity toHide, List<Player> canView) {
        for (Entity entity : toHide.getNearbyEntities(42, 42, 42)) {
            if (entity instanceof Player && !canView.contains(entity)) {
                NMSManager.destroyEntity(toHide.getEntityId(), entity);
            }
        }
    }

}
