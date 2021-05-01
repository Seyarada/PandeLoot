package net.seyarada.pandeloot.damage;

import net.citizensnpcs.api.CitizensAPI;
import net.seyarada.pandeloot.PandeLoot;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DamageTracker implements Listener {

    // Keeps track the damage dealt to a mob, the player and the amount
    public static Map<UUID, Map<Player, Double>> damageTracker = new HashMap<>();
    // damageMap.containsKey(playerName)

    // Stores the mobs registered as mobs that should have the damage tracked
    public static Map<UUID, MobOptions> loadedMobs = new HashMap<>();


    public static Map<UUID, Player> lastHits = new HashMap<>();

    public static void addPlayerDamage(UUID mob, Player player, Double damage) {
        if(PandeLoot.getInstance().getServer().getPluginManager().getPlugin("Citizens")!=null) {
            if(CitizensAPI.getNPCRegistry().isNPC(player)) return;
        }

        Entity entity = Bukkit.getEntity(mob);
        if( entity!=null && ((LivingEntity) entity ).getHealth() < damage ) {
            damage = ((LivingEntity) entity).getHealth();
            lastHits.put(mob, player);
        }

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
        if(!loadedMobs.containsKey(uuid)) return;

        if(e.getDamager() instanceof Player) {
            addPlayerDamage(uuid, (Player) e.getDamager(), e.getFinalDamage());
            return;
        }

        if(e.getDamager() instanceof Projectile) {
            if (((Projectile) e.getDamager()).getShooter() instanceof Player) {
                Player player = (Player) (((Projectile) e.getDamager()).getShooter());
                addPlayerDamage(uuid, player, e.getFinalDamage());
            }
        }

    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player name = e.getEntity();
        remove(name);
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e) {
        Player name = e.getPlayer();
        remove(name);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player name = e.getPlayer();
        remove(name);
    }

    public void remove(Player player) {

        for(UUID uuid : damageTracker.keySet())  {
            Map<Player, Double> map = damageTracker.get(uuid);
            MobOptions mobOptions;

            if(loadedMobs.containsKey(uuid))
                mobOptions = loadedMobs.get(uuid);
            else continue;

            if(map.containsKey(player)) {

                if(mobOptions.resetPlayers) {
                    Double playerDamage = map.get(player);
                    map.remove(player);
                    if(mobOptions.resetHeal) {
                        LivingEntity a = ((LivingEntity) Bukkit.getEntity(uuid));
                        if(a!=null) {
                            double health = a.getHealth()+playerDamage;
                            double maxHP = a.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                            a.setHealth(Math.min(health, maxHP));
                        }
                    }
                }
            }
        }
    }



}
