package net.seyarada.pandeloot.options.mechanics;

import net.seyarada.pandeloot.StringLib;
import net.seyarada.pandeloot.compatibility.DiscordSRVCompatibility;
import net.seyarada.pandeloot.options.MechanicEvent;
import net.seyarada.pandeloot.rewards.Reward;

import java.util.Map;

public class DiscordMechanic implements MechanicEvent {
    @Override
    public void onCall(Reward reward, String value) {
        StringLib.warn("++++++ Applying discord effect with value "+value);
        if(value!=null && !value.isEmpty()) {
            final Map<String, String> options = reward.options;
            final String title = options.get("dtitle");
            final String message = options.get("dmessage");
            final String color = options.get("dcolor");
            final String link = options.get("dlink");
            final boolean avatar = Boolean.parseBoolean(options.get("davatar"));

            DiscordSRVCompatibility.embed(reward.player, title, message, value, color, link, avatar);
        }
    }
}
