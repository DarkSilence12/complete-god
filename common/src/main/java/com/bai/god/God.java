package com.bai.god;

import com.bai.god.feature.GodConfig;
import com.bai.god.network.GodNetwork;
import com.bai.god.platform.Platform;
import com.bai.god.platform.Platforms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class God {

    public static final String MOD_ID = "complete_god";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private God() {
    }

    public static void init(Platform platform) {
        Platforms.set(platform);
        LOGGER.info("完整神 正在初始化...");
        GodNetwork.register();
        GodConfig.load();
        LOGGER.info("成神配置已加载（{} 项开关）", GodConfig.FLAG_COUNT);
        LOGGER.info("完整神 初始化完成（平台 {}）", platform.getClass().getSimpleName());
    }
}
