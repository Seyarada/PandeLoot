package net.seyarada.pandeloot.options;

import net.seyarada.pandeloot.rewards.Reward;
import net.seyarada.pandeloot.utils.PlaceholderUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Options {

    public static Map<String, MechanicEvent> listeners = new HashMap<>();
    public static Map<String, OptionType> types = new HashMap<>();

    public static void registerOption(MechanicEvent listener, OptionType type, String... options) {
        for(String option : options) {
            option = option.toLowerCase();

            listeners.put(option, listener);
            types.put(option, type);
        }
    }

    public static void callOptions(Reward reward) {
        reward.options.replaceAll( (k,v)->v= PlaceholderUtil.parse(v, reward.damageUtil, reward.player, false));
        if(Boolean.parseBoolean(reward.options.get("playonpickup"))) {
            if(reward.item!=null)
                callOptions(reward, OptionType.ITEM);
            return;
        }

        callOptions(reward, OptionType.GENERAL);
        if(reward.item!=null)
            callOptions(reward, OptionType.ITEM);
        if(reward.player!=null)
            callOptions(reward, OptionType.PLAYER);
    }

    public static void callOptions(Reward reward, OptionType executeOption) {
        Set<String> options = reward.options.keySet();
        for(String option : options) {
            option = option.toLowerCase();

            final MechanicEvent listener = listeners.get(option);
            if(listener==null) continue;

            final OptionType type = types.get(option);
            if(type!=executeOption) continue;

            listener.onCall(reward, reward.options.get(option));
        }
    }

}

