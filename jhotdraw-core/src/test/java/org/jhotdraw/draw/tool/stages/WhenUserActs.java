package org.jhotdraw.draw.tool.stages;

import org.assertj.swing.core.MouseButton;
import org.assertj.swing.core.MouseClickInfo;
import org.assertj.swing.fixture.JTextComponentFixture;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.stages.JHotDrawStage;
import org.jhotdraw.draw.stages.ThenDrawingState;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import static org.assertj.swing.core.KeyPressInfo.keyCode;
import static org.assertj.core.api.Assertions.fail;


public class WhenUserActs extends JHotDrawStage<WhenUserActs> {

    private DrawingView drawingView;

    public WhenUserActs the_user_clicks_on_the_canvas_at(int x, int y) {
        Component canvas = window.robot().finder().findByName("drawingCanvas", true);
        window.robot().click(canvas, new Point(x,y));
        return self();
    }

    public WhenUserActs the_user_double_clicks_on_the_canvas_at(int x, int y) {
        Component canvas = window.robot().finder().findByName("drawingCanvas", true);
        window.robot().click(canvas, new Point(x,y), MouseButton.LEFT_BUTTON, 2);
        return self();
    }




    public WhenUserActs the_user_double_clicks_the_figure_at(int x, int y) {
        Figure figure = drawingView.getDrawing().findFigure(new Point2D.Double(x,y));


        // As a fallback, choose the newest
        if (figure == null) {
            List<Figure> children = drawingView.getDrawing().getChildren();
            if (!children.isEmpty()) {
                figure = children.get(children.size() - 1);
            }
        }

        if (figure == null) {
            fail("No figure found at " + x + " " + y + " and drawing is empty.");
        }

        Rectangle2D.Double bounds = figure.getBounds();

        int centerX = (int)  bounds.getCenterX();
        int centerY = (int)  bounds.getCenterY();

        Component canvas = window.robot().finder().findByName("drawingCanvas", true);
        window.robot().click(canvas, new Point(centerX,centerY), MouseButton.LEFT_BUTTON, 2);
        return self();
    }

    public WhenUserActs the_user_types(String text) {
        window.robot().enterText(text);
        window.robot().waitForIdle();
        return self();
    }

    public WhenUserActs the_user_presses_the_escape_key() {
        window.robot().waitForIdle();


        Component canvas = window.robot().finder().findByName("drawingCanvas", true);
        JTextComponent editor = (JTextComponent) window.robot().finder().find((Container) canvas,
                c -> c instanceof JTextComponent && c.isShowing());

        window.robot().focusAndWaitForFocusGain(editor);

        new JTextComponentFixture(window.robot(), editor).pressAndReleaseKey(keyCode(KeyEvent.VK_ESCAPE));

        window.robot().waitForIdle();
        return self();
    }


    public WhenUserActs the_user_clears_the_text_and_deselects() {
        window.robot().pressAndReleaseKey(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK);
        window.robot().pressAndReleaseKey(KeyEvent.VK_BACK_SPACE);
        Component canvas = window.robot().finder().findByName("drawingCanvas", true);

        return the_user_clicks_on_the_canvas_at(0,0);
    }

    public WhenUserActs the_user_finishes_editing() {
        Component canvas = window.robot().finder().findByName("drawingCanvas", true);
        window.robot().click(canvas, new Point(1,1));
        return self();
    }


    public WhenUserActs setView(DrawingView view) {
        this.drawingView = view;
        return self();
    }

}
