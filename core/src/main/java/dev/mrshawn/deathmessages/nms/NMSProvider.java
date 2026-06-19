package dev.mrshawn.deathmessages.nms;

import dev.mrshawn.deathmessages.DeathMessages;
import dev.mrshawn.deathmessages.utils.Util;
import org.jspecify.annotations.Nullable;
import org.bukkit.Bukkit;

import java.lang.reflect.InvocationTargetException;

public class NMSProvider {

    private static NMSAdaptor instance;

    public static void initNMS() {
        // Paper / Folia 26.x (CalVer) and above use a single, reflection-free
        // adaptor backed by stable Paper API only.
        final String instClassPath = "paper.v1_21_4";

        try {
            instance = (NMSAdaptor) Class.forName(Util.NMS_PACKAGE_PREFIX_NAME + instClassPath + ".NMSAdaptorImpl").getConstructor().newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            DeathMessages.LOGGER.error("Could not find NMS implementation for {}", instClassPath, e);
            Bukkit.getPluginManager().disablePlugin(DeathMessages.getInstance());
        }
    }

    public static @Nullable NMSAdaptor get() {
        return instance;
    }
}
