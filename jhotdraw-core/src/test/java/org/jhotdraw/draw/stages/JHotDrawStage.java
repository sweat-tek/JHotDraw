package org.jhotdraw.draw.stages;

import com.tngtech.jgiven.Stage;
import org.assertj.swing.core.Robot;
import org.assertj.swing.fixture.FrameFixture;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.DrawingView;

public class JHotDrawStage<SELF extends JHotDrawStage<SELF>> extends Stage<SELF> {

    protected static FrameFixture window;
    protected static Robot robot;
    protected static DrawingEditor drawingEditor;
    protected static DrawingView drawingView;

    public void setFixtures(FrameFixture fixture, Robot robotInstance,  DrawingEditor drawingEditor, DrawingView drawingView) {
        window = fixture;
        robot = robotInstance;
        drawingEditor = drawingEditor;
        drawingView = drawingView;
    }

}
