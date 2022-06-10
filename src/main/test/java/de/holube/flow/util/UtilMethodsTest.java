package de.holube.flow.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UtilMethodsTest {

    @Test
    void mapTest() {
        assertEquals(0.5f, UtilMethods.map(0.5f, 0, 1, 0, 1));
        assertEquals(0.5f, UtilMethods.map(0.5f, 0, 1, 1, 0));
        assertEquals(0.5f, UtilMethods.map(0.5f, 1, 0, 0, 1));
        assertEquals(0.5f, UtilMethods.map(0.5f, 1, 0, 1, 0));

        assertEquals(5, UtilMethods.map(50, 0, 100, 0, 10));
    }

    @Test
    void modTest() {
        assertEquals(0, UtilMethods.mod(0, 10));
        assertEquals(6, UtilMethods.mod(6, 10));
        assertEquals(0, UtilMethods.mod(10, 10));

        assertEquals(1, UtilMethods.mod(1, 10));
        assertEquals(9, UtilMethods.mod(-1, 10));

        assertEquals(1, UtilMethods.mod(3, 2));
    }

}
