package me.justitems.commands;

import me.justitems.JustItems;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MainTabComplete implements TabCompleter {

    private final JustItems plugin;

    public MainTabComplete(JustItems main) {
        plugin = main;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> commands = new ArrayList<>();
        List<String> allPlayers = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            allPlayers.add(player.getName());
        }
        if (args.length == 1) {
            commands.add("set_texture");
            commands.add("help");
            return commands;
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("set_texture")) {
                commands.add("item_url");
                commands.add("item_base64");
                commands.add("item_player_name");
                commands.add("block_url");
                commands.add("block_base64");
                commands.add("block_player_name");
                return commands;
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("set_texture")) {
                if (args[1].equalsIgnoreCase("item_url") || args[1].equalsIgnoreCase("block_url")) {
                    commands.add("---- URL ----");
                    return commands;
                } else if (args[1].equalsIgnoreCase("item_base64") || args[1].equalsIgnoreCase("block_base64")) {
                    commands.add("---- BASE64 ----");
                    return commands;
                } else if (args[1].equalsIgnoreCase("item_player_name") || args[1].equalsIgnoreCase("block_player_name")) {
                    commands.add("---- Player Name ----");
                    commands.addAll(allPlayers);
                    return commands;
                }

            }
        }
        return null;
    }
}
