package com.angela;

import com.angela.modules.wand.WandsModule;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;


public class Mythical extends JavaPlugin {

    private static Mythical instance;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        instance = this;
        ConsoleCommandSender console = Bukkit.getConsoleSender();
        console.sendMessage(ChatColor.LIGHT_PURPLE + "Mythical plugin loaded");
        FileConfiguration config = getConfig();

        if(config.getBoolean("modules.wand.enable", true)){
            console.sendMessage(ChatColor.GREEN + "Enabling WandModule");
            new WandsModule(this);
        }



    }

    public static Mythical getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {

    }
}
