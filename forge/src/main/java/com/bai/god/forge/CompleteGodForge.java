package com.bai.god.forge;

import com.bai.god.God;
import com.bai.god.GodEvents;
import com.bai.god.platform.Platforms;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod(God.MOD_ID)
public class CompleteGodForge {

    public CompleteGodForge() {
        God.init(new ForgePlatform());
        ForgeNetwork.register();
        MinecraftForge.EVENT_BUS.register(new ForgeEvents());
        God.LOGGER.info("完整神 Forge 入口初始化完成");
    }
}
