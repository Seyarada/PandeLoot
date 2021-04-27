package net.seyarada.pandeloot.compatibility.mythicmobs;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedLocationSkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import net.seyarada.pandeloot.damage.DamageTracker;
import net.seyarada.pandeloot.damage.DamageUtil;
import net.seyarada.pandeloot.drops.DropManager;
import net.seyarada.pandeloot.rewards.RewardLine;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.Collections;

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

        RewardLine lineConfig = new RewardLine(i);
        Location location = BukkitAdapter.adapt(abstractLocation);
        new DropManager(location, Collections.singletonList(lineConfig)).initDrops();
        return false;
    }

    @Override
    public boolean castAtEntity(SkillMetadata skillMetadata, AbstractEntity abstractEntity) {
        String i = config.getLine();
        int j = i.indexOf("{")+1;
        int k = i.lastIndexOf("}");
        i = i.substring(j, k);


        RewardLine lineConfig = new RewardLine(i);
        Entity entity = BukkitAdapter.adapt(abstractEntity);
        if(entity instanceof Player) {
            DropManager manager = new DropManager((Player) entity, BukkitAdapter.adapt(skillMetadata.getCaster().getLocation()), Collections.singletonList(lineConfig));
            if(DamageTracker.loadedMobs.containsKey(skillMetadata.getCaster().getEntity().getUniqueId())) {
                DamageUtil util = new DamageUtil(skillMetadata.getCaster().getEntity().getUniqueId());
                manager.setDamageUtil(util);
            }
            manager.initDrops();
        } else {
            DropManager manager = new DropManager(entity.getLocation(), Collections.singletonList(lineConfig));
            if(DamageTracker.loadedMobs.containsKey(skillMetadata.getCaster().getEntity().getUniqueId())) {
                DamageUtil util = new DamageUtil(skillMetadata.getCaster().getEntity().getUniqueId());
                manager.setDamageUtil(util);
            }
            manager.initDrops();
        }
        return false;
    }
}