package net.seyarada.pandeloot.options.mechanics;

import net.seyarada.pandeloot.StringLib;
import net.seyarada.pandeloot.nms.NMSManager;
import net.seyarada.pandeloot.options.MechanicEvent;
import net.seyarada.pandeloot.rewards.Reward;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class HologramMechanic implements MechanicEvent {
    @Override
    public void onCall(Reward reward, String value) {
        final Map<String, String> options = reward.options;
        final int abandonTime = Integer.parseInt(options.get("abandontime"));

        if(value!=null && !value.isEmpty() && !value.equals("false")) {
            StringLib.warn("++++++ Applying hologram effect with value "+value);
            final String hologram = options.get("hologram");
            final Item item = reward.item;
            final Player player = reward.player;

            // On one hand, this having 3 cases for different types of capitalization is horrible is shouldn't be a
            // thing, on the other hand, I dislike how if elses look so ¯\_(ツ)_/¯
            switch(hologram) {
                case "display":
                case "Display":
                case "DISPLAY":
                    List<String> display = getItemDisplay(item);
                    if(display==null) break;
                    NMSManager.spawnLockedHologram(item, item.getLocation(), display, Collections.singletonList(player), abandonTime, reward);
                    break;
                case "lore":
                case "Lore":
                case "LORE":
                    List<String> lore = getItemLore(item);
                    if(!lore.isEmpty())
                        NMSManager.spawnLockedHologram(item, item.getLocation(), lore, Collections.singletonList(player), abandonTime, reward);
                    break;
                case "full":
                case "Full":
                case "FULL":
                    display = getItemDisplay(item);
                    lore = getItemLore(item);
                    if(lore==null) lore = new ArrayList<>();
                    lore.addAll(display);
                    NMSManager.spawnLockedHologram(item, item.getLocation(), lore, Collections.singletonList(player), abandonTime, reward);
                    break;
                default:
                    List<String> entries = Arrays.asList(hologram.split(","));
                    NMSManager.spawnLockedHologram(item, item.getLocation(), entries, Collections.singletonList(player), abandonTime, reward);
            }
        } else if(abandonTime>0) {
            NMSManager.spawnLockedHologram(reward.item, reward.item.getLocation(), new ArrayList<>(), Collections.singletonList(reward.player), abandonTime, reward);
        }
    }

    private List<String> getItemDisplay(Item item) {
        final ItemStack itemStack = item.getItemStack();
        if(itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()) {
            ArrayList<String> name = new ArrayList<>();
            name.add(itemStack.getItemMeta().getDisplayName());
            return name;
        }
        return null;
    }

    private List<String> getItemLore(Item item) {
        final ItemStack itemStack = item.getItemStack();
        if(itemStack.hasItemMeta() && itemStack.getItemMeta().hasLore()) {
            return itemStack.getItemMeta().getLore();
        }
        return new ArrayList<>();
    }
}
