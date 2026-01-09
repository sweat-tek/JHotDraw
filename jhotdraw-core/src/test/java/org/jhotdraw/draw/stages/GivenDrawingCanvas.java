package org.jhotdraw.draw.stages;

import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.jhotdraw.draw.figure.TextFigure;

import javax.swing.*;
import java.awt.geom.Point2D;

public class GivenDrawingCanvas extends JHotDrawStage<GivenDrawingCanvas> {

    public GivenDrawingCanvas the_text_tool_is_selected() {

        AbstractButton textButton = (AbstractButton) window.robot().finder().findByName("textToolButton",true);

        window.robot().click(textButton);

        return self();

    }

    public GivenDrawingCanvas the_selection_tool_is_selected() {

        AbstractButton textButton = (AbstractButton) window.robot().finder().findByName("selectionToolButton",true);

        window.robot().click(textButton);

        return self();

    }

    public GivenDrawingCanvas a_text_figure_exists_on_the_canvas() {
        TextFigure figure = new TextFigure("Text");

        figure.setBounds(new Point2D.Double(100,100), new Point2D.Double(100,100));

        GuiActionRunner.execute(() -> {
            drawingView.getDrawing().add(figure);
        });
        return self();
    }

}
