package org.jhotdraw.samples.svg.undoredo.BDD.getid.undo;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.jhotdraw.action.edit.RedoAction;
import org.jhotdraw.action.edit.UndoAction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ThenTheInsertedIDIsReturned extends Stage<ThenTheInsertedIDIsReturned> {
    @ExpectedScenarioState
    private UndoAction undoAction;

    public ThenTheInsertedIDIsReturned thenTheInsertedIDIsReturned() {
        try {
            Method method = RedoAction.class.getDeclaredMethod("getID");
            method.setAccessible(true);
            method.invoke(undoAction, "edit.undo");

            assertEquals("Edit.undo", method.invoke(undoAction, "edit.undo") + "");
            assertNotEquals("Edit.undo", method.invoke(undoAction, "edit.redo") + "");
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        return self();
    }
}
