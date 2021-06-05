package net.seyarada.pandeloot.items;

import net.seyarada.pandeloot.Config;
import net.seyarada.pandeloot.PandeLoot;
import net.seyarada.pandeloot.StringLib;
import net.seyarada.pandeloot.drops.Manager;
import net.seyarada.pandeloot.nms.NMSManager;
import net.seyarada.pandeloot.options.Reward;
import net.seyarada.pandeloot.rewards.RewardContainerNew;
import net.seyarada.pandeloot.rewards.RewardLineNew;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static net.seyarada.pandeloot.items.ItemUtils.collectRewards;

public class LootBag extends RewardContainerNew {

    public static List<UUID> openedBags = new ArrayList<>();

    public LootBag(String internalName, Reward reward) {
        super(internalName, reward);
    }

    public void doGroundDrop(Player player, Item item) {
        if(openedBags.contains(item.getUniqueId())) return;

        openedBags.add(item.getUniqueId());

        StringLib.warn("+ Doing ground drop for lootbag "+internalName+", from "+item.getUniqueId());
        List<RewardLineNew> drops = getDrops();

        StringLib.warn("+++ Got for the ground drop: ");
        for(RewardLineNew drop : drops) {
            StringLib.warn("++++ "+drop.baseLine);
        }
        final List<Reward> baseRewards = reward.createNewRewards(drops);
        final List<Reward> rewards = new ArrayList<>();
        collectRewards(baseRewards, rewards, 1, false);

        Manager manager = new Manager();
        manager.fromReward(Collections.singletonList(player), rewards, item.getLocation());

        playArm(player);

        int delay = manager.delay;

        player.spawnParticle(Particle.FIREWORKS_SPARK, item.getLocation(), 10 ,0,0.15,0, 0.3);
        player.spawnParticle(Particle.SMOKE_LARGE, item.getLocation(), 5 ,0,0.15,0, 0.1);

        if(delay>0) {
            item.setItemStack(NMSManager.addNBT(item.getItemStack(), StringLib.onUse, "true"));
            new BukkitRunnable() {
                @Override
                public void run() {
                    item.getItemStack().setAmount(item.getItemStack().getAmount()-1);
                    if(item.getItemStack().getAmount() <= 0) {
                        item.remove();
                        openedBags.remove(item.getUniqueId());
                        return;
                    }
                    openedBags.remove(item.getUniqueId());
                    item.setItemStack(NMSManager.removeNBT(item.getItemStack(), StringLib.onUse));
                }
            }.runTaskLater(PandeLoot.getInstance(), delay);
        } else {
            item.getItemStack().setAmount(item.getItemStack().getAmount()-1);
            if(item.getItemStack().getAmount() <= 0) {
                item.remove();
                openedBags.remove(item.getUniqueId());
                return;
            }
            openedBags.remove(item.getUniqueId());
            item.setItemStack(NMSManager.removeNBT(item.getItemStack(), StringLib.onUse));
        }
    }

    public ItemStack getItemStack() {
        if(rewardContainer==null) return new ItemStack(Material.AIR, 1);

        final String display = rewardContainer.getString("Display");
        final String material = rewardContainer.getString("Material");
        final int model = rewardContainer.getInt("Model");

        ItemStack itemStack = new ItemStack(Material.valueOf(material.toUpperCase()), 1);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(display);
        meta.setCustomModelData(model);
        itemStack.setItemMeta(meta);

        return NMSManager.addNBT(itemStack, StringLib.bag, reward.rewardLine.item);

    }

    private void playArm(Player player) {
        if(player==null) return;

        if(Config.getPlayArm()) {
            if(Config.getPlayArmEmpty()) {
                Material mainHand = player.getInventory().getItemInMainHand().getType();
                Material offHand = player.getInventory().getItemInOffHand().getType();
                if(mainHand==Material.AIR && offHand==Material.AIR)
                    player.swingMainHand();
            } else player.swingMainHand();

        }

    }

}
