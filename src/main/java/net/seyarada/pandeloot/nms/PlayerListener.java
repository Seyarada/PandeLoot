package net.seyarada.pandeloot.nms;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		NMSManager.injectPlayer(e.getPlayer());
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		NMSManager.removePlayer(e.getPlayer());
	}

	@EventHandler
	public void onItemRemove(EntityPickupItemEvent e) {
		NMSManager.hideItemFromPlayerMap.remove(e.getItem().getEntityId());
	}

}
