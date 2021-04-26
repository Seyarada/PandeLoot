package net.seyarada.pandeloot.drops;

import net.seyarada.pandeloot.Config;
import net.seyarada.pandeloot.StringLib;
import net.seyarada.pandeloot.damage.DamageUtil;
import net.seyarada.pandeloot.items.LootTable;
import net.seyarada.pandeloot.nms.NMSManager;
import net.seyarada.pandeloot.rewards.RewardLine;
import net.seyarada.pandeloot.schedulers.HideEntity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

public class DropItem {

    public RewardLine reward;
    public Location location;
    public Player player;
    public Item item;

    public DamageUtil damageUtil;

    public DropItem(RewardLine reward, DamageUtil damageUtil, Player player) {
        this.reward = reward;
        this.location = damageUtil.getLocation();
        this.player = player;
        this.damageUtil = damageUtil;
        initDropItem();
    }

    public DropItem(RewardLine reward, Location location, Player player) {
        this.reward = reward;
        this.location = location;
        this.player = player;
        initDropItem();
    }

    public DropItem(RewardLine reward, Location location) {
        this.reward = reward;
        this.location = location;
        initDropItem();
    }

    private void initDropItem() {
        if(reward.parent!=null) {
            reward = LootTable.setParentTable(reward, Config.getLootTableRaw(reward.parent));
        }

        doDrop();
        new DropEffects(this);
    }

    public void doDrop() {
        ItemStack itemToDrop = reward.getItemStack(player);

        if(itemToDrop==null) {
            StringLib.badItemStack(reward);
            return;
        }

        if(itemToDrop.getType()== Material.AIR) return;

        // Sets the amount
        itemToDrop.setAmount(reward.amount);

        if(player!=null) {
            // Gives the item directly to the player if "toInv" is set to true
            if (reward.toInv && player.getInventory().firstEmpty() >= 0) {
                player.getInventory().addItem(itemToDrop);
                return;
            }
        }

        item = location.getWorld().dropItemNaturally(location, itemToDrop);

        //int i = 0;
        //for(Entity e : location.getWorld().getNearbyEntities(location, 10,10,10)) {
        //    if(e instanceof Item) {
        //        if( V1_16_R3.hasTag(((Item)e).getItemStack(), "mythicloot.preventstack") ) {
        //            i++;
        //        }
        //    }
        //}

        //double angle = 2 * Math.PI / i;
        //double cos = Math.cos(angle);
        //double sin = Math.sin(angle);
        //double iX = location.getX() + 3 * cos;
        //double iZ = location.getZ() + 3 * sin;

        //Location loc = location.clone();
        //loc.setX(iX);
        //loc.setZ(iZ);

        //Bukkit.getScheduler().scheduleSyncRepeatingTask(MythicLoot.getInstance(), () -> {

        //    Color rgb = ColorUtil.getRGB("AQUA");
        //    Particle.DustOptions dustOptions = new Particle.DustOptions(rgb, 1);
        //    location.getWorld().spawnParticle(Particle.REDSTONE, loc, 1, dustOptions);

        //}, 0, 1);

        if(player!=null) {
            item.setItemStack(NMSManager.addNBT(item.getItemStack(), StringLib.root, player.getName()));

            if(reward.canView==null||reward.canView.equalsIgnoreCase("player")) {
                new HideEntity(item, Collections.singletonList(player));
            } else if(reward.canView.equalsIgnoreCase("fight")) {
                new HideEntity(item, Arrays.asList(damageUtil.getPlayers()));
            }
        }
        if(!reward.stackable)
            item.setItemStack(NMSManager.addNBT(item.getItemStack(), StringLib.preventStack, UUID.randomUUID().toString()));
        if(reward.preventpickup) {
            item.setItemStack(NMSManager.addNBT(item.getItemStack(), StringLib.preventPickup, "true"));
        }
    }
    
}
