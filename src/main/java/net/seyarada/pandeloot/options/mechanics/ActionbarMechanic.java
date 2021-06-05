package net.seyarada.pandeloot.options.mechanics;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.seyarada.pandeloot.StringLib;
import net.seyarada.pandeloot.options.MechanicEvent;
import net.seyarada.pandeloot.options.Reward;
import org.bukkit.entity.Player;

public class ActionbarMechanic implements MechanicEvent {
    @Override
    public void onCall(Reward reward, String value) {
        if (value != null && !value.isEmpty()) {

            StringLib.warn("++++++ Applying actionBar effect with value "+value);
            final Player.Spigot spigotPlayer = reward.player.spigot();
            final BaseComponent[] text = TextComponent.fromLegacyText(value);

            spigotPlayer.sendMessage(ChatMessageType.ACTION_BAR, text);
        }
    }
}