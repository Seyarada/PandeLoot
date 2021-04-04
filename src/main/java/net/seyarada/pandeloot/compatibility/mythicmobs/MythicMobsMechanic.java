package net.seyarada.pandeloot.compatibility.mythicmobs;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedLocationSkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
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
        //new DropManager().doDrop(location, lineConfig);
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
        if(entity instanceof Player)
            new DropManager((Player) entity, BukkitAdapter.adapt(skillMetadata.getCaster().getLocation()), Collections.singletonList(lineConfig)).initDrops();
        //else
        //    new DropManager().doDrop(entity.getLocation(), lineConfig);
        return false;
    }
}