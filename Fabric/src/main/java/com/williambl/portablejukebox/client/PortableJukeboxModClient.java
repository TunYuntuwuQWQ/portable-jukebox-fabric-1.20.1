package com.williambl.portablejukebox.client;

import com.williambl.portablejukebox.jukebox.PortableJukeboxMessage;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class PortableJukeboxModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(PortableJukeboxMessage.MESSAGE_ID, (client, handler, buf, responseSender) -> {
            var message = new PortableJukeboxMessage(buf);
            client.execute(() -> {
                if (message.startOrStop()) {
                    ClientUtils.playDiscToPlayer(message.entityId(), message.soundEvent());
                } else {
                    ClientUtils.stopDisc(message.soundEvent());
                }
            });
        });
    }
}
