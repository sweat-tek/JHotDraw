package org.jhotdraw.samples.svg.figures;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ScenarioState;
import org.mockito.Mockito;

import java.awt.*;

public class GivenInitializedRectangle extends Stage<GivenInitializedRectangle> {

    @ScenarioState
    SVGRectFigure rect;

    @ScenarioState
    int width;

    /*@ScenarioState
    Rectangle2D.Double shape;*/

    @ScenarioState
    Graphics2D g;


    public GivenInitializedRectangle user_wants_to_draw_rectangle() {

        width = 100;
        rect = new SVGRectFigure(0,0,width,width);
        //shape = new  Rectangle2D.Double(); I need to define this inside the when state in order to pass it to then state.
        g = Mockito.mock(Graphics2D.class);

        return this;
    }
}