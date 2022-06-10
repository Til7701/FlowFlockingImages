package de.holube.flow.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Vector2Test {

    @Test
    void addTest() {
        Vector2 v1 = new Vector2(1, 2);
        Vector2 v2 = new Vector2(3, 4);
        v1.add(v2);
        assertEquals(4, v1.getX());
        assertEquals(6, v1.getY());
    }

    @Test
    void subTest() {
        Vector2 v1 = new Vector2(1, 2);
        Vector2 v2 = new Vector2(3, 4);
        v1.sub(v2);
        assertEquals(-2, v1.getX());
        assertEquals(-2, v1.getY());
    }

    @Test
    void multiTest() {
        Vector2 v1 = new Vector2(1, 2);
        v1.multi(3);
        assertEquals(3, v1.getX());
        assertEquals(6, v1.getY());
    }

    @Test
    void divTest() {
        Vector2 v1 = new Vector2(6, 9);
        v1.div(3);
        assertEquals(2, v1.getX());
        assertEquals(3, v1.getY());
    }

    @Test
    void limitTest() {
        Vector2 v1 = new Vector2(2, 0);
        v1.limit(3);
        assertEquals(2, v1.getX());
        assertEquals(0, v1.getY());
        v1.limit(2);
        assertEquals(2, v1.getX());
        assertEquals(0, v1.getY());
        v1.limit(1);
        assertEquals(1, v1.getX());
        assertEquals(0, v1.getY());
    }

    @Test
    void magTest() {
        Vector2 v1 = new Vector2(2, 0);
        assertEquals(2, v1.mag());
    }

    @Test
    void setMagTest() {
        Vector2 v1 = new Vector2(2, 0);
        v1.setMag(3);
        assertEquals(3, v1.mag());
    }

    @Test
    void normalizeTest() {
        Vector2 v1 = new Vector2(2, 0);
        v1.normalize();
        assertEquals(1, v1.mag());
    }

    @Test
    void modTest() {
        Vector2 v1 = new Vector2(3, 0);
        v1.mod(4, 3);
        assertEquals(3, v1.getX());
        assertEquals(0, v1.getY());
        v1.mod(2, 5);
        assertEquals(1, v1.getX());
        assertEquals(0, v1.getY());

        v1 = new Vector2(-3, 0);
        v1.mod(4, 3);
        assertEquals(1, v1.getX());
        assertEquals(0, v1.getY());
        v1.mod(2, 5);
        assertEquals(1, v1.getX());
        assertEquals(0, v1.getY());
    }

    @Test
    void setAngleTest() {
        Vector2 v1 = new Vector2(1, 0);
        v1.setAngle((float) (Math.PI / 2));
        assertEquals(0, v1.getX(), 0.00001);
        assertEquals(1, v1.getY(), 0.00001);
    }

    @Test
    void getAngleTest() {
        Vector2 v1 = new Vector2(1, 0);
        assertEquals(0, v1.getAngle(), 0.00001);
        v1 = new Vector2(0, 1);
        assertEquals(Math.PI / 2, v1.getAngle(), 0.00001);
        v1 = new Vector2(-1, 0);
        assertEquals(Math.PI, v1.getAngle(), 0.00001);
        v1 = new Vector2(0, -1);
        assertEquals(Math.PI * 3 / 2, v1.getAngle(), 0.00001);
    }

}
