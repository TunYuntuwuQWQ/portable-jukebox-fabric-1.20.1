package com.williambl.portablejukebox.platform.services;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntries;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;

import java.util.Set;

import static com.williambl.portablejukebox.PortableJukeboxCommon.id;

public interface ILootTableInjector {
    LootPoolEntryContainer.Builder<?> ENTRY = LootTableReference.lootTableReference(id("inject/loot_chests")).setWeight(10);
    Set<ResourceLocation> JUKEBOX_LOOT_TABLES = Set.of(
            new ResourceLocation("chests/desert_pyramid"),
            new ResourceLocation("chests/abandoned_mineshaft"),
            new ResourceLocation("chests/jungle_temple"),
            new ResourceLocation("chests/simple_dungeon"),
            new ResourceLocation("chests/nether_bridge"),
            new ResourceLocation("chests/igloo_chest")
    );

    void injectLootTables();

    default boolean shouldInject(ResourceLocation table) {
        return JUKEBOX_LOOT_TABLES.contains(table);
    }
}
