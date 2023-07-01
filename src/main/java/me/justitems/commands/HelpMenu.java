package me.justitems.commands;

import me.justitems.JustItems;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class HelpMenu {

    private final JustItems plugin;

    public HelpMenu(JustItems main) {
        plugin = main;
    }

    public void playerHelpMenu(Player player) {
        ChatColor gold = ChatColor.GOLD;
        ChatColor gray = ChatColor.GRAY;
        ChatColor green = ChatColor.GREEN;
        player.sendMessage(gold+"Help Menu");
        player.sendMessage(" ");
        player.sendMessage(gold+" /justitems "+gray+" gen "+green+" -> It generates the pack.");
        player.sendMessage(gold+" /justitems "+gray+" help "+green+" -> To see this menu.");
        player.sendMessage(gold+" /justitems "+gray+" set_texture ..."+green+" -> To set texture to an head or block.");
        player.sendMessage(gold+" /justitems "+gray+" reload "+green+" -> To reload the config and adapt new changes.");
        player.sendMessage(" ");
    }

    public void consoleHelpMenu() {
        plugin.getLogger().info("Help Menu");
        plugin.getLogger().info(" ");
        plugin.getLogger().info(" You can't set texture from the console with this command /justitems set_texture ...");
        plugin.getLogger().info(" justitems gen -> It generates the pack.");
        plugin.getLogger().info(" justitems help -> To see this menu.");
        plugin.getLogger().info(" justitems reload -> To reload the config and adapt new changes.");
    }

}
