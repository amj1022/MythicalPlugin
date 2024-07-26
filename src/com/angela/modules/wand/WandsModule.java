package com.angela.modules.wand;

import com.angela.Mythical;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WandsModule implements CommandExecutor {
    private Mythical plugin;
    private Map<String, Wand> wands = new HashMap<>();

    public WandsModule(Mythical plugin) {
        this.plugin = plugin;
        plugin.getCommand("wand").setExecutor(this);
        plugin.getCommand("wand").setTabCompleter(new TabCompleter() { // wand give RadIsAj firewand
            @Override
            public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
                if(args.length == 1){
                    return List.of("give", "list");
                }
                else if(args.length == 2) {
                    return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
                }
                else if (args.length == 3) {
                    return wands.keySet().stream().toList();

                }
                else{
                    return new ArrayList<>();
                }
            }
        });
        createWands();
    }

    private void createWands(){
        // Fire Wand
        Wand fireWand = new Wand("firewand", "Fire Wand", EntityType.SMALL_FIREBALL,
                1.5,"Can you take the heat?");

        // Snow Wand
        Wand snowWand = new Wand("snowwand","Snow Wand", EntityType.SNOWBALL,
                1.5,"3 . . . 2 . . . 1 . . . FIGHT!");

        // Healing Wand
        FireworkWand healthWand = new FireworkWand("healthwand","Health Wand", Color.RED,
                false, true, FireworkEffect.Type.BURST,
                0.5, PotionType.STRONG_HEALING , ChatColor.RED +
                "~ B A S I C ~" + " Go get some food . . .");
        healthWand.setOnDamage(new PotionWandFunction(true));

        // Poison Wand
        FireworkWand poisonWand =  new FireworkWand("poisonwand","Poison Wand", Color.GREEN,
                false,true,FireworkEffect.Type.BURST,
                0.5,PotionType.POISON, ChatColor.GREEN +
                "Death lingers");
        poisonWand.setOnDamage(new PotionWandFunction(false));

        // NEW WANDS:
        // Ice Wand
        FireworkWand iceWand =  new FireworkWand("icewand", "Ice Wand", Color.AQUA,
                false,true,FireworkEffect.Type.BURST,
                0.5,PotionType.SLOWNESS, ChatColor.AQUA +
                "Too cold?");
        iceWand.setOnDamage(new PotionWandFunction(false));

        // Wicked Wand
        FireworkWand wickedWand =  new FireworkWand("wickedwand", "Wicked Wand", Color.GRAY,
                false,true,FireworkEffect.Type.BURST,
                0.5,PotionType.HARMING, ChatColor.GRAY +
                "Mwah ha ha . . .");
        wickedWand.setOnDamage(new PotionWandFunction(false));

        // Sneaky Wand
        FireworkWand sneakyWand =  new FireworkWand("sneakywand", "Sneaky Wand", Color.SILVER,
                false,true,FireworkEffect.Type.BURST,
                0.5,PotionType.INVISIBILITY, ChatColor.GRAY +
                "Hide and Seek?");
        sneakyWand.setOnDamage(new PotionWandFunction(true));
        // Ocean Wand
        FireworkWand oceanWand =  new FireworkWand("oceanwand", "Ocean Wand", Color.BLUE,
                false,true,FireworkEffect.Type.BURST,
                0.5,PotionType.WATER_BREATHING, ChatColor.BLUE +
                "Water resistant");
        oceanWand.setOnDamage(new PotionWandFunction(true));

        // Warrior Wand
        FireworkWand warriorWand =  new FireworkWand("warriorwand", "Warrior Wand", Color.ORANGE,
                false,true,FireworkEffect.Type.BURST,
                0.5,PotionType.STRENGTH, ChatColor.YELLOW +
                "The power within!");
        warriorWand.setOnDamage(new PotionWandFunction(true));

        wands.put("firewand", fireWand);
        wands.put("snowwand", snowWand);
        wands.put("healthwand", healthWand);
        wands.put("poisonwand", poisonWand);
        // NEW WANDS:
        wands.put("icewand", iceWand);
        wands.put("wickedwand", wickedWand);
        wands.put("sneakywand", sneakyWand);
        wands.put("oceanwand", oceanWand);
        wands.put("warriorwand", warriorWand);

    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        // OLD:    0
        // wand [wand name]

        // NEW:
        //       0         1            2
        // wand give [player name] [wand name]
        //       0
        // wand list
        if(commandSender instanceof Player) {
            if(args.length > 0) {
                if(args[0].equalsIgnoreCase("give")){ // give command
                    String playerName = args[1];
                    String wandName = args[2];
                    Player player = Bukkit.getPlayer(playerName);
                    for(Map.Entry<String, Wand> entry: wands.entrySet()){
                        if(entry.getKey().equalsIgnoreCase(wandName)){
                            entry.getValue().give(player);
                        }
                    }

                } else if (args[0].equalsIgnoreCase("list")) {
                    String message = "";
                    String[] keys = wands.keySet().toArray(new String[0]);
                    for(int i = 0; i < keys.length; i++){
                        String wandName = keys[i];
                        if(i == keys.length - 1){
                            message += wandName;
                        } else {
                            message += wandName + ", ";
                        }
                    }
                    commandSender.sendMessage(message);
                }

            } else {
                wands.values().forEach(wand -> wand.give((Player) commandSender));

            }
        }

        return false;
    }
}
