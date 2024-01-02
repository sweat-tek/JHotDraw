package org.jhotdraw.draw.action;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ScenarioState;
import org.jhotdraw.draw.figure.Figure;
import org.mockito.Mockito;

import java.awt.*;
import java.util.List;

import static org.mockito.Mockito.when;

public class WhenColorChooser extends Stage<WhenColorChooser> {

    @ScenarioState
    SelectionColorChooserHandler handler;

    public WhenColorChooser the_user_chooses_a_color() {
        Color color = Color.red;
        Mockito.when(handler.colorChooser.getColor()).thenReturn(color);
        handler.applySelectedColorToFigures();
        return this;
    }

}
