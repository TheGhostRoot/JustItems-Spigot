package me.justitems;

import me.justitems.commands.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class JustItems extends JavaPlugin {

    public JustItems instence;

    public Utils utils;


    public MainConfig config;

    // commands

    public HelpMenu help;

    public SetTextures setTextures;

    public PackHelper packHelper;

    @Override
    public void onEnable() {
        String version = "N/A";
        try {
            version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        } catch (ArrayIndexOutOfBoundsException whatVersionAreYouUsingException) {
            getLogger().severe("Failed to setup JustItems!");
            getLogger().severe("Your server version is not compatible with this plugin!");
            Bukkit.getPluginManager().disablePlugin(this);
        }
        if (!version.equals("N/A")) {
            getLogger().info("Your server is running version " + version);
            switch (version) {
                case "v1_18_R1":
                case "v1_18_R2":
                case "v1_19_R1":
                case "v1_17_R1":
                case "v1_19_R2":
                case "v1_19_R3":
                case "v1_20_R1":
                case "v1_16_R3":
                case "v1_16_R2":
                case "v1_16_R1":
                case "v1_8_R1":
                case "v1_8_R2":
                case "v1_8_R3":
                case "v1_9_R1":
                case "v1_9_R2":
                case "v1_10_R1":
                case "v1_11_R1":
                case "v1_12_R1":
                case "v1_13_R1":
                case "v1_13_R2":
                case "v1_14_R1":
                case "v1_15_R1":
                    instence = this;
                    break;
            }
        }
        if (instence == null) {
            getLogger().severe("Failed to setup JustItems!");
            getLogger().severe("Your server version is not compatible with this plugin!");
            Bukkit.getPluginManager().disablePlugin(this);
        } else {

            // TODO Main Command
            // TODO Main Tab Complete
            // TODO Commands
            // TODO Finish Help Menu
            // TODO Mobs, Music |   Weapons, Cars, Talismats, Amulets ( AKA cusotom mobs and items in the config )


            config = new MainConfig(this);
            packHelper = new PackHelper(this);
            getCommand("justitems").setExecutor(new MainCommand(this));
            getCommand("justitems").setTabCompleter(new MainTabComplete(this));
            utils = new Utils();
            help = new HelpMenu(this);
            setTextures = new SetTextures(this);
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
