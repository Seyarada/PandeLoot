package net.seyarada.pandeloot.options;

import net.seyarada.pandeloot.rewards.Reward;

public interface MechanicEvent {
    void onCall(Reward reward, String value);
}
