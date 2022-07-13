package com.williambl.portablejukebox.jukebox;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import static com.williambl.portablejukebox.PortableJukeboxCommon.id;

public record PortableJukeboxMessage(boolean startOrStop, int entityId, int disc) {

    public static ResourceLocation MESSAGE_ID = id("portable_jukebox");

    public PortableJukeboxMessage(FriendlyByteBuf buf) {
        this(buf.readBoolean(), buf.readVarInt(), buf.readVarInt());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(this.startOrStop);
        buf.writeVarInt(this.entityId);
        buf.writeVarInt(this.disc);
    }
}
