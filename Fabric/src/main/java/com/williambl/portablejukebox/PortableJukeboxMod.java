package com.williambl.portablejukebox;

import com.williambl.portablejukebox.jukebox.PortableJukeboxItem;
import com.williambl.portablejukebox.jukebox.PortableJukeboxLoadRecipe;
import com.williambl.portablejukebox.noteblock.PortableNoteBlockItem;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;

import static com.williambl.portablejukebox.PortableJukeboxCommon.id;

public class PortableJukeboxMod implements ModInitializer {

    public static SimpleRecipeSerializer<PortableJukeboxLoadRecipe> PORTABLE_JUKEBOX_LOAD_RECIPE_SERIALIZER = Registry.register(Registry.RECIPE_SERIALIZER, id("portable_jukebox_load"), PortableJukeboxCommon.createPortableJukeboxLoadRecipeSerializer());
    public static PortableJukeboxItem PORTABLE_JUKEBOX = Registry.register(Registry.ITEM, id("portable_jukebox"), PortableJukeboxCommon.createPortableJukeboxItem());
    public static PortableNoteBlockItem PORTABLE_NOTE_BLOCK = Registry.register(Registry.ITEM, id("portable_note_block"), PortableJukeboxCommon.createPortableNoteBlockItem());

    @Override
    public void onInitialize() {
        PortableJukeboxCommon.init();
    }
}
