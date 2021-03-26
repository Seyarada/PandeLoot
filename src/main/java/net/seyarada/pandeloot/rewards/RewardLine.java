package net.seyarada.pandeloot.rewards;

import net.seyarada.pandeloot.Config;
import net.seyarada.pandeloot.compatibility.MMOItemsCompatibility;
import net.seyarada.pandeloot.compatibility.OraxenCompatibility;
import net.seyarada.pandeloot.compatibility.mythicmobs.MythicMobsCompatibility;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RewardLine {

    private String origin;
    private String item;
    private String line;
    private int amount = 1;
    private double chance = 2;

    private int start;
    private int end;
    private int colon;

    private boolean skip;

    public Map<String, Object> options = new HashMap<>();

    public RewardLine(String string) {
        line = string;
        start = line.indexOf("{");
        end = line.lastIndexOf("}");
        colon = line.indexOf(":");

        // No brackets found, this assumes the format is the following:
        // Rewards:
        // - diamond_block || - minecraft:diamond_block
        if(start == -1 && end == -1) {
            setData(line, colon);
        }

        // Both brackets found
        if(start != -1 && end != -1) {

            setData(line, colon);

            // Stores what's inside the brackets as options
            //                 +1 so it doesn't includes the bracket
            String j = line.substring(start+1, end);

            // Splits the bracket contents into individual options
            // glow=true;color=RED -> [glow=true], [color=RED]
            for(String k : j.split(";")) {

                // Splits again by = to get the option and the value of the option
                // then stores as a map so it can be get without further cost
                String[] l = k.split("=");
                String key = l[0].toLowerCase();
                String value = l[1];
                options.put(key, value);
            }
        }
    }

    public void setData(String line, int colonLocation) {

        if(start!=-1 && end!=-1) {
            // If the line has brackets, remove them
            // stone{color=RED} 5 0.2 -> stone 5 0.2
            line = line.substring(0, start)+line.substring(end+1);
        }

        String[] splitLine;
        splitLine = line.split(" ");

        if(colonLocation==-1) {     // - stone 5 0.2
            origin = "minecraft";
            item = splitLine[0];
        } else {                    // - minecraft:stone 5 0.2
            origin = splitLine[0].split(":")[0];
            item = splitLine[0].split(":")[1];
        }

        if(item.contains(";"))
            item = item.split(";")[0];

        if(splitLine.length>1) {

            double tempAmount;

            tempAmount = Double.parseDouble(splitLine[1]);
            if(splitLine.length>2)
                chance = Double.parseDouble(splitLine[2]);

            System.out.println("TEMP:" + tempAmount);
            System.out.println("TEMP:" + (tempAmount<1d));

            if(tempAmount<1d) {
                chance = tempAmount;
                amount = 1;
            } else {
                amount = (int) tempAmount;
            }
        }

    }

    public String getOption(String fallback, String... option) {
        for(String i : option) {
            i = i.toLowerCase();
            if(options.containsKey(i)) {
                return String.valueOf(options.get(i));
            }
        }
        return fallback;
    }

    public void put(String key, String value) {
        options.putIfAbsent(key.toLowerCase(), value);
    }

    public String getOrigin() {
        return origin;
    }

    public String getItem() {
        return item;
    }

    public double getChance() { return chance; }

    public boolean isShared() {
        return false;
    }

    public void setChance(double chance) {
        this.chance = chance;
    }

    public ItemStack getItemStack() {
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
                return MMOItemsCompatibility.getItem(item, this);
            case "oraxen":
                return OraxenCompatibility.getItem(item);
        }
        return null;
    }

    public int getAmount() {
        return amount;
    }

    public String getLine() {
        return line;
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }

    public boolean getSkip() {
        return skip;
    }

    public static List<RewardLine> StringListToRewardList(List<String> strings) {
        return strings.stream()
                .map(RewardLine::new)
                .collect(Collectors.toList());
    }
}
