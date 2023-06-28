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

    private final File langFile;

    private final File defaultFontFile;


    private FileConfiguration defaultFontData;

    private FileConfiguration langData;

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
        List<String> pack = new ArrayList<>();
        pack.add("pack");
        pack.add("pack/assets");
        pack.add("pack/assets/minecraft");
        pack.add("pack/assets/minecraft/font");
        pack.add("pack/assets/minecraft/textures");
        pack.add("pack/assets/minecraft/textures/font");
        pack.add("pack/assets/minecraft/textures/item");
        pack.add("pack/assets/minecraft/models");
        pack.add("pack/assets/minecraft/models/item");
        pack.add("pack/assets/minecraft/lang");
        pack.add("items");
        pack.add("emojis");
        for (String folder : pack) {
            File f = new File(dataFolder, folder);
            if (!f.exists()) {
                if (f.mkdirs()) {
                    main.getLogger().info("Made a folder " + pack);
                } else {
                    main.getLogger().info("Can't create the folder " + pack);
                }
            }
        }
        defaultFontFile = new File(dataFolder, "pack/assets/minecraft/font/default.json");
        if (!defaultFontFile.exists()) {
            try {
                if (defaultFontFile.createNewFile()) {
                    plugin.getLogger().info("Made a file " + defaultFontFile.getName());
                } else {
                    plugin.getLogger().info("Can't create the file " + defaultFontFile.getName());
                }
            } catch (Exception e) {
                plugin.getLogger().info("Can't create the file " + defaultFontFile.getName());
                e.printStackTrace();
            }
            loadFonts();
            defaultFontData.options().copyDefaults(true);
            try {
                prepareFont();
            }catch (Exception e) {
                plugin.getLogger().info("Can't write in " + defaultFontFile.getName());
                e.printStackTrace();
            }
        } else {
            loadFonts();
            defaultFontData.options().copyDefaults(false);
        }
        langFile = new File(dataFolder, "pack/assets/minecraft/lang/en_us.json");
        if (!langFile.exists()) {
            try {
                if (langFile.createNewFile()) {
                    plugin.getLogger().info("Made a file " + langFile.getName());
                } else {
                    plugin.getLogger().info("Can't create the file " + langFile.getName());
                }
            } catch (Exception e) {
                plugin.getLogger().info("Can't create the file " + langFile.getName());
                e.printStackTrace();
            }
        }
        File langFile2 = new File(dataFolder, "en_us.json");
        if (!langFile2.exists()) {
            main.saveResource("en_us.json", false);
            try {
                Files.copy(langFile2.toPath(), langFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                if (langFile2.exists()){
                    if (langFile2.delete()) {
                        plugin.getLogger().info("Deleted en_us.json");
                    } else {
                        plugin.getLogger().info("Can't delete en_us.json");
                    }
                }
            } catch (Exception e) {
                plugin.getLogger().info("Can't copy the file en_us.json");
                e.printStackTrace();
            }
            loadLang();
            langData.options().copyDefaults(true);
        } else {
            loadLang();
            langData.options().copyDefaults(false);
        }
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

    public void prepareFont() throws IOException {
        FileWriter writer = new FileWriter(defaultFontFile);

        writer.write("{\n");
        writer.write("    \"providers\": [\n");
        writer.write("        {\n");
        writer.write("            \"type\": \"bitmap\",\n");
        writer.write("            \"file\": \"minecraft:font/member.png\",\n");
        writer.write("            \"height\": 10,\n");
        writer.write("            \"ascent\": 9,\n");
        writer.write("            \"chars\": [\"\\uE001\"]\n");
        writer.write("        }\n");
        writer.write("}\n");

        writer.close();
    }
    public void generateResourcePack() {
        String packName = "MyResourcePack";
        String packDescription = "This is a custom resource pack";
        String packVersion = "1.0";
        File dataFolder = plugin.getDataFolder();
        File resourcePackFolder = new File(dataFolder, packName);

        // Create the resource pack folder structure
        try {
            copyFolder(new File(dataFolder, "pack"), resourcePackFolder);

            // Copy items images to textures/item folder and create .mcmeta files
            copyImagesWithMetadata(new File(dataFolder, "items"),
                    new File(resourcePackFolder, "assets/minecraft/textures/item"), new File(resourcePackFolder, "assets/minecraft/models/item"));

            copyImagesWithMetadata(new File(dataFolder, "items"),
                    new File(dataFolder, "pack/assets/minecraft/textures/item"), new File(dataFolder, "pack/assets/minecraft/models/item"));

            // Copy lang
            File updatedLang = new File(resourcePackFolder, "assets/minecraft/lang/en_us.json");
            if (!updatedLang.exists()) {
                if (updatedLang.createNewFile()) {
                    plugin.getLogger().info("Created the file " + updatedLang.getName());
                } else {
                    plugin.getLogger().info("Can't create the file " + updatedLang.getName());
                }
            }
            Files.copy(langFile.toPath(), updatedLang.toPath(), StandardCopyOption.REPLACE_EXISTING);


            // Copy fonts
            File updatedFont = new File(resourcePackFolder, "assets/minecraft/font/default.json");
            if (!updatedFont.exists()) {
                if (updatedFont.createNewFile()) {
                    plugin.getLogger().info("Created the file " + updatedFont.getName());
                } else {
                    plugin.getLogger().info("Can't create the file " + updatedFont.getName());
                }
            }
            Files.copy(defaultFontFile.toPath(), updatedFont.toPath(), StandardCopyOption.REPLACE_EXISTING);

            // Copy emojis images to textures/font folder and update default.json
            updateDefaultJson(new File(dataFolder, "emojis"),
                    new File(resourcePackFolder, "assets/minecraft/textures/font"));

            updateDefaultJson(new File(dataFolder, "emojis"),
                    new File(dataFolder, "pack/assets/minecraft/textures/font"));

            // Generate pack.mcmeta file
            generatePackMcMeta(resourcePackFolder, packDescription, packVersion);
            generatePackMcMeta(new File(dataFolder, "pack"), packDescription, packVersion);

            // Zip the resource pack folder
            zipResourcePack(resourcePackFolder, new File(dataFolder, packName + ".zip"));

            // Provide the resource pack file to all players on the server
            sendResourcePackToPlayers(dataFolder, packName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void copyFolder(File sourceFolder, File destinationFolder) throws IOException {
        if (!sourceFolder.exists()) {
            if (sourceFolder.mkdirs()) {
                plugin.getLogger().info("Made a folder " + sourceFolder.getName());
            } else {
                plugin.getLogger().info("Can't create the folder " + sourceFolder.getName());
            }
        }

        if (!destinationFolder.exists()) {
            if (destinationFolder.mkdirs()) {
                plugin.getLogger().info("Made a folder " + destinationFolder.getName());
            } else {
                plugin.getLogger().info("Can't create the folder " + destinationFolder.getName());
            }
        }

        File[] files = sourceFolder.listFiles();
        if (files != null) {
            for (File file : files) {
                File destinationFile = new File(destinationFolder, file.getName());
                if (file.isDirectory() && !file.exists()) {
                    if (file.mkdirs()) {
                        plugin.getLogger().info("Made a folder " + file.getName());
                    } else {
                        plugin.getLogger().info("Can't make a folder " + file.getName());
                    }
                } else {
                    Files.copy(file.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
    }

    public void copyImagesWithMetadata(File sourceFolder, File texturesItemFolder, File modelFile) throws IOException {
        if (!sourceFolder.exists() || !sourceFolder.isDirectory()) {
            if (sourceFolder.mkdirs()) {
                plugin.getLogger().info("Made a folder " + sourceFolder.getName());
            } else {
                plugin.getLogger().info("Can't create the folder " + sourceFolder.getName());
            }
        }

        if (!texturesItemFolder.exists()) {
            if (texturesItemFolder.mkdirs()) {
                plugin.getLogger().info("Made a folder " + texturesItemFolder.getName());
            } else {
                plugin.getLogger().info("Can't create the folder " + texturesItemFolder.getName());
            }
        }

        if (!modelFile.exists()) {
            if (modelFile.mkdirs()) {
                plugin.getLogger().info("Made a folder " + modelFile.getName());
            } else {
                plugin.getLogger().info("Can't create the folder " + modelFile.getName());
            }
        }

        File[] imageFiles = sourceFolder.listFiles();
        if (imageFiles != null) {
            for (File imageFile : imageFiles) {
                String imageName = imageFile.getName();
                String itemName = imageName.substring(0, imageName.lastIndexOf('.'));
                String jsonContent =
                        "{\n" +
                                "  \"parent\": \"item/handheld\",\n" +
                                "  \"textures\": {\n" +
                                "    \"layer0\": \"item/" + itemName + "\"\n" +
                                "  }\n" +
                                "}";

                try (FileWriter writer = new FileWriter(new File(modelFile, itemName + ".json"))) {
                    writer.write(jsonContent);
                }

                try (FileWriter writer = new FileWriter(new File(texturesItemFolder, itemName + ".png.mcmeta"))) {
                    writer.write(
                            "{\n" +
                                    "  \"animation\": {\n" +
                                    "    \"frames\": [\n" +
                                    "        {\"index\":0, \"time\":10},\n" +
                                    "        {\"index\":1, \"time\":10}\n" +
                                    "    ]\n" +
                                    "  }\n" +
                                    "}");
                }

                Files.copy(imageFile.toPath(), new File(texturesItemFolder, imageName).toPath(),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }

    public void updateDefaultJson(File emojisFolder, File texturesFontFolder) throws IOException {
        if (!emojisFolder.exists() || !emojisFolder.isDirectory()) {
            if (emojisFolder.mkdirs()) {
                plugin.getLogger().info("Made a folder " + emojisFolder.getName());
            } else {
                plugin.getLogger().info("Can't create the folder " + emojisFolder.getName());
            }
        }

        if (!texturesFontFolder.exists()) {
            if (texturesFontFolder.mkdirs()) {
                plugin.getLogger().info("Made a folder " + texturesFontFolder.getName());
            } else {
                plugin.getLogger().info("Can't create the folder " + texturesFontFolder.getName());
            }
        }

        File[] imageFiles = emojisFolder.listFiles();
        StringBuilder stringBuilder = new StringBuilder();
        if (imageFiles != null) {
            for (File imageFile : imageFiles) {
                String imageName = imageFile.getName();
                //stringBuilder.append()

                Files.copy(imageFile.toPath(), new File(texturesFontFolder, imageName).toPath(),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        }

        try (FileWriter writer = new FileWriter(defaultFontFile)) {
            writer.write(stringBuilder.toString());
        }
    }

    public void generatePackMcMeta(File resourcePackFolder, String packDescription, String packVersion) throws IOException {
        FileWriter writer = new FileWriter(new File(resourcePackFolder, "pack.mcmeta"));

        writer.write("{\n");
        writer.write("  \"pack\": {\n");
        writer.write("    \"pack_format\": " + packVersion + ",\n");
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

    public void zipFolder(File folder, String parentFolder, ZipOutputStream zos) throws IOException {
        if (folder.listFiles() == null) {
            return;
        }
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

    public void sendResourcePackToPlayers(File data, String name) throws IOException {
        File resourcePackFile = new File(data, name + ".zip");
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

    public void reloadFont() {
        saveFont();
        loadFonts();
    }

    public void reloadLang() {
        saveLang();
        loadLang();
    }

    public void saveConfig() {
        try {
            configData.save(configFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveLang() {
        try {
            langData.save(langFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveFont() {
        try {
            defaultFontData.save(defaultFontFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadConfig() {
        configData = YamlConfiguration.loadConfiguration(configFile);
    }

    public void loadLang() {
        langData = YamlConfiguration.loadConfiguration(langFile);
    }

    public void loadFonts() {
        defaultFontData = YamlConfiguration.loadConfiguration(defaultFontFile);
    }


}
