package org.jhotdraw.draw.tool;

import org.jhotdraw.draw.DefaultDrawingEditor;
import org.jhotdraw.draw.DrawingEditor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class BaseToolImplTest {
    private BaseToolImpl tool;
    private DrawingEditor editor;

    @Before
    public void setUp() throws Exception {
        tool = new BaseToolImpl();
        editor = mock(DrawingEditor.class);
    }

    @Test
    public void testIsInitiallyInactive() {
        assertFalse(tool.isActive());
    }

    @Test
    public void testIsNotFatInterface() {


        assertFalse("BaseTool should not be a MouseListener", tool instanceof MouseListener);
        assertFalse("BaseTool should not be a MouseMotionListener", tool instanceof MouseMotionListener);
        assertFalse("BaseTool should not be a KeyListener", tool instanceof KeyListener);
    }

    @Test
    public void testActivationLifecycle() {

        assertFalse("Tool should start inactive", tool.isActive());

        tool.activate(editor);
        assertTrue("Tool should be active after being activated", tool.isActive());

        assertEquals("Tool should know its editor", editor, tool.getEditor());

        tool.deactivate(editor);
        assertFalse("Tool should be inactive after being deactivated", tool.isActive());


    }


    @After
    public void tearDown() throws Exception {
    }
}