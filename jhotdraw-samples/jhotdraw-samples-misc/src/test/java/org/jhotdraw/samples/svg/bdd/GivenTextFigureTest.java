package org.jhotdraw.samples.svg.bdd;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.QuadTreeDrawing;
import org.jhotdraw.samples.svg.figures.SVGTextFigure;

public class GivenTextFigureTest extends Stage<GivenTextFigureTest> {
    @ProvidedScenarioState
    Drawing drawing;

    public GivenTextFigureTest anEmptyDrawing() {
        this.drawing = new QuadTreeDrawing();
        return this;
    }

    public GivenTextFigureTest aDrawing() {
        this.drawing = new QuadTreeDrawing();
        return this;
    }

    public GivenTextFigureTest aText() {
        this.drawing.add(new SVGTextFigure());
        return this;
    }
}
