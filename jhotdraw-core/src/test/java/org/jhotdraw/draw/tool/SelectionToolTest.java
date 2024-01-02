package org.jhotdraw.draw.tool;

import org.jhotdraw.draw.DefaultDrawingEditor;
import org.jhotdraw.draw.DefaultDrawingView;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.event.ToolEvent;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class SelectionToolTest {
    @Test
    public void testSelectAreaTrackerMethods() {
        // Create instances of SelectionTool and SelectAreaTracker
        SelectionTool selectionTool = new SelectionTool();
        SelectAreaTracker selectAreaTracker = new DefaultSelectAreaTracker();

        //Makes sure getSelectAreaTracker creates new instance of SelectAreaTracker if none is given
        assertNotNull(selectionTool.getSelectAreaTracker(), "SelectAreaTracker should not be null initially");
        assertNotEquals(selectionTool.getSelectAreaTracker(), selectAreaTracker,"");

        selectionTool.setSelectAreaTracker(selectAreaTracker);

        assertSame(selectAreaTracker, selectionTool.getSelectAreaTracker(), "SelectAreaTracker should be set");

        SelectAreaTracker anotherSelectAreaTracker = new DefaultSelectAreaTracker();
        selectionTool.setSelectAreaTracker(anotherSelectAreaTracker);

        assertSame(anotherSelectAreaTracker, selectionTool.getSelectAreaTracker(), "SelectAreaTracker should be changed");
    }

    @Test
    public void testActivateAndDeactivate() {
        // Create a SelectionTool instance
        SelectionTool selectionTool = new SelectionTool();

        // Create a DrawingEditor instance
        DrawingEditor drawingEditor = new DefaultDrawingEditor();

        // Test activation and deactivation
        assertFalse(selectionTool.isActive(), "SelectionTool should initially be inactive");
        selectionTool.activate(drawingEditor);
        assertTrue(selectionTool.isActive(), "SelectionTool should be active after activation");
        selectionTool.deactivate(drawingEditor);
        assertFalse(selectionTool.isActive(), "SelectionTool should be inactive after deactivation");
    }
}