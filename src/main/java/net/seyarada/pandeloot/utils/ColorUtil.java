package net.seyarada.pandeloot.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.entity.Item;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ColorUtil {

    public static void ColorHandler(Item item, String color) {
        item.setGlowing(true);
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();

        if(color.equals("display") && item.getItemStack().getItemMeta().hasDisplayName()) {
            String display = item.getItemStack().getItemMeta().getDisplayName();

            if (display.startsWith("§0"))
                color = "BLACK";
            else if (display.startsWith("§1"))
                color = "DARK_BLUE";
            else if (display.startsWith("§2"))
                color = "DARK_GREEN";
            else if (display.startsWith("§3"))
                color = "DARK_AQUA";
            else if (display.startsWith("§4"))
                color = "DARK_RED";
            else if (display.startsWith("§5"))
                color = "DARK_PURPLE";
            else if (display.startsWith("§6"))
                color = "GOLD";
            else if (display.startsWith("§7"))
                color = "GRAY";
            else if (display.startsWith("§8"))
                color = "DARK_GRAY";
            else if (display.startsWith("§9"))
                color = "BLUE";
            else if (display.startsWith("§a"))
                color = "GREEN";
            else if (display.startsWith("§b"))
                color = "AQUA";
            else if (display.startsWith("§c"))
                color = "RED";
            else if (display.startsWith("§d"))
                color = "LIGHT_PURPLE";
            else if (display.startsWith("§e"))
                color = "YELLOW";
        }

        switch(color.toUpperCase()) {

            case "YELLOW":
                if(board.getTeam("mLYELLOW")==null) {
                    Team YELLOW = board.registerNewTeam("mLYELLOW");
                    YELLOW.setColor(ChatColor.YELLOW);
                    YELLOW.addEntry(item.getUniqueId().toString());
                } else {
                    board.getTeam("mLYELLOW").addEntry(item.getUniqueId().toString());
                }
                break;

            case "LIGHT_PURPLE":
                if(board.getTeam("mLLIGHT_PURPLE")==null) {
                    Team LIGHT_PURPLE = board.registerNewTeam("mLLIGHT_PURPLE");
                    LIGHT_PURPLE.setColor(ChatColor.LIGHT_PURPLE);
                    LIGHT_PURPLE.addEntry(item.getUniqueId().toString());
                } else {
                    board.getTeam("mLLIGHT_PURPLE").addEntry(item.getUniqueId().toString());
                }
                break;

            case "GREEN":
                if(board.getTeam("mLGREEN")==null) {
                    Team GREEN = board.registerNewTeam("mLGREEN");
                    GREEN.setColor(ChatColor.GREEN);
                    GREEN.addEntry(item.getUniqueId().toString());
                } else {
                    board.getTeam("mLGREEN").addEntry(item.getUniqueId().toString());
                }
                break;

            case "GOLD":
                if(board.getTeam("mLGOLD")==null) {
                    Team GOLD = board.registerNewTeam("mLGOLD");
                    GOLD.setColor(ChatColor.GOLD);
                    GOLD.addEntry(item.getUniqueId().toString());
                } else {
                    board.getTeam("mLGOLD").addEntry(item.getUniqueId().toString());
                }
                break;

            case "DARK_RED":
                if(board.getTeam("mLDARK_RED")==null) {
                    Team DARK_RED = board.registerNewTeam("mLDARK_RED");
                    DARK_RED.setColor(ChatColor.DARK_RED);
                    DARK_RED.addEntry(item.getUniqueId().toString());
                } else {
                    board.getTeam("mLDARK_RED").addEntry(item.getUniqueId().toString());
                }
                break;

            case "DARK_PURPLE":
                if(board.getTeam("mLDARK_PURPLE")==null) {
                    Team DARK_PURPLE = board.registerNewTeam("mLDARK_PURPLE");
                    DARK_PURPLE.setColor(ChatColor.DARK_PURPLE);
                    DARK_PURPLE.addEntry(item.getUniqueId().toString());
                } else {
                    board.getTeam("mLDARK_PURPLE").addEntry(item.getUniqueId().toString());
                }
                break;

            case "DARK_GREEN":
                if(board.getTeam("mLDARK_GREEN")==null) {
                    Team DARK_GREEN = board.registerNewTeam("mLDARK_GREEN");
                    DARK_GREEN.setColor(ChatColor.DARK_GREEN);
                    DARK_GREEN.addEntry(item.getUniqueId().toString());
                } else {
                    board.getTeam("mLDARK_GREEN").addEntry(item.getUniqueId().toString());
                }
                break;

            case "DARK_GRAY":
                if(board.getTeam("mLDARK_GRAY")==null) {
                    Team DARK_GRAY = board.registerNewTeam("mLDARK_GRAY");
                    DARK_GRAY.setColor(ChatColor.DARK_GRAY);
                    DARK_GRAY.addEntry(item.getUniqueId().toString());
                } else {
                    board.getTeam("mLDARK_GRAY").addEntry(item.getUniqueId().toString());
                }
                break;

            case "DARK_BLUE":
                if(board.getTeam("mLDARK_BLUE")==null) {
                    Team DARK_BLUE = board.registerNewTeam("mLDARK_BLUE");
                    DARK_BLUE.setColor(ChatColor.DARK_BLUE);
                    DARK_BLUE.addEntry(item.getUniqueId().toString());
                } else {
                    board.getTeam("mLDARK_BLUE").addEntry(item.getUniqueId().toString());
                }
                break;

            case "DARK_AQUA":
                if(board.getTeam("mLDARK_AQUA")==null) {
                    Team DARK_AQUA = board.registerNewTeam("mLDARK_AQUA");
                    DARK_AQUA.setColor(ChatColor.DARK_AQUA);
                    DARK_AQUA.addEntry(item.getUniqueId().toString());
                } else {
                    board.getTeam("mLDARK_AQUA").addEntry(item.getUniqueId().toString());
                }
                break;

            case "BLUE":
                if(board.getTeam("mLBLUE")==null) {
                    Team BLUE = board.registerNewTeam("mLBLUE");
                    BLUE.setColor(ChatColor.BLUE);
                    BLUE.addEntry(item.getUniqueId().toString());
                } else {
                    board.getTeam("mLBLUE").addEntry(item.getUniqueId().toString());
                }
                break;

            case "BLACK":
                if(board.getTeam("mLBLACK")==null) {
                    Team BLACK = board.registerNewTeam("mLBLACK");
                    BLACK.setColor(ChatColor.BLACK);
                    BLACK.addEntry(item.getUniqueId().toString());
                } else {
                    board.getTeam("mLBLACK").addEntry(item.getUniqueId().toString());
                }
                break;

            case "AQUA":
                if(board.getTeam("mLAQUA")==null) {
                    Team AQUA = board.registerNewTeam("mLAQUA");
                    AQUA.setColor(ChatColor.AQUA);
                    AQUA.addEntry(item.getUniqueId().toString());
                } else {
                    board.getTeam("mLAQUA").addEntry(item.getUniqueId().toString());
                }
                break;

            case "RED":
                if(board.getTeam("mLRED")==null) {
                    Team RED = board.registerNewTeam("mLRED");
                    RED.setColor(ChatColor.RED);
                    RED.addEntry(item.getUniqueId().toString());
                } else {
                    board.getTeam("mLRED").addEntry(item.getUniqueId().toString());
                }
                break;
        }



    }

    /*
    public void addTeam(String teamName, ChatColor color, Scoreboard board, Item item) {
        if(board.getTeam(teamName)==null) {
            Team DARK_AQUA = board.registerNewTeam(teamName);
            DARK_AQUA.setColor(ChatColor.DARK_AQUA);
            DARK_AQUA.addEntry(item.getUniqueId().toString());
        } else {
            board.getTeam(teamName).addEntry(item.getUniqueId().toString());
        }
        break;
    }

     */
    public static String randomColor() {
        List<String> colors = Arrays.asList("RED", "AQUA", "BLACK", "BLUE", "DARK_AQUA", "DARK_BLUE", "DARK_GRAY", "DARK_GREEN", "DARK_PURPLE", "DARK_RED", "GREEN", "LIGHT_PURPLE", "YELLOW" );
        Random rand = new Random();
        return colors.get(rand.nextInt(colors.size()));
    }

    public static String getColor(Item item, String color) {

        if(color.equals("display")) {
            if(item.getItemStack().getItemMeta().getDisplayName().startsWith("§0"))
                return "BLACK";
            else if (item.getItemStack().getItemMeta().getDisplayName().startsWith("§1"))
                return "DARK_BLUE";
            else if (item.getItemStack().getItemMeta().getDisplayName().startsWith("§2"))
                return "DARK_GREEN";
            else if (item.getItemStack().getItemMeta().getDisplayName().startsWith("§3"))
                return "DARK_AQUA";
            else if (item.getItemStack().getItemMeta().getDisplayName().startsWith("§4"))
                return "DARK_RED";
            else if (item.getItemStack().getItemMeta().getDisplayName().startsWith("§5"))
                return "DARK_PURPLE";
            else if (item.getItemStack().getItemMeta().getDisplayName().startsWith("§6"))
                return "GOLD";
            else if (item.getItemStack().getItemMeta().getDisplayName().startsWith("§7"))
                return "GRAY";
            else if (item.getItemStack().getItemMeta().getDisplayName().startsWith("§8"))
                return "DARK_GRAY";
            else if (item.getItemStack().getItemMeta().getDisplayName().startsWith("§9"))
                return "BLUE";
            else if (item.getItemStack().getItemMeta().getDisplayName().startsWith("§a"))
                return "GREEN";
            else if (item.getItemStack().getItemMeta().getDisplayName().startsWith("§b"))
                return "AQUA";
            else if (item.getItemStack().getItemMeta().getDisplayName().startsWith("§c"))
                return "RED";
            else if (item.getItemStack().getItemMeta().getDisplayName().startsWith("§d"))
                return "LIGHT_PURPLE";
            else if (item.getItemStack().getItemMeta().getDisplayName().startsWith("§e"))
                return "YELLOW";
        } else {
            return color.toUpperCase();
        }
        return null;
    }

    public static Color getRGB(String color) {
        if(color==null) return Color.fromRGB(255, 255, 255);
        switch(color) {

            case "YELLOW":
                return Color.fromRGB(255, 255, 85);

            case "LIGHT_PURPLE":
                return Color.fromRGB(255, 85, 255);

            case "GREEN":
                return Color.fromRGB(85, 255, 85);

            case "GOLD":
                return Color.fromRGB(255, 170, 0);

            case "DARK_RED":
                return Color.fromRGB(170, 0, 0);

            case "GRAY":
                return Color.fromRGB(128,128,128);

            case "DARK_PURPLE":
                return Color.fromRGB(170, 0, 170);

            case "DARK_GREEN":
                return Color.fromRGB(0, 170, 0);

            case "DARK_GRAY":
                return Color.fromRGB(85, 85, 85);

            case "DARK_BLUE":
                return Color.fromRGB(0, 0, 170);

            case "DARK_AQUA":
                return Color.fromRGB(0, 170, 170);

            case "BLUE":
                return Color.fromRGB(85, 85, 255);

            case "BLACK":
                return Color.fromRGB(0, 0, 0);

            case "AQUA":
                return Color.fromRGB(85, 255, 255);

            case "RED":
                return Color.fromRGB(255, 85, 85);
        }
        return Color.fromRGB(255, 255, 255);
    }

    public static java.awt.Color getAWTColor(String string) {
        switch (string.toLowerCase()) {
            case "black":
                return java.awt.Color.BLACK;
            case "blue":
                return java.awt.Color.BLUE;
            case "cyan":
                return java.awt.Color.CYAN;
            case "darkgray":
                return java.awt.Color.DARK_GRAY;
            case "gray":
                return java.awt.Color.GRAY;
            case "green":
                return java.awt.Color.GREEN;
            case "yellow":
                return java.awt.Color.YELLOW;
            case "lightgray":
                return java.awt.Color.LIGHT_GRAY;
            case "magneta":
                return java.awt.Color.MAGENTA;
            case "orange":
                return java.awt.Color.ORANGE;
            case "pink":
                return java.awt.Color.PINK;
            case "red":
                return java.awt.Color.RED;
            case "white":
                return java.awt.Color.WHITE;
        }
        return java.awt.Color.BLACK;
    }
}
