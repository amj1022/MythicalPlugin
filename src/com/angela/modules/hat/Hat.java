package com.angela.modules.hat;

import com.angela.Mythical;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class Hat implements CommandExecutor {
    private ItemStack hat;
    private ItemMeta meta;
    private String name;

    public Hat(String id, String name, int modelData, String ...lore){
        hat = new ItemStack(Material.CARVED_PUMPKIN, 1);
        meta = hat.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(List.of(lore));
        meta.setMaxStackSize(1);
        meta.setCustomModelData(modelData);
        hat.setItemMeta(meta);
        Mythical.getInstance().getCommand(id).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        //wizard hat
        if(sender instanceof Player) {
            Player player = (Player) sender;
            player.getInventory().addItem(hat);
        }
        return false;
    }
}
