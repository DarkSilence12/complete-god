package com.bai.god.client.render;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

public final class GodDraw {

    private GodDraw() {
    }

    public static void fill(GuiGraphics graphics, int x1, int y1, int x2, int y2, int color) {
        graphics.fill(x1, y1, x2, y2, color);
    }

    public static void text(GuiGraphics graphics, Font font, String text, int x, int y, int color) {
        graphics.drawString(font, text, x, y, color, false);
    }

    public static void centeredText(GuiGraphics graphics, Font font, String text, int centerX, int y, int color) {
        int width = font.width(text);
        graphics.drawString(font, text, centerX - width / 2, y, color, false);
    }

    public static void shadowText(GuiGraphics graphics, Font font, String text, int x, int y, int color) {
        graphics.drawString(font, text, x, y, color, true);
    }

    public static void shadowCenteredText(GuiGraphics graphics, Font font, String text, int centerX, int y, int color) {
        int width = font.width(text);
        graphics.drawString(font, text, centerX - width / 2, y, color, true);
    }

    public static void shadowText(GuiGraphics graphics, Font font, Component text, int x, int y, int color) {
        graphics.drawString(font, text, x, y, color, true);
    }

    public static void shadowCenteredText(GuiGraphics graphics, Font font, Component text, int centerX, int y, int color) {
        int width = font.width(text);
        graphics.drawString(font, text, centerX - width / 2, y, color, true);
    }

    public static void text(GuiGraphics graphics, Font font, Component text, int x, int y, int color) {
        graphics.drawString(font, text, x, y, color, false);
    }

    public static void text(GuiGraphics graphics, Font font, FormattedCharSequence text, int x, int y, int color) {
        graphics.drawString(font, text, x, y, color, false);
    }

    public static void centeredText(GuiGraphics graphics, Font font, Component text, int centerX, int y, int color) {
        int width = font.width(text);
        graphics.drawString(font, text, centerX - width / 2, y, color, false);
    }

    public static void enableScissor(GuiGraphics graphics, int x1, int y1, int x2, int y2) {
        graphics.enableScissor(x1, y1, x2, y2);
    }

    public static void disableScissor(GuiGraphics graphics) {
        graphics.disableScissor();
    }

    public static int textWidth(Font font, String text) {
        return font.width(text);
    }

    public static int lineHeight(Font font) {
        return font.lineHeight;
    }

    public static String trim(Font font, String text, int maxWidth) {
        return font.plainSubstrByWidth(text, maxWidth);
    }
}
