package org.jhotdraw.samples.svg.BasicEdit;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import org.jhotdraw.samples.svg.figures.SVGRectFigure;



import static org.junit.Assert.*;

public class ThenDelete extends Stage<ThenDelete> {


    @ProvidedScenarioState
    SVGRectFigure rect;

    public ThenDelete theDrawingIsDeleted() {
        assertNotNull(rect);
        return this;
    }




}
