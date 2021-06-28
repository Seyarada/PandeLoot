package net.seyarada.pandeloot.items;

import net.seyarada.pandeloot.StringLib;
import net.seyarada.pandeloot.drops.Manager;
import net.seyarada.pandeloot.rewards.Reward;
import net.seyarada.pandeloot.rewards.RewardContainer;
import net.seyarada.pandeloot.rewards.RewardLine;
import net.seyarada.pandeloot.schedulers.BalloonScheduler;
import net.seyarada.pandeloot.utils.MathUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

import static net.seyarada.pandeloot.items.ItemUtils.collectRewards;

public class LootBalloon extends RewardContainer implements Listener {

    final public static Map<UUID, RewardContainer> balloonRegistry = new HashMap<>();
    final public static Map<UUID, UUID> leashRegistry = new HashMap<>();
    final public static Map<UUID, UUID> headRegistry = new HashMap<>();

    public LootBalloon(String internalName, Reward reward) {
        super(internalName, reward);
    }

    public void spawnBalloon(Location location) {
        // This is awful
        final String skullID = rewardContainer.getString("SkullID");
        final ItemStack iS = ItemUtils.getCustomTextureHead(skullID);
        final World world = location.getWorld();

        location = location.clone();

        if(rewardContainer.getDouble("Noise")>0) {
            location.add(MathUtil.getVelocity(rewardContainer.getDouble("Noise"), 0));
        }

        Location giantLocation = location.clone().add(-2,3,-4.5);
        giantLocation.setPitch(0);
        giantLocation.setYaw(0);

        final PotionEffect effect = new PotionEffect(PotionEffectType.LEVITATION, 600, 1);
        final Entity balloon = world.spawnEntity(giantLocation, EntityType.GIANT);
        final Entity balloonPoint = world.spawnEntity(location.clone().add(0,6,0), EntityType.SLIME);
        final Entity balloonLeash = world.spawnEntity(location.clone().add(0,1.5,0), EntityType.CAT);
        final LivingEntity livingBalloon = (LivingEntity) balloon;
        final LivingEntity livingPoint = (LivingEntity) balloonPoint;
        final LivingEntity livingLeash = (LivingEntity) balloonLeash;

        balloon.setCustomName("Dinnerbone");
        balloon.setSilent(true);
        balloon.setInvulnerable(true);
        livingBalloon.setInvisible(true);
        livingBalloon.getEquipment().clear();
        livingBalloon.addPotionEffect(effect);
        livingBalloon.getEquipment().setHelmet(iS);
        livingBalloon.getEquipment().setItemInMainHand(iS);
        livingBalloon.setGravity(true);
        livingBalloon.setCollidable(false);

        balloonLeash.setSilent(true);
        balloonLeash.setInvulnerable(true);
        livingLeash.setInvisible(true);
        livingLeash.addPotionEffect(effect);
        livingLeash.setGravity(true);
        livingLeash.setCollidable(false);

        balloonPoint.setSilent(true);
        livingPoint.setInvisible(true);
        livingPoint.addPotionEffect(effect);

        ((Slime) balloonPoint).setSize(3);

        livingPoint.setLeashHolder(balloonLeash);

        balloonRegistry.put(balloonPoint.getUniqueId(), this);
        leashRegistry.put(balloonPoint.getUniqueId(), balloonLeash.getUniqueId());
        headRegistry.put(balloonPoint.getUniqueId(), balloon.getUniqueId());

        new BalloonScheduler(balloonPoint, balloonLeash);
    }

    public void explodeBalloon(Player player, Location location) {
        StringLib.warn("+ Doing drop for lootBalloon "+internalName);
        List<RewardLine> drops = getDrops();
        StringLib.warn("+++ Got for the balloon drop: ");
        for(RewardLine drop : drops) {
            StringLib.warn("++++ "+drop.baseLine);
        }
        final List<Reward> baseRewards = reward.createNewRewards(drops);
        final List<Reward> rewards = new ArrayList<>();
        collectRewards(baseRewards, rewards, 1, false);

        Manager manager = new Manager();
        manager.fromReward(Collections.singletonList(player), rewards, location);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDamaged(EntityDamageByEntityEvent e) {
        UUID uuid = e.getEntity().getUniqueId();
        if(!balloonRegistry.containsKey(uuid)) return;

        e.setCancelled(true);

        clear(uuid);

        Player player = null;
        if(e.getDamager() instanceof Player) player = (Player)e.getDamager();
        if(e.getDamager() instanceof Projectile) {
            if (((Projectile) e.getDamager()).getShooter() instanceof Player) {
                player = (Player) (((Projectile) e.getDamager()).getShooter());
            }
        } if(player==null) return;

        ((LootBalloon) balloonRegistry.get(uuid)).explodeBalloon(player, e.getEntity().getLocation());
    }

    public static void clear(UUID uuid) {
        ((LivingEntity)Bukkit.getEntity(uuid)).setLeashHolder(null);
        Bukkit.getEntity(leashRegistry.get(uuid)).remove();
        Bukkit.getEntity(headRegistry.get(uuid)).remove();
        Bukkit.getEntity(uuid).remove();

        leashRegistry.remove(uuid);
        headRegistry.remove(uuid);
        balloonRegistry.remove(uuid);
    }


}
