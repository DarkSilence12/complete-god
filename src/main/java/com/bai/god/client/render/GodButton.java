package com.bai.god.client.render;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class GodButton extends Button {

    public GodButton(int x, int y, int width, int height, Component message, OnPress onPress) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
    }
}
