package com.angela.modules.hat;

import com.angela.Mythical;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class HatModule implements CommandExecutor {
    private Mythical plugin;

    public HatModule(Mythical plugin){
        this.plugin = plugin;
        plugin.getCommand("wizardhat").setExecutor(this);
        createHats();
    }

    private void createHats(){
        //Wizard Hat
        Hat wizardHat = new Hat("wizardhat","Wizard Hat", 123,
                "Somehow the old leather gives", "you ancient knowledge");
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        return false;
    }
}
