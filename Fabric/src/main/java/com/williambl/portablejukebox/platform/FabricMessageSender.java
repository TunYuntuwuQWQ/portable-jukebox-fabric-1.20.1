package com.williambl.portablejukebox.platform;

import com.williambl.portablejukebox.jukebox.PortableJukeboxMessage;
import com.williambl.portablejukebox.platform.services.IMessageSender;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public class FabricMessageSender implements IMessageSender {
    @Override
    public void sendMessage(PortableJukeboxMessage message, Entity tracking) {
        var buf = PacketByteBufs.create();
        message.encode(buf);

        for (var player : PlayerLookup.tracking(tracking)) {
            ServerPlayNetworking.send(player, PortableJukeboxMessage.MESSAGE_ID, buf);
        }
    }
}
