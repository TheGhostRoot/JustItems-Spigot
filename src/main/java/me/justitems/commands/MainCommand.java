package me.justitems.commands;

import me.justitems.JustItems;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainCommand implements CommandExecutor {

    private final JustItems plugin;

    public MainCommand(JustItems main) {
        plugin = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // only player commands: SetTextures
        if (sender instanceof Player) {
            Player player = (Player) sender;

            // checks if the player has permissions to use the plugin.
            if (!player.hasPermission("justitems.use")) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        plugin.config.getMissingPermissionsMessage().replace("<permission>", "justitems.use")));
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
