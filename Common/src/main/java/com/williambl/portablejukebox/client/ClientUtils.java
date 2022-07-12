package com.williambl.portablejukebox.client;

import com.williambl.portablejukebox.client.sound.MovingSound;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.RecordItem;

import java.util.Objects;

public class ClientUtils {

    public static void playDiscToPlayer(int id, ResourceLocation disc) {
        Minecraft.getInstance().getSoundManager().stop();
        Minecraft.getInstance().getSoundManager().play(
                new MovingSound(
                        Minecraft.getInstance().level.getEntity(id),
                        ((RecordItem) Objects.requireNonNull(Registry.ITEM.get(disc))).getSound()
                )
        );
    }

    public static void stopDisc(ResourceLocation disc) {
        Minecraft.getInstance().getSoundManager().stop(((RecordItem) Objects.requireNonNull(Registry.ITEM.get(disc))).getSound().getLocation(), SoundSource.NEUTRAL);
    }
}
