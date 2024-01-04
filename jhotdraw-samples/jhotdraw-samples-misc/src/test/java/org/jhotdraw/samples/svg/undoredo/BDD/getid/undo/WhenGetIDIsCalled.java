package org.jhotdraw.samples.svg.undoredo.BDD.getid.undo;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.jhotdraw.action.edit.UndoAction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class WhenGetIDIsCalled extends Stage<WhenGetIDIsCalled> {
        @ExpectedScenarioState
        private UndoAction undoAction;
    public WhenGetIDIsCalled whenGetIDIsCalled() {
        try {
            Method method = UndoAction.class.getDeclaredMethod("getID");
            method.setAccessible(true);
            method.invoke(undoAction, "edit.undo");
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
