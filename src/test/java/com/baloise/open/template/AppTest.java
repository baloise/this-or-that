/*
 * 
 */
package com.baloise.open.template;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * @author Markus Tiede
 */
class AppTest {
    /** @throws java.lang.Exception */
    @BeforeAll
    static void setUpBeforeClass() throws Exception {}

    /** @throws java.lang.Exception */
    @AfterAll
    static void tearDownAfterClass() throws Exception {}

    /** @throws java.lang.Exception */
    @BeforeEach
    void setUp() throws Exception {}

    /** @throws java.lang.Exception */
    @AfterEach
    void tearDown() throws Exception {}

    /**
     * Test method for {@link com.baloise.open.template.App#main(java.lang.String[])}.
     */
    @Test
    void testMain() {
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        App.main(null);
        assertEquals("Hello World!" + System.getProperty("line.separator"), outContent.toString());
        System.setOut(null);
    }
}
