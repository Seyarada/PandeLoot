package net.seyarada.pandeloot.options.mechanics;

import net.seyarada.pandeloot.StringLib;
import net.seyarada.pandeloot.options.MechanicEvent;
import net.seyarada.pandeloot.options.Reward;

public class SoundMechanic implements MechanicEvent {
    @Override
    public void onCall(Reward reward, String value) {
        StringLib.warn("+ Test sound effect with value "+value);
        if (value!=null && !value.isEmpty()) {
            StringLib.warn("++++++ Applying sound effect with value "+value);
            reward.player.playSound(reward.player.getLocation(), value, 1, 1);
        }
    }
}
