package org.jhotdraw.samples.svg.undoredo.BDD.updateenabledstate.undo;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.jhotdraw.action.edit.UndoAction;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class WhenStateUpdated extends Stage<WhenStateUpdated> {
    @ExpectedScenarioState
    private UndoAction undoAction;
    @ExpectedScenarioState
    private Action realAction;

    public WhenStateUpdated actionIsNull() {
        try {

            Method method2 = UndoAction.class.getDeclaredMethod("updateView");
            method2.setAccessible(true);
            method2.invoke(undoAction, undoAction.getActiveView(), null);

            Method method1 = UndoAction.class.getDeclaredMethod("updateEnabledState");
            method1.setAccessible(true);
            method1.invoke(undoAction);
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
            Method method2 = UndoAction.class.getDeclaredMethod("updateView");
            method2.setAccessible(true);
            method2.invoke(undoAction, undoAction.getActiveView(), undoAction.getActiveView());

            Method method = UndoAction.class.getDeclaredMethod("updateEnabledState");
            method.setAccessible(true);
            method.invoke(undoAction);
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
            Method method2 = UndoAction.class.getDeclaredMethod("updateView");
            method2.setAccessible(true);
            method2.invoke(undoAction, undoAction.getActiveView(), undoAction.getActiveView().getActionMap().get("edit.undo"));

            Method method = UndoAction.class.getDeclaredMethod("updateEnabledState");
            method.setAccessible(true);
            method.invoke(undoAction);
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
