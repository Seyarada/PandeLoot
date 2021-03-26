package net.seyarada.pandeloot.compatibility.mythicmobs;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicDropLoadEvent;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMechanicLoadEvent;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
import io.lumine.xikage.mythicmobs.drops.Drop;
import io.lumine.xikage.mythicmobs.io.MythicConfig;
import io.lumine.xikage.mythicmobs.items.MythicItem;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import net.seyarada.pandeloot.damage.DamageUtil;
import net.seyarada.pandeloot.drops.DropManager;
import net.seyarada.pandeloot.rewards.RewardLine;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MythicMobsCompatibility implements Listener {

    public static ItemStack getItem(String item) {
        Optional<MythicItem> mI = MythicMobs.inst().getItemManager().getItem(item);
        if (mI.isPresent())
            return BukkitAdapter.adapt(mI.get().generateItemStack(1));
        return null;
    }

    @EventHandler
    public void onDeath(MythicMobDeathEvent e) {
        System.out.println(1);
        UUID mob = e.getEntity().getUniqueId();

        MythicConfig config = e.getMobType().getConfig();
        boolean rank = config.getBoolean("Options.AnnounceRank");
        boolean score = config.getBoolean("Options.AnnounceScore");

        //if(rank) ChatUtils.announceChatRank(mob);
        //if(score) NMSManager.spawnHologram(mob);

        List<String> strings = e.getMobType().getConfig().getStringList("Rewards");
        List<RewardLine> rewards = RewardLine.StringListToRewardList(strings);

        DamageUtil damageUtil = new DamageUtil(mob);
        DropManager manager = new DropManager(Arrays.asList(damageUtil.getPlayers()), rewards);

        manager.setDamageUtil(damageUtil);
        manager.initDrops();
    }

    @EventHandler
    public void onMythicMechanicLoad(MythicMechanicLoadEvent event)	{
        if(event.getMechanicName().equalsIgnoreCase("PANDELOOT"))	{
            SkillMechanic mechanic = new MythicMobsMechanic(event.getConfig());
            event.register(mechanic);
        }
    }

    @EventHandler
    public void onMythicDropLoad(MythicDropLoadEvent event)	{
        if(event.getDropName().equalsIgnoreCase("PANDELOOT"))	{
            Drop drop = new MythicMobsDrop(event.getConfig());
            event.register(drop);
        }
    }

}
