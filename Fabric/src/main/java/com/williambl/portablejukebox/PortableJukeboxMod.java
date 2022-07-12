package com.williambl.portablejukebox;

import net.fabricmc.api.ModInitializer;

public class PortableJukeboxMod implements ModInitializer {

    @Override
    public void onInitialize() {
        PortableJukeboxCommon.init();
    }
}
