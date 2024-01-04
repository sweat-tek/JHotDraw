package org.jhotdraw.draw.action;

public class UndoableGroupEdit extends UndoableGroupOrUngroupEdit {

  public UndoableGroupEdit(GroupingManager groupingManager) {
    super(groupingManager);
  }

  @Override
  public void undo() {
    super.undo();
    groupingManager.ungroupFigures();
  }

  @Override
  public void redo() {
    super.redo();
    groupingManager.groupFigures();
  }
}
