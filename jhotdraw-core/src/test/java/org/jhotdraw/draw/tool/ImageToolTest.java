
package org.jhotdraw.draw.tool;

import java.io.IOException;
import java.util.Collections;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.figure.ImageHolderFigure;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/*
 * The dependencies (DrawingEditor, DrawingView, and prototype ImageHolderFigure)
 * are mocked so that each test exercises a single code path through a single method
 * without opening real dialogs or touching the file system.
 */
public class ImageToolTest {

    private ImageHolderFigure prototype;
    private ImageTool tool;

    @Before
    public void setUp() {
        // Dependency → replaced with a mock so no real image is needed.
        prototype = mock(ImageHolderFigure.class);
        when(prototype.clone()).thenReturn(prototype);
        tool = new ImageTool(prototype);
    }

    // ============ Best-case tests ============
    // The best case is the tool being used as intended: constructing it and
    // setting/reading the file-selection mode.

    /**
     * Test best case: Default constructor behavior
     *
     * A freshly created tool must start in JFileChooser mode (not FileDialog mode).
     */
    @Test
    public void newToolDefaultsToJFileChooserMode() {
        // When - tool is created without configuration
        // Then - default should be JFileChooser (false)
        assertFalse("Newly created tool must default to JFileChooser mode",
                tool.isUseFileDialog());
    }

    /**
     * Test best case: Enabling FileDialog mode
     *
     * When setUseFileDialog(true) is called, the tool should switch to
     * native FileDialog mode.
     */
    @Test
    public void setUseFileDialogTrue_enablesFileDialogMode() {
        // Given
        tool.setUseFileDialog(true);

        // When/Then
        assertTrue("FileDialog mode should be enabled after setUseFileDialog(true)",
                tool.isUseFileDialog());
    }

    /**
     * Test best case: Switching back to JFileChooser mode
     *
     * When setUseFileDialog(false) is called, the tool should switch back to
     * JFileChooser mode.
     */
    @Test
    public void setUseFileDialogFalse_enablesChooserMode() {
        // Given - first switch to FileDialog mode
        tool.setUseFileDialog(true);
        assertTrue(tool.isUseFileDialog());

        // When - switch back to JFileChooser mode
        tool.setUseFileDialog(false);

        // Then
        assertFalse("JFileChooser mode should be enabled after setUseFileDialog(false)",
                tool.isUseFileDialog());
    }

    // ============ Boundary-case tests ============
    // Boundary cases probe the edges of the logic: the activate() guard when
    // there is no view, and repeated/alternating mode switches.

    /**
     * Boundary case: Activation with no active view
     *
     * The activate() method has a guard: if (view == null) return;
     * This test verifies that when the editor has no active view,
     * activate() returns early without attempting to select or load an image.
     */
    @Test
    public void activateWithNoView_returnsWithoutLoadingImage() throws IOException {
        // Given - a mock editor with no views
        DrawingEditor editor = mock(DrawingEditor.class);
        // super.activate() iterates the views, so return an empty list.
        when(editor.getDrawingViews()).thenReturn(Collections.<DrawingView>emptyList());
        // getView() returns editor.getActiveView(); null triggers the guard.
        when(editor.getActiveView()).thenReturn(null);

        // When
        tool.activate(editor);

        // Then - the guard returned early: no figure was created and no image was set.
        verify(prototype, never()).setImage(any(), any());
    }

    /**
     * Boundary case: Repeated mode switches maintain consistency
     *
     * The tool should maintain consistent state even when mode is switched
     * many times in succession.
     */
    @Test
    public void switchingModeRepeatedly_keepsStateConsistent() {
        // When - switching mode 5 times (even indices -> true, odd -> false)
        for (int i = 0; i < 5; i++) {
            tool.setUseFileDialog(i % 2 == 0);
        }

        // Then - last call was i=4 (even) → true
        assertTrue("After 5 switches, final mode should be FileDialog (true)",
                tool.isUseFileDialog());
    }

    // ============ Invariant test ============
    // The lab requires Java assertions for things that should never happen.
    // The relevant invariant for ImageTool is that its file-selection mode
    // is always in a well-defined state.

    /**
     * Invariant test: File-selection mode state is always well-defined
     *
     * Reading the useFileDialog flag must always agree with the value that was
     * last set. The invariant is that after setting the mode, reading it back
     * must return the exact value that was set.
     *
     * Run with JVM assertions enabled: java -ea
     */
    @Test
    public void useFileDialogState_isAlwaysWellDefined() {
        // When - enable FileDialog mode
        tool.setUseFileDialog(true);
        // Invariant: after enabling FileDialog mode the flag must be true.
        assert tool.isUseFileDialog() : "mode must be true after enabling FileDialog";

        // When - enable JFileChooser mode
        tool.setUseFileDialog(false);
        // Invariant: after enabling chooser mode the flag must be false.
        assert !tool.isUseFileDialog() : "mode must be false after enabling chooser";
    }
}
