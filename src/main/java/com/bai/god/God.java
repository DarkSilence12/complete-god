package com.bai.god;

import com.bai.god.feature.GodConfig;
import com.bai.god.network.GodNetwork;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class God implements ModInitializer {

    public static final String MOD_ID = "god";
    public static final Logger LOGGER = LoggerFactory.getLogger("god");

    @Override
    public void onInitialize() {
        LOGGER.info("god mod by -1 正在初始化...");

        GodNetwork.register();

        GodEvents.register();

        GodConfig.load();
        LOGGER.info("成神配置已加载（{} 项开关）", GodConfig.FLAG_COUNT);

        LOGGER.info("god mod 初始化完成");
    }
}
