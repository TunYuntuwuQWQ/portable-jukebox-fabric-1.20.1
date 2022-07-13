package com.williambl.portablejukebox.client;

import com.williambl.portablejukebox.client.sound.MovingSound;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.RecordItem;

import java.util.Objects;

public class ClientUtils {

    public static void playDiscToPlayer(int entityId, int discId) {
        Registry.ITEM.getHolder(discId).map(Holder::value).ifPresent(item -> {
            if (!(item instanceof RecordItem disc)) {
                return;
            }

            var level = Minecraft.getInstance().level;
            if (level == null) {
                return;
            }

            var entity = level.getEntity(entityId);
            if (entity == null) {
                return;
            }

            Minecraft.getInstance().getSoundManager().stop();
            Minecraft.getInstance().getSoundManager().queueTickingSound(new MovingSound(entity, disc.getSound()));
        });
    }

    public static void stopDisc(int discId) {
        Registry.ITEM.getHolder(discId).map(Holder::value).ifPresent(item -> {
            if (!(item instanceof RecordItem disc)) {
                return;
            }

            Minecraft.getInstance().getSoundManager().stop(disc.getSound().getLocation(), SoundSource.NEUTRAL);
        });
    }
}
