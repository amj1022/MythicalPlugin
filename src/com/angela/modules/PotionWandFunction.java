package com.angela.modules;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;

import java.util.function.BiFunction;
import java.util.function.Function;

public class PotionWandFunction implements BiFunction<EntityDamageByEntityEvent, FireworkWand, Void> {

    private boolean cancelDamage;

    public PotionWandFunction(boolean cancelDamage) {
        this.cancelDamage = cancelDamage;
    }

    @Override
    public Void apply(EntityDamageByEntityEvent event, FireworkWand fireworkWand) {
        Entity damager = event.getDamager();
        Entity damagee = event.getEntity();
        if(damager instanceof Firework && damagee instanceof Player){
            if(cancelDamage) {
                event.setCancelled(true);
            }
            ItemStack potion = new ItemStack(Material.SPLASH_POTION);
            PotionMeta meta = (PotionMeta) potion.getItemMeta();
            meta.setBasePotionType(fireworkWand.getPotionType());
            potion.setItemMeta(meta);
            Location location = damager.getLocation();
            World world = damagee.getWorld();
            ThrownPotion entity = (ThrownPotion) world.spawnEntity(location, EntityType.POTION);
            entity.setItem(potion);
        }
        return null;
    }
}
