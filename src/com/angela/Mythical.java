package com.angela;

import com.angela.modules.*;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.List;


public class Mythical extends JavaPlugin {
    private List<Wand> wands = new ArrayList<>();
    private static Mythical instance;

    @Override
    public void onEnable() {
        instance = this;
        System.out.println(ChatColor.LIGHT_PURPLE + " Mythical plugin loaded");

        // because both event and command need this
        // where each wand is created
        Wand fireWand = new Wand("Fire Wand", EntityType.SMALL_FIREBALL,
                1.5,"Can you take the heat?");

        Wand snowWand = new Wand("Snow Wand", EntityType.SNOWBALL,
                1.5,"3 . . . 2 . . . 1 . . . FIGHT!");

        // Healing Wand
        FireworkWand healthWand = new FireworkWand("Health Wand", Color.RED,
                false, true, FireworkEffect.Type.BURST,
                0.5, PotionType.STRONG_HEALING ,ChatColor.RED +
                "~ B A S I C ~" + " Go get some food . . .");
        healthWand.setOnDamage(new PotionWandFunction(true));

        // Poison Wand
        FireworkWand poisonWand =  new FireworkWand("Poison Wand", Color.GREEN,
                false,true,FireworkEffect.Type.BURST,
                0.5,PotionType.POISON, ChatColor.GREEN +
                "Death lingers");
        poisonWand.setOnDamage(new PotionWandFunction(false));

        wands.add(fireWand);
        wands.add(snowWand);
        wands.add(healthWand);
        wands.add(poisonWand);

        //Commands
        getCommand("firewand").setExecutor(fireWand);
        getCommand("snowwand").setExecutor(snowWand);
        getCommand("healthwand").setExecutor(healthWand);
        getCommand("poisonwand").setExecutor(poisonWand);
        getCommand("wands").setExecutor(new WandsCommand(this));

        //Events
        getServer().getPluginManager().registerEvents(fireWand,this);
        getServer().getPluginManager().registerEvents(snowWand,this);
        getServer().getPluginManager().registerEvents(healthWand, this);
        getServer().getPluginManager().registerEvents(poisonWand, this);
    }


    public List<Wand> getWands() {
        return wands;
    }

    public static Mythical getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {

    }
}
