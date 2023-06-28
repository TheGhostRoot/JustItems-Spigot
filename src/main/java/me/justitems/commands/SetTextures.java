package me.justitems.commands;

import me.justitems.JustItems;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;

public class SetTextures {

    private final JustItems plugin;

    public SetTextures(JustItems main) {
        plugin = main;
    }

    public void command(Player player, String[] args) {
        // args >= 2
        // checks player permissions
        HashMap<String, String> msgTable = new HashMap<>();
        if (!player.hasPermission("justitems.settextures")) {
            msgTable.put("<permission>", "justitems.settextures");
            for (String message : plugin.utils.convertMessage(plugin.config.getMissingPermissionsMessage(), msgTable, 1)) {
                player.sendMessage(message);
            }
            return;
        }
        // checks if the player gave any arguments
        if (args.length < 3) {
            msgTable.put("<args>", "URL");
            for (String message : plugin.utils.convertMessage(plugin.config.getMissingArgsMessage(), msgTable, 1)) {
                player.sendMessage(message);
            }
            return;
        }
        // goes over all options for setting textures
        PlayerInventory inventory = player.getInventory();
        switch (args[1].toLowerCase()) {
            case "item_url": {
                // args[2] == URL
                // gets the item in the player's hands
                ItemStack itemInHand = inventory.getItemInMainHand();
                if (!itemInHand.getType().isAir()) {
                    // modify the holding item
                    try {
                        inventory.addItem(plugin.utils.itemWithUrl(itemInHand, args[2]));
                    }catch (Exception e) {
                        msgTable.put("<error>", "Can't set textures or inventory full");
                        for (String message : plugin.utils.convertMessage(plugin.config.getErrorMessage(), msgTable, 1)) {
                            player.sendMessage(message);
                        }
                    }
                } else {
                    // give a new item
                    try {
                        inventory.addItem(plugin.utils.itemFromUrl(args[2], 1));
                    } catch (Exception e){
                        msgTable.put("<error>", "Can't set textures or inventory full");
                        for (String message : plugin.utils.convertMessage(plugin.config.getErrorMessage(), msgTable, 1)) {
                            player.sendMessage(message);
                        }
                    }
                }
                break;
            }
            case "item_base64": {
                // args[2] == BASE64
                // gets the item in the player's hands
                ItemStack itemInHand = inventory.getItemInMainHand();
                if (!itemInHand.getType().isAir()) {
                    // modify the holding item
                    try {
                        inventory.addItem(plugin.utils.itemWithBase64(itemInHand, args[2]));
                    } catch (Exception e) {
                        msgTable.put("<error>", "Can't set textures or inventory full");
                        for (String message : plugin.utils.convertMessage(plugin.config.getErrorMessage(), msgTable, 1)) {
                            player.sendMessage(message);
                        }
                    }
                } else {
                    // give a new item
                    try {
                        inventory.addItem(plugin.utils.itemFromBase64(args[2], 1));
                    } catch (Exception e) {
                        msgTable.put("<error>", "Can't set textures or inventory full");
                        for (String message : plugin.utils.convertMessage(plugin.config.getErrorMessage(), msgTable, 1)) {
                            player.sendMessage(message);
                        }
                    }
                }
                break;
            }
            case "item_player_name": {
                // args[2] == Player Name
                // gets the item in the player's hands
                ItemStack itemInHand = inventory.getItemInMainHand();
                if (!itemInHand.getType().isAir()) {
                    // modify the holding item
                    try {
                        inventory.addItem(plugin.utils.itemWithName(itemInHand, args[2]));
                    } catch (Exception e) {
                        msgTable.put("<error>", "Can't set textures or inventory full");
                        for (String message : plugin.utils.convertMessage(plugin.config.getErrorMessage(), msgTable, 1)) {
                            player.sendMessage(message);
                        }
                    }
                } else {
                    // give a new item
                    try {
                        inventory.addItem(plugin.utils.itemFromName(args[2], 1));
                    }catch (Exception e) {
                        msgTable.put("<error>", "Can't set textures or inventory full");
                        for (String message : plugin.utils.convertMessage(plugin.config.getErrorMessage(), msgTable, 1)) {
                            player.sendMessage(message);
                        }
                    }
                }
                break;
            }
            case "block_url": {
                // args[2] == URL
                // gets the item in the player's hands
                ItemStack itemInHand = inventory.getItemInMainHand();
                if (itemInHand.getType().isBlock()) {
                    // modify the holding item
                    try {
                        plugin.utils.blockWithUrl((Block) itemInHand, args[2]);
                    }catch (Exception e) {
                        msgTable.put("<error>", "Can't set textures");
                        for (String message : plugin.utils.convertMessage(plugin.config.getErrorMessage(), msgTable, 1)) {
                            player.sendMessage(message);
                        }
                    }
                } else {
                    msgTable.put("<item>", itemInHand.getType().toString());
                    msgTable.put("<req_item>", "BLOCK");
                    for (String message : plugin.utils.convertMessage(plugin.config.getWrongItemMessage(), msgTable, 1)) {
                        player.sendMessage(message);
                    }
                }
                break;
            }
            case "block_base64": {
                // args[2] == BASE64
                // gets the item in the player's hands
                ItemStack itemInHand = inventory.getItemInMainHand();
                if (itemInHand.getType().isBlock()) {
                    // modify the holding item
                    try {
                        plugin.utils.blockWithBase64((Block) itemInHand, args[2]);
                    } catch (Exception e) {
                        msgTable.put("<error>", "Can't set textures");
                        for (String message : plugin.utils.convertMessage(plugin.config.getErrorMessage(), msgTable, 1)) {
                            player.sendMessage(message);
                        }
                    }
                } else {
                    msgTable.put("<item>", itemInHand.getType().toString());
                    msgTable.put("<req_item>", "BLOCK");
                    for (String message : plugin.utils.convertMessage(plugin.config.getWrongItemMessage(), msgTable, 1)) {
                        player.sendMessage(message);
                    }
                }
                break;
            }
            case "block_player_name": {
                // args[2] == Player Name
                // gets the item in the player's hands
                ItemStack itemInHand = inventory.getItemInMainHand();
                if (itemInHand.getType().isBlock()) {
                    // modify the holding item
                    try {
                        plugin.utils.blockWithName((Block) itemInHand, args[2]);
                    }catch (Exception e) {
                        msgTable.put("<error>", "Can't set textures");
                        for (String message : plugin.utils.convertMessage(plugin.config.getErrorMessage(), msgTable, 1)) {
                            player.sendMessage(message);
                        }
                    }
                } else {
                    msgTable.put("<item>", itemInHand.getType().toString());
                    msgTable.put("<req_item>", "BLOCK");
                    for (String message : plugin.utils.convertMessage(plugin.config.getWrongItemMessage(), msgTable, 1)) {
                        player.sendMessage(message);
                    }
                }
                break;
            }
            default: {

                break;
            }
        }
    }
}
