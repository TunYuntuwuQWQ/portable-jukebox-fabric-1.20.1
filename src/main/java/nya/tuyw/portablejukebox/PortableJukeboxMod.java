package nya.tuyw.portablejukebox;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.LootTableEntry;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import nya.tuyw.portablejukebox.jukebox.PortableJukeboxItem;
import nya.tuyw.portablejukebox.jukebox.PortableJukeboxLoadRecipe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class PortableJukeboxMod implements ModInitializer {

    public static final String MOD_ID = "portablejukebox";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final Item PORTABLE_JUKEBOX = Registry.register(Registries.ITEM, new Identifier("portablejukebox:portable_jukebox"),
            new PortableJukeboxItem(new Item.Settings().maxCount(1))
    );
    public static final RecipeSerializer<?> PORTABLE_JUKEBOX_LOAD = Registry.register(Registries.RECIPE_SERIALIZER, new Identifier("portablejukebox:crafting_special_portable_jukebox_load"),
            new SpecialRecipeSerializer<>(PortableJukeboxLoadRecipe::new)
    );

    private static final List<Identifier> JUKEBOX_LOOT_TABLES = Arrays.asList(
            LootTables.DESERT_PYRAMID_CHEST,
            LootTables.ABANDONED_MINESHAFT_CHEST,
            LootTables.JUNGLE_TEMPLE_CHEST,
            LootTables.SIMPLE_DUNGEON_CHEST,
            LootTables.NETHER_BRIDGE_CHEST,
            LootTables.IGLOO_CHEST_CHEST
    );

    private static final LootTableEntry.Builder<?> entry = LootTableEntry.builder(new Identifier("portablejukebox:inject/loot_chests")).weight(10);
    private static final ItemGroup ITEM_GROUP = Registry.register(Registries.ITEM_GROUP, new Identifier("portablejukebox:main"), FabricItemGroup.builder()
            .icon(PORTABLE_JUKEBOX::getDefaultStack)
            .displayName(Text.translatable("group.portablejukebox.main"))
            .entries((context, entries) -> {
                entries.add(PORTABLE_JUKEBOX);
                entries.addAll(PortableJukeboxItem.getJukeboxes());
            })
            .build());

    @Override
    public void onInitialize() {
        LootTableEvents.MODIFY.register(((resourceManager, lootManager, identifier, fabricLootSupplierBuilder, lootTableSetter) -> {
            if (JUKEBOX_LOOT_TABLES.contains(identifier)) {
                fabricLootSupplierBuilder.pool(LootPool.builder().with(entry).build());
            }
        }));
    }

}
