package com.bai.god.client.render;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public class GodToggleSwitchWidget extends AbstractWidget {

    private static final int TRACK_W = 36;
    private static final int TRACK_H = 14;
    private static final int KNOB = 10;

    private boolean value;
    private final Consumer<Boolean> onChange;

    public GodToggleSwitchWidget(int x, int y, boolean value, Consumer<Boolean> onChange) {
        super(x, y, TRACK_W, TRACK_H, Component.empty());
        this.value = value;
        this.onChange = onChange;
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        value = !value;
        if (onChange != null) {
            onChange.accept(value);
        }
    }

    @Override
    public void renderWidget(GuiGraphics pose, int mouseX, int mouseY, float delta) {
        int trackColor = value ? GodUiConfig.getBtnSaveBg() : GodUiConfig.getBtnDangerBg();
        if (isHoveredOrFocused()) {
            trackColor = value ? GodUiConfig.getBtnSaveHover() : GodUiConfig.getBtnDangerHover();
        }
        int border = GodUiConfig.getCardBorder();

        GodRoundedRender.drawRoundedRect(pose, this.getX(), this.getY(), TRACK_W, TRACK_H, TRACK_H / 2, border);
        GodRoundedRender.drawRoundedRect(pose, this.getX() + 1, this.getY() + 1, TRACK_W - 2, TRACK_H - 2,
                (TRACK_H - 2) / 2, trackColor);

        int knobX = value ? this.getX() + TRACK_W - KNOB - 2 : this.getX() + 2;
        int knobY = this.getY() + (TRACK_H - KNOB) / 2;
        GodRoundedRender.drawRoundedRect(pose, knobX, knobY, KNOB, KNOB, KNOB / 2, 0xFFFFFFFF);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {
        this.defaultButtonNarrationText(output);
    }
}
