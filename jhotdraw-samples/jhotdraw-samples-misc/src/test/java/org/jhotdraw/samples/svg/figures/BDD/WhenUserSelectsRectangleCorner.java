package org.jhotdraw.samples.svg.figures.BDD;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.jhotdraw.samples.svg.figures.SVGRectFigure;

import java.awt.geom.Point2D;

public class WhenUserSelectsRectangleCorner extends Stage<WhenUserSelectsRectangleCorner> {
    @ExpectedScenarioState
    private SVGRectFigure svgRectFigure;

    public WhenUserSelectsRectangleCorner whenUserSelectsRectangleCorner() {
        svgRectFigure.setBounds(svgRectFigure.getStartPoint(), svgRectFigure.getEndPoint());
        return self();
    }

    public WhenUserSelectsRectangleCorner whenMouseIsMoved() {
        Point2D.Double newPoint = new Point2D.Double(svgRectFigure.getX() + 10, svgRectFigure.getY() + 10);
        svgRectFigure.setBounds(svgRectFigure.getStartPoint(), newPoint);
        return self();
    }


}
