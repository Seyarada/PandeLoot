package net.seyarada.pandeloot.damage;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.*;

public class DamageTracker implements Listener {

    // Keeps track the damage dealt to a mob, the player and the amount
    public static Map<UUID, Map<Player, Double>> damageTracker = new HashMap<>();
    // damageMap.containsKey(playerName)

    // Stores the mobs registered as mobs that should have the damage tracked
    public static List<UUID> loadedMobs = new ArrayList<>();

    public static void addPlayerDamage(UUID mob, Player player, Double damage) {
        // Discard the event if the mob isn't being tracked
        //if(!loadedMobs.contains(mob))
        //    return;

        if( ((LivingEntity) Bukkit.getEntity(mob) ).getHealth() < damage )
            damage = ((LivingEntity)Bukkit.getEntity(mob) ).getHealth();

        // Creates the map if it doesn't exists
        if(!damageTracker.containsKey(mob)) {
            HashMap<Player, Double> damageMap = new HashMap<>();
            damageMap.put(player, damage);
            // Creates a new entry in the hashmap with the player and damage
            damageTracker.put(mob, damageMap);
            return;
        }

        // Gets the damage map of the entity
        Map<Player, Double> damageMap = damageTracker.get(mob);

        // Checks if the entity already has that player mapped
        if (damageMap.containsKey(player)) {
            // Updates the map to add the new damage
            damageMap.put(player, damageMap.get(player) + damage);
        } else {
            // Creates a new entry in the hashmap with the player and damage
            damageMap.put(player, damage);}

        // Pushes the data to the main hashmap
        damageTracker.put(mob, damageMap);

    }

    public static Map<Player, Double> get(UUID uuid) {
        return damageTracker.get(uuid);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDamaged(EntityDamageByEntityEvent e) {
        UUID uuid = e.getEntity().getUniqueId();

        if(e.getDamager() instanceof Player) {
            addPlayerDamage(uuid, (Player) e.getDamager(), e.getFinalDamage());
            return;
        }

        if(e.getDamager() instanceof Projectile) {
            if (((Projectile) e.getDamager()).getShooter() instanceof Player) {
                Player player = (Player) (((Projectile) e.getDamager()).getShooter());
                addPlayerDamage(uuid, player, e.getFinalDamage());
                return;
            }
        }

    }



}
