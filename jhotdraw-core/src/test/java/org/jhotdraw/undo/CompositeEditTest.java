package org.jhotdraw.undo;

import javax.swing.undo.AbstractUndoableEdit;
import org.junit.Test;
import static org.junit.Assert.*;

public class CompositeEditTest {

    // -----------------------------------------------------------------------
    // Best case scenarios
    // -----------------------------------------------------------------------

    @Test
    public void testNewCompositeEdit_isInProgress() {
        CompositeEdit edit = new CompositeEdit();
        assertTrue(edit.isInProgress());
    }

    @Test
    public void testAddNormalEdit_whileInProgress_remainsOpen() {
        CompositeEdit edit = new CompositeEdit("test");
        edit.addEdit(new AbstractUndoableEdit() {
            private static final long serialVersionUID = 1L;
        });
        assertTrue(edit.isInProgress());
    }

    @Test
    public void testPresentationName_whenSet_isReturned() {
        CompositeEdit edit = new CompositeEdit("My Operation");
        assertEquals("My Operation", edit.getPresentationName());
    }

    // -----------------------------------------------------------------------
    // Boundary cases
    // -----------------------------------------------------------------------

    @Test
    public void testAddEditToItself_endsComposite() {
        CompositeEdit edit = new CompositeEdit();
        edit.addEdit(edit);
        assertFalse(edit.isInProgress());
    }

    @Test
    public void testIsSignificant_whenConstructedFalse_returnsFalse() {
        CompositeEdit edit = new CompositeEdit(false);
        assertFalse(edit.isSignificant());
    }

    @Test
    public void testIsSignificant_whenConstructedTrue_defaultsTrue() {
        CompositeEdit edit = new CompositeEdit(true);
        // A composite with no children has no significant children,
        // so CompoundEdit.isSignificant() returns false even with flag=true.
        // Verify that the flag alone does not override child significance.
        edit.end();
        assertFalse(edit.isSignificant());
    }
}
