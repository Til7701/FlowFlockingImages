package de.holube.flow.util;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Vector2 {

    private float x;
    private float y;

    public void add(Vector2 other) {
        x += other.x;
        y += other.y;
    }

    public void sub(Vector2 other) {
        x -= other.x;
        y -= other.y;
    }

    public void multi(float value) {
        x *= value;
        y *= value;
    }

    public void div(float value) {
        x /= value;
        y /= value;
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
        x = UtilMethods.mod(x, width);
        y = UtilMethods.mod(y, height);
    }

    public void setAngle(float angle) {
        float mag = mag();
        x = (float) Math.cos(angle) * mag;
        y = (float) Math.sin(angle) * mag;
    }

    public float getAngle() {
        float angle = (float) Math.atan2(y, x);
        if (angle < 0) {
            angle += 2 * Math.PI;
        }
        return angle;
    }

    public static Vector2 sub(Vector2 a, Vector2 b) {
        return new Vector2(a.x - b.x, a.y - b.y);
    }

    public static float dist(Vector2 v1, Vector2 v2) {
        return (float) Math.sqrt(Math.pow(v1.getX() - v2.getX(), 2) + Math.pow(v1.getY() - v2.getY(), 2));
    }
}
