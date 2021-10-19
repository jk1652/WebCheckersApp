package com.webcheckers.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Map;

@Tag("Model-tier")
public class PlayerTest {

    @Test
    public void test_PlayerName() {
        final Player tester = new Player("test");

        // test name matches string
        assertEquals("test", tester.getName());

        // Test name wont match
        assertNotEquals("Not Name", tester.getName());

    }

    @Test
    public void test_NameEquals() {
        final Player tester = new Player("test");
        final Player other = new Player("other");
        final Object badobject = new Object();

        // test if objects are not equal
        assertNotEquals(tester, badobject);

        // equals checks for the name of player to be the same as it is unique
        assertEquals(tester, new Player("test"));

        // bad match
        assertNotEquals(tester, other);

    }


}
