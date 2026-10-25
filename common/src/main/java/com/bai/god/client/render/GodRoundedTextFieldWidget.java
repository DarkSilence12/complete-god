package com.bai.god.client.render;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

public class GodRoundedTextFieldWidget extends EditBox {

    private final Font fontRef;
    private int godTextColor = 0xFF000000;

    public GodRoundedTextFieldWidget(Font font, int x, int y, int width, int height, Component text) {
        super(font, x, y, width, height, text);
        this.fontRef = font;
        this.setBordered(false);
        this.setTextColor(0xFF000000);
        this.setTextColorUneditable(0xFF888888);
    }

    public void godSetTextColor(int color) {
        this.godTextColor = color;
        this.setTextColor(color);
    }

    @Override
    public void renderWidget(GuiGraphics pose, int mouseX, int mouseY, float delta) {
        int bg = GodUiConfig.getRowBg();
        int border = isFocused() ? GodUiConfig.getAccent() : GodUiConfig.getCardBorder();

        GodRoundedRender.drawRoundedRect(pose, this.getX(), this.getY(), this.getWidth(), this.getHeight(), 4, bg);
        GodRoundedRender.drawRoundedRectOutline(pose, this.getX(), this.getY(), this.getWidth(), this.getHeight(),
                4, border, 1);

        String visible = this.fontRef.plainSubstrByWidth(this.getValue(), this.getWidth() - 8);
        GodDraw.text(pose, this.fontRef, visible, this.getX() + 4, this.getY() + (this.getHeight() - 8) / 2, godTextColor);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
            this.setFocused(false);
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
