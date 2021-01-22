package com.williambl.portablejukebox;

import com.williambl.portablejukebox.jukebox.PortableJukeboxItem;
import com.williambl.portablejukebox.jukebox.PortableJukeboxLoadRecipe;
import com.williambl.portablejukebox.jukebox.PortableJukeboxMessage;
import com.williambl.portablejukebox.noteblock.PortableNoteBlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("portablejukebox")
public class PortableJukeboxMod
{
    private static final Logger LOGGER = LogManager.getLogger();
    private static final DeferredRegister<Item> ITEM_DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, "portablejukebox");
    private static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZER_DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, "portablejukebox");

    public static final RegistryObject<Item> PORTABLE_JUKEBOX = ITEM_DEFERRED_REGISTER.register("portable_jukebox",
            () -> new PortableJukeboxItem(new Item.Properties().maxStackSize(1).group(ItemGroup.MISC))
    );

    public static final RegistryObject<Item> PORTABLE_NOTE_BLOCK = ITEM_DEFERRED_REGISTER.register("portable_note_block",
            () -> new PortableNoteBlockItem(new Item.Properties().maxStackSize(1).group(ItemGroup.MISC))
    );

    public static final RegistryObject<IRecipeSerializer<?>> PORTABLE_JUKEBOX_LOAD = RECIPE_SERIALIZER_DEFERRED_REGISTER.register("portable_jukebox",
            () -> new SpecialRecipeSerializer<>(PortableJukeboxLoadRecipe::new)
    );

    private static String PROTOCOL_VERSION = "1";

    public static SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("portablejukebox", "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public PortableJukeboxMod() {
        ITEM_DEFERRED_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
        RECIPE_SERIALIZER_DEFERRED_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        int discriminator = 0;
        CHANNEL.registerMessage(
                discriminator++,
                PortableJukeboxMessage.class,
                PortableJukeboxMessage::encode,
                PortableJukeboxMessage::new,
                PortableJukeboxMessage::handle
        );
    }
}
