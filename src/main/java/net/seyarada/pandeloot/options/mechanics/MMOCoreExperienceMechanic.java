package net.seyarada.pandeloot.options.mechanics;

import net.seyarada.pandeloot.StringLib;
import net.seyarada.pandeloot.compatibility.MMOCoreCompatibility;
import net.seyarada.pandeloot.options.MechanicEvent;
import net.seyarada.pandeloot.rewards.Reward;

public class MMOCoreExperienceMechanic implements MechanicEvent {
    @Override
    public void onCall(Reward reward, String value) {
        if (value!=null && !value.isEmpty()) {
            final int experience = Integer.parseInt(value);

            if(experience>0) {
                StringLib.warn("++++++ Applying mmocoreexp effect with value "+value);
                MMOCoreCompatibility.exp(reward.player, experience);
            }
        }
    }
}