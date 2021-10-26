package com.webcheckers.model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;



import static org.junit.jupiter.api.Assertions.*;

@Tag("Model-tier")
public class BoardTest {

    @Test
    public void test_Iterator() {
        final Board Cut = new Board();
        final Board compare = new Board();

        for(int x = 0; x < 8; x++){
            assertEquals(Cut.iterator(true).next().iterator().next().getCellIdx(),compare.iterator(true).next().iterator().next().getCellIdx());
        }

        for(int x = 0; x < 8; x++){
            assertEquals(Cut.iterator(false).next().iterator().next().getCellIdx(),compare.iterator(false).next().iterator().next().getCellIdx());
        }

    }
}
