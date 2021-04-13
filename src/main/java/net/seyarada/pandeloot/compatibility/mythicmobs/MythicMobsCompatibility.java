package net.seyarada.pandeloot.compatibility.mythicmobs;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicDropLoadEvent;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMechanicLoadEvent;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobSpawnEvent;
import io.lumine.xikage.mythicmobs.drops.Drop;
import io.lumine.xikage.mythicmobs.io.MythicConfig;
import io.lumine.xikage.mythicmobs.items.MythicItem;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import net.seyarada.pandeloot.damage.DamageTracker;
import net.seyarada.pandeloot.damage.DamageUtil;
import net.seyarada.pandeloot.damage.MobOptions;
import net.seyarada.pandeloot.drops.DropManager;
import net.seyarada.pandeloot.nms.NMSManager;
import net.seyarada.pandeloot.rewards.RewardLine;
import net.seyarada.pandeloot.utils.ChatUtil;
import org.bukkit.entity.Player;
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
        UUID mob = e.getEntity().getUniqueId();

        // Don't drop if the mob isn't loaded or nobody has damaged it
        if(!DamageTracker.loadedMobs.containsKey(mob)) return;
        DamageTracker.loadedMobs.remove(mob);
        if(!DamageTracker.damageTracker.containsKey(mob)) return;
        if(DamageTracker.get(mob).size()==0) return;

        MythicConfig config = e.getMobType().getConfig();
        boolean rank = config.getBoolean("Options.ScoreMessage");
        boolean score = config.getBoolean("Options.ScoreHologram");

        List<String> strings = e.getMobType().getConfig().getStringList("Rewards");
        List<RewardLine> rewards = RewardLine.StringListToRewardList(strings);

        DamageUtil damageUtil = new DamageUtil(mob);
        if(e.getKiller() instanceof Player) damageUtil.lastHit = (Player) e.getKiller();
        DropManager manager = new DropManager(Arrays.asList(damageUtil.getPlayers()), rewards);

        if(rank) ChatUtil.announceChatRank(damageUtil);
        if(score) NMSManager.spawnHologram(damageUtil);
        manager.setDamageUtil(damageUtil);
        manager.initDrops();
    }

    @EventHandler
    public void onSpawn(MythicMobSpawnEvent e) {
        MythicConfig mobConfig = e.getMobType().getConfig();
        boolean shouldStore = mobConfig.getList("Rewards")!=null&&mobConfig.getList("Rewards").size()>0;
        if(shouldStore) {
            MobOptions mobOptions = new MobOptions();
            mobOptions.resetPlayers = mobConfig.getBoolean("Options.ResetPlayers");
            mobOptions.resetHeal = mobConfig.getBoolean("Options.ResetHeal");
            DamageTracker.loadedMobs.put(e.getEntity().getUniqueId(), mobOptions);
        }
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
