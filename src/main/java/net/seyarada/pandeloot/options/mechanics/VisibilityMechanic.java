package net.seyarada.pandeloot.options.mechanics;

import net.seyarada.pandeloot.StringLib;
import net.seyarada.pandeloot.options.MechanicEvent;
import net.seyarada.pandeloot.options.Reward;
import net.seyarada.pandeloot.schedulers.HideEntity;

import java.util.Arrays;
import java.util.Collections;

public class VisibilityMechanic implements MechanicEvent {
    @Override
    public void onCall(Reward reward, String value) {
        StringLib.warn("++++++ Applying visibility effect with value "+value);
        if(value.equalsIgnoreCase("player")) new HideEntity(reward.item, Collections.singletonList(reward.player));
        else if(value.equalsIgnoreCase("fight")) new HideEntity(reward.item, Arrays.asList(reward.damageUtil.getPlayers()));
    }
}
