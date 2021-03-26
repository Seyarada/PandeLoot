package net.seyarada.pandeloot.compatibility;

import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.Type;
import net.Indyuce.mmoitems.api.droptable.item.MMOItemDropItem;
import net.seyarada.pandeloot.rewards.RewardLine;
import org.apache.commons.lang3.Validate;
import org.bukkit.inventory.ItemStack;

public class MMOItemsCompatibility {

    public static ItemStack getItem(String item, RewardLine line) {
        String typeFormat = line.getOption(null, "type").toUpperCase().replace("-", "_");
        Validate.isTrue(MMOItems.plugin.getTypes().has(typeFormat), "Could not find type with ID " + typeFormat);
        Type type = MMOItems.plugin.getTypes().get(typeFormat);
        item = item.toUpperCase(); // MMOItems only accepts uppercase items as valid
        MMOItemDropItem dropItem = new MMOItemDropItem(type, item, 1.0D, Double.parseDouble(line.getOption("0", "unidentified")), 1, 1);
        //PlayerData playerData = PlayerData.get(player);
        return dropItem.getItem(null);
    }

}
