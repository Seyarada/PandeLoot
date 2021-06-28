package net.seyarada.pandeloot.rewards;

import net.seyarada.pandeloot.StringLib;
import net.seyarada.pandeloot.damage.DamageUtil;
import net.seyarada.pandeloot.utils.PlaceholderUtil;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RewardLine {


    final int optionsStartIndex;
    int optionsEndIndex;
    final int originIndicatorIndex;
    final public String baseLine;
    final public String origin;

    final public Map<String, String> specialOptions = new HashMap<>();
    final public Map<String, String> insideOptions = new HashMap<>();

    public String item;
    public String chance = "1";
    public String damage;
    public int amount = 1;
    public boolean skipConditions;

    public RewardLine(String baseLine) {
        this.baseLine = baseLine;
        optionsStartIndex = baseLine.indexOf("{");
        optionsEndIndex = baseLine.lastIndexOf("}");
        originIndicatorIndex = baseLine.indexOf(":");

        origin = setOrigin();
        item = setItem();
    }

    public void build(Player player, DamageUtil util) {
        final String parsedBaseLine = PlaceholderUtil.parse(baseLine, util, player, false);
        generateOutsideOptions(parsedBaseLine);
        generateInsideOptions(parsedBaseLine);
    }

    public double getChance(DamageUtil u, Player p) {
        chance = PlaceholderUtil.parse(chance, u, p, true);
        double newChance = PlaceholderUtil.parseMath(chance);
        if(specialOptions.containsKey("multiplier")) {
            final double multiplier = Double.parseDouble(specialOptions.get("multiplier"));
            final double percent = u.getPercentageDamage(p);
            newChance += percent*multiplier;
        }
        return newChance;
    }

    private String setOrigin() {
        if(originIndicatorIndex==-1) // diamond{...}
            return "minecraft";
        if(optionsStartIndex==-1 || originIndicatorIndex<optionsStartIndex) // minecraft:diamond || minecraft:diamond{...}
            return baseLine.substring(0,originIndicatorIndex);
        return "minecraft";
    }

    private String setItem() {
        if(originIndicatorIndex==-1) {  // diamond || diamond{...}
            if (optionsStartIndex == -1) { // diamond
                if (baseLine.contains(" "))
                    return baseLine.substring(0, baseLine.indexOf(" "));
                return baseLine;
            }
            return baseLine.substring(0, optionsStartIndex); // diamond{...}
        }

        if(optionsStartIndex==-1) {  // minecraft:diamond
            if(baseLine.contains(" "))
                return baseLine.substring(originIndicatorIndex + 1, baseLine.indexOf(" "));
            return baseLine.substring(originIndicatorIndex + 1);
        }

        if(originIndicatorIndex>optionsStartIndex) { // diamond{say :o:}
            return baseLine.substring(0, optionsStartIndex);
        }

        return baseLine.substring(originIndicatorIndex+1,optionsStartIndex); // minecraft:diamond{...}
    }

    public void generateInsideOptions(String baseLine) {
        int aStart = baseLine.indexOf("{");
        int aEnd = baseLine.lastIndexOf("}");
        if(aStart==-1||aEnd==-1) return;
        StringLib.warn("+++ BaseLine "+ baseLine);
        final String[] options = baseLine.substring(aStart+1, aEnd).split(";");
        StringLib.warn("++++ Options for generateInside are "+ Arrays.toString(options));
        for(String option : options) {
            StringLib.warn("+++++ Option is "+ option);
            final String[] l = option.split("=");
            final String optionKey = l[0].toLowerCase();
            final String optionValue = l[1];
            insideOptions.putIfAbsent(optionKey, optionValue);
        }
    }

    private void generateOutsideOptions(String baseLine) {
        optionsEndIndex = baseLine.lastIndexOf("}");
        if(optionsEndIndex+1 >= baseLine.length()) return;
        if(!baseLine.contains(" ")) return;

        if(optionsEndIndex>-1)
            baseLine = baseLine.substring(optionsEndIndex + 2);
        else
            baseLine = baseLine.substring(baseLine.indexOf(" ")+1);


        final String[] options = baseLine.split(" ");
        for(String option : options) {

            if(option.trim().isEmpty()) continue;

            if(option.startsWith("0.")) {
                chance = parseOutsideOption(option);
                continue;
            }
            if(option.contains("=") || option.contains(">") || option.contains("<")) {
                damage = option; continue; }

            if(option.startsWith("-")) {
                final String[] specialOption = option.substring(1).split(":");
                if(specialOption.length==1) {
                    specialOptions.putIfAbsent(specialOption[0], "true");
                    continue;
                }
                final String optionKey = specialOption[0];
                final String optionValue = specialOption[1];
                specialOptions.putIfAbsent(optionKey, optionValue);
                continue;
            }

            final String thingLeft = parseOutsideOption(option);
            if(!thingLeft.equals("1"))
                amount = (int) Double.parseDouble(thingLeft);
        }
    }

    private String parseOutsideOption(String i) {
        if(i.contains("to")) {
            final String[] n = i.split("to");
            final Random r = new Random();
            if(n[0].isEmpty()||n[1].isEmpty()) return i;

            final double rangeMin = Double.parseDouble(n[0]);
            final double rangeMax = Double.parseDouble(n[1]);
            return String.valueOf(rangeMin + (rangeMax - rangeMin) * r.nextDouble());
        }
        return i;
    }

}
