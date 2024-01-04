package org.jhotdraw.samples.svg.undoredo.BDD;

import org.jhotdraw.samples.svg.undoredo.BDD.constructor.redo.GivenAnRedoActionTest;
import org.jhotdraw.samples.svg.undoredo.BDD.constructor.undo.GivenAnUndoActionTest;
import org.jhotdraw.samples.svg.undoredo.BDD.getid.redo.GetIDRedoTest;
import org.jhotdraw.samples.svg.undoredo.BDD.getid.undo.GetIDUndoTest;
import org.jhotdraw.samples.svg.undoredo.BDD.updateenabledstate.redo.RedoStateTest;
import org.jhotdraw.samples.svg.undoredo.BDD.updateenabledstate.undo.UndoStateTest;
import org.jhotdraw.samples.svg.undoredo.BDD.updateview.redo.UpdateRedoUpdateViewTest;
import org.jhotdraw.samples.svg.undoredo.BDD.updateview.undo.UpdateUndoUpdateViewTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        GivenAnRedoActionTest.class,
        GivenAnUndoActionTest.class,
        RedoStateTest.class,
        UndoStateTest.class,
        UpdateRedoUpdateViewTest.class,
        UpdateUndoUpdateViewTest.class,
        GetIDRedoTest.class,
        GetIDUndoTest.class
})
public class UndoAndRedoTestSuite {
}
