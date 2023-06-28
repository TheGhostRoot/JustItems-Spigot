package me.justitems.commands;

import me.justitems.JustItems;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Objects;

public class MainConfig {

    private final File configFile;

    private FileConfiguration configData;

    public MainConfig(JustItems main) {
        File dataFolder = main.getDataFolder();
        if (!dataFolder.exists()) {
            if (dataFolder.mkdirs()) {
                main.getLogger().info("Made a folder");
            } else {
                main.getLogger().info("Can't create a folder");
            }
        }
        configFile = new File(dataFolder, "config.yml");
        if (!configFile.exists()) {
            main.saveResource("config.yml", false);
            configData = main.getConfig();
            configData.options().copyDefaults(true);
        } else {
            configData = main.getConfig();
            configData.options().copyDefaults(false);
        }
        /*
        if (!playerFile.exists()) {
            plugin.saveResource("player_data.yml", false);
            loadPlayer();
            playerData.options().copyDefaults(true);
        } else {
            loadPlayer();
            playerData.options().copyDefaults(false);
        }*/
    }

    public String getMissingPermissionsMessage() {
        String got = configData.getString("messages.missing_permission");
        if (got == null) {
            configData.set("messages.missing_permission", "&cYou don't have &6<permission> &cpermission");
            reloadConfig();
            return "";
        } else {
            return got;
        }
    }

    public String getMissingArgsMessage() {
        String got = configData.getString("messages.missing_option");
        if (got == null) {
            configData.set("messages.missing_option", "&cYou have to provide &6<args>&c arguments!");
            reloadConfig();
            return "";
        } else {
            return got;
        }
    }

    public String getWrongItemMessage() {
        String got = configData.getString("messages.wrong_item");
        if (got == null) {
            configData.set("messages.wrong_item", "&cThe &6<item>&c your holding must be &6<req_item>");
            reloadConfig();
            return "";
        } else {
            return got;
        }
    }


    public void reloadConfig() {
        saveConfig();
        loadConfig();
    }

    public void saveConfig() {
        try {
            configData.save(configFile);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadConfig() {
        configData = YamlConfiguration.loadConfiguration(configFile);
    }


}
