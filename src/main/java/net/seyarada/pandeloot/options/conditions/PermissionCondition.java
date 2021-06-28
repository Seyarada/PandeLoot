package net.seyarada.pandeloot.options.conditions;

import net.seyarada.pandeloot.options.ConditionEvent;
import net.seyarada.pandeloot.rewards.Reward;

public class PermissionCondition implements ConditionEvent {
    @Override
    public boolean onCall(Reward reward, String value) {
        if(value==null) return true;

        return reward.player.hasPermission(value);
    }
}
