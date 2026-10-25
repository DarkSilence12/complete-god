package com.bai.god.client.render;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.function.IntConsumer;

public class GodCustomizeScreen extends Screen {

    private static final String[] THEME_LABELS = {"粉色", "暗色", "青色", "金色"};
    private static final GodUiConfig.ThemePreset[] THEMES = {
            GodUiConfig.ThemePreset.PINK, GodUiConfig.ThemePreset.DARK,
            GodUiConfig.ThemePreset.CYAN, GodUiConfig.ThemePreset.GOLD
    };

    private static final String[] STYLE_LABELS = {"默认", "Windows", "macOS", "Linux"};
    private static final String[] STYLE_DESCS = {"经典布局", "Win11风格", "macOS风格", "GNOME风格"};
    private static final GodUiConfig.UIStyle[] STYLES = {
            GodUiConfig.UIStyle.DEFAULT, GodUiConfig.UIStyle.WINDOWS,
            GodUiConfig.UIStyle.MACOS, GodUiConfig.UIStyle.LINUX
    };

    private static final Object[][] COLOR_SLOTS = {
            {"面板背景", (java.util.function.IntSupplier) GodUiConfig::getPanelBg, (IntConsumer) GodUiConfig::setPanelBg},
            {"侧边栏背景", (java.util.function.IntSupplier) GodUiConfig::getSidebarBg, (IntConsumer) GodUiConfig::setSidebarBg},
            {"分类选中", (java.util.function.IntSupplier) GodUiConfig::getCategoryActive, (IntConsumer) GodUiConfig::setCategoryActive},
            {"分类悬停", (java.util.function.IntSupplier) GodUiConfig::getCategoryHover, (IntConsumer) GodUiConfig::setCategoryHover},
            {"主文字色", (java.util.function.IntSupplier) GodUiConfig::getTextPrimary, (IntConsumer) GodUiConfig::setTextPrimary},
            {"次文字色", (java.util.function.IntSupplier) GodUiConfig::getTextSecondary, (IntConsumer) GodUiConfig::setTextSecondary},
            {"内容区背景", (java.util.function.IntSupplier) GodUiConfig::getContentBg, (IntConsumer) GodUiConfig::setContentBg},
            {"行背景", (java.util.function.IntSupplier) GodUiConfig::getRowBg, (IntConsumer) GodUiConfig::setRowBg},
            {"行悬停", (java.util.function.IntSupplier) GodUiConfig::getRowBgHover, (IntConsumer) GodUiConfig::setRowBgHover},
            {"强调色", (java.util.function.IntSupplier) GodUiConfig::getAccent, (IntConsumer) GodUiConfig::setAccent},
            {"保存按钮", (java.util.function.IntSupplier) GodUiConfig::getBtnSaveBg, (IntConsumer) GodUiConfig::setBtnSaveBg},
            {"保存悬停", (java.util.function.IntSupplier) GodUiConfig::getBtnSaveHover, (IntConsumer) GodUiConfig::setBtnSaveHover},
            {"危险按钮", (java.util.function.IntSupplier) GodUiConfig::getBtnDangerBg, (IntConsumer) GodUiConfig::setBtnDangerBg},
            {"危险悬停", (java.util.function.IntSupplier) GodUiConfig::getBtnDangerHover, (IntConsumer) GodUiConfig::setBtnDangerHover},
            {"按钮文字", (java.util.function.IntSupplier) GodUiConfig::getBtnText, (IntConsumer) GodUiConfig::setBtnText},
            {"危险文字", (java.util.function.IntSupplier) GodUiConfig::getBtnDangerText, (IntConsumer) GodUiConfig::setBtnDangerText},
            {"卡片背景", (java.util.function.IntSupplier) GodUiConfig::getCardBg, (IntConsumer) GodUiConfig::setCardBg},
            {"卡片边框", (java.util.function.IntSupplier) GodUiConfig::getCardBorder, (IntConsumer) GodUiConfig::setCardBorder}
    };

    private final Screen parent;

    private GodRoundedTextFieldWidget titleField;
    private String titleDraft;

    private int panelX;
    private int panelY;
    private int panelW;
    private int panelH;
    private int slotStartY;

    public GodCustomizeScreen(Screen parent) {
        super(Component.literal("界面自定义"));
        this.parent = parent;
        this.titleDraft = GodUiConfig.getTitleText();
    }

    private boolean rebuildRequested = false;

    private void requestRebuild() {
        this.rebuildRequested = true;
    }

    private void deferredRebuild() {
        if (!this.rebuildRequested) {
            return;
        }
        this.rebuildRequested = false;
        this.clearWidgets();
        buildWidgets();
    }

    public static int slotCount() {
        return COLOR_SLOTS.length;
    }

    public static String slotName(int i) {
        return (String) COLOR_SLOTS[i][0];
    }

    public static int slotColor(int i) {
        return ((java.util.function.IntSupplier) COLOR_SLOTS[i][1]).getAsInt();
    }

    public static void setSlotColor(int i, int color) {
        ((IntConsumer) COLOR_SLOTS[i][2]).accept(color);
    }

    @Override
    protected void init() {
        super.init();
        this.clearWidgets();

        panelW = Math.min(560, this.width - 40);
        panelH = Math.min(340, this.height - 40);
        panelX = (this.width - panelW) / 2;
        panelY = (this.height - panelH) / 2;

        buildWidgets();
    }

    private void buildWidgets() {
        int y = panelY + 30;
        int x = panelX + 20;

        for (int i = 0; i < THEMES.length; i++) {
            final GodUiConfig.ThemePreset preset = THEMES[i];
            this.addRenderableWidget(new GodButton(x + i * 86, y, 80, 20,
                    Component.literal(THEME_LABELS[i]), b -> {
                GodUiConfig.applyTheme(preset);
                requestRebuild();
            }));
        }
        y += 30;

        for (int i = 0; i < STYLES.length; i++) {
            final GodUiConfig.UIStyle style = STYLES[i];
            this.addRenderableWidget(new GodButton(x + i * 86, y, 80, 20,
                    Component.literal(STYLE_LABELS[i]), b -> {
                GodUiConfig.setUIStyle(style);
                requestRebuild();
            }));
        }
        y += 34;

        titleField = new GodRoundedTextFieldWidget(this.font, x, y, 200, 20,
                Component.literal("UI标题"));
        titleField.setMaxLength(20);
        titleField.setValue(titleDraft);
        titleField.setResponder(s -> titleDraft = s);
        this.addWidget(titleField);
        y += 34;

        slotStartY = y;

        this.addRenderableWidget(new GodButton(panelX + panelW - 190, panelY + panelH - 30, 80, 20,
                Component.literal("← 返回"), b -> onClose()));
        this.addRenderableWidget(new GodButton(panelX + panelW - 100, panelY + panelH - 30, 80, 20,
                Component.literal("应用标题"), b -> {
            GodUiConfig.setTitleText(titleDraft != null && !titleDraft.isEmpty()
                    ? titleDraft : GodUiConfig.DEFAULT_TITLE);
            requestRebuild();
        }));
    }

    @Override
    public void render(GuiGraphics pose, int mouseX, int mouseY, float delta) {
        deferredRebuild();

        this.renderBackground(pose);

        GodRoundedRender.drawRoundedRect(pose, panelX, panelY, panelW, panelH, 8, GodUiConfig.getPanelBg());
        GodRoundedRender.drawRoundedRectOutline(pose, panelX, panelY, panelW, panelH, 8,
                GodUiConfig.getCardBorder(), 1);

        int x = panelX + 20;
        int y = panelY + 12;

        GodDraw.text(pose, this.font, this.title, x, y, GodUiConfig.getTextPrimary());

        GodDraw.text(pose, this.font, "透明度  " + String.format("%.2f", GodUiConfig.getAlpha()),
                x, panelY + 30 + 26 + 30 - 22, GodUiConfig.getTextSecondary());
        GodDraw.text(pose, this.font, "模糊度  " + String.format("%.2f", GodUiConfig.getBlur()),
                x + 280, panelY + 30 + 26 + 30 - 22, GodUiConfig.getTextSecondary());

        GodDraw.text(pose, this.font, "UI标题", x, slotStartY - 12, GodUiConfig.getTextSecondary());
        GodDraw.text(pose, this.font, "颜色槽位（点击色块修改）", x, slotStartY + 22,
                GodUiConfig.getTextSecondary());

        int sw = 40;
        int sh = 18;
        int gap = 6;
        for (int i = 0; i < COLOR_SLOTS.length; i++) {
            int col = i % 6;
            int row = i / 6;
            int sx = x + col * (sw + gap);
            int sy = slotStartY + 38 + row * (sh + gap);
            GodRoundedRender.drawRoundedRect(pose, sx, sy, sw, sh, 3, slotColor(i));
            GodRoundedRender.drawRoundedRectOutline(pose, sx, sy, sw, sh, 3,
                    GodUiConfig.getCardBorder(), 1);
        }

        if (titleField != null) {
            titleField.renderWidget(pose, mouseX, mouseY, delta);
        }

        super.render(pose, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int x = panelX + 20;
        int sw = 40;
        int sh = 18;
        int gap = 6;
        for (int i = 0; i < COLOR_SLOTS.length; i++) {
            int col = i % 6;
            int row = i / 3 * 1;
            row = i / 6;
            int sx = x + col * (sw + gap);
            int sy = slotStartY + 38 + row * (sh + gap);
            if (mouseX >= sx && mouseX < sx + sw && mouseY >= sy && mouseY < sy + sh) {
                final int index = i;
                if (this.minecraft != null) {
                    this.minecraft.setScreen(new GodColorPickerScreen(this, slotName(i), slotColor(i),
                            c -> setSlotColor(index, c)));
                }
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        GodUiConfig.setAlpha(GodUiConfig.getAlpha() + (float) amount * 0.05f);
        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        return super.charTyped(chr, modifiers);
    }

    @Override
    public void onClose() {
        if (titleDraft != null && !titleDraft.isEmpty()) {
            GodUiConfig.setTitleText(titleDraft);
        }
        if (this.minecraft != null) {
            this.minecraft.setScreen(parent);
        }
    }
}
