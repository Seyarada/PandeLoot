package net.seyarada.pandeloot.compatibility.mythicmobs;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractPlayer;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicDropLoadEvent;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMechanicLoadEvent;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobSpawnEvent;
import io.lumine.xikage.mythicmobs.drops.Drop;
import io.lumine.xikage.mythicmobs.drops.DropMetadata;
import io.lumine.xikage.mythicmobs.drops.DropTable;
import io.lumine.xikage.mythicmobs.io.MythicConfig;
import io.lumine.xikage.mythicmobs.items.MythicItem;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import net.seyarada.pandeloot.damage.DamageTracker;
import net.seyarada.pandeloot.damage.DamageUtil;
import net.seyarada.pandeloot.damage.MobOptions;
import net.seyarada.pandeloot.drops.Manager;
import net.seyarada.pandeloot.drops.StartDrops;
import net.seyarada.pandeloot.nms.NMSManager;
import net.seyarada.pandeloot.options.Reward;
import net.seyarada.pandeloot.rewards.RewardLineNew;
import net.seyarada.pandeloot.utils.ChatUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class MythicMobsCompatibility implements Listener {

    public static ItemStack getItem(String item) {
        Optional<MythicItem> mI = MythicMobs.inst().getItemManager().getItem(item);
        if (mI.isPresent())
            return BukkitAdapter.adapt(mI.get().generateItemStack(1));

        return null;
    }

    public static Map.Entry<Collection<Drop>, DropMetadata> getDropTableDrops(Reward i) {
        DropTable dropTable;
        Optional<DropTable> maybeTable = MythicMobs.inst().getDropManager().getDropTable(i.rewardLine.item);
        if(maybeTable.isPresent()) {
            dropTable = maybeTable.get();
            DropMetadata dropMeta;
            if(i.player==null && i.damageUtil==null) {
                dropMeta = null;
            } else if (i.player==null) {
                AbstractEntity entity = BukkitAdapter.adapt(i.damageUtil.entity);
                ActiveMob caster = MythicMobs.inst().getMobManager().getMythicMobInstance(entity);
                dropMeta = new DropMetadata(caster, null);
            } else if(i.damageUtil==null) {
                AbstractPlayer p = BukkitAdapter.adapt(i.player);
                dropMeta = new DropMetadata(null, p);
            } else {
                AbstractPlayer p = BukkitAdapter.adapt(i.player);
                AbstractEntity entity = BukkitAdapter.adapt(i.damageUtil.entity);
                ActiveMob caster = MythicMobs.inst().getMobManager().getMythicMobInstance(entity);
                dropMeta = new DropMetadata(caster, p);
            }
            Collection<Drop> drops = dropTable.generate(dropMeta).getDrops();
            return new AbstractMap.SimpleImmutableEntry<>(drops, dropMeta);
        }
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

        DamageUtil damageUtil = new DamageUtil(mob);
        if(e.getKiller() instanceof Player) damageUtil.lastHit = (Player) e.getKiller();
        new StartDrops(Arrays.asList(damageUtil.getPlayers()), strings, damageUtil, e.getEntity().getLocation());

        if(rank) ChatUtil.announceChatRank(damageUtil);
        if(score) NMSManager.spawnHologram(damageUtil);
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
