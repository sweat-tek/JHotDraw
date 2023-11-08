package org.jhotdraw.samples.svg.figures.jgivenstages;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.tngtech.jgiven.annotation.ScenarioState;
import org.jhotdraw.draw.DefaultDrawing;
import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.QuadTreeDrawing;

public class GivenRectangle extends Stage<GivenRectangle>{

    @ScenarioState
    Drawing drawing;

    public GivenRectangle empty_canvas(){
        this.drawing = new DefaultDrawing();
        return this;
    }
}