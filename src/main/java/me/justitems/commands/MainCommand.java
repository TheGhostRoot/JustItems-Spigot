package me.justitems.commands;

import me.justitems.JustItems;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class MainCommand implements CommandExecutor {

    private final JustItems plugin;

    public MainCommand(JustItems main) {
        plugin = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // only player commands: SetTextures
        if (sender instanceof ConsoleCommandSender) {

            plugin.config.generateResourcePack();

            // on /justitems
            if (args.length == 0) {
                // help menu
                plugin.help.consoleHelpMenu();
                return true;
            }

            // go over the commands
            switch (args[0].toLowerCase()) {
                case "help": {
                    // help menu
                    plugin.help.consoleHelpMenu();
                    break;
                }
                case "reload": {
                    // reload the config
                    plugin.config.loadConfig();
                    plugin.getLogger().info("§2Just Items §6| §аConfig reloaded!");
                    break;
                }
                default: {
                    // help menu
                    plugin.help.consoleHelpMenu();
                    break;
                }
            }

        } else if (sender instanceof Player) {
            Player player = (Player) sender;

            HashMap<String, String> msgTable = new HashMap<>();

            // checks if the player has permissions to use the plugin.
            if (!player.hasPermission("justitems.use")) {
                msgTable.put("<permission>", "justitems.use");
                for (String msg : plugin.utils.convertMessage(plugin.config.getMissingPermissionsMessage(), msgTable, 1)) {
                    player.sendMessage(msg);
                }
                return true;
            }

            // on /justitems
            if (args.length == 0) {
                // help menu
                plugin.help.playerHelpMenu(player);
            }

            // go over the commands
            switch (args[0].toLowerCase()) {
                case "set_texture": {
                    // set textures for item or skin
                    plugin.setTextures.command(player, args);
                    break;
                }
                case "help": {
                    // help menu
                    plugin.help.playerHelpMenu(player);
                    break;
                }
                case "reload": {
                    // reload the config
                    plugin.config.loadConfig();
                    player.sendMessage("§2Just Items §6| §аConfig reloaded!");
                    break;
                }
                default: {
                    // help menu
                    plugin.help.playerHelpMenu(player);
                    break;
                }
            }

        }
        return true;
    }
}
