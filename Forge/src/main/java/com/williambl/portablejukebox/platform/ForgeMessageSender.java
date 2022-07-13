package com.williambl.portablejukebox.platform;

import com.williambl.portablejukebox.client.ClientUtils;
import com.williambl.portablejukebox.jukebox.PortableJukeboxMessage;
import com.williambl.portablejukebox.platform.services.IMessageSender;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import static com.williambl.portablejukebox.PortableJukeboxCommon.id;

public class ForgeMessageSender implements IMessageSender {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            id("main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void init() {
        int id = 0;
        CHANNEL.registerMessage(id, PortableJukeboxMessage.class, PortableJukeboxMessage::encode, PortableJukeboxMessage::new, (msg, ctxSup) -> {
            var ctx = ctxSup.get();
            if (ctx.getDirection().getReceptionSide().isClient()) {
                ctx.enqueueWork(() -> {
                    if (msg.startOrStop()) {
                        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientUtils.playDiscToPlayer(msg.entityId(), msg.disc()));
                    } else {
                        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientUtils.stopDisc(msg.disc()));
                    }
                });
            }

            ctx.setPacketHandled(true);
        });
    }

    @Override
    public void sendMessage(PortableJukeboxMessage message, Entity tracking) {
        CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> tracking), message);
    }
}
