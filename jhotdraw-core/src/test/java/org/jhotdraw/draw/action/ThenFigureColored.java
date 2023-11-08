package org.jhotdraw.draw.action;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ScenarioState;
import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.figure.Figure;
import org.mockito.Mockito;

import java.awt.*;
import java.util.List;

import static org.mockito.Mockito.times;

public class ThenFigureColored extends Stage<ThenFigureColored> {

    @ScenarioState
    SelectionColorChooserHandler handler;

    @ScenarioState
    List<Figure> selectedFigures;

    public ThenFigureColored the_color_is_applied_to_the_selected_figure() {
        Mockito.verify(handler.colorChooser, times(1)).getColor();
        Mockito.verify(selectedFigures.get(0), times(1)).willChange();
        Mockito.verify(selectedFigures.get(0), times(1)).set(AttributeKeys.FILL_COLOR, Color.red);
        Mockito.verify(selectedFigures.get(0), times(1)).changed();
        Mockito.verify(handler.getEditor(), times(1)).setDefaultAttribute(AttributeKeys.FILL_COLOR, Color.red);
        return this;
    }

}
