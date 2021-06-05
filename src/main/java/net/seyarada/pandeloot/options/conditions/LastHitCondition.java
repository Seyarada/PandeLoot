package net.seyarada.pandeloot.options.conditions;

import net.seyarada.pandeloot.options.ConditionEvent;
import net.seyarada.pandeloot.options.Reward;

public class LastHitCondition implements ConditionEvent {
    @Override
    public boolean onCall(Reward reward, String value) {
        if(reward.damageUtil==null) return true;

        boolean lastHit = Boolean.parseBoolean(value);
        if(lastHit)
            return reward.player == reward.damageUtil.lastHit;
        return true;
    }
}
