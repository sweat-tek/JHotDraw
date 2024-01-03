package org.jhotdraw.samples.svg.undoredo.BDD.updateview.redo;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.jhotdraw.action.edit.RedoAction;
import org.jhotdraw.api.app.View;
import org.mockito.Mockito;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class WhenViewIsUpdated extends Stage<WhenViewIsUpdated> {
        @ExpectedScenarioState
        private RedoAction redoAction;
        @ExpectedScenarioState
        private View viewOld;
        @ExpectedScenarioState
        private View viewNew;

    public WhenViewIsUpdated whenViewIsUpdatedNew() {
        try {
            Method method = RedoAction.class.getDeclaredMethod("updateView");
            method.setAccessible(true);

            viewNew = Mockito.mock(View.class);
            method.invoke(redoAction, viewOld, viewNew);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        return self();
    }

    public WhenViewIsUpdated whenViewIsUpdatedOld() {
        try {
            Method method = RedoAction.class.getDeclaredMethod("updateView");
            method.setAccessible(true);

            viewNew = Mockito.mock(View.class);
            method.invoke(redoAction, viewOld, viewOld);
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
