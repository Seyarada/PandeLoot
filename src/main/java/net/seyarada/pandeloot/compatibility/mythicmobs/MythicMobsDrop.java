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
import net.seyarada.pandeloot.drops.Manager;
import net.seyarada.pandeloot.rewards.RewardLineNew;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.UUID;

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

        RewardLineNew lineConfig = new RewardLineNew(i);
        Location location = BukkitAdapter.adapt(metadata.getDropper().get().getLocation());
        Player p = (Player) BukkitAdapter.adapt(metadata.getCause().get());

        UUID uuid = metadata.getDropper().get().getEntity().getUniqueId();
        if(DamageTracker.loadedMobs.containsKey(uuid))
            new Manager().fromRewardLine(Collections.singletonList(p), Collections.singletonList(lineConfig), new DamageUtil(uuid), location);


        // We don't want to drop anything with MythicMobs
        final LootBag loot = new LootBag(metadata);
        ItemStack item = new ItemStack(Material.AIR, 1);
        loot.add(new ItemDrop(this.getLine(), (MythicLineConfig) this.getConfig(), new BukkitItemStack(item)));
        return loot;
    }

}