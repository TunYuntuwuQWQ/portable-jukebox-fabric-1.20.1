package com.williambl.portablejukebox;

import com.williambl.portablejukebox.jukebox.PortableJukeboxItem;
import com.williambl.portablejukebox.jukebox.PortableJukeboxLoadRecipe;
import com.williambl.portablejukebox.noteblock.PortableNoteBlockItem;
import com.williambl.portablejukebox.platform.ForgeMessageSender;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.williambl.portablejukebox.PortableJukeboxCommon.id;

@Mod(Constants.MOD_ID)
public class PortableJukeboxMod {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MOD_ID);
    private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Constants.MOD_ID);

    public static final RegistryObject<PortableJukeboxItem> PORTABLE_JUKEBOX = ITEMS.register("portable_jukebox", PortableJukeboxCommon::createPortableJukeboxItem);
    public static final RegistryObject<PortableNoteBlockItem> PORTABLE_NOTE_BLOCK = ITEMS.register("portable_note_block", PortableJukeboxCommon::createPortableNoteBlockItem);
    public static final RegistryObject<SimpleRecipeSerializer<PortableJukeboxLoadRecipe>> PORTABLE_JUKEBOX_LOAD_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("portable_jukebox_load", PortableJukeboxCommon::createPortableJukeboxLoadRecipeSerializer);

    public PortableJukeboxMod() {
        PortableJukeboxCommon.init();

        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        RECIPE_SERIALIZERS.register(FMLJavaModLoadingContext.get().getModEventBus());

        ForgeMessageSender.init();
    }
}