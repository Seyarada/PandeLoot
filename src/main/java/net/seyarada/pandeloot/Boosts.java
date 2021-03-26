package net.seyarada.pandeloot;

import java.util.HashMap;
import java.util.Map;

public class Boosts {

    private static int globalPower;
    private static long globalDuration;
    private static Map<String, Integer> playerPower = new HashMap<>();
    private static Map<String, Long> playerDuration = new HashMap<>();

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

    public static int getPlayerBoost(String player) {
        if(playerPower.containsKey(player) && System.currentTimeMillis()<playerDuration.get(player))
            return playerPower.get(player);
        return 1;
    }

}
