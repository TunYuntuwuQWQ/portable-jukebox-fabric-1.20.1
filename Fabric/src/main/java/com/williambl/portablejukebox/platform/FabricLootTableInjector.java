package com.williambl.portablejukebox.platform;

import com.williambl.portablejukebox.platform.services.ILootTableInjector;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.world.level.storage.loot.LootPool;

public class FabricLootTableInjector implements ILootTableInjector {
    @Override
    public void injectLootTables() {
        LootTableEvents.MODIFY.register(((resourceManager, lootManager, identifier, fabricLootSupplierBuilder, lootTableSetter) -> {
            if (this.shouldInject(identifier)) {
                fabricLootSupplierBuilder.withPool(LootPool.lootPool().add(ENTRY));
            }
        }));
    }
}
