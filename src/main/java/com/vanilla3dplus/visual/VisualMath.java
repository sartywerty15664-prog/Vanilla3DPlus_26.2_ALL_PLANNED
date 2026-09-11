package com.vanilla3dplus.visual;

public final class VisualMath {
    private VisualMath() {}

    public static boolean containsAny(String value, String... values) {
        if (value == null) return false;
        for (String candidate : values) {
            if (candidate != null && value.contains(candidate)) return true;
        }
        return false;
    }

    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}
