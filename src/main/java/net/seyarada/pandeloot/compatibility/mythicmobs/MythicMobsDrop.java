package net.seyarada.pandeloot.compatibility.mythicmobs;

import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitItemStack;
import io.lumine.xikage.mythicmobs.drops.Drop;
import io.lumine.xikage.mythicmobs.drops.DropMetadata;
import io.lumine.xikage.mythicmobs.drops.IMultiDrop;
import io.lumine.xikage.mythicmobs.drops.LootBag;
import io.lumine.xikage.mythicmobs.drops.droppables.ItemDrop;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import net.seyarada.pandeloot.damage.DamageTracker;
import net.seyarada.pandeloot.damage.DamageUtil;
import net.seyarada.pandeloot.drops.DropManager;
import net.seyarada.pandeloot.rewards.RewardLine;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;

public class MythicMobsDrop extends Drop implements IMultiDrop, Listener {

    private final MythicLineConfig config;

    public MythicMobsDrop(MythicLineConfig config) {
        super(config.getLine(), config);
        this.config = config;
    }

    @Override
    public LootBag get(DropMetadata metadata) {
        String i = config.getLine();
        int j = i.indexOf("{")+1;
        int k = i.lastIndexOf("}");
        i = i.substring(j, k);

        RewardLine lineConfig = new RewardLine(i);
        Location location = BukkitAdapter.adapt(metadata.getDropper().get().getLocation());
        Player p = (Player) BukkitAdapter.adapt(metadata.getCause().get());
        DropManager manager = new DropManager(p, location, Collections.singletonList(lineConfig));
        if(DamageTracker.loadedMobs.containsKey(metadata.getDropper().get().getEntity().getUniqueId())) {
            DamageUtil util = new DamageUtil(metadata.getDropper().get().getEntity().getUniqueId());
            manager.setDamageUtil(util);
        }
        manager.initDrops();

        // We don't want to drop anything with MythicMobs
        final LootBag loot = new LootBag(metadata);
        ItemStack item = new ItemStack(Material.AIR, 1);
        loot.add(new ItemDrop(this.getLine(), (MythicLineConfig) this.getConfig(), new BukkitItemStack(item)));
        return loot;
    }

}