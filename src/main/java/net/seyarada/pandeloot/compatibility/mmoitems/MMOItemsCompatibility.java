package net.seyarada.pandeloot.compatibility.mmoitems;

import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.Type;
import net.Indyuce.mmoitems.api.droptable.item.MMOItemDropItem;
import net.Indyuce.mmoitems.api.player.PlayerData;
import net.seyarada.pandeloot.rewards.RewardLine;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MMOItemsCompatibility {

    public static ItemStack getItem(String item, RewardLine reward, Player player) {
        String typeFormat = reward.insideOptions.get("type").toUpperCase().replace("-", "_");
        Type type = MMOItems.plugin.getTypes().get(typeFormat);
        item = item.toUpperCase(); // MMOItems only accepts uppercase items as valid

        MMOItemDropItem dropItem;
        if(reward.insideOptions.containsKey("unidentified"))
            dropItem = new MMOItemDropItem(type, item, 1.0D, Double.parseDouble(reward.insideOptions.get("unidentified")), 1, 1);
        else
            dropItem = new MMOItemDropItem(type, item, 1.0D, 0d, 1, 1);

        PlayerData playerData = PlayerData.get(player);
        return dropItem.getItem(playerData);
    }

}
