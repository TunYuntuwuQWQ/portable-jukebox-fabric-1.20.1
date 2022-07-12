package com.williambl.portablejukebox.platform.services;

import com.williambl.portablejukebox.jukebox.PortableJukeboxMessage;
import net.minecraft.server.level.ServerPlayer;

public interface IMessageSender {
    void sendMessage(PortableJukeboxMessage message, ServerPlayer... players);
}
