package org.jhotdraw.draw;

import org.junit.After;
import org.junit.Before;

import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DefaultDrawingEditorTest {
    private MouseListener mouseListener;
    private KeyListener keyListener;
    private MouseListener combinedInterface;



    @Before
    public void setUp() throws Exception {
        mouseListener = mock(MouseListener.class);

    }



    @After
    public void tearDown() throws Exception {
    }
}