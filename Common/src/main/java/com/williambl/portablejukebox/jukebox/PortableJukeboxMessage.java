package com.williambl.portablejukebox.jukebox;

import com.williambl.portablejukebox.client.DistHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketBuffer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class PortableJukeboxMessage {

    private final boolean startOrStop;
    private final int id;
    private final ResourceLocation disc;

    public PortableJukeboxMessage(boolean startOrStopIn, int id, ResourceLocation discIn) {
        this.startOrStop = startOrStopIn;
        this.id = id;
        this.disc = discIn;
    }

    public PortableJukeboxMessage(FriendlyByteBuf buf) {
        this(buf.readBoolean(), buf.readVarInt(), buf.readResourceLocation());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(this.startOrStop);
        buf.writeVarInt(this.id);
        buf.writeResourceLocation(this.disc);
    }

    public void handle(Consumer<Runnable> workExecutor) {
        if (this.startOrStop) {
            workExecutor.accept(() -> DistHelper.playDiscToPlayer(this.id, this.disc));
        } else {
            workExecutor.accept(() -> DistHelper.stopDisc(this.disc));
        }
    }
}
