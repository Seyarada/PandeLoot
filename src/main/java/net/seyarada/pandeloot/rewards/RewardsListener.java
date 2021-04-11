package net.seyarada.pandeloot.rewards;

import net.seyarada.pandeloot.Config;
import net.seyarada.pandeloot.damage.DamageTracker;
import net.seyarada.pandeloot.damage.DamageUtil;
import net.seyarada.pandeloot.damage.MobOptions;
import net.seyarada.pandeloot.drops.DropEffects;
import net.seyarada.pandeloot.drops.DropItem;
import net.seyarada.pandeloot.drops.DropManager;
import net.seyarada.pandeloot.items.LootBag;
import net.seyarada.pandeloot.nms.NMSManager;
import net.seyarada.pandeloot.utils.ChatUtil;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class RewardsListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if(e.getAction().equals(Action.PHYSICAL)) return;

        if(e.getHand().equals(EquipmentSlot.HAND)) {

            ItemStack iS = e.getPlayer().getInventory().getItemInMainHand();
            if(NMSManager.hasTag(iS, NBTNames.bag)) {
                String lootBag = NMSManager.getTag(iS, NBTNames.bag);
                Location fLoc = e.getPlayer().getLocation();

                iS.setAmount(iS.getAmount()-1);

                List<RewardLine> rewards = RewardLine.StringListToRewardList(Config.getLootBagRaw(lootBag).getStringList("Rewards"));
                new DropManager(e.getPlayer(), fLoc, rewards).initDrops();

                return;

            }

            if(e.getClickedBlock()==null) return;

            Location loc = e.getClickedBlock().getLocation();
            for(Entity i : loc.getWorld().getNearbyEntities(loc, 1.5, 1.5 ,1.5)) {
                if(i instanceof Item) {

                    if ( NMSManager.hasTag(((Item)i).getItemStack(), NBTNames.bag) ) {
                        if(NMSManager.hasTag(((Item)i).getItemStack(), NBTNames.onUse)) continue;

                        String lootBag = NMSManager.getTag(((Item)i).getItemStack(), NBTNames.bag);
                        LootBag LootBag = new LootBag(Config.getLootBagRaw(lootBag), new RewardLine(lootBag));

                        LootBag.doGroundDrop(e.getPlayer(), (Item)i);
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

            if(NMSManager.hasTag(iS, NBTNames.preventPickup) || NMSManager.hasTag(iS, NBTNames.onUse) )
                e.setCancelled(true);

            if(NMSManager.hasTag(iS, NBTNames.root) && !NMSManager.getTag(iS, NBTNames.root).equals(player.getName())) {
                e.setCancelled(true);

            } else {
                if(NMSManager.hasTag(iS, NBTNames.playOnPickup)) {
                    DropItem source = DropEffects.playOnPickupStorage.get(UUID.fromString(NMSManager.getTag(iS, NBTNames.playOnPickup)));
                    new DropEffects(source, true);
                }

                NMSManager.removeNBT(iS, NBTNames.playOnPickup);
                NMSManager.removeNBT(iS, NBTNames.preventStack);
                NMSManager.removeNBT(iS, NBTNames.root); // TODO Change this to e.getItem / e.getEntity instead of  an event
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onSpawn(EntitySpawnEvent e) {
        ConfigurationSection mobConfig = Config.getMob(e.getEntity());
        if(mobConfig!=null) {
            MobOptions mobOptions = new MobOptions();
            mobOptions.resetPlayers = mobConfig.getBoolean("Options.ResetPlayers");
            mobOptions.resetHeal = mobConfig.getBoolean("Options.ResetHeal");
            DamageTracker.loadedMobs.put(e.getEntity().getUniqueId(), mobOptions);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDeath(EntityDeathEvent e) {
        Entity mob = e.getEntity();
        UUID uuid = mob.getUniqueId();

        // Don't drop if the mob isn't loaded or nobody has damaged it
        if(!DamageTracker.loadedMobs.containsKey(uuid)) return;
        DamageTracker.loadedMobs.remove(uuid);
        if(!DamageTracker.damageTracker.containsKey(uuid)) return;
        if(DamageTracker.get(uuid).size()==0) return;

        ConfigurationSection config = Config.getMob(mob);
        boolean rank = config.getBoolean("Options.ScoreMessage");
        boolean score = config.getBoolean("Options.ScoreHologram");

        List<String> strings = config.getStringList("Rewards");
        List<RewardLine> rewards = RewardLine.StringListToRewardList(strings);

        DamageUtil damageUtil = new DamageUtil(uuid);
        DropManager manager = new DropManager(Arrays.asList(damageUtil.getPlayers()), rewards);

        if(rank) ChatUtil.announceChatRank(damageUtil);
        if(score) NMSManager.spawnHologram(damageUtil);
        manager.setDamageUtil(damageUtil);
        manager.initDrops();
    }

}
