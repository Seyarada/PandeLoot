package net.seyarada.pandeloot.options.mechanics;

import net.seyarada.pandeloot.StringLib;
import net.seyarada.pandeloot.options.MechanicEvent;
import net.seyarada.pandeloot.options.Reward;
import org.bukkit.EntityEffect;

public class TotemMechanic implements MechanicEvent {
    @Override
    public void onCall(Reward reward, String value) {
        if (Boolean.parseBoolean(value)) {
            StringLib.warn("++++++ Applying totem effect with value "+value);
            reward.player.playEffect(EntityEffect.TOTEM_RESURRECT);
        }
    }
}
