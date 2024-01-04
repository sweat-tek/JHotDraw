/*
 * @(#)GroupAction.java
 *
 * Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.draw.action;

import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.figure.CompositeFigure;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.figure.GroupFigure;
import org.jhotdraw.util.ResourceBundleUtil;

import java.util.LinkedList;

/**
 * GroupAction.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class GroupAction extends AbstractSelectedAction {

  private static final long serialVersionUID = 1L;
  public static final String ID = "edit.groupSelection";

  private final CompositeFigure prototype;

  /**
   * Creates a new instance.
   */
  public GroupAction(DrawingEditor editor) {
    this(editor, new GroupFigure());
  }

  public GroupAction(DrawingEditor editor, CompositeFigure prototype) {
    super(editor);
    this.prototype = prototype;
    ResourceBundleUtil labels
            = ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
    labels.configureAction(this, ID);
    updateEnabledState();
  }


  @Override
  protected void updateEnabledState() {
    setEnabled(canGroup());
  }

  protected boolean canGroup() {
    return getView() != null && getView().getSelectionCount() > 1;
  }

  @Override
  public void actionPerformed(java.awt.event.ActionEvent e) {
    if (canGroup()) {
      final DrawingView view = getView();
      final LinkedList<Figure> ungroupedFigures = new LinkedList<>(
              view.getSelectedFigures());
      final CompositeFigure group = (CompositeFigure) prototype.clone();
      GroupingManager groupingManager = new GroupingManager(view, group,
              ungroupedFigures);
      UndoableGroupEdit edit = new UndoableGroupEdit(groupingManager);
      groupingManager.groupFigures();
      fireUndoableEditHappened(edit);
    }
  }
}
