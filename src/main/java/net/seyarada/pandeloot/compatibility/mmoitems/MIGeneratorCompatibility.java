package net.seyarada.pandeloot.compatibility.mmoitems;

import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.ItemTier;
import net.Indyuce.mmoitems.api.Type;
import net.Indyuce.mmoitems.api.item.template.MMOItemTemplate;
import net.Indyuce.mmoitems.api.item.template.explorer.ClassFilter;
import net.Indyuce.mmoitems.api.item.template.explorer.IDFilter;
import net.Indyuce.mmoitems.api.item.template.explorer.TemplateExplorer;
import net.Indyuce.mmoitems.api.item.template.explorer.TypeFilter;
import net.Indyuce.mmoitems.api.player.PlayerData;
import net.Indyuce.mmoitems.api.player.RPGPlayer;
import net.seyarada.pandeloot.rewards.RewardLine;
import org.apache.commons.lang3.Validate;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Optional;
import java.util.Random;

public class MIGeneratorCompatibility {
    private static final Random random = new Random();


    public static ItemStack getItem(String item, RewardLine reward, Player player) {
        if(player==null || reward.specialOptions.isEmpty()) return MMOItemsCompatibility.getItem(item, reward, player);

        Map<String, String> handler = reward.specialOptions;
        RPGPlayer rpgPlayer = PlayerData.get(player).getRPG();

        int itemLevel;
        ItemTier itemTier;
        TemplateExplorer builder = new TemplateExplorer();

        if(handler.containsKey("level")) {
            itemLevel = Integer.parseInt(handler.get("level"));
        } else if(handler.containsKey("matchlevel")) {
            itemLevel = MMOItems.plugin.getTemplates().rollLevel(rpgPlayer.getLevel());
        } else itemLevel = (1 + random.nextInt(100));

        if(handler.containsKey("tier")) {
            itemTier = MMOItems.plugin.getTiers().getOrThrow(handler.get("tier").toUpperCase().replace("-", "_"));
        } else itemTier = MMOItems.plugin.getTemplates().rollTier();

        if (handler.containsKey("matchclass"))
            builder.applyFilter(new ClassFilter(rpgPlayer));

        if (handler.containsKey("class"))
            builder.applyFilter(new ClassFilter(handler.get("class").replace("-", " ").replace("_", " ")));

        builder.applyFilter(new TypeFilter(Type.get(reward.type)));
        builder.applyFilter(new IDFilter(item));


        Optional<MMOItemTemplate> optional = builder.rollLoot();

        ItemStack iS = optional.get().newBuilder(itemLevel, itemTier).build().newBuilder().build();
        Validate.isTrue((iS != null && iS.getType() != Material.AIR), "Could not generate item with ID '" + optional.get().getId() + "'");
        return iS;
    }

}
