package net.seyarada.pandeloot.items;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.seyarada.pandeloot.Config;
import net.seyarada.pandeloot.StringLib;
import net.seyarada.pandeloot.compatibility.mythicmobs.UnpackDropTable;
import net.seyarada.pandeloot.options.Conditions;
import net.seyarada.pandeloot.options.Options;
import net.seyarada.pandeloot.rewards.Reward;
import net.seyarada.pandeloot.rewards.RewardContainer;
import net.seyarada.pandeloot.rewards.RewardLine;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

public class ItemUtils {

    public static void collectRewards(List<Reward> rewardsToCollect, List<Reward> store, int playerSize, boolean applyConditions) {
        if(applyConditions) Conditions.filter(rewardsToCollect);

        StringLib.warn("++++ Base rewards filtering completed");

        for(Reward i : rewardsToCollect) {
            StringLib.warn("+++++ Starting unpacking of "+i.rewardLine.baseLine+" "+i.rewardLine.origin+" "+i.rewardLine.item);
            switch(i.rewardLine.origin) {
                case "loottable":
                case "lt":
                    Options.callOptions(i);
                    StringLib.warn("++++++ Unpacking loottable");
                    StringLib.warn("++++++++++ ================");
                    StringLib.depthBonus++;
                    final RewardContainer rewardContainer = new RewardContainer(i.rewardLine.item, i);
                    List<RewardLine> rewardLines = rewardContainer.getDrops();
                    List<Reward> rewards = i.createNewRewards(rewardLines);
                    collectRewards(rewards, store, playerSize, false);
                    StringLib.depthBonus--;
                    StringLib.warn("++++++++++ ================");
                    break;
                case "droptable":
                case "dt":
                    if (Bukkit.getServer().getPluginManager().getPlugin("MythicMobs") == null) {
                        new UnpackDropTable(i, store);
                    }
                    StringLib.depthBonus--;
                    StringLib.warn("++++++++++ ================");
                    break;
                case "lootbag":
                case "lb":
                    StringLib.warn("++++++ Unpacking lootbag");
                    final ConfigurationSection configSection = Config.getRewardContainer(i.rewardLine.item);
                    RewardContainer.addRewardContainerOptions(i, configSection);
                default:
                    if(Boolean.parseBoolean(i.get("shared"))) {
                        i.rewardLine.chance = String.valueOf(1d/playerSize);
                    }
                    StringLib.warn("++++++ Collected "+i.rewardLine.baseLine);
                    store.add(i);
            }
        }
    }

    public static ItemStack getCustomTextureHead(String value) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) head.getItemMeta();

        if(value==null) {
            meta.setOwner("PandemoniumHK");
            head.setItemMeta(meta);
            return head;
        }

        GameProfile profile = new GameProfile(UUID.randomUUID(), "");
        profile.getProperties().put("textures", new Property("textures", value));
        Field profileField;  // TODO Reflection is ugly, change this in the future please
        try {
            profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        head.setItemMeta(meta);
        return head;
    }

}
