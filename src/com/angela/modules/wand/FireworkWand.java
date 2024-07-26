package com.angela.modules.wand;

import com.angela.Mythical;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

//color, trail, fade, with(effect), build
public class FireworkWand extends Wand {
    private Color color;
    private boolean trail;
    private boolean flicker;
    private FireworkEffect.Type type;
    private double velocityMultiplier;
    private BiFunction<EntityDamageByEntityEvent, FireworkWand, Void> onDamage;
    private PotionType potionType;
    private MetadataValue metadata;
    private static List<FireworkData> fireworks = new ArrayList<>();

    public FireworkWand(String id, String name, Color color, boolean trail, boolean flicker,
                        FireworkEffect.Type type, double velocityMultiplier, PotionType potionType, String... lore) {
        super(id, name, EntityType.FIREWORK_ROCKET, velocityMultiplier,lore);
        this.color = color;
        this.trail = trail;
        this.flicker = flicker;
        this.type = type;
        this.velocityMultiplier  = velocityMultiplier;
        this.potionType = potionType;
        this.metadata = new FixedMetadataValue(Mythical.getInstance(), true);

        attackNearby();
    }

    public void attackNearby() {
        Bukkit.getScheduler().runTaskTimer(Mythical.getInstance(), new Runnable() {
            @Override
            public void run() {
                // remove invalid fireworks
                fireworks.removeIf(fireworkData ->  fireworkData.getFirework().isDead() || !fireworkData.getFirework().isValid());
                for(FireworkData data : fireworks) {
                    Firework firework = data.getFirework();
                    Location closest = null;
                    double distance = 0.0;
                    for(Entity entity : firework.getNearbyEntities(15, 5, 15)) {
                        if(entity instanceof Mob && !firework.getShooter().equals(entity)) {
                            double currentDistance = entity.getLocation().distance(firework.getLocation());
                            if(closest == null || currentDistance < distance) {
                                closest = entity.getLocation();
                                distance = currentDistance;
                            }
                        }
                    }
                    if(closest != null) {
                        Vector direction = closest.toVector().subtract(firework.getLocation().toVector()).normalize();
                        firework.getLocation().setDirection(direction);
                        firework.setVelocity(direction.multiply(1.5));
                    } else {
                        // set original direction
                        Vector direction = data.getOriginalLocation().getDirection();
                        firework.getLocation().setDirection(direction);
                        firework.setVelocity(direction.multiply(1.5));
;                    }
                }
            }
        }, 5, 5);
    }

    @Override
    @EventHandler
    public void onWandClick(PlayerInteractEvent event) {
        ItemStack mainHand = event.getPlayer().getInventory().getItemInMainHand();
        if(!mainHand.hasItemMeta()) {
            return;
        }

        if(mainHand.getItemMeta().equals(getMeta())) {
            if (event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                Location playerLocation = event.getPlayer().getLocation();

                Firework entity = (Firework) playerLocation.getWorld()
                        .spawnEntity(playerLocation.clone().add(0, 1, 0), getEntityType());
                FireworkMeta meta = entity.getFireworkMeta();

                //Effect
                FireworkEffect fireworkEffect = FireworkEffect.builder().flicker(flicker)
                        .withColor(color)
                        .withTrail()
                        .withFade(color)
                        .with(type)
                        .build();
                meta.addEffect(fireworkEffect);
                entity.setMetadata(getName(), metadata);
                entity.setFireworkMeta(meta);
                entity.setShooter(event.getPlayer());

                //direction of firework
                Vector direction = event.getPlayer().getLocation().getDirection();
                entity.getLocation().setDirection(direction);
                entity.setVelocity(direction.multiply(velocityMultiplier));
                fireworks.add(new FireworkData(entity, playerLocation));
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Firework) {
            Firework firework = (Firework) event.getDamager();
            if(firework.hasMetadata(getName())) {
                if(onDamage != null) {
                    try {
                        onDamage.apply(event, this);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    public void setOnDamage(BiFunction<EntityDamageByEntityEvent,FireworkWand,Void> onDamage) {
        this.onDamage = onDamage;
    }

    public PotionType getPotionType() {
        return potionType;
    }
}

class FireworkData {

    private Firework firework;
    private Location originalLocation;

    public FireworkData(Firework firework, Location originalLocation) {
        this.firework = firework;
        this.originalLocation = originalLocation;
    }

    public Firework getFirework() {
        return firework;
    }

    public Location getOriginalLocation() {
        return originalLocation;
    }
}
