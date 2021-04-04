package net.seyarada.pandeloot;

import java.util.HashMap;
import java.util.Map;

public class Boosts {

    private static int globalPower;
    private static long globalDuration;
    public static Map<String, Integer> playerPower = new HashMap<>();
    public static Map<String, Long> playerDuration = new HashMap<>();

    public static void setGlobalBoost(int power, long duration) {

        globalPower = power;
        globalDuration = duration;

    }

    public static void setPlayerBoost(int power, long duration, String player) {

        playerPower.put(player, power);
        playerDuration.put(player, duration);

    }

    public static int getGlobalBoost() {
        if(System.currentTimeMillis()<globalDuration)
            return globalPower;
        return 1;
    }

    public static long getGlobalDuration() {
        return  globalDuration-System.currentTimeMillis();
    }

    public static int getPlayerBoost(String player) {
        if(playerPower.containsKey(player) && System.currentTimeMillis()<playerDuration.get(player))
            return playerPower.get(player);
        return 1;
    }

    public static void terminateGlobal() {
        globalPower = 1;
        globalDuration = 0;
    }

    public static void terminatePlayer(String i) {
        playerPower.remove(i);
        playerDuration.remove(i);
    }

}
