package net.seyarada.pandeloot.compatibility;

import io.th0rgal.oraxen.items.OraxenItems;
import org.bukkit.inventory.ItemStack;

public class OraxenCompatibility {

    public static ItemStack getItem(String item) {

        return OraxenItems.getItemById(item).build();

    }

}
