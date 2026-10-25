package com.bai.god.util;

import com.bai.god.God;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class GodEnv {

    private GodEnv() {
    }

    public static Path configDir() {
        return FabricLoader.getInstance().getConfigDir();
    }

    public static Path gameDir() {
        return FabricLoader.getInstance().getGameDir();
    }

    public static boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    public static boolean isClient() {
        return FabricLoader.getInstance().getEnvironmentType()
                == net.fabricmc.api.EnvType.CLIENT;
    }

    public static File configFile(String name) {
        File dir = configDir().resolve(God.MOD_ID).toFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return new File(dir, name);
    }

    public static void writeString(File file, String content) {
        try {
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            Files.write(file.toPath(), content.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            God.LOGGER.error("写入文件失败: {}", file, e);
        }
    }

    public static String readString(File file) {
        try {
            if (!file.exists()) {
                return null;
            }
            return new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
        } catch (IOException e) {
            God.LOGGER.error("读取文件失败: {}", file, e);
            return null;
        }
    }
}
