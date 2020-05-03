package HighSeaTowerGame;

public final class Utility {

    private Utility() {
    }

    public static double clamp(double min, double val, double max) {
        return Math.max(min, Math.min(max, val));
    }
}
