package net.seyarada.pandeloot;

import net.seyarada.pandeloot.rewards.RewardLine;

public class Errors {

    public static void UnableToGenerateItemStack(RewardLine reward) {
        System.err.println("[PandeLoot] Couldn't create the item -"+reward.getItem()+"- from origin "+reward.getOrigin());
    }

    public static void UnableToFindLootTable(String lootTable) {
        System.err.println("[PandeLoot] Couldn't find the LootTable -"+lootTable+"- !");
    }

}
