package de.holube.flow.util;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Vector2 {

    private float x;
    private float y;

    public void multi(float value) {
        x *= value;
        y *= value;
    }

    public void div(float value) {
        x /= value;
        y /= value;
    }

    public void add(Vector2 other) {
        x += other.x;
        y += other.y;
    }

    public void sub(Vector2 other) {
        x -= other.x;
        y -= other.y;
    }

    public void limit(float limit) {
        float mag = mag();
        if (mag > limit) {
            multi(limit / mag);
        }
    }

    public float mag() {
        return (float) Math.sqrt(x * x + y * y);
    }

    public void setMag(float mag) {
        multi(mag / mag());
    }

    public void normalize() {
        float m = mag();
        x /= m;
        y /= m;
    }

    public void mod(int width, int height) {
        x = modImpl(x, width);
        y = modImpl(y, height);
    }

    private static float modImpl(float value, float max) {
        if (value < 0) {
            return max + value % max;
        } else {
            return value % max;
        }
    }

    public void setAngle(float angle) {
        float mag = mag();
        x = (float) Math.cos(angle) * mag;
        y = (float) Math.sin(angle) * mag;
    }

    public static Vector2 sub(Vector2 a, Vector2 b) {
        return new Vector2(a.x - b.x, a.y - b.y);
    }
}
