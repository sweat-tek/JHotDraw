package org.jhotdraw.draw.action;

import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.figure.CompositeFigure;
import org.jhotdraw.draw.figure.Figure;

import java.util.Collection;
import java.util.LinkedList;

public class GroupingManager {
  private final DrawingView view;
  private final CompositeFigure group;
  private final Collection<Figure> figures;

  public GroupingManager(DrawingView view, CompositeFigure group,
                         Collection<Figure> figures) {
    this.view = view;
    this.group = group;
    this.figures = figures;
  }


  public void groupFigures() {
    Collection<Figure> sorted = view.getDrawing().sort(figures);
    int index = view.getDrawing().indexOf(sorted.iterator().next());
    view.getDrawing().basicRemoveAll(figures);
    view.clearSelection();
    view.getDrawing().add(index, group);
    group.willChange();
    for (Figure f : sorted) {
      f.willChange();
      group.basicAdd(f);
    }
    group.changed();
    view.addToSelection(group);
  }

  public Collection<Figure> ungroupFigures() {
    LinkedList<Figure> figures = new LinkedList<>(group.getChildren());
    view.clearSelection();
    group.basicRemoveAllChildren();
    view.getDrawing().basicAddAll(view.getDrawing().indexOf(group), figures);
    view.getDrawing().remove(group);
    view.addToSelection(figures);
    return figures;
  }
}
