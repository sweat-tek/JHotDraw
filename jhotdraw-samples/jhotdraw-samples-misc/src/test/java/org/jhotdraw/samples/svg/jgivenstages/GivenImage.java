package org.jhotdraw.samples.svg.jgivenstages;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.QuadTreeDrawing;
import org.jhotdraw.samples.svg.figures.SVGImageFigure;

public class GivenImage extends Stage<GivenImage> {

    @ProvidedScenarioState
    Drawing drawing;

    public GivenImage anEmptyDrawing() {
        this.drawing = new QuadTreeDrawing();
        return this;
    }
    public GivenImage anImageIsAdded() {
        this.drawing.add(new SVGImageFigure());
        return this;
    }
}
