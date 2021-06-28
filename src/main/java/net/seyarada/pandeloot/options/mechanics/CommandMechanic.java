package net.seyarada.pandeloot.options.mechanics;

import net.seyarada.pandeloot.StringLib;
import net.seyarada.pandeloot.options.MechanicEvent;
import net.seyarada.pandeloot.rewards.Reward;
import org.bukkit.Bukkit;

public class CommandMechanic implements MechanicEvent {
    @Override
    public void onCall(Reward reward, String value) {
        if (value!=null && !value.isEmpty()) {
            StringLib.warn("++++++ Applying command effect with value "+value);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), value);
        }
    }
}
