package me.justitems.commands;

import me.justitems.JustItems;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class SetTextures {

    private final JustItems plugin;

    public SetTextures(JustItems main) {
        plugin = main;
    }

    public void command(Player player, String[] args) {
        // args >= 2
        // checks player permissions
        if (!player.hasPermission("justitems.settextures")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    plugin.config.getMissingPermissionsMessage().replace("<permission>", "justitems.settextures")));
            return;
        }
        // checks if the player gave any arguments
        if (args.length < 3) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    plugin.config.getMissingArgsMessage().replace("<args>", "URL")));
            return;
        }
        // goes over all options for setting textures
        switch (args[1].toLowerCase()) {
            case "item_url": {
                // args[2] == URL
                // gets the item in the player's hands
                ItemStack itemInHand = player.getInventory().getItemInMainHand();
                if (!itemInHand.getType().isAir()) {
                    // modify the holding item
                    plugin.utils.itemWithUrl(itemInHand, args[2]);
                } else {
                    // give a new item
                    plugin.utils.itemFromUrl(args[2], 1);
                }
                break;
            }
            case "item_base64": {
                // args[2] == BASE64
                // gets the item in the player's hands
                ItemStack itemInHand = player.getInventory().getItemInMainHand();
                if (!itemInHand.getType().isAir()) {
                    // modify the holding item
                    plugin.utils.itemWithBase64(itemInHand, args[2]);
                } else {
                    // give a new item
                    plugin.utils.itemFromBase64(args[2], 1);
                }
                break;
            }
            case "item_player_name": {
                // args[2] == Player Name
                // gets the item in the player's hands
                ItemStack itemInHand = player.getInventory().getItemInMainHand();
                if (!itemInHand.getType().isAir()) {
                    // modify the holding item
                    plugin.utils.itemWithName(itemInHand, args[2]);
                } else {
                    // give a new item
                    plugin.utils.itemFromName(args[2], 1);
                }
                break;
            }
            case "item_player_uuid": {
                // args[2] == Player UUID
                // gets the item in the player's hands
                ItemStack itemInHand = player.getInventory().getItemInMainHand();
                if (!itemInHand.getType().equals(Material.AIR)) {
                    // modify the holding item
                    plugin.utils.itemWithUuid(itemInHand, UUID.fromString(args[2]));
                } else {
                    // give a new item
                    plugin.utils.itemFromUuid(UUID.fromString(args[2]), 1);
                }
                break;
            }
            case "block_url": {
                // args[2] == URL
                // gets the item in the player's hands
                ItemStack itemInHand = player.getInventory().getItemInMainHand();
                if (itemInHand.getType().isBlock()) {
                    // modify the holding item
                    plugin.utils.blockWithUrl((Block) itemInHand, args[2]);
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            plugin.config.getWrongItemMessage().replace("<item>", itemInHand.getType().toString())
                                    .replace("<req_item>", "BLOCK")));
                }
                break;
            }
            case "block_base64": {
                // args[2] == BASE64
                // gets the item in the player's hands
                ItemStack itemInHand = player.getInventory().getItemInMainHand();
                if (itemInHand.getType().isBlock()) {
                    // modify the holding item
                    plugin.utils.blockWithBase64((Block) itemInHand, args[2]);
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            plugin.config.getWrongItemMessage().replace("<item>", itemInHand.getType().toString())
                                    .replace("<req_item>", "BLOCK")));
                }
                break;
            }
            case "block_player_name": {
                // args[2] == Player Name
                // gets the item in the player's hands
                ItemStack itemInHand = player.getInventory().getItemInMainHand();
                if (itemInHand.getType().isBlock()) {
                    // modify the holding item
                    plugin.utils.blockWithName((Block) itemInHand, args[2]);
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            plugin.config.getWrongItemMessage().replace("<item>", itemInHand.getType().toString())
                                    .replace("<req_item>", "BLOCK")));
                }
                break;
            }
            case "block_player_uuid": {
                // args[2] == UUID
                // gets the item in the player's hands
                ItemStack itemInHand = player.getInventory().getItemInMainHand();
                if (itemInHand.getType().isBlock()) {
                    // modify the holding item
                    plugin.utils.blockWithUuid((Block) itemInHand, UUID.fromString(args[2]));
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            plugin.config.getWrongItemMessage().replace("<item>", itemInHand.getType().toString())
                                    .replace("<req_item>", "BLOCK")));
                }
                break;
            }
            default: {

                break;
            }
        }
    }
}
