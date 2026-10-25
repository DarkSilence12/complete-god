package com.bai.god.client.render;

import com.bai.god.util.GodEnv;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

public class GodUiConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static final String DEFAULT_TITLE = "-1 配置";

    private static File configFile;

    private static File configFile() {
        if (configFile == null) {
            configFile = GodEnv.configFile("god_ui_config.json");
        }
        return configFile;
    }

    public enum ThemePreset {
        PINK,
        DARK,
        CYAN,
        GOLD,
        CUSTOM
    }

    public enum UIStyle {
        DEFAULT,
        WINDOWS,
        MACOS,
        LINUX
    }

    private static float alpha = 1.0f;
    private static float blur = 0.0f;
    private static ThemePreset themePreset = ThemePreset.PINK;
    private static UIStyle uiStyle = UIStyle.DEFAULT;

    private static int panelBg = 0xFFFFFFFF;
    private static int sidebarBg = 0xFFF5F5F5;
    private static int categoryActive = 0xFFF38BA8;
    private static int categoryHover = 0xFFF5E0E6;
    private static int textPrimary = 0xFF0A0A14;
    private static int textSecondary = 0xFF3A3A48;
    private static int contentBg = 0xFFFAFAFA;
    private static int rowBg = 0xFFFFFFFF;
    private static int rowBgHover = 0xFFF0F0F0;
    private static int accent = 0xFFF38BA8;
    private static int btnSaveBg = 0xFFF38BA8;
    private static int btnSaveHover = 0xFFF5A0C0;
    private static int btnDangerBg = 0xFFE8E8E8;
    private static int btnDangerHover = 0xFFFFCDD2;
    private static int btnText = 0xFFFFFFFF;
    private static int btnDangerText = 0xFFE53935;
    private static int cardBg = 0xFFFFFFFF;
    private static int cardBorder = 0xFFE8E8E8;
    private static String titleText = DEFAULT_TITLE;

    static {
        load();
    }

    public static void applyTheme(ThemePreset preset) {
        themePreset = preset;
        switch (preset) {
            case PINK -> {
                panelBg = 0xFFFFFFFF;
                sidebarBg = 0xFFF5F5F5;
                categoryActive = 0xFFF38BA8;
                categoryHover = 0xFFF5E0E6;
                textPrimary = 0xFF0A0A14;
                textSecondary = 0xFF3A3A48;
                contentBg = 0xFFFAFAFA;
                rowBg = 0xFFFFFFFF;
                rowBgHover = 0xFFF0F0F0;
                accent = 0xFFF38BA8;
                btnSaveBg = 0xFFF38BA8;
                btnSaveHover = 0xFFF5A0C0;
                btnDangerBg = 0xFFE8E8E8;
                btnDangerHover = 0xFFFFCDD2;
                btnText = 0xFFFFFFFF;
                btnDangerText = 0xFFE53935;
                cardBg = 0xFFFFFFFF;
                cardBorder = 0xFFE8E8E8;
            }
            case DARK -> {
                panelBg = 0xFF1A1A2E;
                sidebarBg = 0xFF16213E;
                categoryActive = 0xFFE94560;
                categoryHover = 0xFF0F3460;
                textPrimary = 0xFFEAEAEA;
                textSecondary = 0xFFA0A0B0;
                contentBg = 0xFF1F1F35;
                rowBg = 0xFF252545;
                rowBgHover = 0xFF2A2A50;
                accent = 0xFFE94560;
                btnSaveBg = 0xFFE94560;
                btnSaveHover = 0xFFFF6B81;
                btnDangerBg = 0xFF2A2A50;
                btnDangerHover = 0xFF533483;
                btnText = 0xFFFFFFFF;
                btnDangerText = 0xFFFF6B6B;
                cardBg = 0xFF252545;
                cardBorder = 0xFF3A3A5C;
            }
            case CYAN -> {
                panelBg = 0xFFF0FAFF;
                sidebarBg = 0xFFE0F0FF;
                categoryActive = 0xFF00BCD4;
                categoryHover = 0xFFB2EBF2;
                textPrimary = 0xFF0A1414;
                textSecondary = 0xFF3A5050;
                contentBg = 0xFFF5FBFF;
                rowBg = 0xFFFFFFFF;
                rowBgHover = 0xFFE0F7FA;
                accent = 0xFF00BCD4;
                btnSaveBg = 0xFF00BCD4;
                btnSaveHover = 0xFF4DD0E1;
                btnDangerBg = 0xFFE0E0E0;
                btnDangerHover = 0xFFFFCDD2;
                btnText = 0xFFFFFFFF;
                btnDangerText = 0xFFE53935;
                cardBg = 0xFFFFFFFF;
                cardBorder = 0xFFB2EBF2;
            }
            case GOLD -> {
                panelBg = 0xFFFFFDE7;
                sidebarBg = 0xFFFFF8E1;
                categoryActive = 0xFFFF8F00;
                categoryHover = 0xFFFFECB3;
                textPrimary = 0xFF1A1000;
                textSecondary = 0xFF5A4A20;
                contentBg = 0xFFFFFCF0;
                rowBg = 0xFFFFFFFF;
                rowBgHover = 0xFFFFF3CD;
                accent = 0xFFFF8F00;
                btnSaveBg = 0xFFFF8F00;
                btnSaveHover = 0xFFFFAB40;
                btnDangerBg = 0xFFF0E0C0;
                btnDangerHover = 0xFFFFCDD2;
                btnText = 0xFFFFFFFF;
                btnDangerText = 0xFFE53935;
                cardBg = 0xFFFFFFFF;
                cardBorder = 0xFFFFECB3;
            }
            case CUSTOM -> {
            }
        }
        save();
    }

    public static int applyAlpha(int color) {
        if (alpha >= 1.0f) {
            return color;
        }
        int origAlpha = (color >>> 24) & 0xFF;
        int newAlpha = (int) (origAlpha * alpha);
        if (newAlpha < 0) {
            newAlpha = 0;
        }
        if (newAlpha > 255) {
            newAlpha = 255;
        }
        return (color & 0x00FFFFFF) | (newAlpha << 24);
    }

    public static float getAlpha() { return alpha; }

    public static void setAlpha(float v) { alpha = Math.max(0.0f, Math.min(1.0f, v)); save(); }

    public static float getBlur() { return blur; }

    public static void setBlur(float v) { blur = Math.max(0.0f, Math.min(1.0f, v)); save(); }

    public static ThemePreset getThemePreset() { return themePreset; }

    public static int getPanelBg() { return applyAlpha(panelBg); }

    public static void setPanelBg(int v) { panelBg = v; themePreset = ThemePreset.CUSTOM; save(); }

    public static int getSidebarBg() { return applyAlpha(sidebarBg); }

    public static void setSidebarBg(int v) { sidebarBg = v; themePreset = ThemePreset.CUSTOM; save(); }

    public static int getCategoryActive() { return applyAlpha(categoryActive); }

    public static void setCategoryActive(int v) { categoryActive = v; themePreset = ThemePreset.CUSTOM; save(); }

    public static int getCategoryHover() { return applyAlpha(categoryHover); }

    public static void setCategoryHover(int v) { categoryHover = v; themePreset = ThemePreset.CUSTOM; save(); }

    public static int getTextPrimary() { return applyAlpha(textPrimary); }

    public static void setTextPrimary(int v) { textPrimary = v; themePreset = ThemePreset.CUSTOM; save(); }

    public static int getTextSecondary() { return applyAlpha(textSecondary); }

    public static void setTextSecondary(int v) { textSecondary = v; themePreset = ThemePreset.CUSTOM; save(); }

    public static int getContentBg() { return applyAlpha(contentBg); }

    public static void setContentBg(int v) { contentBg = v; themePreset = ThemePreset.CUSTOM; save(); }

    public static int getRowBg() { return applyAlpha(rowBg); }

    public static void setRowBg(int v) { rowBg = v; themePreset = ThemePreset.CUSTOM; save(); }

    public static int getRowBgHover() { return applyAlpha(rowBgHover); }

    public static void setRowBgHover(int v) { rowBgHover = v; themePreset = ThemePreset.CUSTOM; save(); }

    public static int getAccent() { return applyAlpha(accent); }

    public static void setAccent(int v) { accent = v; themePreset = ThemePreset.CUSTOM; save(); }

    public static int getBtnSaveBg() { return applyAlpha(btnSaveBg); }

    public static void setBtnSaveBg(int v) { btnSaveBg = v; themePreset = ThemePreset.CUSTOM; save(); }

    public static int getBtnSaveHover() { return applyAlpha(btnSaveHover); }

    public static void setBtnSaveHover(int v) { btnSaveHover = v; themePreset = ThemePreset.CUSTOM; save(); }

    public static int getBtnDangerBg() { return applyAlpha(btnDangerBg); }

    public static void setBtnDangerBg(int v) { btnDangerBg = v; themePreset = ThemePreset.CUSTOM; save(); }

    public static int getBtnDangerHover() { return applyAlpha(btnDangerHover); }

    public static void setBtnDangerHover(int v) { btnDangerHover = v; themePreset = ThemePreset.CUSTOM; save(); }

    public static int getBtnText() { return applyAlpha(btnText); }

    public static void setBtnText(int v) { btnText = v; themePreset = ThemePreset.CUSTOM; save(); }

    public static int getBtnDangerText() { return applyAlpha(btnDangerText); }

    public static void setBtnDangerText(int v) { btnDangerText = v; themePreset = ThemePreset.CUSTOM; save(); }

    public static int getCardBg() { return applyAlpha(cardBg); }

    public static void setCardBg(int v) { cardBg = v; themePreset = ThemePreset.CUSTOM; save(); }

    public static int getCardBorder() { return applyAlpha(cardBorder); }

    public static void setCardBorder(int v) { cardBorder = v; themePreset = ThemePreset.CUSTOM; save(); }

    public static String getTitleText() { return titleText; }

    public static void setTitleText(String v) { titleText = v; save(); }

    public static UIStyle getUIStyle() { return uiStyle; }

    public static void setUIStyle(UIStyle style) { uiStyle = style; save(); }

    private static void load() {
        try {
            File file = configFile();
            if (file.exists()) {
                try (Reader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
                    ConfigData data = GSON.fromJson(reader, ConfigData.class);
                    if (data != null) {
                        alpha = data.alpha;
                        blur = data.blur;
                        themePreset = data.themePreset != null ? data.themePreset : ThemePreset.PINK;
                        uiStyle = data.uiStyle != null ? data.uiStyle : UIStyle.DEFAULT;
                        titleText = data.titleText != null ? data.titleText : DEFAULT_TITLE;
                        panelBg = data.panelBg;
                        sidebarBg = data.sidebarBg;
                        categoryActive = data.categoryActive;
                        categoryHover = data.categoryHover;
                        textPrimary = data.textPrimary;
                        textSecondary = data.textSecondary;
                        contentBg = data.contentBg;
                        rowBg = data.rowBg;
                        rowBgHover = data.rowBgHover;
                        accent = data.accent;
                        btnSaveBg = data.btnSaveBg;
                        btnSaveHover = data.btnSaveHover;
                        btnDangerBg = data.btnDangerBg;
                        btnDangerHover = data.btnDangerHover;
                        btnText = data.btnText;
                        btnDangerText = data.btnDangerText;
                        cardBg = data.cardBg;
                        cardBorder = data.cardBorder;
                    }
                }
            }
        } catch (Exception ignored) {
        }
    }

    private static void save() {
        try {
            File file = configFile();
            File parent = file.getParentFile();
            if (parent != null) {
                parent.mkdirs();
            }
            ConfigData data = new ConfigData();
            data.alpha = alpha;
            data.blur = blur;
            data.themePreset = themePreset;
            data.uiStyle = uiStyle;
            data.titleText = titleText;
            data.panelBg = panelBg;
            data.sidebarBg = sidebarBg;
            data.categoryActive = categoryActive;
            data.categoryHover = categoryHover;
            data.textPrimary = textPrimary;
            data.textSecondary = textSecondary;
            data.contentBg = contentBg;
            data.rowBg = rowBg;
            data.rowBgHover = rowBgHover;
            data.accent = accent;
            data.btnSaveBg = btnSaveBg;
            data.btnSaveHover = btnSaveHover;
            data.btnDangerBg = btnDangerBg;
            data.btnDangerHover = btnDangerHover;
            data.btnText = btnText;
            data.btnDangerText = btnDangerText;
            data.cardBg = cardBg;
            data.cardBorder = cardBorder;
            try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
                GSON.toJson(data, writer);
            }
        } catch (Exception ignored) {
        }
    }

    private static class ConfigData {
        float alpha = 1.0f;
        float blur = 0.0f;
        ThemePreset themePreset = ThemePreset.PINK;
        UIStyle uiStyle = UIStyle.DEFAULT;
        String titleText = DEFAULT_TITLE;
        int panelBg = 0xFFFFFFFF;
        int sidebarBg = 0xFFF5F5F5;
        int categoryActive = 0xFFF38BA8;
        int categoryHover = 0xFFF5E0E6;
        int textPrimary = 0xFF0A0A14;
        int textSecondary = 0xFF3A3A48;
        int contentBg = 0xFFFAFAFA;
        int rowBg = 0xFFFFFFFF;
        int rowBgHover = 0xFFF0F0F0;
        int accent = 0xFFF38BA8;
        int btnSaveBg = 0xFFF38BA8;
        int btnSaveHover = 0xFFF5A0C0;
        int btnDangerBg = 0xFFE8E8E8;
        int btnDangerHover = 0xFFFFCDD2;
        int btnText = 0xFFFFFFFF;
        int btnDangerText = 0xFFE53935;
        int cardBg = 0xFFFFFFFF;
        int cardBorder = 0xFFE8E8E8;
    }
}
