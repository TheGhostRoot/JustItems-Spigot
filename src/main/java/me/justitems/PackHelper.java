package me.justitems;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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

public class PackHelper {

    private final File langFile;

    private final File defaultFontFile;

    private FileConfiguration defaultFontData;

    private FileConfiguration langData;

    private final JustItems plugin;

    public PackHelper(JustItems main) {
        plugin = main;
        File dataFolder = main.getDataFolder();
        if (!dataFolder.exists()) {
            if (dataFolder.mkdirs()) {
                main.getLogger().info("Made the main folder");
            } else {
                main.getLogger().info("Can't create the main folder");
            }
        }
        generatePackFolders(main, dataFolder);

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
                if (langFile2.exists()) {
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
    }

    private static void generatePackFolders(JustItems main, File dataFolder) {
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
        pack.add("pack/assets/minecraft/blockstates");
        pack.add("pack/assets/minecraft/textures/block");
        pack.add("pack/assets/minecraft/models/block");
        pack.add("pack/assets/minecraft/textures/misc");
        pack.add("items");
        pack.add("emojis");
        pack.add("blocks");
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
    }


    public void generateResourcePack() {
        String packName = "MyResourcePack";
        File dataFolder = plugin.getDataFolder();
        File resourcePackFolder = new File(dataFolder, packName);

        // Create the resource pack folder structure
        try {

            // Copy items images to textures/item folder and create .mcmeta files
            copyItems(new File(dataFolder, "items"),
                    new File(dataFolder, "pack/assets/minecraft/textures/item"), new File(dataFolder, "pack/assets/minecraft/models/item"));

            // Copy lang
            File updatedLang = new File(resourcePackFolder, "assets/minecraft/lang/en_us.json");
            File langFolder = new File(resourcePackFolder, "assets/minecraft/lang");
            if (!langFolder.exists()) {
                if (langFolder.mkdirs()) {
                    plugin.getLogger().info("Created the folder " + langFolder.getName());
                } else {
                    plugin.getLogger().info("Created the folder " + langFolder.getName());
                }
            }
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
            File fontFolder = new File(resourcePackFolder, "assets/minecraft/font");
            if (!fontFolder.exists()) {
                if (fontFolder.mkdirs()) {
                    plugin.getLogger().info("Created the folder " + fontFolder.getName());
                } else {
                    plugin.getLogger().info("Created the folder " + fontFolder.getName());
                }
            }
            if (!updatedFont.exists()) {
                if (updatedFont.createNewFile()) {
                    plugin.getLogger().info("Created the file " + updatedFont.getName());
                } else {
                    plugin.getLogger().info("Can't create the file " + updatedFont.getName());
                }
            }
            Files.copy(defaultFontFile.toPath(), updatedFont.toPath(), StandardCopyOption.REPLACE_EXISTING);

            // Copy emojis images to textures/font folder and update default.json
            copyFonts(new File(dataFolder, "emojis"),
                    new File(dataFolder, "pack/assets/minecraft/textures/font"));


            // Blocks
            copyBlocks(new File(dataFolder, "blocks"), new File(dataFolder, "pack/assets/minecraft/textures/block"),
                    new File(dataFolder, "pack/assets/minecraft/models/block"),
                    new File(dataFolder, "pack/assets/minecraft/blockstates"));

            // Generate pack.mcmeta file
            generatePackMcMeta(new File(dataFolder, "pack"));

            copyFolder(new File(dataFolder, "pack"), resourcePackFolder);

            // Zip the resource pack folder
            zipResourcePack(resourcePackFolder, new File(dataFolder, packName + ".zip"));

            // Provide the resource pack file to all players on the server
            sendResourcePackToPlayers(dataFolder, packName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void copyBlocks(File sourceFolder, File textureFolder, File modelsFolder, File stateFolder) throws IOException {
        if (!sourceFolder.exists()) {
            if (sourceFolder.mkdirs()) {
                plugin.getLogger().info("Made a folder " + sourceFolder.getName());
            } else {
                plugin.getLogger().info("Can't create the folder " + sourceFolder.getName());
            }
        }

        if (!textureFolder.exists()) {
            if (textureFolder.mkdirs()) {
                plugin.getLogger().info("Made a folder " + textureFolder.getName());
            } else {
                plugin.getLogger().info("Can't create the folder " + textureFolder.getName());
            }
        }

        if (!modelsFolder.exists()) {
            if (modelsFolder.mkdirs()) {
                plugin.getLogger().info("Made a folder " + modelsFolder.getName());
            } else {
                plugin.getLogger().info("Can't create the folder " + modelsFolder.getName());
            }
        }


        if (!stateFolder.exists()) {
            if (stateFolder.mkdirs()) {
                plugin.getLogger().info("Made a folder " + stateFolder.getName());
            } else {
                plugin.getLogger().info("Can't create the folder " + stateFolder.getName());
            }
        }

        File[] files = sourceFolder.listFiles();
        if (files == null) { return; }
        for (String filePath : plugin.config.getBlocks()) {

            File SourceBlock = new File(sourceFolder, filePath+".png");
            if (!SourceBlock.exists())  { return; }

            File BlockTextureFolder = new File(textureFolder, getDirByFilePath(filePath).toString());
            if (!BlockTextureFolder.exists()) {
                if (BlockTextureFolder.mkdirs()) {
                    plugin.getLogger().info("Made a folder " + BlockTextureFolder.getName());
                } else {
                    plugin.getLogger().info("Can't make a folder " + BlockTextureFolder.getName());
                }
            }

            File BlockTextureFile = new File(textureFolder, filePath+".png");
            if (!BlockTextureFile.exists()) {
                if (BlockTextureFile.createNewFile()) {
                    plugin.getLogger().info("Made a file " + BlockTextureFile.getName());
                } else {
                    plugin.getLogger().info("Can't make a file " + BlockTextureFile.getName());
                }
            }

            Files.copy(SourceBlock.toPath(), BlockTextureFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            if (plugin.config.isItemAnimationEnabled(filePath)) {
                generateMcmetaAnimation(textureFolder, filePath);
            }

            String model = plugin.config.getItemModel(filePath);
            File SourceModelBlock = new File(sourceFolder, model +".json");
            if (SourceModelBlock.exists()) {

                File ModelBlockFolder = new File(modelsFolder, getDirByFilePath(model).toString());
                if (!ModelBlockFolder.exists()) {
                    if (ModelBlockFolder.mkdirs()) {
                        plugin.getLogger().info("Made a folder " + ModelBlockFolder.getName());
                    } else {
                        plugin.getLogger().info("Can't make a folder " + ModelBlockFolder.getName());
                    }
                }

                File ModelBlockFile = new File(modelsFolder, model+".json");
                if (!ModelBlockFile.exists()) {
                    if (ModelBlockFile.createNewFile()) {
                        plugin.getLogger().info("Made a file " + ModelBlockFile.getName());
                    } else {
                        plugin.getLogger().info("Can't make a file " + ModelBlockFile.getName());
                    }
                }

                Files.copy(SourceModelBlock.toPath(), ModelBlockFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            } else {
                File ModelBlockFileCreate = new File(modelsFolder, filePath+".json");
                if (!ModelBlockFileCreate.exists()) {
                    if (ModelBlockFileCreate.createNewFile()) {
                        plugin.getLogger().info("Made a file " + ModelBlockFileCreate.getName());
                    } else {
                        plugin.getLogger().info("Can't make a file " + ModelBlockFileCreate.getName());
                    }
                }
                /*
                JsonObject parent = new JsonObject();
                JsonObject textures = new JsonObject();
                parent.addProperty("parent", "block/cube_all");
                textures.addProperty("all", "block/"+filePath);
                parent.add("textures", textures);
                addJsonToFile(ModelBlockFileCreate, parent);
                 */
                String blockBottom = plugin.config.getBlockBottom(filePath);
                String blockSide = plugin.config.getBlockSide(filePath);
                String blockTop = plugin.config.getBlockTop(filePath);
                String blockFront = plugin.config.getBlockFront(filePath);
                String blockPart = plugin.config.getBlockParticle(filePath);

                File SourceBottomFolder = new File(sourceFolder, getDirByFilePath(blockBottom).toString());
                File SourceSideFolder = new File(sourceFolder, getDirByFilePath(blockSide).toString());
                File SourceTopFolder = new File(sourceFolder, getDirByFilePath(blockTop).toString());
                File SourceFrontFolder = new File(sourceFolder, getDirByFilePath(blockFront).toString());
                File SourcePartFolder = new File(sourceFolder, getDirByFilePath(blockPart).toString());

                if (!SourceBottomFolder.exists() || !SourceSideFolder.exists() ||
                        !SourceTopFolder.exists() || !SourceFrontFolder.exists() || !SourcePartFolder.exists()) {
                    plugin.getLogger().info("One of the sides is incorrect!");
                    return;
                }

                File TextureBottomFolder = new File(textureFolder, getDirByFilePath(blockBottom).toString());
                File TextureSideFolder = new File(textureFolder, getDirByFilePath(blockSide).toString());
                File TextureTopFolder = new File(textureFolder, getDirByFilePath(blockTop).toString());
                File TextureFrontFolder = new File(textureFolder, getDirByFilePath(blockFront).toString());
                File TexturePartFolder = new File(textureFolder, getDirByFilePath(blockPart).toString());

                if (!TextureBottomFolder.exists()) {
                    if (TextureBottomFolder.mkdirs()) {
                        plugin.getLogger().info("Made a folder "+TextureBottomFolder.getName());
                    } else {
                        plugin.getLogger().info("Can't make a folder "+TextureBottomFolder.getName());
                    }
                }

                if (!TextureSideFolder.exists()) {
                    if (TextureSideFolder.mkdirs()) {
                        plugin.getLogger().info("Made a folder "+TextureSideFolder.getName());
                    } else {
                        plugin.getLogger().info("Can't make a folder "+TextureSideFolder.getName());
                    }
                }

                if (!TextureTopFolder.exists()) {
                    if (TextureTopFolder.mkdirs()) {
                        plugin.getLogger().info("Made a folder "+TextureTopFolder.getName());
                    } else {
                        plugin.getLogger().info("Can't make a folder "+TextureTopFolder.getName());
                    }
                }

                if (!TextureFrontFolder.exists()) {
                    if (TextureFrontFolder.mkdirs()) {
                        plugin.getLogger().info("Made a folder "+TextureFrontFolder.getName());
                    } else {
                        plugin.getLogger().info("Can't make a folder "+TextureFrontFolder.getName());
                    }
                }

                if (!TexturePartFolder.exists()) {
                    if (TexturePartFolder.mkdirs()) {
                        plugin.getLogger().info("Made a folder "+TexturePartFolder.getName());
                    } else {
                        plugin.getLogger().info("Can't make a folder "+TexturePartFolder.getName());
                    }
                }

                File TextureBottomFile = new File(textureFolder, blockBottom+".png");
                File TextureSideFile = new File(textureFolder, blockSide +".png");
                File TextureTopFile = new File(textureFolder, blockTop +".png");
                File TextureFrontFile = new File(textureFolder, blockFront +".png");
                File TexturePartFile = new File(textureFolder, blockPart +".png");

                if (!TextureBottomFile.exists()) {
                    if (TextureBottomFile.createNewFile()) {
                        plugin.getLogger().info("Made a file "+TextureBottomFile.getName());
                    } else {
                        plugin.getLogger().info("Can't make a file "+TextureBottomFile.getName());
                    }
                }

                if (!TextureSideFile.exists()) {
                    if (TextureSideFile.createNewFile()) {
                        plugin.getLogger().info("Made a file "+TextureSideFile.getName());
                    } else {
                        plugin.getLogger().info("Can't make a file "+TextureSideFile.getName());
                    }
                }

                if (!TextureTopFile.exists()) {
                    if (TextureTopFile.createNewFile()) {
                        plugin.getLogger().info("Made a file "+TextureTopFile.getName());
                    } else {
                        plugin.getLogger().info("Can't make a file "+TextureTopFile.getName());
                    }
                }

                if (!TextureFrontFile.exists()) {
                    if (TextureFrontFile.createNewFile()) {
                        plugin.getLogger().info("Made a file "+TextureFrontFile.getName());
                    } else {
                        plugin.getLogger().info("Can't make a file "+TextureFrontFile.getName());
                    }
                }

                if (!TexturePartFile.exists()) {
                    if (TexturePartFile.createNewFile()) {
                        plugin.getLogger().info("Made a file "+TexturePartFile.getName());
                    } else {
                        plugin.getLogger().info("Can't make a file "+TexturePartFile.getName());
                    }
                }

                Files.copy(new File(sourceFolder, blockBottom+".png").toPath(), TextureBottomFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                Files.copy(new File(sourceFolder, blockSide +".png").toPath(), TextureSideFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                Files.copy(new File(sourceFolder, blockTop +".png").toPath(), TextureTopFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                Files.copy(new File(sourceFolder, blockFront +".png").toPath(), TextureFrontFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                Files.copy(new File(sourceFolder, blockPart +".png").toPath(), TexturePartFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                JsonObject block = new JsonObject();
                block.addProperty("parent", "block/block");
                JsonObject AllTextuers = new JsonObject();
                JsonObject textuers = new JsonObject();
                textuers.addProperty("bottom", blockBottom);
                textuers.addProperty("side", blockSide);
                textuers.addProperty("top", blockTop);
                textuers.addProperty("front", blockFront);
                textuers.addProperty("particle", blockPart);
                AllTextuers.add("textures", textuers);

                JsonArray elements = new JsonArray();
                JsonObject element = new JsonObject();
                element.addProperty("from", "[0, 0, 0]");
                element.addProperty("to", "[16, 16, 16]");

                JsonObject faces = new JsonObject();
                JsonObject downFace = new JsonObject();
                downFace.addProperty("uv", "[0, 0, 16, 16]");
                downFace.addProperty("texture", "#top");
                downFace.addProperty("cullface", "down");
                faces.add("down", downFace);

                JsonObject upFace = new JsonObject();
                upFace.addProperty("uv", "[0, 16, 16, 0]");
                upFace.addProperty("texture", "#top");
                upFace.addProperty("cullface", "up");
                faces.add("up", upFace);

                JsonObject northFace = new JsonObject();
                northFace.addProperty("uv", "[0, 0, 16, 16]");
                northFace.addProperty("texture", "#front");
                northFace.addProperty("cullface", "north");
                faces.add("north", northFace);

                JsonObject southFace = new JsonObject();
                southFace.addProperty("uv", "[0, 0, 16, 16]");
                southFace.addProperty("texture", "#bottom");
                southFace.addProperty("cullface", "south");
                faces.add("south", southFace);

                JsonObject westFace = new JsonObject();
                westFace.addProperty("uv", "[0, 0, 16, 16]");
                westFace.addProperty("texture", "#side");
                westFace.addProperty("cullface", "west");
                faces.add("west", westFace);

                JsonObject eastFace = new JsonObject();
                eastFace.addProperty("uv", "[0, 0, 16, 16]");
                eastFace.addProperty("texture", "#side");
                eastFace.addProperty("cullface", "east");
                faces.add("east", eastFace);

                element.add("faces", faces);
                elements.add(element);

                block.add("elements", elements);
                addJsonToFile(ModelBlockFileCreate, block);
            }

            File BlockStates = new File(stateFolder, plugin.config.getItemType(filePath)+".json");
            if (!BlockStates.exists()) {
                if (BlockStates.createNewFile()) {
                    plugin.getLogger().info("Made a file " + BlockStates.getName());
                } else {
                    plugin.getLogger().info("Can't make a file " + BlockStates.getName());
                }
            }

            JsonObject vari = new JsonObject();
            JsonObject allVari = new JsonObject();
            JsonObject modelVari = new JsonObject();
            modelVari.addProperty("model", "block/"+filePath);
            allVari.add("", modelVari);
            vari.add("variants", allVari);
            addJsonToFile(BlockStates, vari);
        }
    }

    private static StringBuilder getDirByFilePath(String filePath) {
        StringBuilder dir = new StringBuilder();
        String[] dirs = filePath.split("/");
        for (int i = 0; i < dirs.length - 1; i++) {
            dir.append("/").append(dirs[i]);
        }
        return dir;
    }

    public void addJsonToFile(File file, String key, String value) {
        JsonObject json = new JsonObject();
        json.addProperty(key, value);

        // Write the JSON string to a file
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(json));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addJsonToFile(File file, String key, JsonObject value) {
        JsonObject json = new JsonObject();
        json.add(key, value);

        // Write the JSON string to a file
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(json));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addJsonToFile(File file, JsonObject value) {

        // Write the JSON string to a file
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(value));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addJsonToFile(File file, String key, int value) {
        JsonObject json = new JsonObject();
        json.addProperty(key, value);

        // Write the JSON string to a file
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(json));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addJsonToFile(File file, String key, JsonArray value) {
        JsonObject json = new JsonObject();
        json.add(key, value);

        // Write the JSON string to a file
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(json));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeJsonToFile(File file, String key) {
        JsonObject json;
        try (FileReader reader = new FileReader("path/to/file.json")) {
            json = new Gson().fromJson(reader, JsonObject.class);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        json.remove(key);
        // Write the modified JSON string back to the file
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(json));
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
                if (destinationFile.isDirectory() && !destinationFile.exists()) {
                    if (destinationFile.mkdirs()) {
                        plugin.getLogger().info("Made a folder " + destinationFile.getName());
                    } else {
                        plugin.getLogger().info("Can't make a folder " + destinationFile.getName());
                    }
                } else {
                    Files.copy(file.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
    }

    public void copyItems(File sourceFolder, File texturesItemFolder, File modelFile) throws IOException {
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

        File[] files = sourceFolder.listFiles();
        if (files == null) {
            return;
        }

        for (String filePath : plugin.config.getItems()) {
            // new_items/item
            if (filePath.contains(".")) {
                filePath = filePath.substring(0, filePath.lastIndexOf('.'));
            }
            File SourceItem = new File(sourceFolder, filePath + ".png");
            if (!SourceItem.exists()) {
                return;
            }

            File destFolder = new File(texturesItemFolder, getDirByFilePath(filePath).toString());
            if (!destFolder.exists()) {
                if (destFolder.mkdirs()) {
                    plugin.getLogger().info("Made a folder " + destFolder.getName());
                } else {
                    plugin.getLogger().info("Can't make a folder " + destFolder.getName());
                    return;
                }
            }
            File DestinationItem = new File(texturesItemFolder, filePath + ".png");
            if (!DestinationItem.exists()) {
                if (DestinationItem.createNewFile()) {
                    plugin.getLogger().info("Made a file " + DestinationItem.getName());
                } else {
                    plugin.getLogger().info("Can't make a file " + DestinationItem.getName());
                    return;
                }
            }

            Files.copy(SourceItem.toPath(), DestinationItem.toPath(),
                    StandardCopyOption.REPLACE_EXISTING);

            if (plugin.config.isItemAnimationEnabled(filePath)) {
                generateMcmetaAnimation(texturesItemFolder, filePath);
            }

            String itemModel = plugin.config.getItemModel(filePath);
            File SourceModel = new File(sourceFolder, itemModel + ".json");
            if (SourceModel.exists()) {
                File DestinationModel = new File(modelFile, itemModel + ".json");
                if (!DestinationModel.exists()) {
                    if (DestinationModel.createNewFile()) {
                        plugin.getLogger().info("Made a file " + DestinationModel.getName());
                        Files.copy(SourceModel.toPath(), DestinationItem.toPath(),
                                StandardCopyOption.REPLACE_EXISTING);
                    } else {
                        plugin.getLogger().info("Can't make a file " + DestinationModel.getName());
                    }
                }
            } else {

                String itemType = plugin.config.getItemType(filePath);
                File modelFileType = new File(modelFile, itemType + ".json");

                File modelFilePath = new File(modelFile, getDirByFilePath(filePath).toString());
                File modelFileJson = new File(modelFile, filePath + ".json");

                if (!modelFilePath.exists()) {
                    if (modelFilePath.mkdirs()) {
                        plugin.getLogger().info("Made a folder " + modelFilePath.getName());
                    } else {
                        plugin.getLogger().info("Can't make a folder " + modelFilePath.getName());
                        return;
                    }
                }

                if (!modelFileJson.exists()) {
                    if (modelFileJson.createNewFile()) {
                        plugin.getLogger().info("Made a file " + modelFileJson.getName());
                    } else {
                        plugin.getLogger().info("Can't make a file " + modelFileJson.getName());
                        return;
                    }
                }

                File modelFileTypeFolder = new File(modelFile, getDirByFilePath(itemType).toString());

                if (!modelFileTypeFolder.exists()) {
                    if (modelFileTypeFolder.mkdirs()) {
                        plugin.getLogger().info("Made a folder " + modelFileTypeFolder.getName());
                    } else {
                        plugin.getLogger().info("Can't make a folder " + modelFileTypeFolder.getName());
                        return;
                    }
                }

                if (!modelFileType.exists()) {
                    if (modelFileType.createNewFile()) {
                        plugin.getLogger().info("Made a file " + modelFileType.getName());
                    } else {
                        plugin.getLogger().info("Can't make a file " + modelFileType.getName());
                        return;
                    }
                }

                // Model
                JsonObject Model = new JsonObject();
                JsonObject texturesModel = new JsonObject();
                texturesModel.addProperty("layer0", "item/" + filePath);
                Model.addProperty("parent", "item/handheld");
                Model.add("textures", texturesModel);
                addJsonToFile(modelFileJson, Model);

                // Item type model
                JsonObject OverrideModel = new JsonObject();
                JsonObject textures = new JsonObject();
                textures.addProperty("layer0", "item/" + itemType);
                OverrideModel.addProperty("parent", "item/handheld");
                OverrideModel.add("textures", textures);
                // overrides
                JsonArray overrides = new JsonArray();
                JsonObject override = new JsonObject();
                JsonObject overrideModel = new JsonObject();
                overrideModel.addProperty("custom_model_data", plugin.config.getItemModelData(filePath));
                override.addProperty("model", "item/" + filePath);
                override.add("predicate", overrideModel);
                overrides.add(override);
                OverrideModel.add("overrides", overrides);
                addJsonToFile(modelFileType, OverrideModel);
            }
        }
    }

    private void generateMcmetaAnimation(File texturesItemFolder, String filePath) {
        JsonArray animation = new JsonArray();
        for (int i = 0; i < plugin.config.getItemAnimationFrames(filePath); i++) {
            JsonObject ani1 = new JsonObject();
            ani1.addProperty("index", i);
            ani1.addProperty("time", plugin.config.getItemAnimationFrameTime(filePath, i + 1));
            animation.add(ani1);
        }
        JsonObject frames = new JsonObject();
        frames.add("frames", animation);
        addJsonToFile(new File(texturesItemFolder, filePath + ".png.mcmeta"), "animation", frames);
    }

    public void copyFonts(File emojisFolder, File texturesFontFolder) throws IOException {
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
        if (imageFiles == null) {
            return;
        }

        JsonArray providers = new JsonArray();


        for (String filePath : plugin.config.getEmojis()) {

            File SourceFont = new File(emojisFolder, filePath + ".png");
            if (!SourceFont.exists()) {
                return;
            }

            File destFolder = new File(texturesFontFolder, getDirByFilePath(filePath).toString());
            if (!destFolder.exists()) {
                if (destFolder.mkdirs()) {
                    plugin.getLogger().info("Made a folder " + destFolder.getName());
                } else {
                    plugin.getLogger().info("Can't make a folder " + destFolder.getName());
                    return;
                }
            }
            File DestinationItem = new File(texturesFontFolder, filePath + ".png");
            if (!DestinationItem.exists()) {
                if (DestinationItem.createNewFile()) {
                    plugin.getLogger().info("Made a file " + DestinationItem.getName());
                } else {
                    plugin.getLogger().info("Can't make a file " + DestinationItem.getName());
                    return;
                }
            }

            Files.copy(SourceFont.toPath(), DestinationItem.toPath(),
                    StandardCopyOption.REPLACE_EXISTING);

            if (plugin.config.isItemAnimationEnabled(filePath)) {
                generateMcmetaAnimation(texturesFontFolder, filePath);
            }

            JsonObject font = new JsonObject();
            font.addProperty("type", plugin.config.getEmojiType(filePath));
            font.addProperty("file", "minecraft:font/"+filePath+".png");
            font.addProperty("height", plugin.config.getEmojiHeight(filePath));
            font.addProperty("ascent", plugin.config.getEmojiAscent(filePath));
            JsonArray charsStuff = new JsonArray();
            for (String c : plugin.config.getEmojiChars(filePath)) {
                charsStuff.add(c);
            }
            font.add("chars", charsStuff);
            providers.add(font);
        }
        addJsonToFile(defaultFontFile, "providers", providers);
    }

    public void generatePackMcMeta(File resourcePackFolder) throws IOException {
        JsonObject pack = new JsonObject();
        JsonObject pack2 = new JsonObject();
        pack.addProperty("pack_format", plugin.config.getPackFormat());
        pack.addProperty("description", plugin.config.getPackDescription());
        pack2.add("pack", pack);
        addJsonToFile(new File(resourcePackFolder, "pack.mcmeta"), pack2);
    }

    private void zipResourcePack(File resourcePackFolder, File zipFile) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(zipFile);
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            zipFolder(resourcePackFolder, resourcePackFolder.getName(), zos);
        }
    }

    public void zipFolder(File folder, String parentFolder, ZipOutputStream zos) throws IOException {
        File[] files = folder.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
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

    public void reloadFont() {
        saveFont();
        loadFonts();
    }

    public void reloadLang() {
        saveLang();
        loadLang();
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


    public void loadLang() {
        langData = YamlConfiguration.loadConfiguration(langFile);
    }

    public void loadFonts() {
        defaultFontData = YamlConfiguration.loadConfiguration(defaultFontFile);
    }
}
