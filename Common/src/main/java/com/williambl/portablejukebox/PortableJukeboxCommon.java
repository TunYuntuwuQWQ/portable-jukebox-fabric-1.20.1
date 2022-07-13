package com.williambl.portablejukebox;

import com.williambl.portablejukebox.jukebox.PortableJukeboxItem;
import com.williambl.portablejukebox.jukebox.PortableJukeboxLoadRecipe;
import com.williambl.portablejukebox.noteblock.PortableNoteBlockItem;
import com.williambl.portablejukebox.platform.Services;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraft.world.level.storage.loot.LootTables;

public class PortableJukeboxCommon {

    public static void init() {
        Services.LOOT.injectLootTables();
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(Constants.MOD_ID, path);
    }

    public static PortableJukeboxItem createPortableJukeboxItem() {
        return new PortableJukeboxItem(new Item.Properties().tab(CreativeModeTab.TAB_MISC).stacksTo(1).rarity(Rarity.RARE));
    }

    public static PortableNoteBlockItem createPortableNoteBlockItem() {
        return new PortableNoteBlockItem(new Item.Properties().tab(CreativeModeTab.TAB_MISC).stacksTo(1));
    }

    public static SimpleRecipeSerializer<PortableJukeboxLoadRecipe> createPortableJukeboxLoadRecipeSerializer() {
        return new SimpleRecipeSerializer<>(PortableJukeboxLoadRecipe::new);
    }
}