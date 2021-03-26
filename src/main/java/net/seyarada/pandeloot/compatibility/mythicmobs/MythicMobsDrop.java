package net.seyarada.pandeloot.compatibility.mythicmobs;

import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.drops.Drop;
import io.lumine.xikage.mythicmobs.drops.DropMetadata;
import io.lumine.xikage.mythicmobs.drops.IMultiDrop;
import io.lumine.xikage.mythicmobs.drops.LootBag;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import net.seyarada.pandeloot.rewards.RewardLine;
import org.bukkit.Location;
import org.bukkit.event.Listener;

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
        //new DropManager().doDrop(location, lineConfig);

        // We don't want to drop anything with MythicMobs
        final LootBag loot = new LootBag(metadata);
        //ItemStack item = new ItemStack(Material.AIR, 1);
        //loot.add(new ItemDrop(this.getLine(), (MythicLineConfig) this.getConfig(), new BukkitItemStack(item)));
        return loot;
    }

}