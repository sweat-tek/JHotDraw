package org.jhotdraw.samples.odg.action;

import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.figure.CompositeFigure;
import java.util.*;
import org.jhotdraw.draw.*;
import org.jhotdraw.draw.action.*;
import org.jhotdraw.samples.odg.figures.ODGPathFigure;
import org.jhotdraw.util.*;

public class SplitAction extends UngroupAction {

    private static final long serialVersionUID = 1L;
    public static final String ID = "edit.splitPath";
    private ResourceBundleUtil labels
            = ResourceBundleUtil.getBundle("org.jhotdraw.samples.odg.Labels");

    public SplitAction(DrawingEditor editor) {
        super(editor, new ODGPathFigure());
        labels.configureAction(this, ID);
    }

    @Override
    protected boolean canUngroup() {
        if (super.canUngroup()) {
            return ((CompositeFigure) getView().getSelectedFigures().iterator().next()).getChildCount() > 1;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<Figure> ungroupFigures(DrawingView view, CompositeFigure group) {
        LinkedList<Figure> figures = new LinkedList<Figure>(group.getChildren());
        view.clearSelection();
        group.basicRemoveAllChildren();
        LinkedList<Figure> paths = new LinkedList<Figure>();
        for (Figure f : figures) {
            ODGPathFigure path = new ODGPathFigure();
            path.removeAllChildren();
            for (Map.Entry<AttributeKey<?>, Object> entry : group.getAttributes().entrySet()) {
                path.set((AttributeKey<Object>) entry.getKey(), entry.getValue());
            }
            path.add(f);
            view.getDrawing().basicAdd(path);
            paths.add(path);
        }
        view.getDrawing().remove(group);
        view.addToSelection(paths);
        return figures;
    }
}