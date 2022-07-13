package com.williambl.portablejukebox.client.platform;

import com.williambl.portablejukebox.Constants;

import java.util.ServiceLoader;

public class ClientServices {

    public static <T> T load(Class<T> clazz) {

        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        Constants.LOG.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}
