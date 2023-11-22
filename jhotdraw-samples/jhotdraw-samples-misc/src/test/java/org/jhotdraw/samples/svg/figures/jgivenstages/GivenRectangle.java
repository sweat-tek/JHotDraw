package org.jhotdraw.samples.svg.figures.jgivenstages;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import org.jhotdraw.draw.DefaultDrawing;
import org.jhotdraw.draw.Drawing;


public class GivenRectangle extends Stage<GivenRectangle>{

    @ProvidedScenarioState
    Drawing drawing;

    public GivenRectangle empty_canvas(){
        this.drawing = new DefaultDrawing();
        return this;
    }
}