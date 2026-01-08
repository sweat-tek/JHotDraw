package org.jhotdraw.samples.svg.gui.bdd;

import com.tngtech.jgiven.Stage;
import org.jhotdraw.draw.figure.RectangleFigure;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;


public class GivenStrokeFigure extends Stage<GivenStrokeFigure> {

    @ProvidedScenarioState
    RectangleFigure figure;

    public GivenStrokeFigure a_selected_rectangle_figure() {
        figure = new RectangleFigure();
        return self();
    }
}

