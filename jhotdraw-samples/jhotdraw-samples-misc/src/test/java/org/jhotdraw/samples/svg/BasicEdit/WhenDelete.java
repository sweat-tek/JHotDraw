package org.jhotdraw.samples.svg.BasicEdit;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import org.jhotdraw.draw.Drawing;
import static org.junit.Assert.*;

public class WhenDelete extends Stage<WhenDelete> {

    @ProvidedScenarioState
    Drawing drawing;


    public WhenDelete deletingTheDrawing() {
        assertNotNull(this.drawing);
       //rect.getActions().add(new DeleteAction());
        return this;
    }
}

