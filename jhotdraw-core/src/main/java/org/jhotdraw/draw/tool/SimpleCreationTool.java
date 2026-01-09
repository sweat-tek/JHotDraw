package org.jhotdraw.draw.tool;

import org.jhotdraw.draw.AttributeKey;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.figure.Figure;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Map;

public class SimpleCreationTool extends AbstractCreationTool {

    public SimpleCreationTool(Figure prototype) {
        super(prototype);
    }

    public SimpleCreationTool(Figure prototype, Map<AttributeKey<?>, Object> attributes, String name) {
        super(prototype, attributes, name);
    }

    public SimpleCreationTool(Figure prototype, Map<AttributeKey<?>, Object> attributes) {
        super(prototype, attributes);
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        DrawingView view = (getView() == null) ? null : prepareView(event);
        if (view == null) return;

        Point anchor = new Point(event.getX(), event.getY());
        createdFigure = createFigure();

        Point2D.Double p = constrainPoint(viewToDrawing(anchor), createdFigure);
        createdFigure.setBounds(p,p);

        getDrawing().add(createdFigure);
        view.clearSelection();
        view.addToSelection(createdFigure);
    }


    @Override
    public void mouseReleased(MouseEvent event) {
        super.mouseReleased(event);
    }
}
