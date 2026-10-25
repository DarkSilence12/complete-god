package com.bai.god.client.render;

import net.minecraft.client.gui.GuiGraphics;

import java.util.HashMap;
import java.util.Map;

public final class GodRoundedRender {

    private GodRoundedRender() {
    }

    private static final Map<Integer, int[]> RADIUS_CACHE = new HashMap<>();

    private static int[] getIndents(int radius) {
        return RADIUS_CACHE.computeIfAbsent(radius, r -> {
            int[] indents = new int[r];
            double rd = (double) r;
            for (int i = 0; i < r; i++) {
                double dy = rd - (i + 0.5);
                double dx = Math.sqrt(Math.max(0.0, rd * rd - dy * dy));
                int indent = (int) Math.ceil(rd - dx + 0.001);
                if (indent < 0) {
                    indent = 0;
                }
                indents[i] = indent;
            }
            return indents;
        });
    }

    public static void drawRoundedRect(GuiGraphics pose, int x, int y, int width, int height, int radius, int color) {
        int right = x + width;
        int bottom = y + height;

        if (height <= 2 || width <= 2 || radius < 2) {
            GodDraw.fill(pose, x, y, right, bottom, color);
            return;
        }

        radius = Math.min(radius, Math.min(width, height) / 2);
        int[] indents = getIndents(radius);

        GodDraw.fill(pose, x, y + radius, right, bottom - radius, color);

        for (int i = 0; i < radius; i++) {
            int indent = indents[i];
            GodDraw.fill(pose, x + indent, y + i, right - indent, y + i + 1, color);
            GodDraw.fill(pose, x + indent, bottom - i - 1, right - indent, bottom - i, color);
        }
    }

    public static void drawRoundedRectOutline(GuiGraphics pose, int x, int y, int width, int height,
                                              int radius, int color, int borderWidth) {
        int right = x + width;
        int bottom = y + height;

        if (radius < 2 || borderWidth < 1) {
            GodDraw.fill(pose, x, y, right, y + borderWidth, color);
            GodDraw.fill(pose, x, bottom - borderWidth, right, bottom, color);
            GodDraw.fill(pose, x, y, x + borderWidth, bottom, color);
            GodDraw.fill(pose, right - borderWidth, y, right, bottom, color);
            return;
        }

        radius = Math.min(radius, Math.min(width, height) / 2);
        int[] indents = getIndents(radius);

        GodDraw.fill(pose, x + radius, y, right - radius, y + borderWidth, color);
        GodDraw.fill(pose, x + radius, bottom - borderWidth, right - radius, bottom, color);
        GodDraw.fill(pose, x, y + radius, x + borderWidth, bottom - radius, color);
        GodDraw.fill(pose, right - borderWidth, y + radius, right, bottom - radius, color);

        for (int i = 0; i < radius; i++) {
            int indent = indents[i];

            int leftStart = x + indent;
            int leftEnd = Math.min(leftStart + borderWidth, right - indent);
            if (leftStart < leftEnd) {
                GodDraw.fill(pose, leftStart, y + i, leftEnd, y + i + 1, color);
                GodDraw.fill(pose, leftStart, bottom - i - 1, leftEnd, bottom - i, color);
            }

            int rightStart = Math.max(right - indent - borderWidth, x + indent);
            int rightEnd = right - indent;
            if (rightStart < rightEnd) {
                GodDraw.fill(pose, rightStart, y + i, rightEnd, y + i + 1, color);
                GodDraw.fill(pose, rightStart, bottom - i - 1, rightEnd, bottom - i, color);
            }
        }
    }
}
