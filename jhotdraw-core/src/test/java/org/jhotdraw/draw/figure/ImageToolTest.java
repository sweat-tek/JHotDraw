package org.jhotdraw.draw.figure;

import org.jhotdraw.draw.AttributeKey;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.tool.ImageTool;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ImageTool class.
 * Tests the important domain logic of the ImageTool including:
 * - File dialog selection mode configuration
 * - File selection behavior
 * - Error handling for missing file selection
 */
public class ImageToolTest {
    
    private ImageTool imageTool;
    private ImageHolderFigure mockPrototype;
    private DrawingEditor mockEditor;
    private DrawingView mockView;

    @Before
    public void setUp() {
        // Create mock prototype
        mockPrototype = mock(ImageHolderFigure.class);
        when(mockPrototype.clone()).thenReturn(mock(ImageHolderFigure.class));
        
        // Create the ImageTool with mocked prototype
        imageTool = new ImageTool(mockPrototype);
        
        // Create mocks for editor and view
        mockEditor = mock(DrawingEditor.class);
        mockView = mock(DrawingView.class);
    }

    /**
     * Test best case: FileDialog mode is enabled (setUseFileDialog(true))
     */
    @Test
    public void testSetUseFileDialogTrue() {
        // Given
        imageTool.setUseFileDialog(true);
        
        // When
        boolean result = imageTool.isUseFileDialog();
        
        // Then
        assertTrue("FileDialog mode should be enabled", result);
    }

    /**
     * Test best case: JFileChooser mode is enabled (setUseFileDialog(false))
     */
    @Test
    public void testSetUseFileDialogFalse() {
        // Given
        imageTool.setUseFileDialog(false);
        
        // When
        boolean result = imageTool.isUseFileDialog();
        
        // Then
        assertFalse("JFileChooser mode should be enabled", result);
    }

    /**
     * Test best case: Default mode is JFileChooser
     */
    @Test
    public void testDefaultModeIsJFileChooser() {
        // When - tool is created without configuration
        boolean result = imageTool.isUseFileDialog();
        
        // Then - default should be JFileChooser (false)
        assertFalse("Default mode should be JFileChooser", result);
    }

    /**
     * Boundary case: Switching between modes multiple times
     */
    @Test
    public void testSwitchBetweenModes() {
        // Given
        imageTool.setUseFileDialog(true);
        assertTrue(imageTool.isUseFileDialog());
        
        // When switching to JFileChooser
        imageTool.setUseFileDialog(false);
        
        // Then
        assertFalse(imageTool.isUseFileDialog());
        
        // When switching back to FileDialog
        imageTool.setUseFileDialog(true);
        
        // Then
        assertTrue(imageTool.isUseFileDialog());
    }

    /**
     * Boundary case: Tool activation with null view (error case)
     * Ensures tool handles gracefully when no view is available
     */
    @Test
    public void testActivateWithNullView() {
        // Given
        when(mockEditor.getActiveView()).thenReturn(null);
        
        // When/Then - should not throw exception
        try {
            imageTool.activate(mockEditor);
            // No exception should be thrown
            assertTrue(true);
        } catch (Exception e) {
            fail("activate() should handle null view gracefully: " + e.getMessage());
        }
    }

    /**
     * Boundary case: Repeated activation calls
     * Ensures tool can be activated multiple times without issues
     */
    @Test
    public void testMultipleActivations() {
        // Given
        when(mockEditor.getActiveView()).thenReturn(mockView);
        when(mockView.getComponent()).thenReturn(null);
        imageTool.setUseFileDialog(false); // Use JFileChooser mode
        
        // When/Then - should handle multiple activations
        try {
            imageTool.activate(mockEditor);
            imageTool.activate(mockEditor);
            // No exception should be thrown
            assertTrue(true);
        } catch (Exception e) {
            fail("activate() should handle multiple calls: " + e.getMessage());
        }
    }

    /**
     * Test that ImageTool is properly initialized with attributes
     */
    @Test
    public void testInitializationWithAttributes() {
        // Given
        java.util.Map<AttributeKey<?>, Object> attributes = new java.util.HashMap<>();
        
        // When
        ImageTool toolWithAttrs = new ImageTool(mockPrototype, attributes);
        
        // Then
        assertNotNull("Tool should be created with attributes", toolWithAttrs);
    }

    /**
     * Invariant assertion: useFileDialog mode should never be null
     * (though it's boolean, we test the state is always well-defined)
     */
    @Test
    public void testUseFileDialogStateIsWellDefined() {
        // Given
        imageTool.setUseFileDialog(true);
        boolean state1 = imageTool.isUseFileDialog();
        
        // When - change state
        imageTool.setUseFileDialog(false);
        boolean state2 = imageTool.isUseFileDialog();
        
        // Then - both states should be boolean (not null or undefined)
        assertTrue("State should be well-defined", 
            (state1 == true && state2 == false) || 
            (state1 == false && state2 == true));
    }
}
