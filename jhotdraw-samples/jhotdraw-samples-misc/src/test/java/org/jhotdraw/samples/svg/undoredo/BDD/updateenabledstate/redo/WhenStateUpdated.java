package org.jhotdraw.samples.svg.undoredo.BDD.updateenabledstate.redo;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.jhotdraw.action.edit.RedoAction;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class WhenStateUpdated extends Stage<WhenStateUpdated> {
    @ExpectedScenarioState
    private RedoAction redoAction;
    @ExpectedScenarioState
    private Action realAction;

    public WhenStateUpdated actionIsNull() {
        try {

            Method method2 = RedoAction.class.getDeclaredMethod("updateView");
            method2.setAccessible(true);
            method2.invoke(redoAction, redoAction.getActiveView(), null);

            Method method1 = RedoAction.class.getDeclaredMethod("updateEnabledState");
            method1.setAccessible(true);
            method1.invoke(redoAction);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return self();
    }

    public WhenStateUpdated actionIsThis() {
        try {
            Method method2 = RedoAction.class.getDeclaredMethod("updateView");
            method2.setAccessible(true);
            method2.invoke(redoAction, redoAction.getActiveView(), redoAction.getActiveView());

            Method method = RedoAction.class.getDeclaredMethod("updateEnabledState");
            method.setAccessible(true);
            method.invoke(redoAction);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return self();
    }

    public WhenStateUpdated actionIsOther() {
        try {
            Method method2 = RedoAction.class.getDeclaredMethod("updateView");
            method2.setAccessible(true);
            method2.invoke(redoAction, redoAction.getActiveView(), redoAction.getActiveView().getActionMap().get("edit.undo"));

            Method method = RedoAction.class.getDeclaredMethod("updateEnabledState");
            method.setAccessible(true);
            method.invoke(redoAction);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return self();
    }
}
