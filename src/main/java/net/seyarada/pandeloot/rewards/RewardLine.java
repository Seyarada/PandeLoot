package net.seyarada.pandeloot.rewards;

import net.seyarada.pandeloot.Config;
import net.seyarada.pandeloot.compatibility.DenizenCompatibility;
import net.seyarada.pandeloot.compatibility.OraxenCompatibility;
import net.seyarada.pandeloot.compatibility.mmoitems.MIGeneratorCompatibility;
import net.seyarada.pandeloot.compatibility.mmoitems.MMOItemsCompatibility;
import net.seyarada.pandeloot.compatibility.mythicmobs.MythicMobsCompatibility;
import net.seyarada.pandeloot.damage.DamageUtil;
import net.seyarada.pandeloot.items.LootBag;
import net.seyarada.pandeloot.utils.PlaceholderUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class RewardLine extends RewardOptions {

    public final String reward;

    public Map<String, String> specialOptions = new HashMap<>();

    public DamageUtil damageUtil;
    public Player player;

    public RewardLine(String line) {
        this.line = line;
        this.reward = line;

        setOriginAndItem();
    }

    public void build() {
        if(reward.contains(" "))
            setOutsideOptions();
        setInsideOptions(line);
    }

    private void setOriginAndItem() {
        int indexBracket = reward.indexOf("{");
        int originIndicator = reward.indexOf(":");
        String originAndItem;

        if(indexBracket>-1)
            originAndItem = reward.substring(0, indexBracket);
        else if(reward.contains(" ")) {
            originAndItem = reward.substring(0, reward.indexOf(" "));
        } else
            originAndItem = reward;

        if(originIndicator>-1) {
            origin = originAndItem.substring(0, originIndicator);
            item = originAndItem.substring(originIndicator+1);
        } else {
            origin = "minecraft";
            item = originAndItem;
        }
    }

    private void setOutsideOptions() {
        int indexBracket = reward.indexOf("}");
        if(indexBracket+1 >= reward.length()) return;
        String outOptions;

        if(indexBracket>-1) {
            outOptions = reward.substring(indexBracket + 2);
        } else
            outOptions = reward.substring(reward.indexOf(" ")+2);

        for(String i : outOptions.split(" ")) {
           if(i.trim().isEmpty()) continue;

           if(i.startsWith("0.")) {
               chance = parseOutsideOption(i); continue; }
           if(i.contains("=") || i.contains(">") || i.contains("<")) {
               damage = i; continue; }
           if(i.startsWith("-")) {
               String[] b = i.substring(1).split(":");

               if(b.length==1) {
                   specialOptions.put(b[0], "true");
                   continue;
               }

               String key = b[0];
               String value = b[1];
               specialOptions.put(key, value);
               continue;

           }

           amount = Integer.parseInt(parseOutsideOption(i));
        }
    }

    private String parseOutsideOption(String i) {
        if(i.contains("to")) {
            String[] n = "to".split(i);
            Random r = new Random();
            double rangeMin = Double.parseDouble(n[0]);
            double rangeMax = Double.parseDouble(n[1]);
            return String.valueOf(rangeMin + (rangeMax - rangeMin) * r.nextDouble());
        }

        return i;
    }

    public void setInsideOptions(String str) {
        Map<String, String> insideOptions = getOptions(str);
        if(insideOptions==null) return;

        for(String option : insideOptions.keySet()) {
            String value = PlaceholderUtil.parse(insideOptions.get(option), damageUtil, player);
            optionsSwitch(option, value);
        }
    }

    public static List<RewardLine> StringListToRewardList(List<String> strings) {
        return strings.stream()
                .map(RewardLine::new)
                .collect(Collectors.toList());
    }

    public ItemStack getItemStack(Player player) {

        if(itemStack!=null) {
            return itemStack;
        }

        switch (origin) {
            case "minecraft":
                Material material = Material.valueOf(item.toUpperCase());
                return new ItemStack(material, 1);
            case "lootbag":
                return new LootBag(Config.getLootBagRaw(item), this).getItemStack();
            case "mythicmobs":
            case "mm":
                return MythicMobsCompatibility.getItem(item);
            case "mmoitems":
            case "mi":
                return MMOItemsCompatibility.getItem(item, this, player);
            case "mmoitemsgenerator":
            case "migenerator":
            case "migen":
                return MIGeneratorCompatibility.getItem(item, this, player);
            case "oraxen":
                return OraxenCompatibility.getItem(item);
            /*
            case "denizenscript":
            case "dscript":
                new DenizenCompatibility().execute(item);
                return new ItemStack(Material.AIR, 1);

             */
            case "denizenitem":
            case "ditem":
                return DenizenCompatibility.getItem(item);

        }
        return null;
    }

}
