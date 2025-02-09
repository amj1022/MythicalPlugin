package com.angela.modules.wand;

import com.angela.Mythical;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class Wand implements Listener, CommandExecutor {
    private ItemStack wand;
    private ItemMeta meta;
    private EntityType entityType;
    private double velocityMultiplier;
    private String name;
    private Mythical plugin = Mythical.getInstance();

    public Wand(String id, String name, EntityType entityType, double velocityMultiplier, String ...lore){
        this.entityType = entityType;
        this.velocityMultiplier = velocityMultiplier;
        this.name = name;
        wand = new ItemStack(Material.STICK, 1);
        meta = wand.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(List.of(lore));
        meta.setMaxStackSize(1);
        meta.setCustomModelData(18);
        wand.setItemMeta(meta);
        plugin.getCommand(id).setExecutor(this);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(ignoreCancelled = false)
    public void onWandClick(PlayerInteractEvent event){
        ItemStack mainHand = event.getPlayer().getInventory().getItemInMainHand();
        if(mainHand.getItemMeta() == null) {
            return;
        }

        if(mainHand.getItemMeta().equals(meta)) {
            if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                Location playerLocation = event.getPlayer().getLocation();
                Projectile entity = (Projectile) playerLocation.getWorld()
                        .spawnEntity(playerLocation.clone().add(0,1,0), entityType);
                entity.setShooter(event.getPlayer());
                entity.getLocation().setDirection(playerLocation.getDirection());
                entity.setVelocity(playerLocation.getDirection().multiply(velocityMultiplier));
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            player.getInventory().addItem(wand);
        }
        return true;
    }

    public ItemStack getWand() {
        return wand;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public ItemMeta getMeta() {
        return meta;
    }

    public double getVelocityMultiplier() {
        return velocityMultiplier;
    }

    public void give(Player player) {
        player.getInventory().addItem(wand);
        player.sendMessage(ChatColor.LIGHT_PURPLE + player.getName() + " has received a " + name);
    }

    public String getName() {
        return name;

    }
}
