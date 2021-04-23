package net.seyarada.pandeloot.compatibility;

import net.Indyuce.mmocore.api.experience.EXPSource;
import net.Indyuce.mmocore.api.player.PlayerData;
import org.bukkit.entity.Player;

public class MMOCoreCompatibility {

    public static void exp(Player player, int amount) {
        PlayerData pData = PlayerData.get(player);
        pData.giveExperience(amount, EXPSource.COMMAND);
    }

}
