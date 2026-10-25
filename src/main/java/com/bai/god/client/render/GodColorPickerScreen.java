package com.bai.god.client.render;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class GodColorPickerScreen extends Screen {

    private static final int[] PRESETS = {
            0xFFF38BA8, 0xFFE94560, 0xFF00BCD4, 0xFFFF8F00,
            0xFF4CAF50, 0xFF9C27B0, 0xFF2196F3, 0xFF795548,
            0xFFFFFFFF, 0xFFF5F5F5, 0xFFE0E0E0, 0xFF9E9E9E,
            0xFF616161, 0xFF212121, 0xFF000000, 0xFF1A1A2E,
            0xFF16213E, 0xFF0F3460
    };

    private final Screen parent;
    private final String slotLabel;
    private final java.util.function.IntConsumer onPick;
    private final int originalColor;

    private int currentColor;

    public GodColorPickerScreen(Screen parent, String slotLabel, int currentColor,
                                java.util.function.IntConsumer onPick) {
        super(Component.literal("颜色选择 - " + slotLabel));
        this.parent = parent;
        this.slotLabel = slotLabel;
        this.currentColor = currentColor;
        this.originalColor = currentColor;
        this.onPick = onPick;
    }

    @Override
    protected void init() {
        int panelW = 320;
        int panelH = 220;
        int panelX = (this.width - panelW) / 2;
        int panelY = (this.height - panelH) / 2;

        int btnY = panelY + panelH - 30;
        this.addRenderableWidget(new GodButton(panelX + panelW / 2 - 80, btnY, 70, 20,
                Component.literal("确定"), b -> {
            if (onPick != null) {
                onPick.accept(currentColor);
            }
            if (this.minecraft != null) {
                this.minecraft.setScreen(parent);
            }
        }));
        this.addRenderableWidget(new GodButton(panelX + panelW / 2 + 10, btnY, 70, 20,
                Component.literal("取消"), b -> {
            if (onPick != null) {
                onPick.accept(originalColor);
            }
            if (this.minecraft != null) {
                this.minecraft.setScreen(parent);
            }
        }));
    }

    @Override
    public void render(GuiGraphics pose, int mouseX, int mouseY, float delta) {
        this.renderBackground(pose);

        int panelW = 320;
        int panelH = 220;
        int panelX = (this.width - panelW) / 2;
        int panelY = (this.height - panelH) / 2;

        GodRoundedRender.drawRoundedRect(pose, panelX, panelY, panelW, panelH, 8, GodUiConfig.getPanelBg());
        GodRoundedRender.drawRoundedRectOutline(pose, panelX, panelY, panelW, panelH, 8,
                GodUiConfig.getCardBorder(), 1);

        GodDraw.text(pose, this.font, this.title, panelX + 12, panelY + 10, GodUiConfig.getTextPrimary());

        GodRoundedRender.drawRoundedRect(pose, panelX + 12, panelY + 32, 60, 24, 4, currentColor);

        int swatchW = 40;
        int swatchH = 24;
        int gap = 6;
        int startX = panelX + 12;
        int startY = panelY + 66;
        for (int i = 0; i < PRESETS.length; i++) {
            int col = i % 6;
            int row = i / 6;
            int sx = startX + col * (swatchW + gap);
            int sy = startY + row * (swatchH + gap);
            GodRoundedRender.drawRoundedRect(pose, sx, sy, swatchW, swatchH, 3, PRESETS[i]);
            GodRoundedRender.drawRoundedRectOutline(pose, sx, sy, swatchW, swatchH, 3,
                    GodUiConfig.getCardBorder(), 1);
        }

        String hex = String.format("#%08X", currentColor);
        GodDraw.text(pose, this.font, hex, panelX + 84, panelY + 40, GodUiConfig.getTextSecondary());

        super.render(pose, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int panelW = 320;
        int panelH = 220;
        int panelX = (this.width - panelW) / 2;
        int panelY = (this.height - panelH) / 2;

        int swatchW = 40;
        int swatchH = 24;
        int gap = 6;
        int startX = panelX + 12;
        int startY = panelY + 66;

        for (int i = 0; i < PRESETS.length; i++) {
            int col = i % 6;
            int row = i / 6;
            int sx = startX + col * (swatchW + gap);
            int sy = startY + row * (swatchH + gap);
            if (mouseX >= sx && mouseX < sx + swatchW && mouseY >= sy && mouseY < sy + swatchH) {
                currentColor = PRESETS[i];
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void onClose() {
        if (onPick != null) {
            onPick.accept(originalColor);
        }
        if (this.minecraft != null) {
            this.minecraft.setScreen(parent);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
