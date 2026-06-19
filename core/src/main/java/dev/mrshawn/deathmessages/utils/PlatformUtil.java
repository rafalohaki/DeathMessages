package dev.mrshawn.deathmessages.utils;

import dev.mrshawn.deathmessages.DeathMessages;
import dev.mrshawn.deathmessages.nms.NMSProvider;
import org.bukkit.Bukkit;

import java.util.Arrays;

public class PlatformUtil {

    public static final boolean IS_PAPER = Util.doesClassExists("io.papermc.paper.configuration.GlobalConfiguration");
    public static final boolean IS_FOLIA = DeathMessages.getInstance().foliaLib.isFolia();

    public static void init() {
        NMSProvider.initNMS();
    }

     /*
        Sakamoto Util
        Cute Sakamoto helps you to process versions =w=
     */
    private final static String[] serverVersion;
    private final static int majorVersion;
    private final static int minorVersion;
    private final static int patchVersion;

    static {
        String[] parsedVersion;
        int parsedMajor = 0, parsedMinor = 0, parsedPatch = 0;
        try {
            parsedVersion = getServerVersion();
            parsedMajor = Integer.parseInt(parsedVersion[0]);
            parsedMinor = Integer.parseInt(parsedVersion[1]);
            parsedPatch = parsedVersion.length == 3 ? Integer.parseInt(parsedVersion[2]) : 0;
        } catch (final Throwable throwable) {
            DeathMessages.LOGGER.error("Failed to parse server version; version comparison will default to 0.0.0", throwable);
            parsedVersion = new String[]{"0", "0", "0"};
        }
        serverVersion = parsedVersion;
        majorVersion = parsedMajor;
        minorVersion = parsedMinor;
        patchVersion = parsedPatch;
    }

    // > (major, minor, patch)
    public static boolean isNewerThan(int major, int minor, int patch) {
        return compare(major, minor, patch) > 0;
    }

    // == (major, minor, patch)
    public static boolean isEqualTo(int major, int minor, int patch) {
        return compare(major, minor, patch) == 0;
    }

    // < (major, minor, patch)
    public static boolean isOlderThan(int major, int minor, int patch) {
        return compare(major, minor, patch) < 0;
    }

    // >= (major, minor, patch)
    public static boolean isNewerAndEqual(int major, int minor, int patch) {
        return compare(major, minor, patch) >= 0;
    }

    // <= (major, minor, patch)
    public static boolean isOlderAndEqual(int major, int minor, int patch) {
        return compare(major, minor, patch) <= 0;
    }

    // Redirections for old version schema

    // > (major, minor)
    public static boolean isNewerThan(int major, int minor) {
        return isNewerThan(1, major, minor);
    }

    // == (major, minor)
    public static boolean isEqualTo(int major, int minor) {
        return isEqualTo(1, major, minor);
    }

    // < (major, minor)
    public static boolean isOlderThan(int major, int minor) {
        return isOlderThan(1, major, minor);
    }

    // >= (major, minor)
    public static boolean isNewerAndEqual(int major, int minor) {
        return isNewerAndEqual(1, major, minor);
    }

    // <= (major, minor)
    public static boolean isOlderAndEqual(int major, int minor) {
        return isOlderAndEqual(1, major, minor);
    }

    private static int compare(int major, int minor, int patch) {
        if (majorVersion != major) {
            return Integer.compare(majorVersion, major);
        }

        if (minorVersion != minor) {
            return Integer.compare(minorVersion, minor);
        }

        return Integer.compare(patchVersion, patch);
    }

    // New server version schema
    // Paper / Folia / Canvas:
    //   getMinecraftVersion() -> "26.2" or "1.21.11" (clean, no build suffix)
    //   getBukkitVersion()    -> "26.2.build.821-alpha" or "1.21.11-R0.1-SNAPSHOT"
    //
    // We prefer Server#getMinecraftVersion() (Paper/Folia 1.20.5+, returns clean
    // Minecraft version without build suffixes) and fall back to getBukkitVersion()
    // with manual stripping for older Bukkit/Spigot.
    private static String[] getServerVersion() {
        String version;

        // Try the clean Paper/Folia API first (available since Paper 1.20.5+).
        try {
            version = Bukkit.getServer().getMinecraftVersion();
        } catch (final NoSuchMethodError | UnknownError ignored) {
            // Pre-1.20.5 Bukkit/Spigot — fall back to the legacy, messier source.
            version = Bukkit.getServer().getBukkitVersion();

            final int dashIndex = version.indexOf('-');

            // Strip after "-"
            if (dashIndex != -1) {
                version = version.substring(0, dashIndex);
            }

            // Process Paper's version schema
            if (PlatformUtil.IS_PAPER) {
                final int buildIndex = version.indexOf(".build");
                if (buildIndex != -1) {
                    version = version.substring(0, buildIndex);
                } else {
                    final int localIndex = version.indexOf(".local");
                    if (localIndex != -1) {
                        version = version.substring(0, localIndex);
                    }
                }
            }
        }

        String[] ret = version.split("\\.");

        if (ret.length < 2 || ret.length > 3) {
            throw new IllegalArgumentException("Invalid version format: [" + version + "]!");
        }

        return ret;
    }
}
