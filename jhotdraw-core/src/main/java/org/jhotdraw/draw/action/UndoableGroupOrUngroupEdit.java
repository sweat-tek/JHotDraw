package org.jhotdraw.draw.action;

import org.jhotdraw.util.ResourceBundleUtil;

import javax.swing.undo.AbstractUndoableEdit;

abstract public class UndoableGroupOrUngroupEdit extends AbstractUndoableEdit {
  private static final long serialVersionUID = 1L;
  protected GroupingManager groupingManager;

  public UndoableGroupOrUngroupEdit(GroupingManager groupingManager) {
    this.groupingManager = groupingManager;
  }

  public String getPresentationName() {
    ResourceBundleUtil labels
            = ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
    return labels.getString("edit.groupSelection.text");
  }
}
