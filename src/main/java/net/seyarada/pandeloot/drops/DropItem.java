package net.seyarada.pandeloot.drops;

import net.seyarada.pandeloot.Config;
import net.seyarada.pandeloot.Errors;
import net.seyarada.pandeloot.damage.DamageUtil;
import net.seyarada.pandeloot.items.LootTable;
import net.seyarada.pandeloot.nms.NMSManager;
import net.seyarada.pandeloot.rewards.RewardLine;
import net.seyarada.pandeloot.rewards.RewardOptions;
import net.seyarada.pandeloot.schedulers.Beam;
import net.seyarada.pandeloot.schedulers.HideItem;
import net.seyarada.pandeloot.schedulers.ParticleTrail;
import net.seyarada.pandeloot.utils.ColorUtil;
import net.seyarada.pandeloot.utils.DropUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DropItem extends RewardOptions {

    private RewardLine reward;
    private Location location;
    private Player player;
    private Item item;

    private DamageUtil damageUtil;

    public static Map<UUID, DropItem> save = new HashMap<>();

    public DropItem(RewardLine reward, Location location, Player player) {
        super(reward);
        this.reward = reward;
        this.location = location;
        this.player = player;
        initDropItem();
    }

    public DropItem(RewardLine reward, Location location) {
        super(reward);
        this.reward = reward;
        this.location = location;
        initDropItem();
    }

    private void initDropItem() {

        System.out.println(5);

        if(getParent()!=null) {
            reward = LootTable.setParentTable(reward, Config.getLootTableRaw(parent));
        }

        parseEffects();
        parseItemData();
        parseConditions();

        doDrop();
        doEffects();
    }

    public void doDrop() {

        ItemStack itemToDrop = reward.getItemStack();

        if(itemToDrop==null) {
            Errors.UnableToGenerateItemStack(reward);
            return;
        }

        if(itemToDrop.getType()== Material.AIR) return;

        // Sets the amount
        itemToDrop.setAmount(reward.getAmount());

        if(player!=null) {
            // Gives the item directly to the player if "toInv" is set to true
            if (toInv && player.getInventory().firstEmpty() >= 0) {
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
            item.setItemStack(NMSManager.addNBT(item.getItemStack(), "mythicloot", player.getName()));
            new HideItem(item, player);
        }
        if(!stackable)
            item.setItemStack(NMSManager.addNBT(item.getItemStack(), "mythicloot.preventstack", UUID.randomUUID().toString()));
        if(preventpickup)
            item.setItemStack(NMSManager.addNBT(item.getItemStack(), "mythicloot.preventpickup", "true"));
    }

    public void doEffects() {

        // Effects that require the dropped item
        if(item!=null) {
            if (explode) {
                Vector velocity = DropUtil.getVelocity(expoffset, expheight);
                item.setVelocity(velocity);
            }

            if (glow) {
                if (color.equalsIgnoreCase("random")) {
                    color = ColorUtil.randomColor();
                }
                ColorUtil.ColorHandler(item, color);

                if (player != null) {
                    new ParticleTrail(item, color, player);
                } else
                    new ParticleTrail(item, color, location);
            }

            if (beam > 0) {
                if (player != null) {
                    new Beam(item, color, player, beam);
                } else
                    new Beam(item, color, location, beam);
            }

            if (playonpickup) {
                UUID uuid = UUID.randomUUID();
                item.setItemStack(NMSManager.addNBT(item.getItemStack(), "mythicloot.playonpickup", uuid.toString()));
                save.put(uuid, this);
                playonpickup = false;
                return;
            }
        }

        // Effects that do not require the dropped item
        DropUtil.sendDiscordEmbed(player, damageUtil, dTitle, dMessage, dChannel, dColor, dLink, dAvatar);

        if(player!=null) {

            if (message != null)
                player.sendMessage(message);

            if (!title.isEmpty() || !subtitle.isEmpty())
                player.sendTitle(title, subtitle, titleFade, titleDuration, titleFade);

            if (sound != null)
                player.playSound(player.getLocation(), sound, 1, 1);

            if (broadcast != null)
                Bukkit.broadcastMessage(broadcast);

            if (command != null)
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);

            if (experience > 0)
                player.giveExp(experience);
        }

        // TODO
        // eco
        // if(actionbar!=null)
    }

}
