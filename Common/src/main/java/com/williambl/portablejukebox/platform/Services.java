package com.williambl.portablejukebox.platform;

import com.williambl.portablejukebox.Constants;
import com.williambl.portablejukebox.platform.services.IMessageSender;
import com.williambl.portablejukebox.platform.services.IPlatformHelper;
import com.williambl.portablejukebox.platform.services.IPortableJukeboxRegistry;

import java.util.ServiceLoader;

public class Services {

    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);
    public static final IPortableJukeboxRegistry REGISTRY = load(IPortableJukeboxRegistry.class);
    public static final IMessageSender MESSAGES = load(IMessageSender.class);

    public static <T> T load(Class<T> clazz) {

        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        Constants.LOG.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}
