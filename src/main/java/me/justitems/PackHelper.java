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
        pack.add("pack/assets/minecraft/models/armor");
        pack.add("pack/assets/minecraft/textures/models");
        pack.add("pack/assets/minecraft/textures/models/armor");
        pack.add("pack/assets/minecraft/textures/misc");
        pack.add("pack/assets/minecraft/textures/map");
        pack.add("pack/assets/minecraft/textures/environment");
        pack.add("pack/assets/minecraft/textures/gui");
        pack.add("pack/assets/minecraft/textures/gui/advancements");
        pack.add("pack/assets/minecraft/textures/gui/advancements/backgrounds");
        pack.add("pack/assets/minecraft/textures/gui/container");
        pack.add("pack/assets/minecraft/textures/gui/container/creative_inventory");
        pack.add("items");
        pack.add("maps");
        pack.add("environments");
        pack.add("emojis");
        pack.add("blocks");
        pack.add("armors");
        pack.add("hats");
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

    public void loadGUI(File gui) throws IOException {
        File dataFolder = plugin.getDataFolder();

        plugin.saveResource("accessibility.png", true);
        plugin.saveResource("bars.png", true);
        plugin.saveResource("book.png", true);
        plugin.saveResource("checkbox.png", true);
        plugin.saveResource("demo_background.png", true);
        plugin.saveResource("icons.png", true);
        plugin.saveResource("options_background.png", true);
        plugin.saveResource("recipe_book.png", true);
        plugin.saveResource("recipe_button.png", true);
        plugin.saveResource("resource_packs.png", true);
        plugin.saveResource("server_selection.png", true);
        plugin.saveResource("social_interactions.png", true);
        plugin.saveResource("stream_indicator.png", true);
        plugin.saveResource("toasts.png", true);
        plugin.saveResource("widgets.png", true);
        plugin.saveResource("world_selection.png", true);

        File accessibility = new File(gui, "accessibility.png");
        if (!accessibility.exists()) {
            accessibility.createNewFile();
        }
        File bars = new File(gui, "bars.png");
        if (!bars.exists()) {
            bars.createNewFile();
        }
        File book = new File(gui, "book.png");
        if (!book.exists()) {
            book.createNewFile();
        }
        File checkbox = new File(gui, "checkbox.png");
        if (!checkbox.exists()) {
            checkbox.createNewFile();
        }
        File demo_background = new File(gui, "demo_background.png");
        if (!demo_background.exists()) {
            demo_background.createNewFile();
        }
        File icons = new File(gui, "icons.png");
        if (!icons.exists()) {
            icons.createNewFile();
        }
        File options_background = new File(gui, "options_background.png");
        if (!options_background.exists()) {
            options_background.createNewFile();
        }
        File recipe_book = new File(gui, "recipe_book.png");
        if (!recipe_book.exists()) {
            recipe_book.createNewFile();
        }
        File recipe_button = new File(gui, "recipe_button.png");
        if (!recipe_button.exists()) {
            recipe_button.createNewFile();
        }
        File resource_packs = new File(gui, "resource_packs.png");
        if (!resource_packs.exists()) {
            resource_packs.createNewFile();
        }
        File server_selection = new File(gui, "server_selection.png");
        if (!server_selection.exists()) {
            server_selection.createNewFile();
        }
        File social_interactions = new File(gui, "social_interactions.png");
        if (!social_interactions.exists()) {
            social_interactions.createNewFile();
        }
        File stream_indicator = new File(gui, "stream_indicator.png");
        if (!stream_indicator.exists()) {
            stream_indicator.createNewFile();
        }
        File toasts = new File(gui, "toasts.png");
        if (!toasts.exists()) {
            toasts.createNewFile();
        }
        File widgetsGUI = new File(gui, "widgets.png");
        if (!widgetsGUI.exists()) {
            widgetsGUI.createNewFile();
        }
        File world_selection = new File(gui, "world_selection.png");
        if (!world_selection.exists()) {
            world_selection.createNewFile();
        }

        File accessibilitySource = new File(dataFolder, "accessibility.png");
        File barsSource = new File(dataFolder, "bars.png");
        File bookSource = new File(dataFolder, "book.png");
        File checkboxSource = new File(dataFolder, "checkbox.png");
        File demo_backgroundSource = new File(dataFolder, "demo_background.png");
        File iconsSource = new File(dataFolder, "icons.png");
        File options_backgroundSource = new File(dataFolder, "options_background.png");
        File recipe_bookSource = new File(dataFolder, "recipe_book.png");
        File recipe_buttonSource = new File(dataFolder, "recipe_button.png");
        File resource_packsSource = new File(dataFolder, "resource_packs.png");
        File server_selectionSource = new File(dataFolder, "server_selection.png");
        File social_interactionsSource = new File(dataFolder, "social_interactions.png");
        File stream_indicatorSource = new File(dataFolder, "stream_indicator.png");
        File toastsSource = new File(dataFolder, "toasts.png");
        File widgetsSource = new File(dataFolder, "widgets.png");
        File world_selectionSource = new File(dataFolder, "world_selection.png");

        Files.copy(accessibilitySource.toPath(), accessibility.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(barsSource.toPath(), bars.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(bookSource.toPath(), book.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(checkboxSource.toPath(), checkbox.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(demo_backgroundSource.toPath(), demo_background.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(iconsSource.toPath(), icons.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(options_backgroundSource.toPath(), options_background.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(recipe_bookSource.toPath(), recipe_book.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(recipe_buttonSource.toPath(), recipe_button.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(resource_packsSource.toPath(), resource_packs.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(server_selectionSource.toPath(), server_selection.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(social_interactionsSource.toPath(), social_interactions.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(stream_indicatorSource.toPath(), stream_indicator.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(toastsSource.toPath(), toasts.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(widgetsSource.toPath(), widgetsGUI.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(world_selectionSource.toPath(), world_selection.toPath(), StandardCopyOption.REPLACE_EXISTING);

        accessibilitySource.delete();
        barsSource.delete();
        bookSource.delete();
        checkboxSource.delete();
        demo_backgroundSource.delete();
        iconsSource.delete();
        options_backgroundSource.delete();
        recipe_bookSource.delete();
        recipe_buttonSource.delete();
        resource_packsSource.delete();
        server_selectionSource.delete();
        social_interactionsSource.delete();
        stream_indicatorSource.delete();
        toastsSource.delete();
        widgetsSource.delete();
        world_selectionSource.delete();

        // container
        plugin.saveResource("container/anvil.png", true);
        plugin.saveResource("container/beacon.png", true);
        plugin.saveResource("container/blast_furnace.png", true);
        plugin.saveResource("container/brewing_stand.png", true);
        plugin.saveResource("container/bundle.png", true);
        plugin.saveResource("container/cartography_table.png", true);
        plugin.saveResource("container/crafting_table.png", true);
        plugin.saveResource("container/dispenser.png", true);
        plugin.saveResource("container/enchanting_table.png", true);
        plugin.saveResource("container/furnace.png", true);
        plugin.saveResource("container/gamemode_switcher.png", true);
        plugin.saveResource("container/generic_54.png", true);
        plugin.saveResource("container/grindstone.png", true);
        plugin.saveResource("container/hopper.png", true);
        plugin.saveResource("container/horse.png", true);
        plugin.saveResource("container/inventory.png", true);
        plugin.saveResource("container/loom.png", true);
        plugin.saveResource("container/shulker_box.png", true);
        plugin.saveResource("container/smithing.png", true);
        plugin.saveResource("container/smoker.png", true);
        plugin.saveResource("container/stats_icons.png", true);
        plugin.saveResource("container/stonecutter.png", true);
        plugin.saveResource("container/villager2.png", true);

        File anvil = new File(gui, "container/anvil.png");
        if (!anvil.exists()) {
            anvil.createNewFile();
        }
        File beacon = new File(gui, "container/beacon.png");
        if (!beacon.exists()) {
            beacon.createNewFile();
        }
        File blast_furnace = new File(gui, "container/blast_furnace.png");
        if (!blast_furnace.exists()) {
            blast_furnace.createNewFile();
        }
        File brewing_stand = new File(gui, "container/brewing_stand.png");
        if (!brewing_stand.exists()) {
            brewing_stand.createNewFile();
        }
        File bundle = new File(gui, "container/bundle.png");
        if (!bundle.exists()) {
            bundle.createNewFile();
        }
        File cartography_table = new File(gui, "container/cartography_table.png");
        if (!cartography_table.exists()) {
            cartography_table.createNewFile();
        }
        File crafting_table = new File(gui, "container/crafting_table.png");
        if (!crafting_table.exists()) {
            crafting_table.createNewFile();
        }
        File dispenser = new File(gui, "container/dispenser.png");
        if (!dispenser.exists()) {
            dispenser.createNewFile();
        }
        File enchanting_table = new File(gui, "container/enchanting_table.png");
        if (!enchanting_table.exists()) {
            enchanting_table.createNewFile();
        }
        File furnace = new File(gui, "container/furnace.png");
        if (!furnace.exists()) {
            furnace.createNewFile();
        }
        File gamemode_switcher = new File(gui, "container/gamemode_switcher.png");
        if (!gamemode_switcher.exists()) {
            gamemode_switcher.createNewFile();
        }
        File generic_54 = new File(gui, "container/generic_54.png");
        if (!generic_54.exists()) {
            generic_54.createNewFile();
        }
        File grindstone = new File(gui, "container/grindstone.png");
        if (!grindstone.exists()) {
            grindstone.createNewFile();
        }
        File hopper = new File(gui, "container/hopper.png");
        if (!hopper.exists()) {
            hopper.createNewFile();
        }
        File horse = new File(gui, "container/horse.png");
        if (!horse.exists()) {
            horse.createNewFile();
        }
        File inventory = new File(gui, "container/inventory.png");
        if (!inventory.exists()) {
            inventory.createNewFile();
        }
        File loom = new File(gui, "container/loom.png");
        if (!loom.exists()) {
            loom.createNewFile();
        }
        File shulker_box = new File(gui, "container/shulker_box.png");
        if (!shulker_box.exists()) {
            shulker_box.createNewFile();
        }
        File smithing = new File(gui, "container/smithing.png");
        if (!smithing.exists()) {
            smithing.createNewFile();
        }
        File smoker = new File(gui, "container/smoker.png");
        if (!smoker.exists()) {
            smoker.createNewFile();
        }
        File stats_icons = new File(gui, "container/stats_icons.png");
        if (!stats_icons.exists()) {
            stats_icons.createNewFile();
        }
        File stonecutter = new File(gui, "container/stonecutter.png");
        if (!stonecutter.exists()) {
            stonecutter.createNewFile();
        }
        File villager2 = new File(gui, "container/villager2.png");
        if (!villager2.exists()) {
            villager2.createNewFile();
        }


        File anvilSource = new File(dataFolder, "container/anvil.png");
        File beaconSource = new File(dataFolder, "container/beacon.png");
        File blast_furnaceSource = new File(dataFolder, "container/blast_furnace.png");
        File brewing_standSource = new File(dataFolder, "container/brewing_stand.png");
        File bundleSource = new File(dataFolder, "container/bundle.png");
        File cartography_tableSource = new File(dataFolder, "container/cartography_table.png");
        File crafting_tableSource = new File(dataFolder, "container/crafting_table.png");
        File dispenserSource = new File(dataFolder, "container/dispenser.png");
        File enchanting_tableSource = new File(dataFolder, "container/enchanting_table.png");
        File furnaceSource = new File(dataFolder, "container/furnace.png");
        File gamemode_switcherSource = new File(dataFolder, "container/gamemode_switcher.png");
        File generic_54Source = new File(dataFolder, "container/generic_54.png");
        File grindstoneSource = new File(dataFolder, "container/grindstone.png");
        File hopperSource = new File(dataFolder, "container/hopper.png");
        File horseSource = new File(dataFolder, "container/horse.png");
        File inventorySource = new File(dataFolder, "container/inventory.png");
        File loomSource = new File(dataFolder, "container/loom.png");
        File shulker_boxSource = new File(dataFolder, "container/shulker_box.png");
        File smithingSource = new File(dataFolder, "container/smithing.png");
        File smokerSource = new File(dataFolder, "container/smoker.png");
        File stats_iconsSource = new File(dataFolder, "container/stats_icons.png");
        File stonecutterSource = new File(dataFolder, "container/stonecutter.png");
        File villager2Source = new File(dataFolder, "container/villager2.png");

        Files.copy(anvilSource.toPath(), anvil.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(beaconSource.toPath(), beacon.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(blast_furnaceSource.toPath(), blast_furnace.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(brewing_standSource.toPath(), brewing_stand.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(bundleSource.toPath(), bundle.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(cartography_tableSource.toPath(), cartography_table.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(crafting_tableSource.toPath(), crafting_table.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(dispenserSource.toPath(), dispenser.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(enchanting_tableSource.toPath(), enchanting_table.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(furnaceSource.toPath(), furnace.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(gamemode_switcherSource.toPath(), gamemode_switcher.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(generic_54Source.toPath(), generic_54.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(grindstoneSource.toPath(), grindstone.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(hopperSource.toPath(), hopper.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(horseSource.toPath(), horse.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(inventorySource.toPath(), inventory.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(loomSource.toPath(), loom.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(shulker_boxSource.toPath(), shulker_box.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(smithingSource.toPath(), smithing.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(smokerSource.toPath(), smoker.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(stats_iconsSource.toPath(), stats_icons.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(stonecutterSource.toPath(), stonecutter.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(villager2Source.toPath(), villager2.toPath(), StandardCopyOption.REPLACE_EXISTING);

        anvilSource.delete();
        beaconSource.delete();
        blast_furnaceSource.delete();
        brewing_standSource.delete();
        bundleSource.delete();
        cartography_tableSource.delete();
        crafting_tableSource.delete();
        dispenserSource.delete();
        enchanting_tableSource.delete();
        furnaceSource.delete();
        gamemode_switcherSource.delete();
        generic_54Source.delete();
        grindstoneSource.delete();
        hopperSource.delete();
        horseSource.delete();
        inventorySource.delete();
        loomSource.delete();
        shulker_boxSource.delete();
        smithingSource.delete();
        smokerSource.delete();
        stats_iconsSource.delete();
        stonecutterSource.delete();
        villager2Source.delete();

        // container/creative_inventory
        plugin.saveResource("container/creative_inventory/tab_inventory.png", true);
        plugin.saveResource("container/creative_inventory/tab_item_search.png", true);
        plugin.saveResource("container/creative_inventory/tab_items.png", true);
        plugin.saveResource("container/creative_inventory/tabs.png", true);

        File tab_inventory = new File(gui, "container/creative_inventory/tab_inventory.png");
        if (!tab_inventory.exists()) {
            tab_inventory.createNewFile();
        }
        File tab_item_search = new File(gui, "container/creative_inventory/tab_item_search.png");
        if (!tab_item_search.exists()) {
            tab_item_search.createNewFile();
        }
        File tab_items = new File(gui, "container/creative_inventory/tab_items.png");
        if (!tab_items.exists()) {
            tab_items.createNewFile();
        }
        File tabs = new File(gui, "container/creative_inventory/tabs.png");
        if (!tabs.exists()) {
            tabs.createNewFile();
        }

        File tab_inventorySource = new File(dataFolder, "container/creative_inventory/tab_inventory.png");
        File tab_item_searchSource = new File(dataFolder, "container/creative_inventory/tab_item_search.png");
        File tab_itemsSource = new File(dataFolder, "container/creative_inventory/tab_items.png");
        File tabsSource = new File(dataFolder, "container/creative_inventory/tabs.png");

        Files.copy(tab_inventorySource.toPath(), tab_inventory.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(tab_item_searchSource.toPath(), tab_item_search.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(tab_itemsSource.toPath(), tab_items.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(tabsSource.toPath(), tabs.toPath(), StandardCopyOption.REPLACE_EXISTING);

        tab_inventorySource.delete();
        tab_item_searchSource.delete();
        tab_itemsSource.delete();
        tabsSource.delete();

        // advancements
        plugin.saveResource("advancements/widgets.png", true);
        plugin.saveResource("advancements/window.png", true);
        plugin.saveResource("advancements/tabs.png", true);

        File widgetsAD = new File(gui, "advancements/widgets.png");
        if (!widgetsAD.exists()) {
            widgetsAD.createNewFile();
        }
        File window = new File(gui, "advancements/window.png");
        if (!window.exists()) {
            window.createNewFile();
        }
        File tabsAD = new File(gui, "advancements/tabs.png");
        if (!tabsAD.exists()) {
            tabsAD.createNewFile();
        }

        File widgetsADSource = new File(dataFolder, "advancements/widgets.png");
        File windowSource = new File(dataFolder, "advancements/window.png");
        File tabsADSource = new File(dataFolder, "advancements/tabs.png");

        Files.copy(widgetsADSource.toPath(), widgetsAD.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(windowSource.toPath(), window.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(tabsADSource.toPath(), tabsAD.toPath(), StandardCopyOption.REPLACE_EXISTING);

        widgetsADSource.delete();
        windowSource.delete();
        tabsADSource.delete();

        // advancements/backgrounds
        plugin.saveResource("advancements/backgrounds/adventure.png", true);
        plugin.saveResource("advancements/backgrounds/end.png", true);
        plugin.saveResource("advancements/backgrounds/husbandry.png", true);
        plugin.saveResource("advancements/backgrounds/nether.png", true);
        plugin.saveResource("advancements/backgrounds/stone.png", true);

        File adventure = new File(gui, "advancements/backgrounds/adventure.png");
        if (!adventure.exists()) {
            adventure.createNewFile();
        }
        File end = new File(gui, "advancements/backgrounds/end.png");
        if (!end.exists()) {
            end.createNewFile();
        }
        File husbandry = new File(gui, "advancements/backgrounds/husbandry.png");
        if (!husbandry.exists()) {
            husbandry.createNewFile();
        }
        File nether = new File(gui, "advancements/backgrounds/nether.png");
        if (!nether.exists()) {
            nether.createNewFile();
        }
        File stone = new File(gui, "advancements/backgrounds/stone.png");
        if (!stone.exists()) {
            stone.createNewFile();
        }

        File adventureSource = new File(dataFolder, "advancements/backgrounds/adventure.png");
        File endSource = new File(dataFolder, "advancements/backgrounds/end.png");
        File husbandrySource = new File(dataFolder, "advancements/backgrounds/husbandry.png");
        File netherSource = new File(dataFolder, "advancements/backgrounds/nether.png");
        File stoneSource = new File(dataFolder, "advancements/backgrounds/stone.png");

        Files.copy(adventureSource.toPath(), adventure.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(endSource.toPath(), end.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(husbandrySource.toPath(), husbandry.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(netherSource.toPath(), nether.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(stoneSource.toPath(), stone.toPath(), StandardCopyOption.REPLACE_EXISTING);

        adventureSource.delete();
        endSource.delete();
        husbandrySource.delete();
        netherSource.delete();
        stoneSource.delete();
    }


    public void generateResourcePack() {
        String packName = "MyResourcePack";
        File dataFolder = plugin.getDataFolder();
        File resourcePackFolder = new File(dataFolder, packName);

        // Create the resource pack folder structure
        try {

            loadGUI(new File(dataFolder, "pack/assets/minecraft/textures/gui"));

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

            // Hats
            copyHats(new File(dataFolder, "hats"),
                    new File(dataFolder, "pack/assets/minecraft/textures/block"),
                    new File(dataFolder, "pack/assets/minecraft/models/block"),
                    new File(dataFolder, "pack/assets/minecraft/models/item"),
                    new File(dataFolder, "pack/assets/minecraft/textures/misc"));

            // Armor
            copyArmor(new File(dataFolder, "armors"),
                    new File(dataFolder, "pack/assets/minecraft/textures/models/armor"));

            // Env
            copyEnv(new File(dataFolder, "environments"),
                    new File(dataFolder, "pack/assets/minecraft/textures/environment"));

            // Map
            copyEnv(new File(dataFolder, "maps"),
                    new File(dataFolder, "pack/assets/minecraft/textures/map"));

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



    public void copyEnv(File sourceFolder, File textureFolder) throws IOException {
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

        File[] files = sourceFolder.listFiles();
        if (files == null) { return; }

        for (String filePath : plugin.config.getEnv()) {

            File SourceTexture = new File(sourceFolder, filePath+".png");
            if (!SourceTexture.exists()) { return; }

            File TextureEnv = new File(textureFolder, filePath+".png");
            if (!TextureEnv.exists()) {
                if (TextureEnv.createNewFile()) {
                    plugin.getLogger().info("Made a file " + TextureEnv.getName());
                } else {
                    plugin.getLogger().info("Can't make a file " + TextureEnv.getName());
                }
            }

            Files.copy(SourceTexture.toPath(), TextureEnv.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public void copyArmor(File sourceFolder, File textureFolder) throws IOException {
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

        File[] files = sourceFolder.listFiles();
        if (files == null) { return; }

        for (String filePath : plugin.config.getArmors()) {

            File SourceTexture = new File(sourceFolder, filePath+".png");
            if (!SourceTexture.exists()) { return; }

            String SourceTextureDir = getDirByFilePath(filePath).toString();
            File TextureFolder = new File(textureFolder, SourceTextureDir);
            if (!TextureFolder.exists()) {
                if (TextureFolder.mkdirs()) {
                    plugin.getLogger().info("Made a folder " + TextureFolder.getName());
                } else {
                    plugin.getLogger().info("Can't make a folder " + TextureFolder.getName());
                }
            }

            File TextureFile = new File(textureFolder, filePath+".png");
            if (!TextureFile.exists()) {
                if (TextureFile.createNewFile()) {
                    plugin.getLogger().info("Made a file " + TextureFile.getName());
                } else {
                    plugin.getLogger().info("Can't make a file " + TextureFile.getName());
                }
            }

            Files.copy(SourceTexture.toPath(), TextureFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            if (plugin.config.isItemAnimationEnabled(filePath)) {
                generateMcmetaAnimation(TextureFolder, filePath);
            }
        }
    }


    public void copyHats(File sourceFolder, File textureFolder, File modelsFolder, File carvedPumpkinFolder, File miscCarvePumpkinTextureFolder) throws IOException {
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

        if (!carvedPumpkinFolder.exists()) {
            if (carvedPumpkinFolder.mkdirs()) {
                plugin.getLogger().info("Made a folder " + carvedPumpkinFolder.getName());
            } else {
                plugin.getLogger().info("Can't create the folder " + carvedPumpkinFolder.getName());
            }
        }

        if (!miscCarvePumpkinTextureFolder.exists()) {
            if (miscCarvePumpkinTextureFolder.mkdirs()) {
                plugin.getLogger().info("Made a folder " + miscCarvePumpkinTextureFolder.getName());
            } else {
                plugin.getLogger().info("Can't create the folder " + miscCarvePumpkinTextureFolder.getName());
            }
        }

        File[] files = sourceFolder.listFiles();
        if (files == null) { return; }
        for (String filePath : plugin.config.getHats()) {

            File SourceHatTexture = new File(sourceFolder, filePath+".png");
            String model = plugin.config.getItemModel(filePath);
            File SourceHatModel = new File(sourceFolder, model +".json");

            if (!SourceHatTexture.exists() && !SourceHatModel.exists()) { return; }

            String textureHarDir = getDirByFilePath(filePath).toString();
            String modelHarDir = getDirByFilePath(model).toString();

            File TextureHatFolder = new File(textureFolder, textureHarDir);
            File ModelHatFolder = new File(modelsFolder, modelHarDir);

            if (!TextureHatFolder.exists()) {
                if (TextureHatFolder.mkdirs()) {
                    plugin.getLogger().info("Made a folder "+TextureHatFolder.getName());
                } else {
                    plugin.getLogger().info("Can't make a folder "+TextureHatFolder.getName());
                }
            }

            if (!ModelHatFolder.exists()) {
                if (ModelHatFolder.mkdirs()) {
                    plugin.getLogger().info("Made a folder "+ModelHatFolder.getName());
                } else {
                    plugin.getLogger().info("Can't make a folder "+ModelHatFolder.getName());
                }
            }

            File TextureHatFile = new File(textureFolder, filePath+".png");
            File ModelHatFile = new File(modelsFolder, model+".json");

            if (!TextureHatFile.exists()) {
                if (TextureHatFile.createNewFile()) {
                    plugin.getLogger().info("Made a file "+TextureHatFile.getName());
                } else {
                    plugin.getLogger().info("Can't make a file "+TextureHatFile.getName());
                }
            }

            if (!ModelHatFile.exists()) {
                if (ModelHatFile.createNewFile()) {
                    plugin.getLogger().info("Made a file "+ModelHatFile.getName());
                } else {
                    plugin.getLogger().info("Can't make a file "+ModelHatFile.getName());
                }
            }

            Files.copy(SourceHatTexture.toPath(), TextureHatFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(SourceHatModel.toPath(), ModelHatFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            String carvedPumpkinTextures = plugin.config.getHatPumpkinTexture(filePath);
            String carvedPumpkinTexturesFolder = getDirByFilePath(carvedPumpkinTextures).toString();

            File SourceCarvedPumpkinTextureFile = new File(sourceFolder, carvedPumpkinTextures+".png");
            if (SourceCarvedPumpkinTextureFile.exists()) {
                File TextureCarvedPumpkinFolder = new File(miscCarvePumpkinTextureFolder, carvedPumpkinTexturesFolder);
                if (!TextureCarvedPumpkinFolder.exists()) {
                    if (TextureCarvedPumpkinFolder.mkdirs()) {
                        plugin.getLogger().info("Made a folder "+ TextureCarvedPumpkinFolder.getName());
                    } else {
                        plugin.getLogger().info("Can't make a folder "+ TextureCarvedPumpkinFolder.getName());
                    }
                }

                File TextureCarvedPumpkinFile = new File(miscCarvePumpkinTextureFolder, carvedPumpkinTextures+".png");
                if (!TextureCarvedPumpkinFile.exists()) {
                    if (TextureCarvedPumpkinFile.createNewFile()) {
                        plugin.getLogger().info("Made a file "+ TextureCarvedPumpkinFile.getName());
                    } else {
                        plugin.getLogger().info("Can't make a file "+ TextureCarvedPumpkinFile.getName());
                    }
                }

                Files.copy(SourceCarvedPumpkinTextureFile.toPath(), TextureCarvedPumpkinFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            } else {
                plugin.saveResource("pumpkinblur.png", true);
                File pumpkinBlur = new File(plugin.getDataFolder(), "pumpkinblur.png");
                File TextureCarvedPumpkinFile2 = new File(miscCarvePumpkinTextureFolder, "pumpkinblur.png");
                if (!TextureCarvedPumpkinFile2.exists()) {
                    if (TextureCarvedPumpkinFile2.createNewFile()) {
                        plugin.getLogger().info("Made a file "+ TextureCarvedPumpkinFile2.getName());
                    } else {
                        plugin.getLogger().info("Can't make a file "+ TextureCarvedPumpkinFile2.getName());
                    }
                }

                Files.copy(pumpkinBlur.toPath(), TextureCarvedPumpkinFile2.toPath(), StandardCopyOption.REPLACE_EXISTING);

                if (pumpkinBlur.exists()) {
                    if (pumpkinBlur.delete()) {
                        plugin.getLogger().info("Deleted pumpkinblur.png");
                    } else {
                        plugin.getLogger().info("Can't delete pumpkinblur.png");
                    }
                }
            }

            if (plugin.config.isItemAnimationEnabled(filePath)) {
                generateMcmetaAnimation(TextureHatFolder, filePath);
            }

            File ModelCarvedPumpkin = new File(carvedPumpkinFolder, "carved_pumpkin.json");
            if (!ModelCarvedPumpkin.exists()) {
                if (ModelCarvedPumpkin.createNewFile()) {
                    plugin.getLogger().info("Made a file "+ ModelCarvedPumpkin.getName());
                } else {
                    plugin.getLogger().info("Can't make a file "+ ModelCarvedPumpkin.getName());
                }
            }

            JsonObject blockJson = new JsonObject();
            blockJson.addProperty("parent", "block/carved_pumpkin");

            JsonObject texturesJson = new JsonObject();
            texturesJson.addProperty("layer0", "block/carved_pumpkin");
            blockJson.add("textures", texturesJson);

            JsonArray overridesJson = new JsonArray();
            JsonObject overrideJson = new JsonObject();
            JsonObject predicateJson = new JsonObject();
            predicateJson.addProperty("custom_model_data", plugin.config.getItemModelData(filePath));
            overrideJson.add("predicate", predicateJson);
            overrideJson.addProperty("model", "block/"+model);
            overridesJson.add(overrideJson);

            blockJson.add("overrides", overridesJson);
            addJsonToFile(ModelCarvedPumpkin, blockJson);
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

            File BlockStates = new File(stateFolder, plugin.config.getItemType(filePath)+".json");
            File SourceBlock = new File(sourceFolder, filePath+".png");

            if (BlockStates.exists() || !SourceBlock.exists()) { return; }

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
                String blockNorth = plugin.config.getBlockNorth(filePath);
                String blockSouth = plugin.config.getBlockSouth(filePath);
                String blockEast = plugin.config.getBlockEast(filePath);
                String blockWest = plugin.config.getBlockWest(filePath);
                String blockUp = plugin.config.getBlockUp(filePath);
                String blockDown = plugin.config.getBlockDown(filePath);
                String blockPart = plugin.config.getBlockParticle(filePath);

                String northDir = getDirByFilePath(blockNorth).toString();
                String southDir = getDirByFilePath(blockSouth).toString();
                String eastDir = getDirByFilePath(blockEast).toString();
                String westDir = getDirByFilePath(blockWest).toString();
                String upDir = getDirByFilePath(blockUp).toString();
                String downDir = getDirByFilePath(blockDown).toString();
                String particDir = getDirByFilePath(blockPart).toString();

                File SourceNorthFolder = new File(sourceFolder, northDir);
                File SourceSouthFolder = new File(sourceFolder, southDir);
                File SourceEastFolder = new File(sourceFolder, eastDir);
                File SourceWestFolder = new File(sourceFolder, westDir);
                File SourceUpFolder = new File(sourceFolder, upDir);
                File SourceDownFolder = new File(sourceFolder, downDir);
                File SourcePartFolder = new File(sourceFolder, particDir);

                if (!SourceNorthFolder.exists() || !SourceSouthFolder.exists() ||
                        !SourceEastFolder.exists() || !SourceWestFolder.exists() ||
                        !SourcePartFolder.exists() || !SourceUpFolder.exists() || !SourceDownFolder.exists()) {
                    plugin.getLogger().info("One of the sides is incorrect!");
                    return;
                }

                File TextureNorthFolder = new File(textureFolder, northDir);
                File TextureSouthFolder = new File(textureFolder, southDir);
                File TextureEastFolder = new File(textureFolder, eastDir);
                File TextureWestFolder = new File(textureFolder, westDir);
                File TextureUpFolder = new File(textureFolder, upDir);
                File TextureDownFolder = new File(textureFolder, downDir);
                File TextureParticFolder = new File(textureFolder, particDir);

                if (!TextureNorthFolder.exists()) {
                    if (TextureNorthFolder.mkdirs()) {
                        plugin.getLogger().info("Made a folder "+TextureNorthFolder.getName());
                    } else {
                        plugin.getLogger().info("Can't make a folder "+TextureNorthFolder.getName());
                    }
                }

                if (!TextureSouthFolder.exists()) {
                    if (TextureSouthFolder.mkdirs()) {
                        plugin.getLogger().info("Made a folder "+TextureSouthFolder.getName());
                    } else {
                        plugin.getLogger().info("Can't make a folder "+TextureSouthFolder.getName());
                    }
                }

                if (!TextureEastFolder.exists()) {
                    if (TextureEastFolder.mkdirs()) {
                        plugin.getLogger().info("Made a folder "+TextureEastFolder.getName());
                    } else {
                        plugin.getLogger().info("Can't make a folder "+TextureEastFolder.getName());
                    }
                }

                if (!TextureWestFolder.exists()) {
                    if (TextureWestFolder.mkdirs()) {
                        plugin.getLogger().info("Made a folder "+TextureWestFolder.getName());
                    } else {
                        plugin.getLogger().info("Can't make a folder "+TextureWestFolder.getName());
                    }
                }

                if (!TextureUpFolder.exists()) {
                    if (TextureUpFolder.mkdirs()) {
                        plugin.getLogger().info("Made a folder "+TextureUpFolder.getName());
                    } else {
                        plugin.getLogger().info("Can't make a folder "+TextureUpFolder.getName());
                    }
                }

                if (!TextureDownFolder.exists()) {
                    if (TextureDownFolder.mkdirs()) {
                        plugin.getLogger().info("Made a folder "+TextureDownFolder.getName());
                    } else {
                        plugin.getLogger().info("Can't make a folder "+TextureDownFolder.getName());
                    }
                }

                if (!TextureParticFolder.exists()) {
                    if (TextureParticFolder.mkdirs()) {
                        plugin.getLogger().info("Made a folder "+TextureParticFolder.getName());
                    } else {
                        plugin.getLogger().info("Can't make a folder "+TextureParticFolder.getName());
                    }
                }

                File TextureNorthFile = new File(textureFolder, blockNorth+".png");
                File TextureSouthFile = new File(textureFolder, blockSouth +".png");
                File TextureEastFile = new File(textureFolder, blockEast +".png");
                File TextureWestFile = new File(textureFolder, blockWest +".png");
                File TextureUpFile = new File(textureFolder, blockUp +".png");
                File TextureDownFile = new File(textureFolder, blockDown +".png");
                File TexturePartFile = new File(textureFolder, blockPart +".png");

                if (!TextureNorthFile.exists()) {
                    if (TextureNorthFile.createNewFile()) {
                        plugin.getLogger().info("Made a file "+TextureNorthFile.getName());
                    } else {
                        plugin.getLogger().info("Can't make a file "+TextureNorthFile.getName());
                    }
                }

                if (!TextureSouthFile.exists()) {
                    if (TextureSouthFile.createNewFile()) {
                        plugin.getLogger().info("Made a file "+TextureSouthFile.getName());
                    } else {
                        plugin.getLogger().info("Can't make a file "+TextureSouthFile.getName());
                    }
                }

                if (!TextureEastFile.exists()) {
                    if (TextureEastFile.createNewFile()) {
                        plugin.getLogger().info("Made a file "+TextureEastFile.getName());
                    } else {
                        plugin.getLogger().info("Can't make a file "+TextureEastFile.getName());
                    }
                }

                if (!TextureWestFile.exists()) {
                    if (TextureWestFile.createNewFile()) {
                        plugin.getLogger().info("Made a file "+TextureWestFile.getName());
                    } else {
                        plugin.getLogger().info("Can't make a file "+TextureWestFile.getName());
                    }
                }

                if (!TextureUpFile.exists()) {
                    if (TextureUpFile.createNewFile()) {
                        plugin.getLogger().info("Made a file "+TextureUpFile.getName());
                    } else {
                        plugin.getLogger().info("Can't make a file "+TextureUpFile.getName());
                    }
                }

                if (!TextureDownFile.exists()) {
                    if (TextureDownFile.createNewFile()) {
                        plugin.getLogger().info("Made a file "+TextureDownFile.getName());
                    } else {
                        plugin.getLogger().info("Can't make a file "+TextureDownFile.getName());
                    }
                }

                if (!TexturePartFile.exists()) {
                    if (TexturePartFile.createNewFile()) {
                        plugin.getLogger().info("Made a file "+TexturePartFile.getName());
                    } else {
                        plugin.getLogger().info("Can't make a file "+TexturePartFile.getName());
                    }
                }

                Files.copy(new File(sourceFolder, blockNorth+".png").toPath(), TextureNorthFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                Files.copy(new File(sourceFolder, blockSouth +".png").toPath(), TextureSouthFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                Files.copy(new File(sourceFolder, blockEast +".png").toPath(), TextureEastFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                Files.copy(new File(sourceFolder, blockWest +".png").toPath(), TextureWestFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                Files.copy(new File(sourceFolder, blockUp +".png").toPath(), TextureUpFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                Files.copy(new File(sourceFolder, blockDown +".png").toPath(), TextureDownFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                Files.copy(new File(sourceFolder, blockPart +".png").toPath(), TexturePartFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                JsonObject block = new JsonObject();
                block.addProperty("parent", "block/cube");
                JsonObject textuers = new JsonObject();
                textuers.addProperty("north", "minecraft:block/"+blockNorth);
                textuers.addProperty("south", "minecraft:block/"+blockSouth);
                textuers.addProperty("east", "minecraft:block/"+blockEast);
                textuers.addProperty("west","minecraft:block/"+ blockWest);
                textuers.addProperty("up", "minecraft:block/"+blockUp);
                textuers.addProperty("down", "minecraft:block/"+blockDown);
                textuers.addProperty("particle", "minecraft:block/"+blockPart);
                block.add("textures", textuers);
                addJsonToFile(ModelBlockFileCreate, block);
            }

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
