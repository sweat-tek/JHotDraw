package org.jhotdraw.samples.svg.figures.jgivenstages;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ScenarioState;
import org.jhotdraw.draw.Drawing;
import org.jhotdraw.samples.svg.figures.SVGRectFigure;

import static org.junit.Assert.assertNotNull;

public class WhenRectangle extends Stage<WhenRectangle> {

    @ScenarioState
    Drawing drawing;

    public WhenRectangle a_rectangle_is_drawn() {
       assertNotNull(drawing);
       drawing.add(new SVGRectFigure());
       return this;
    }

}
