package net.seyarada.pandeloot.options.mechanics;

import net.seyarada.pandeloot.StringLib;
import net.seyarada.pandeloot.options.MechanicEvent;
import net.seyarada.pandeloot.rewards.Reward;

public class RemoveMechanic implements MechanicEvent {
    @Override
    public void onCall(Reward reward, String value) {
        StringLib.warn("++++++ Remove value is "+value);
        if (value!=null && !value.isEmpty() && Boolean.parseBoolean(value) && reward.item!=null) {
            StringLib.warn("++++++ Applying remove effect with value "+value);
            reward.item.remove();
        }
    }
}
