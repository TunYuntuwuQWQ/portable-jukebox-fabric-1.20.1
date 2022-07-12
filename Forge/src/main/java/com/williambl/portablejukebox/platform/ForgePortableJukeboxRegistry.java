package com.williambl.portablejukebox.platform;

import com.williambl.portablejukebox.PortableJukeboxMod;
import com.williambl.portablejukebox.jukebox.PortableJukeboxItem;
import com.williambl.portablejukebox.jukebox.PortableJukeboxLoadRecipe;
import com.williambl.portablejukebox.noteblock.PortableNoteBlockItem;
import com.williambl.portablejukebox.platform.services.IPortableJukeboxRegistry;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;

import java.util.function.Supplier;

public class ForgePortableJukeboxRegistry implements IPortableJukeboxRegistry {
    @Override
    public Supplier<SimpleRecipeSerializer<PortableJukeboxLoadRecipe>> portableJukeboxLoadRecipeSerializer() {
        return PortableJukeboxMod.PORTABLE_JUKEBOX_LOAD_RECIPE_SERIALIZER;
    }

    @Override
    public Supplier<PortableJukeboxItem> portableJukeboxItem() {
        return PortableJukeboxMod.PORTABLE_JUKEBOX;
    }

    @Override
    public Supplier<PortableNoteBlockItem> portableNoteBlockItem() {
        return PortableJukeboxMod.PORTABLE_NOTE_BLOCK;
    }
}
