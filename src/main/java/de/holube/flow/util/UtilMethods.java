package de.holube.flow.util;

public class UtilMethods {

    public static float map(float value, float fromStart, float fromEnd, float toStart, float toEnd) {
        return toStart + (value - fromStart) * (toEnd - toStart) / (fromEnd - fromStart);
    }

    public static float mod(float value, float max) {
        if (value < 0) {
            return max + value % max;
        } else {
            return value % max;
        }
    }

}
