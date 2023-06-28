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
import java.util.Objects;
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



    private void generateResourcePack() {
        String packName = "MyResourcePack";
        String packDescription = "This is a custom resource pack";
        String packVersion = "1.0";
        File packFolder = new File(plugin.getDataFolder(), "pack");
        File resourcePackFolder = new File(plugin.getDataFolder(), packName);

        // Create the resource pack folder structure
        try {
            copyFolder(packFolder, resourcePackFolder);

            // Copy items images to textures/item folder and create .mcmeta files
            File itemsFolder = new File(plugin.getDataFolder(), "items");
            File texturesItemFolder = new File(resourcePackFolder, "assets/minecraft/textures/item");
            File modelsItemFolder = new File(resourcePackFolder, "assets/minecraft/textures/models/item");
            copyImagesWithMetadata(itemsFolder, texturesItemFolder, modelsItemFolder);

            // Copy emojis images to textures/font folder and update default.json
            File emojisFolder = new File(plugin.getDataFolder(), "emojis");
            File texturesFontFolder = new File(resourcePackFolder, "assets/minecraft/textures/font");
            updateDefaultJson(emojisFolder, texturesFontFolder);

            // Generate pack.mcmeta file
            generatePackMcMeta(resourcePackFolder, packName, packDescription, packVersion);

            // Zip the resource pack folder
            File zipFile = new File(plugin.getDataFolder(), packName + ".zip");
            zipResourcePack(resourcePackFolder, zipFile);

            // Provide the resource pack file to all players on the server
            sendResourcePackToPlayers(packName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void copyFolder(File sourceFolder, File destinationFolder) throws IOException {
        if (!sourceFolder.exists() || !sourceFolder.isDirectory()) {
            throw new IllegalArgumentException("Source folder does not exist or is not a directory");
        }

        if (!destinationFolder.exists()) {
            destinationFolder.mkdirs();
        }

        File[] files = sourceFolder.listFiles();
        if (files != null) {
            for (File file : files) {
                File destinationFile = new File(destinationFolder, file.getName());
                if (file.isDirectory()) {
                    copyFolder(file, destinationFile);
                } else {
                    Files.copy(file.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
    }

    private void copyImagesWithMetadata(File sourceFolder, File texturesItemFolder, File modelsItemFolder) throws IOException {
        if (!sourceFolder.exists() || !sourceFolder.isDirectory()) {
            throw new IllegalArgumentException("Source folder does not exist or is not a directory");
        }

        if (!texturesItemFolder.exists()) {
            texturesItemFolder.mkdirs();
        }

        if (!modelsItemFolder.exists()) {
            modelsItemFolder.mkdirs();
        }

        File[] imageFiles = sourceFolder.listFiles();
        if (imageFiles != null) {
            for (File imageFile : imageFiles) {
                String imageName = imageFile.getName();
                String itemName = imageName.substring(0, imageName.lastIndexOf('.'));
                String jsonContent = "{\n" +
                        "  \"parent\": \"item/generated\",\n" +
                        "  \"textures\": {\n" +
                        "    \"layer0\": \"minecraft:item/" + itemName + "\"\n" +
                        "  }\n" +
                        "}";
                File jsonFile = new File(modelsItemFolder, itemName + ".json");
                File mcmetaFile = new File(texturesItemFolder, itemName + ".png.mcmeta");

                try (FileWriter writer = new FileWriter(jsonFile)) {
                    writer.write(jsonContent);
                }

                try (FileWriter writer = new FileWriter(mcmetaFile)) {
                    writer.write("{\n" +
                            "  \"texture\": {\n" +
                            "    \"meta\": {\n" +
                            "      \"model\": \"item/" + itemName + "\"\n" +
                            "    }\n" +
                            "  }\n" +
                            "}");
                }

                Files.copy(imageFile.toPath(), new File(texturesItemFolder, imageName).toPath(),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }

    private void updateDefaultJson(File emojisFolder, File texturesFontFolder) throws IOException {
        if (!emojisFolder.exists() || !emojisFolder.isDirectory()) {
            throw new IllegalArgumentException("Emojis folder does not exist or is not a directory");
        }

        if (!texturesFontFolder.exists()) {
            texturesFontFolder.mkdirs();
        }

        File defaultJsonFile = new File(texturesFontFolder, "default.json");
        StringBuilder jsonBuilder = new StringBuilder();

        if (defaultJsonFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(defaultJsonFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonBuilder.append(line).append("\n");
                }
            }
        } else {
            jsonBuilder.append("{\n").append("  \"providers\": []\n").append("}\n");
        }

        File[] imageFiles = emojisFolder.listFiles();
        if (imageFiles != null) {
            for (File imageFile : imageFiles) {
                String imageName = imageFile.getName();
                String imageJson = "{\n" +
                        "  \"id\": \"" + imageName + "\",\n" +
                        "  \"file\": \"" + imageName + "\"\n" +
                        "}";
                jsonBuilder.insert(jsonBuilder.length() - 3, imageJson + ",\n");
                Files.copy(imageFile.toPath(), new File(texturesFontFolder, imageName).toPath(),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        }

        try (FileWriter writer = new FileWriter(defaultJsonFile)) {
            writer.write(jsonBuilder.toString());
        }
    }

    private void generatePackMcMeta(File resourcePackFolder, String packName, String packDescription, String packVersion) throws IOException {
        File packMcmetaFile = new File(resourcePackFolder, "pack.mcmeta");
        FileWriter writer = new FileWriter(packMcmetaFile);

        writer.write("{\n");
        writer.write("  \"pack\": {\n");
        writer.write("    \"pack_format\": 7,\n");
        writer.write("    \"description\": \"" + packDescription + "\"\n");
        writer.write("  }\n");
        writer.write("}");

        writer.close();
    }

    private void zipResourcePack(File resourcePackFolder, File zipFile) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(zipFile);
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            zipFolder(resourcePackFolder, resourcePackFolder.getName(), zos);
        }
    }

    private void zipFolder(File folder, String parentFolder, ZipOutputStream zos) throws IOException {
        if (folder.listFiles() == null) { return; }
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                zipFolder(file, parentFolder + "/" + file.getName(), zos);
            } else {
                ZipEntry zipEntry = new ZipEntry(parentFolder + "/" + file.getName());
                zos.putNextEntry(zipEntry);

                try (FileInputStream fis = new FileInputStream(file)) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        zos.write(buffer, 0, length);
                    }
                }

                zos.closeEntry();
            }
        }
    }

    private void sendResourcePackToPlayers(String name) throws IOException {
        File resourcePackFile = new File(plugin.getDataFolder(), name + ".zip");
        String resourcePackUrl = resourcePackFile.toURI().toString();
        byte[] resourcePackHash = calculateSHA1(resourcePackFile);

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setResourcePack(resourcePackUrl, resourcePackHash);
        }
    }

    public byte[] calculateSHA1(File file) throws IOException {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-1");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        try (FileInputStream fis = new FileInputStream(file);
             DigestInputStream dis = new DigestInputStream(fis, digest)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = dis.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }
            digest = dis.getMessageDigest();
        }

        return digest.digest();
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
