package net.seyarada.pandeloot.utils;

import net.seyarada.pandeloot.schedulers.Beam;
import net.seyarada.pandeloot.schedulers.ParticleTrail;
import net.seyarada.pandeloot.schedulers.RainbowRunner;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Random;

public class ColorUtil {

    public static final String[] chatColors = new String[]{"AQUA", "BLACK", "BLUE", "DARK_AQUA", "DARK_BLUE", "DARK_GRAY", "DARK_GREEN", "DARK_PURPLE", "DARK_RED", "GOLD", "GRAY", "GREEN", "LIGHT_PURPLE", "RED", "WHITE", "YELLOW"};
    private static final Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();

    public static void setColorEffects(Item item, String color, Player player, double beam) {
        color = getColor(item, color);

        if(color.equals("RAINBOW")) {
            if (player != null) {
                new RainbowRunner(item, player, beam);
            } else
                new RainbowRunner(item, item.getLocation(), beam);
            return;
        }

        setItemColor(item, color, player);

        if (player != null) {
            new ParticleTrail(item, color, player);
        } else
            new ParticleTrail(item, color, item.getLocation());

        if (beam > 0) {
            if (player != null) {
                new Beam(item, color, player, beam);
            } else
                new Beam(item, color, item.getLocation(), beam);
        }
    }

    public static void setItemColor(Item item, String color, Player player) {
        color = getColor(item, color);
        final String teamName = "PandeLoot" + ChatColor.valueOf(color).toString();

        Scoreboard toUseBoard;
        if(player==null)
            toUseBoard = board;
        else
            toUseBoard = player.getScoreboard();

        if (toUseBoard.getTeam(teamName) == null) {
            Team team = toUseBoard.registerNewTeam(teamName);
            team.setColor(ChatColor.valueOf(color));
            team.addEntry(item.getUniqueId().toString());
            return;
        }
        toUseBoard.getTeam(teamName).addEntry(item.getUniqueId().toString());
    }

    public static String getColor(Item item, String color) {

        if(color.equalsIgnoreCase("display")) {
            if(item.getItemStack().getItemMeta().hasDisplayName()) {
                String display = item.getItemStack().getItemMeta().getDisplayName().substring(0, 2);
                return colorSwitch(display.substring(1));
            } else return "WHITE";
        }

        if(color.equalsIgnoreCase("random")) {
            int rnd = new Random().nextInt(chatColors.length);
            return chatColors[rnd];
        }

        return color.toUpperCase();
    }

    public static String colorSwitch(String color) {
        switch (color) {
            case "0": return "BLACK";
            case "1": return "DARK_BLUE";
            case "2": return "DARK_GREEN";
            case "3": return "DARK_AQUA";
            case "4": return "DARK_RED";
            case "5": return "DARK_PURPLE";
            case "6": return "GOLD";
            case "7": return "GRAY";
            case "8": return "DARK_GRAY";
            case "9": return "BLUE";
            case "a": return "GREEN";
            case "b": return "AQUA";
            case "c": return "RED";
            case "d": return "LIGHT_PURPLE";
            case "e": return "YELLOW";
            default:   return "WHITE";
        }
    }

    public static Color getRGB(String color) {
        switch(color) {
            case "YELLOW":       return Color.fromRGB(255, 255, 85);
            case "LIGHT_PURPLE": return Color.fromRGB(255, 85, 255);
            case "GREEN":        return Color.fromRGB(85, 255, 85);
            case "GOLD":         return Color.fromRGB(255, 170, 0);
            case "DARK_RED":     return Color.fromRGB(170, 0, 0);
            case "GRAY":         return Color.fromRGB(128,128,128);
            case "DARK_PURPLE":  return Color.fromRGB(170, 0, 170);
            case "DARK_GREEN":   return Color.fromRGB(0, 170, 0);
            case "DARK_GRAY":    return Color.fromRGB(85, 85, 85);
            case "DARK_BLUE":    return Color.fromRGB(0, 0, 170);
            case "DARK_AQUA":    return Color.fromRGB(0, 170, 170);
            case "BLUE":         return Color.fromRGB(85, 85, 255);
            case "BLACK":        return Color.fromRGB(0, 0, 0);
            case "AQUA":         return Color.fromRGB(85, 255, 255);
            case "RED":          return Color.fromRGB(255, 85, 85);
            default:             return Color.fromRGB(255, 255, 255);
        }
    }

    public static java.awt.Color getAWTColor(String string) {
        switch (string.toLowerCase()) {
            case "blue":       return java.awt.Color.BLUE;
            case "cyan":       return java.awt.Color.CYAN;
            case "dark_gray":  return java.awt.Color.DARK_GRAY;
            case "gray":       return java.awt.Color.GRAY;
            case "green":      return java.awt.Color.GREEN;
            case "yellow":     return java.awt.Color.YELLOW;
            case "light_gray": return java.awt.Color.LIGHT_GRAY;
            case "magenta":    return java.awt.Color.MAGENTA;
            case "orange":     return java.awt.Color.ORANGE;
            case "pink":       return java.awt.Color.PINK;
            case "red":        return java.awt.Color.RED;
            case "white":      return java.awt.Color.WHITE;
            default:           return java.awt.Color.BLACK;
        }
    }
}
