package com.williambl.portablejukebox.platform;

import com.williambl.portablejukebox.Constants;
import com.williambl.portablejukebox.platform.services.ILootTableInjector;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.LootTableLoadEvent;

public class ForgeLootTableInjector implements ILootTableInjector {
    @Override
    public void injectLootTables() {
        MinecraftForge.EVENT_BUS.addListener(this::lootTableInject);
    }

    private void lootTableInject(final LootTableLoadEvent event) {
        if (this.shouldInject(event.getName())) {
            event.getTable().addPool(LootPool.lootPool().add(ENTRY).name(Constants.MOD_ID + "_injected").build());
        }
    }
}
