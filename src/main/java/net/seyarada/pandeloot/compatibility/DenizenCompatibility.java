package net.seyarada.pandeloot.compatibility;

import com.denizenscript.denizen.objects.ItemTag;
import org.bukkit.inventory.ItemStack;

public class DenizenCompatibility {

    public static ItemStack getItem(String item) {
        ItemTag itemTag = ItemTag.valueOf(item, true);
        return itemTag.getItemStack();
    }
}
