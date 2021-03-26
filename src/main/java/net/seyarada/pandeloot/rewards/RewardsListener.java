package net.seyarada.pandeloot.rewards;

import net.seyarada.pandeloot.Config;
import net.seyarada.pandeloot.drops.DropManager;
import net.seyarada.pandeloot.nms.NMSManager;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class RewardsListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if(e.getAction().equals(Action.PHYSICAL)) return;

        System.out.println("AYAYA_-1");

        if(e.getHand().equals(EquipmentSlot.HAND)) {

            ItemStack iS = e.getPlayer().getInventory().getItemInMainHand();
            if(NMSManager.hasTag(iS, "mythicloot.lootbag")) {
                String lootBag = NMSManager.getTag(iS, "mythicloot.lootbag");
                Location fLoc = e.getPlayer().getLocation();

                List<RewardLine> rewards = RewardLine.StringListToRewardList(Config.getLootBagRaw(lootBag).getStringList("Rewards"));
                new DropManager(e.getPlayer(), fLoc, rewards).initDrops();

                iS.setAmount(iS.getAmount()-1);
                return;

            }

            if(e.getClickedBlock()==null) return;

            Location loc = e.getClickedBlock().getLocation();
            for(Entity i : loc.getWorld().getNearbyEntities(loc, 1.5, 1.5 ,1.5)) {
                if(i instanceof Item) {

                    if ( NMSManager.hasTag(((Item)i).getItemStack(), "mythicloot.lootbag") ) {
                        String lootBag = NMSManager.getTag(((Item)i).getItemStack(), "mythicloot.lootbag");

                        LootBag LootBag = new LootBag(Config.getLootBagRaw(lootBag), new RewardLine(lootBag));
                        LootBag.doGroundDrop(e.getPlayer(), (Item)i);

                        return;
                    }
                }
            }
        }

    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent e) {
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            ItemStack iS = e.getItem().getItemStack();

            if(NMSManager.hasTag(iS, "mythicloot.preventpickup") || NMSManager.hasTag(iS, "mythicloot.onuse") )
                e.setCancelled(true);
            if(NMSManager.hasTag(iS, "mythicloot") && !NMSManager.getTag(iS, "mythicloot").equals(player.getName())) {
                e.setCancelled(true);
            } else {
                //if(NMSManager.hasTag(iS, "mythicloot.playonpickup")) {
                //    DropItem dropItem = DropItem.save.get(UUID.fromString(NMSManager.getTag(iS, "mythicloot.playonpickup")));
                //    dropItem.doEffects();
                // }

                NMSManager.removeNBT(iS, "mythicloot.playonpickup");
                NMSManager.removeNBT(iS, "mythicloot.preventstack");
                NMSManager.removeNBT(iS, "mythicloot"); // TODO Change this to e.getItem / e.getEntity instead of  an event
            }
        }
    }

}
