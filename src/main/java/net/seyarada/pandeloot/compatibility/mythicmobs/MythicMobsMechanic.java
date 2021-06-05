package net.seyarada.pandeloot.compatibility.mythicmobs;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedLocationSkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import net.seyarada.pandeloot.rewards.RewardLineNew;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.Listener;

public class MythicMobsMechanic extends SkillMechanic implements ITargetedLocationSkill, ITargetedEntitySkill, Listener {


    public MythicMobsMechanic(MythicLineConfig config) {
        super(config.getLine(), config);
        this.setAsyncSafe(false);
        this.setTargetsCreativePlayers(false);
    }

    @Override
    public boolean castAtLocation(SkillMetadata skillMetadata, AbstractLocation abstractLocation) {
        String i = config.getLine();
        int j = i.indexOf("{")+1;
        int k = i.lastIndexOf("}");
        i = i.substring(j, k);

        RewardLineNew lineConfig = new RewardLineNew(i);
        Location location = BukkitAdapter.adapt(abstractLocation);
        //new DropManager(location, Collections.singletonList(lineConfig)).initDrops();

        //new Manager().RewardLine(Collections.singletonList(p), Collections.singletonList(lineConfig), new DamageUtil(uuid), location);

        return false;
    }

    @Override
    public boolean castAtEntity(SkillMetadata skillMetadata, AbstractEntity abstractEntity) {
        String i = config.getLine();
        int j = i.indexOf("{")+1;
        int k = i.lastIndexOf("}");
        i = i.substring(j, k);

        //if(entity instanceof Player) {
        //    manager = new DropManager((Player) entity, BukkitAdapter.adapt(skillMetadata.getCaster().getLocation()), Collections.singletonList(lineConfig));
        // } else {
        //    manager = new DropManager(entity.getLocation(), Collections.singletonList(lineConfig));
        // }
        //if(DamageTracker.loadedMobs.containsKey(skillMetadata.getCaster().getEntity().getUniqueId())) {
        //    manager.damageUtil = new DamageUtil(skillMetadata.getCaster().getEntity().getUniqueId());
        //}
        //manager.initDrops();
        return false;
    }
}