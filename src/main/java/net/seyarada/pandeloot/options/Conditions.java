package net.seyarada.pandeloot.options;

import net.seyarada.pandeloot.StringLib;

import java.util.*;

public class Conditions {

    public static Map<String, ConditionEvent> listeners = new HashMap<>();
    public static List<ConditionEvent> globalConditions = new ArrayList<>();

    public static void registerOption(ConditionEvent listener, String... options) {
        for(String option : options) {
            option = option.toLowerCase();

            listeners.put(option, listener);
        }
    }

    public static void registerOption(ConditionEvent listener) {
        globalConditions.add(listener);
    }

    public static void filter(List<Reward> rewards) {
        StringLib.warn("++++ Starting reward filtering");
        rewards.removeIf(i -> Conditions.getResult(i) && !i.skipConditions);
    }

    public static boolean getResult(Reward reward) {
        Set<String> options = reward.options.keySet();

        StringLib.warn("+++++ Checking conditions for "+reward.rewardLine.baseLine);

        for(String option : options) {
            option = option.toLowerCase();

            final ConditionEvent listener = listeners.get(option);
            if(listener==null) continue;

            if(!listener.onCall(reward, reward.options.get(option)))
                return true;
        }

        // What's this supposed to do??
        for(ConditionEvent listener : globalConditions) {
            if(listener==null) continue;

            if(!listener.onCall(reward, null))
                return true;
        }

        return false;
    }


}
