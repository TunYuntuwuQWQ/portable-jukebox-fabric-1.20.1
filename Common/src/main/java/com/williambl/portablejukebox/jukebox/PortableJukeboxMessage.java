package com.williambl.portablejukebox.jukebox;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;

import static com.williambl.portablejukebox.PortableJukeboxCommon.id;

public record PortableJukeboxMessage(boolean startOrStop, int entityId, int soundEventId) {

    public static ResourceLocation MESSAGE_ID = id("portable_jukebox");

    public static PortableJukeboxMessage start(Entity entity, SoundEvent soundEvent) {
        return new PortableJukeboxMessage(true, entity.getId(), Registry.SOUND_EVENT.getId(soundEvent));
    }

    public static PortableJukeboxMessage stop(Entity entity, SoundEvent soundEvent) {
        return new PortableJukeboxMessage(false, entity.getId(), Registry.SOUND_EVENT.getId(soundEvent));
    }

    public PortableJukeboxMessage(FriendlyByteBuf buf) {
        this(buf.readBoolean(), buf.readVarInt(), buf.readVarInt());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(this.startOrStop);
        buf.writeVarInt(this.entityId);
        buf.writeVarInt(this.soundEventId);
    }

    public SoundEvent soundEvent() {
        return Registry.SOUND_EVENT.getHolder(this.soundEventId).map(Holder::value).orElse(SoundEvents.CREEPER_PRIMED);
    }
}
