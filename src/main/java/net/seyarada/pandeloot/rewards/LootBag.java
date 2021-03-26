package net.seyarada.pandeloot.rewards;

import net.seyarada.pandeloot.Config;
import net.seyarada.pandeloot.drops.DropManager;
import net.seyarada.pandeloot.nms.NMSManager;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class LootBag extends RewardContainer {

    private final ConfigurationSection lootBag;
    private final RewardLine line;

    public LootBag(ConfigurationSection lootBag, RewardLine line) {
        super(lootBag, line);
        this.lootBag = lootBag;
        this.line = line;
    }

    public void doGroundDrop(Player player, Item item) {
        List<RewardLine> rewards = RewardLine.StringListToRewardList(lootBag.getStringList("Rewards"));
        new DropManager(player, item.getLocation(), rewards).initDrops();

        item.getItemStack().setAmount(item.getItemStack().getAmount()-1);
        if(item.getItemStack().getAmount() <= 0)
            item.remove();
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

        return NMSManager.addNBT(itemStack, "mythicloot.lootbag", line.getItem());

    }

}
