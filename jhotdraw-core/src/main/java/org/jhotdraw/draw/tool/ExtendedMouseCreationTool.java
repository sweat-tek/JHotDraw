package org.jhotdraw.draw.tool;

import org.jhotdraw.draw.AttributeKey;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.figure.Figure;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Map;

public abstract class ExtendedMouseCreationTool extends AbstractCreationTool {

    @Override
    public void mousePressed(MouseEvent evt) {
        DrawingView view = prepareView(evt);
        if (view == null) return;
        if (getView() == null) {
            return;
        }

        anchor = new Point(evt.getX(), evt.getY());
        isWorking = true;

        getView().clearSelection();
        createdFigure = createFigure();
        Point2D.Double p = constrainPoint(viewToDrawing(anchor), createdFigure);
        anchor.x = evt.getX();
        anchor.y = evt.getY();
        createdFigure.setBounds(p, p);
        getDrawing().add(createdFigure);
    }

    public ExtendedMouseCreationTool(Figure prototype, Map<AttributeKey<?>, Object> prototypeAttributes, String name) {
        super(prototype, prototypeAttributes, name);
    }

    public ExtendedMouseCreationTool(Figure prototype, Map<AttributeKey<?>, Object> attributes) {
        super(prototype, attributes);
    }
}
