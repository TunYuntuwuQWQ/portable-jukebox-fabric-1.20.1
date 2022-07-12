package com.williambl.portablejukebox;

import net.minecraft.resources.ResourceLocation;

public class PortableJukeboxCommon {

    public static void init() {
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(Constants.MOD_ID, path);
    }
}