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
        String typeFormat = reward.type.toUpperCase().replace("-", "_");
        Type type = MMOItems.plugin.getTypes().get(typeFormat);
        item = item.toUpperCase(); // MMOItems only accepts uppercase items as valid

        MMOItemDropItem dropItem = new MMOItemDropItem(type, item, 1.0D, Double.parseDouble(reward.unidentified), 1, 1);
        PlayerData playerData = PlayerData.get(player);
        return dropItem.getItem(playerData);
    }

}
