package com.baloise.open.thisorthat.service;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


public class CodeCreatorTest {

    @Test
    public void createCode() {
        String code0 = CodeCreator.createCode(0);
        assertEquals(code0.length(), 4);
        String code1 = CodeCreator.createCode(1);
        assertEquals(code1.length(), 4);
        String code2 = CodeCreator.createCode(2);
        assertEquals(code2.length(), 4);
        String code3 = CodeCreator.createCode(3);
        assertEquals(code3.length(), 4);
        String code4 = CodeCreator.createCode(4);
        assertEquals(code4.length(), 4);
        assertNotEquals(code0, code1);
        assertNotEquals(code0, code2);
        assertNotEquals(code0, code3);
        assertNotEquals(code0, code4);
    }
}