package me.justitems.commands;

import me.justitems.JustItems;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
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

    public List<String> getMissingPermissionsMessage() {
        List<String> got = (List<String>) configData.getList("messages.missing_permission");
        if (got == null) {
            List<String> array = new ArrayList<>();
            array.add("&cYou don't have &6<permission> &cpermission");
            configData.set("messages.missing_permission", array);
            reloadConfig();
            return new ArrayList<>();
        } else {
            return got;
        }
    }

    public List<String> getErrorMessage() {
        List<String> got = (List<String>) configData.getList("messages.error");
        if (got == null) {
            List<String> array = new ArrayList<>();
            array.add("&cFailed &4<error>");
            configData.set("messages.error", array);
            reloadConfig();
            return new ArrayList<>();
        } else {
            return got;
        }
    }

    public List<String> getMissingArgsMessage() {
        List<String> got = (List<String>) configData.getList("messages.missing_option");
        if (got == null) {
            List<String> array = new ArrayList<>();
            array.add("&cYou have to provide &6<args>&c arguments!");
            configData.set("messages.missing_option", array);
            reloadConfig();
            return new ArrayList<>();
        } else {
            return got;
        }
    }

    public List<String> getWrongItemMessage() {
        List<String> got = (List<String>) configData.getList("messages.wrong_item");
        if (got == null) {
            List<String> array = new ArrayList<>();
            array.add("&cThe &6<item>&c your holding must be &6<req_item>");
            configData.set("messages.wrong_item", array);
            reloadConfig();
            return new ArrayList<>();
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
