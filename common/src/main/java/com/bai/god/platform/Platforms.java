package com.bai.god.platform;

public final class Platforms {

    private static Platform impl;

    private Platforms() {
    }

    public static void set(Platform platform) {
        impl = platform;
    }

    public static Platform get() {
        if (impl == null) {
            throw new IllegalStateException("Platform 尚未初始化");
        }
        return impl;
    }
}
