package com.williambl.portablejukebox.client;

import com.williambl.portablejukebox.client.sound.MovingSound;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.RecordItem;

import java.util.Objects;

public class ClientUtils {

    public static void playDiscToPlayer(int entityId, SoundEvent soundEvent) {
            var level = Minecraft.getInstance().level;
            if (level == null) {
                return;
            }

            var entity = level.getEntity(entityId);
            if (entity == null) {
                return;
            }

            Minecraft.getInstance().getSoundManager().stop();
            Minecraft.getInstance().getSoundManager().queueTickingSound(new MovingSound(entity, soundEvent));
    }

    public static void stopDisc(SoundEvent soundEvent) {
        Minecraft.getInstance().getSoundManager().stop(soundEvent.getLocation(), SoundSource.NEUTRAL);
    }
}
