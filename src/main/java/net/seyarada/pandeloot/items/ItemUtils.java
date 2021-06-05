package net.seyarada.pandeloot.items;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import io.lumine.xikage.mythicmobs.adapters.AbstractPlayer;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.drops.Drop;
import io.lumine.xikage.mythicmobs.drops.DropMetadata;
import io.lumine.xikage.mythicmobs.drops.IIntangibleDrop;
import io.lumine.xikage.mythicmobs.drops.IItemDrop;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import net.seyarada.pandeloot.Config;
import net.seyarada.pandeloot.StringLib;
import net.seyarada.pandeloot.compatibility.mythicmobs.MythicMobsCompatibility;
import net.seyarada.pandeloot.damage.DamageUtil;
import net.seyarada.pandeloot.options.Conditions;
import net.seyarada.pandeloot.options.Options;
import net.seyarada.pandeloot.options.Reward;
import net.seyarada.pandeloot.rewards.RewardContainerNew;
import net.seyarada.pandeloot.rewards.RewardLineNew;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.*;

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
                    final RewardContainerNew rewardContainer = new RewardContainerNew(i.rewardLine.item, i);
                    List<RewardLineNew> rewardLines = rewardContainer.getDrops();
                    List<Reward> rewards = i.createNewRewards(rewardLines);
                    collectRewards(rewards, store, playerSize, false);
                    StringLib.depthBonus--;
                    StringLib.warn("++++++++++ ================");
                    break;
                case "droptable":
                case "dt":
                    StringLib.warn("++++++ Unpacking droptable");
                    StringLib.warn("++++++++++ ================");
                    StringLib.depthBonus++;
                    Map.Entry<Collection<Drop>, DropMetadata> pair = MythicMobsCompatibility.getDropTableDrops(i);
                    if(pair==null) break;

                    for (Drop drop : pair.getKey()) {
                        final RewardLineNew rewardLine = new RewardLineNew(drop.getLine());
                        rewardLine.generateInsideOptions(i.rewardLine.baseLine);
                        rewardLine.chance = "1"; // MM already does the chance check on their side

                        final Reward reward = new Reward(rewardLine, i.player, i.damageUtil);

                        if (drop instanceof IItemDrop) {
                            reward.itemStack = BukkitAdapter.adapt(((IItemDrop) drop).getDrop(pair.getValue()));
                            reward.rewardLine.amount = reward.itemStack.getAmount();
                        } else if(drop instanceof IIntangibleDrop) {
                            final AbstractPlayer abstractPlayer = BukkitAdapter.adapt(i.player);
                            ((IIntangibleDrop) drop).giveDrop(abstractPlayer, new DropMetadata(null, abstractPlayer));
                            continue;
                        }
                        store.add(reward);
                    }
                    StringLib.depthBonus--;
                    StringLib.warn("++++++++++ ================");
                    break;
                case "lootbag":
                case "lb":
                    StringLib.warn("++++++ Unpacking lootbag");
                    final ConfigurationSection configSection = Config.getRewardContainer(i.rewardLine.item);
                    RewardContainerNew.addRewardContainerOptions(i, configSection);
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
