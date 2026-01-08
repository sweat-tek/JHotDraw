package org.jhotdraw.draw.figure.BDDEllipse;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import org.jhotdraw.draw.figure.EllipseFigure;

public class GivenEllipse extends Stage<GivenEllipse> {

    @ProvidedScenarioState
    EllipseFigure ellipse;

    public GivenEllipse an_ellipse() {
        ellipse = new EllipseFigure();
        return self();
    }
}
