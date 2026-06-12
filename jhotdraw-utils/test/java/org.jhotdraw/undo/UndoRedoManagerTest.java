import org.junit.Before;
import org.junit.Test;

import javax.swing.Action;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/** * Class-level unit tests for the Undo/Redo feature (UndoRedoManager). * Each test is mapped to a business rule (BRx) documented in the test plan. */
public class UndoRedoManagerTest {
    private UndoRedoManager manager;
    private StringBuilder model;
    @Before
    public void setUp() {
        manager = new UndoRedoManager();
        model = new StringBuilder();
    }

    // ===== BR1 : Undo reverts the most recent edit =======================
    @Test
    public void undo_revertsLastEdit() {
        manager.addEdit(new TestEdit(model, "A"));
        assertEquals("A", model.toString());

        manager.undo();

        assertEquals("Undo must revert the last edit", "", model.toString());
    }

    // ===== BR2 : Multiple undos revert in reverse order ==================
    @Test
    public void undo_revertsInReverseOrder() {
        manager.addEdit(new TestEdit(model, "A"));
        manager.addEdit(new TestEdit(model, "B"));
        manager.addEdit(new TestEdit(model, "C"));
        assertEquals("ABC", model.toString());

        manager.undo();
        assertEquals("AB", model.toString());
        manager.undo();
        assertEquals("A", model.toString());
        manager.undo();
        assertEquals("", model.toString());
    }

    // ===== BR3 : Redo re-applies the most recently undone edit ===========
    @Test
    public void redo_reappliesUndoneEdit() {
        manager.addEdit(new TestEdit(model, "A"));
        manager.undo();
        assertEquals("", model.toString());

        manager.redo();
        assertEquals("Redo must re-apply the undone edit", "A", model.toString());
    }

    // ===== BR4 : Multiple redos re-apply in original order ===============
    @Test
    public void redo_reappliesInOriginalOrder() {
        manager.addEdit(new TestEdit(model, "A"));
        manager.addEdit(new TestEdit(model, "B"));
        manager.undo();
        manager.undo();
        assertEquals("", model.toString());

        manager.redo();
        assertEquals("A", model.toString());
        manager.redo();
        assertEquals("AB", model.toString());
    }

    // ===== BR5 : New edit after undo discards redo history ===============
    @Test
    public void addEdit_afterUndo_clearsRedoHistory() {
        manager.addEdit(new TestEdit(model, "A"));
        manager.addEdit(new TestEdit(model, "B"));
        manager.undo(); // -> "A", redo available
        assertTrue(manager.canRedo());

        manager.addEdit(new TestEdit(model, "C")); // -> "AC"
        assertFalse("A new edit must clear the redo stack", manager.canRedo());
        assertEquals("AC", model.toString());
    }

    // ===== BR6 : Undo/redo fail safely when unavailable ==================
    @Test(expected = CannotUndoException.class)
    public void undo_withNothingToUndo_throws() {
        manager.undo();
    }

    @Test(expected = CannotRedoException.class)
    public void redo_withNothingToRedo_throws() {
        manager.addEdit(new TestEdit(model, "A"));
        manager.redo(); // nothing has been undone
    }

    // ===== BR7 : Edits during undo are ignored and killed ================
    @Test
    public void editsAddedDuringUndo_areIgnoredAndDie() {
        TestEdit nested = new TestEdit(new StringBuilder(), "N");
        manager.addEdit(new ReentrantEdit(manager, nested));

        manager.undo();

        assertEquals("Edit added during undo must be die()'d", 1, nested.dieCount);
        assertEquals("Killed edit must not run its undo logic", 0, nested.undoCount);
    }

    // ===== BR8 : canUndo / canRedo reflect state =========================
    @Test
    public void canUndoCanRedo_reflectAvailableOperations() {
        assertFalse(manager.canUndo());
        assertFalse(manager.canRedo());

        manager.addEdit(new TestEdit(model, "A"));
        assertTrue(manager.canUndo());
        assertFalse(manager.canRedo());

        manager.undo();
        assertFalse(manager.canUndo());
        assertTrue(manager.canRedo());

        manager.redo();
        assertTrue(manager.canUndo());
        assertFalse(manager.canRedo());
    }

    // ===== BR9 : Significant-edit flag + change event ====================
    @Test
    public void significantEdit_setsFlagAndFiresEvent() {
        final List<PropertyChangeEvent> events = new ArrayList<>();
        manager.addPropertyChangeListener(new PropertyChangeListener() {
            @Override public void propertyChange(PropertyChangeEvent e) {
                if ("hasSignificantEdits".equals(e.getPropertyName())) events.add(e);
            }
        });

        assertFalse(manager.hasSignificantEdits());
        manager.addEdit(new TestEdit(model, "A", true));

        assertTrue("Flag must be set", manager.hasSignificantEdits());
        assertEquals("Exactly one event expected", 1, events.size());
        assertEquals(Boolean.FALSE, events.get(0).getOldValue());
        assertEquals(Boolean.TRUE, events.get(0).getNewValue());
    }

    // ===== Supporting behaviour : Actions reflect state ==================
    @Test
    public void actions_reflectUndoRedoAvailability() {
        Action undoAction = manager.getUndoAction();
        Action redoAction = manager.getRedoAction();

        assertFalse(undoAction.isEnabled());
        assertFalse(redoAction.isEnabled());

        manager.addEdit(new TestEdit(model, "A"));
        assertTrue(undoAction.isEnabled());

        undoAction.actionPerformed(null); // perform undo via Action
        assertEquals("", model.toString());
        assertTrue(redoAction.isEnabled());

        redoAction.actionPerformed(null); // perform redo via Action
        assertEquals("A", model.toString());
    }

    // ===== discardAllEdits resets everything =============================
    @Test
    public void discardAllEdits_resetsState() {
        manager.addEdit(new TestEdit(model, "A"));
        manager.addEdit(new TestEdit(model, "B"));

        manager.discardAllEdits();

        assertFalse(manager.canUndo());
        assertFalse(manager.canRedo());
        assertFalse(manager.hasSignificantEdits());
    }
}
