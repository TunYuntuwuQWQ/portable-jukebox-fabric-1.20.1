package com.williambl.portablejukebox.platform.services;

import com.williambl.portablejukebox.jukebox.PortableJukeboxMessage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public interface IMessageSender {
    void sendMessage(PortableJukeboxMessage message, Entity tracking);
}
