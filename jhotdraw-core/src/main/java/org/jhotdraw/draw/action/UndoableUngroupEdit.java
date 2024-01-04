package org.jhotdraw.draw.action;

public class UndoableUngroupEdit extends UndoableGroupOrUngroupEdit {
  public UndoableUngroupEdit(GroupingManager groupingManager) {
    super(groupingManager);
  }

  @Override
  public void undo() {
    super.undo();
    groupingManager.groupFigures();
  }

  @Override
  public void redo() {
    super.redo();
    groupingManager.ungroupFigures();
  }
}
