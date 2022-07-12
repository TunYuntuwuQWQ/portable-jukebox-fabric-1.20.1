package com.williambl.portablejukebox.platform.services;

import com.williambl.portablejukebox.jukebox.PortableJukeboxItem;
import com.williambl.portablejukebox.jukebox.PortableJukeboxLoadRecipe;
import com.williambl.portablejukebox.noteblock.PortableNoteBlockItem;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;

import java.util.Map;
import java.util.function.Supplier;

public interface IPortableJukeboxRegistry {
    Supplier<SimpleRecipeSerializer<PortableJukeboxLoadRecipe>> portableJukeboxLoadRecipeSerializer();

    Supplier<PortableJukeboxItem> portableJukeboxItem();
    Supplier<PortableNoteBlockItem> portableNoteBlockItem();
}
