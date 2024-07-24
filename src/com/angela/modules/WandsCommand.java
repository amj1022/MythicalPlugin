package com.angela.modules;

import com.angela.Mythical;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WandsCommand implements CommandExecutor {

    Mythical plugin;

    public WandsCommand(Mythical plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;
            plugin.getWands().forEach(wand -> wand.give(player));
        }
        return false;
    }
}
