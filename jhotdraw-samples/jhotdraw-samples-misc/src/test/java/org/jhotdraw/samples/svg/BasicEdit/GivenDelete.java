package org.jhotdraw.samples.svg.BasicEdit;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.QuadTreeDrawing;
import org.jhotdraw.samples.svg.figures.SVGRectFigure;

public class GivenDelete extends Stage<GivenDelete> {

    @ProvidedScenarioState
    Drawing drawing;

    @ProvidedScenarioState
    SVGRectFigure rect;


    public GivenDelete EmptyDrawing() {
        this.drawing = new QuadTreeDrawing();
        rect = new SVGRectFigure(10, 10, 50, 100);
        drawing.add(rect);
        return this;
    }

}
