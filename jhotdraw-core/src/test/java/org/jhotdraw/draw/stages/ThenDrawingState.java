package org.jhotdraw.draw.stages;


import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ScenarioState;
import org.assertj.swing.exception.ComponentLookupException;
import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.figure.TextFigure;
import org.jhotdraw.draw.figure.TextHolderFigure;

import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class ThenDrawingState extends JHotDrawStage<ThenDrawingState> {

    private DrawingView drawingView;

    public ThenDrawingState setView(DrawingView view) {
        this.drawingView = view;
        return self();
    }

    public ThenDrawingState a_new_text_figure_should_exist() {
        Drawing drawing = drawingView.getDrawing();
        assertThat(drawing.getChildCount())
                .as("Check that at least one figure exists on the canvas")
                .isGreaterThan(0);
        return self();
    }

    public ThenDrawingState the_figure_text_should_be(String expectedText) {
        Figure f = drawingView.getSelectedFigures().iterator().next();
        if (f instanceof TextHolderFigure)
            assertThat(((TextHolderFigure) f).getText()).isEqualTo(expectedText);
        else
            fail("Selected figure does not hold text");
        return self();
    }

    public ThenDrawingState the_figure_at_index_should_have_text(int index, String expectedText) {
        TextFigure figure = (TextFigure) drawingView.getDrawing().getChild(index);

        assertThat(figure.getText())
                .as("Verify content of figure")
                .isEqualTo(expectedText);
        return self();
    }

    public Point2D.Double get_figure_anchor() {
        Figure figure = drawingView.getDrawing().getChildren().get(0);
        return figure.getStartPoint();
    }

    public ThenDrawingState the_figure_should_exist_at(int x, int y) {
        List<Figure> children = drawingView.getDrawing().getChildren();
        Figure figure = children.get(children.size() - 1);

        Point2D.Double currentPoint = figure.getStartPoint();

        assertThat(currentPoint.x).isCloseTo(x, within(1.0));
        assertThat(currentPoint.y).isCloseTo(y, within(1.0));

        return self();
    }

    public ThenDrawingState the_canvas_should_be_empty() {
        assertThat(drawingView.getDrawing().getChildCount())
                .as("Check that no figures exist")
                .isEqualTo(0);
        return self();
    }

    public ThenDrawingState the_figure_should_be_selected() {
        List<Figure> figures = new ArrayList<>(drawingView.getDrawing().getChildren());
        Figure newestFigure = figures.get(figures.size() - 1);

        assertThat(drawingView.getSelectedFigures())
                .as("Check that there's only a text figure selected")
                .hasSize(1)
                .allMatch(f -> f instanceof TextHolderFigure, "Should be a TextFigure");

        return self();
    }

    public ThenDrawingState the_figure_should_be_in_edit_mode() {
        Container viewContainer = (Container) drawingView.getComponent();


        window.robot().waitForIdle();

        Component canvas = window.robot().finder().findByName("drawingCanvas", true);

        try {
            window.robot().finder().find((Container) canvas, c ->
                    c instanceof JTextComponent && c.isShowing()
            );
        } catch (ComponentLookupException e) {
            fail("There should be a floating text editor visible on the canvas");
        }

        return self();
    }

    public ThenDrawingState the_figure_should_not_be_in_edit_mode() {
        window.robot().waitForIdle();
        Container canvas = (Container) window.robot().finder().findByName("drawingCanvas", true);

        // Verify NO JTextComponent is currently showing on the canvas
        boolean found = false;
        for (Component c : canvas.getComponents()) {
            if (c instanceof javax.swing.text.JTextComponent && c.isShowing()) {
                found = true;
                break;
            }
        }

        assertThat(found).as("The floating text editor should be closed").isFalse();
        return self();
    }


}
