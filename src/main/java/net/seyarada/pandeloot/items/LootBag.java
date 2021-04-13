package net.seyarada.pandeloot.items;

import net.seyarada.pandeloot.Config;
import net.seyarada.pandeloot.PandeLoot;
import net.seyarada.pandeloot.drops.DropConditions;
import net.seyarada.pandeloot.drops.DropManager;
import net.seyarada.pandeloot.nms.NMSManager;
import net.seyarada.pandeloot.rewards.NBTNames;
import net.seyarada.pandeloot.rewards.RewardContainer;
import net.seyarada.pandeloot.rewards.RewardLine;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class LootBag extends RewardContainer {

    private final ConfigurationSection lootBag;
    private final RewardLine line;

    public LootBag(ConfigurationSection lootBag, RewardLine line) {
        super(lootBag, line);
        this.lootBag = lootBag;
        this.line = RewardContainer.setParentTable(line, lootBag);
    }

    public void doGroundDrop(Player player, Item item) {
        List<RewardLine> toCollect = getDrops();
        List<RewardLine> rewards = new ArrayList<>();

        ItemUtils.collectRewards(toCollect, rewards, 1, null, player);

        DropManager dropManager = new DropManager(player, item.getLocation(), rewards);
        dropManager.initDrops();

        playArm(player);

        int delay = dropManager.delay;

        item.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, item.getLocation(), 10 ,0,0.15,0, 0.3);
        item.getWorld().spawnParticle(Particle.SMOKE_LARGE, item.getLocation(), 5 ,0,0.15,0, 0.1);

        if(delay>0)
            item.setItemStack(NMSManager.addNBT(item.getItemStack(), NBTNames.onUse, "true"));

        new BukkitRunnable() {
            @Override
            public void run() {
                item.getItemStack().setAmount(item.getItemStack().getAmount()-1);
                if(item.getItemStack().getAmount() <= 0) {
                    item.remove();
                    return;
                }
                item.setItemStack(NMSManager.removeNBT(item.getItemStack(), NBTNames.onUse));
            }
        }.runTaskLater(PandeLoot.getInstance(), delay);
    }

    public ItemStack getItemStack() {
        final String display = lootBag.getString("Display");
        final String material = lootBag.getString("Material");
        final int model = lootBag.getInt("Model");

        ItemStack itemStack = new ItemStack(Material.valueOf(material.toUpperCase()), 1);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(display);
        meta.setCustomModelData(model);
        itemStack.setItemMeta(meta);

        return NMSManager.addNBT(itemStack, NBTNames.bag, line.getItem());

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
