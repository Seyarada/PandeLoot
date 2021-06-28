package net.seyarada.pandeloot.options;

import net.seyarada.pandeloot.rewards.Reward;

public interface ConditionEvent {
    boolean onCall(Reward reward, String value);
}