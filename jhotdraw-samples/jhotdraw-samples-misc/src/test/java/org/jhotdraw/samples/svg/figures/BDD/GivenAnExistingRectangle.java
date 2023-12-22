package org.jhotdraw.samples.svg.figures.BDD;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.jhotdraw.samples.svg.figures.SVGRectFigure;

public class GivenAnExistingRectangle extends Stage<GivenAnExistingRectangle> {
    @ExpectedScenarioState
    private SVGRectFigure svgRectFigure;

    public GivenAnExistingRectangle givenAnExistingRectangle(){
        svgRectFigure = new SVGRectFigure(0, 0, 50, 25); // Create or set up an existing rectangle
        return self();
    }
}
