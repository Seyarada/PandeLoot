package net.seyarada.pandeloot.options;

public interface ConditionEvent {
    boolean onCall(Reward reward, String value);
}