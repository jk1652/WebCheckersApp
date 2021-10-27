package com.webcheckers.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;


@Tag("Model-tier")
public class PlayerTest {

    /**
     * Test if the player name works
     */
    @Test
    public void test_PlayerName() {
        final Player tester = new Player("test");

        // test name matches string
        assertEquals("test", tester.getName());

        // Test name wont match
        assertNotEquals("Not Name", tester.getName());

    }

    /**
     * to check if the equals function works to compare Players
     */
    @Test
    public void test_NameEquals() {
        final Player tester = new Player("test");
        final Player other = new Player("other");
        final Object badObject = new Object();

        // test if objects are not equal
        assertNotEquals(tester, badObject);

        // equals checks only for the name of player to be the same as it has to be unique
        assertEquals(tester, new Player("test"));

        // bad match
        assertNotEquals(tester, other);

    }


}
