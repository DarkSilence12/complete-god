package com.bai.god;

import com.bai.god.feature.GodConfig;
import com.bai.god.network.GodNetwork;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 完整神 —— Forge 1.20.1 入口（Fabric 版移植）。
 * <p>成神配置 + /wanzhengshen on|off + 极简防护（全伤害免疫 / 防 setHealth / 防 discard / 拦截死亡）。</p>
 * <p>技术标识使用 wanzhengshen（modId/命令/通道/存档），与「言出法随」模组的 god 标识完全隔离，可共存加载。</p>
 */
@Mod(God.MOD_ID)
public class God {

    public static final String MOD_ID = "wanzhengshen";
    public static final Logger LOGGER = LoggerFactory.getLogger("wanzhengshen");

    public God() {
        LOGGER.info("完整神(wanzhengshen) 正在初始化...");

        GodNetwork.register();

        GodEvents.register();

        GodConfig.load();
        LOGGER.info("成神配置已加载（{} 项开关）", GodConfig.FLAG_COUNT);

        LOGGER.info("完整神(wanzhengshen) 初始化完成");
    }
}
