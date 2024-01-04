package org.jhotdraw.samples.svg.undoredo.BDD.getid.redo;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.jhotdraw.action.edit.RedoAction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class WhenGetIDIsCalled extends Stage<WhenGetIDIsCalled> {
        @ExpectedScenarioState
        private RedoAction redoAction;
    public WhenGetIDIsCalled whenGetIDIsCalled() {
        try {
            Method method = RedoAction.class.getDeclaredMethod("getID");
            method.setAccessible(true);
            method.invoke(redoAction, "edit.redo");
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
