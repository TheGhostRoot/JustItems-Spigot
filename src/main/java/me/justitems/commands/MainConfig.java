package me.justitems.commands;

import me.justitems.JustItems;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class MainConfig {

    private final File configFile;


    private FileConfiguration configData;

    private final JustItems plugin;

    public MainConfig(JustItems main) {
        plugin = main;
        File dataFolder = main.getDataFolder();
        if (!dataFolder.exists()) {
            if (dataFolder.mkdirs()) {
                main.getLogger().info("Made the main folder");
            } else {
                main.getLogger().info("Can't create the main folder");
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

    public int getPackFormat() {
        return configData.getInt("pack.format");
    }

    public String getPackDescription() {
        String got = configData.getString("pack.description");
        if (got == null) {
            configData.set("pack.description", "Your own pack");
            reloadConfig();
            return "Your own pack";
        } else {
            return got;
        }
    }

    public List<String> getBlocks() {
        List<String> got = (List<String>) configData.getList("pack.blocks");
        if (got == null) {
            List<String> array = new ArrayList<>();
            array.add("block1");
            array.add("block2");
            configData.set("pack.blocks", array);
            reloadConfig();
            return new ArrayList<>();
        } else {
            return got;
        }
    }

    public List<String> getItems() {
        List<String> got = (List<String>) configData.getList("pack.items");
        if (got == null) {
            List<String> array = new ArrayList<>();
            array.add("item1");
            array.add("item2");
            configData.set("pack.items", array);
            reloadConfig();
            return new ArrayList<>();
        } else {
            return got;
        }
    }

    public List<String> getEmojis() {
        List<String> got = (List<String>) configData.getList("pack.emojis");
        if (got == null) {
            List<String> array = new ArrayList<>();
            array.add("emoji1");
            array.add("emoji2");
            configData.set("pack.emojis", array);
            reloadConfig();
            return new ArrayList<>();
        } else {
            return got;
        }
    }

    public int getItemModelData(String item) {
        return configData.getInt("pack."+item+".model_data");
    }

    public String getEmojiType(String emoji) {
        String got = configData.getString("pack."+emoji+".type");
        if (got == null) {
            configData.set("pack."+emoji+".type", "bitmap");
            reloadConfig();
            return "bitmap";
        } else {
            return got;
        }
    }

    public int getEmojiHeight(String emoji) {
        return configData.getInt("pack."+emoji+".height");
    }

    public int getEmojiAscent(String emoji) {
        return configData.getInt("pack."+emoji+".ascent");
    }

    public boolean getRemovePumpkinHat(String hat) {
        return configData.getBoolean("pack."+hat+".remove_pumpkin");
    }

    public List<String> getEmojiChars(String emoji) {
        List<String> got = (List<String>) configData.getList("pack."+emoji+".chars");
        if (got == null) {
            List<String> array = new ArrayList<>();
            array.add("\uE001");
            configData.set("pack."+emoji+".chars", array);
            reloadConfig();
            return new ArrayList<>();
        } else {
            return got;
        }
    }

    public String getItemType(String item) {
        String got = configData.getString("pack."+item+".type");
        if (got == null) {
            configData.set("pack."+item+".type", "dirt");
            reloadConfig();
            return "dirt";
        } else {
            return got;
        }
    }

    public String getBlockSouth(String block) {
        String got = configData.getString("pack."+block+".sides.south");
        if (got == null) {
            configData.set("pack."+block+".sides.south", "blockSouth");
            reloadConfig();
            return "blockSouth";
        } else {
            return got;
        }
    }

    public String getBlockNorth(String block) {
        String got = configData.getString("pack."+block+".sides.north");
        if (got == null) {
            configData.set("pack."+block+".sides.north", "blockNorth");
            reloadConfig();
            return "blockNorth";
        } else {
            return got;
        }
    }

    public String getBlockEast(String block) {
        String got = configData.getString("pack."+block+".sides.east");
        if (got == null) {
            configData.set("pack."+block+".sides.east", "blockEast");
            reloadConfig();
            return "blockEast";
        } else {
            return got;
        }
    }

    public String getBlockWest(String block) {
        String got = configData.getString("pack."+block+".sides.west");
        if (got == null) {
            configData.set("pack."+block+".sides.west", "blockWest");
            reloadConfig();
            return "blockWest";
        } else {
            return got;
        }
    }

    public String getBlockUp(String block) {
        String got = configData.getString("pack."+block+".sides.up");
        if (got == null) {
            configData.set("pack."+block+".sides.up", "blockUp");
            reloadConfig();
            return "blockUp";
        } else {
            return got;
        }
    }

    public String getBlockDown(String block) {
        String got = configData.getString("pack."+block+".sides.down");
        if (got == null) {
            configData.set("pack."+block+".sides.down", "blockDown");
            reloadConfig();
            return "blockDown";
        } else {
            return got;
        }
    }

    public String getBlockParticle(String block) {
        String got = configData.getString("pack."+block+".sides.particle");
        if (got == null) {
            configData.set("pack."+block+".sides.particle", "blockParticle");
            reloadConfig();
            return "blockParticle";
        } else {
            return got;
        }
    }

    public boolean isItemAnimationEnabled(String item) {
        return configData.getBoolean("pack."+item+".animation.enable");
    }

    public int getItemAnimationFrames(String item) {
        return configData.getInt("pack."+item+".animation.amount_of_frames");
    }

    public int getItemAnimationFrameTime(String item, int frame) {
        return configData.getInt("pack."+item+".animation."+frame+".time");
    }


    public String getItemModel(String item) {
        String got = configData.getString("pack."+item+".item_model");
        if (got == null) {
            configData.set("pack."+item+".item_model", item);
            reloadConfig();
            return item;
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void loadConfig() {
        configData = YamlConfiguration.loadConfiguration(configFile);
    }


}
