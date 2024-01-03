package org.jhotdraw.samples.svg.undoredo.BDD.getid.redo;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.jhotdraw.action.edit.RedoAction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ThenTheInsertedIDIsReturned extends Stage<ThenTheInsertedIDIsReturned> {
    @ExpectedScenarioState
    private RedoAction redoAction;

    public ThenTheInsertedIDIsReturned thenTheInsertedIDIsReturned() {
        try {
            Method method = RedoAction.class.getDeclaredMethod("getID");
            method.setAccessible(true);
            method.invoke(redoAction, "edit.undo");

            assertEquals("Edit.redo", method.invoke(redoAction, "edit.redo") + "");
            assertNotEquals("Edit.redo", method.invoke(redoAction, "edit.undo") + "");
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
