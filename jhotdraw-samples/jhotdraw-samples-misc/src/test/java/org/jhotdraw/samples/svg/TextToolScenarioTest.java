package org.jhotdraw.samples.svg;

import com.tngtech.jgiven.annotation.ScenarioState;
import com.tngtech.jgiven.junit.ScenarioTest;
import org.assertj.swing.core.BasicRobot;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.core.Robot;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.stages.GivenDrawingCanvas;
import org.jhotdraw.draw.stages.ThenDrawingState;
import org.jhotdraw.draw.tool.stages.WhenUserActs;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

public class TextToolScenarioTest  extends ScenarioTest<GivenDrawingCanvas, WhenUserActs, ThenDrawingState> {
    
    private FrameFixture window;
    private Robot robot;

    private DrawingView view;
    
    @Before
    public void setup() {
        
        robot = BasicRobot.robotWithNewAwtHierarchy();
        GuiActionRunner.execute(() -> org.jhotdraw.samples.svg.Main.main(new String[0]));

        window = WindowFinder.findFrame(new GenericTypeMatcher<JFrame>(JFrame.class) {
            @Override
            protected boolean isMatching(JFrame frame) {
                return frame.isShowing() && frame.getTitle().toLowerCase().contains("svg");
            }
        }).withTimeout(5000).using(robot);

        window.maximize();

        this.view = (DrawingView) window.robot().finder().findByName("drawingCanvas");
        DrawingEditor editor = view.getEditor();

        given().setFixtures(window, robot, editor, view);
        when().setFixtures(window, robot, editor, view);
        when().setView(view);
        then().setView(view);

        
    }

    @Test
    public void user_can_create_and_type_text() {
        given().the_text_tool_is_selected();

        when().the_user_clicks_on_the_canvas_at(200,200)
                .and().the_user_types("Hello Near World");
        then().a_new_text_figure_should_exist()
                .and().the_figure_should_be_selected();
    }

    @Test
    public void text_figure_is_removed_if_empty_on_deselect() {
        given().the_text_tool_is_selected();

        when().the_user_clicks_on_the_canvas_at(200,200)
                .and().the_user_clears_the_text_and_deselects();

        then().the_canvas_should_be_empty();
    }

    @Test
    public void clicking_existing_text_with_selection_tool_enters_edit_mode() {
        // Create figure
        given().the_text_tool_is_selected();
        when().the_user_clicks_on_the_canvas_at(200,200)
                .and().the_user_types("Hello Far World")
                .and().the_user_finishes_editing();

        given().the_selection_tool_is_selected();
        when().the_user_double_clicks_the_figure_at(200,200);
        then().the_figure_should_be_in_edit_mode();

    }

    @Test
    public void figure_stays_at_original_location() {
        int x = 200;
        int y = 200;
        given().the_text_tool_is_selected();
        when().the_user_clicks_on_the_canvas_at(x,y)
                .and().the_user_types("Red is defamation");

        // Find where JHotDraw actually positioned it
        Point2D.Double initialPosition = then().get_figure_anchor();

        when().the_user_finishes_editing();

        then().the_figure_should_exist_at( (int) initialPosition.x, (int) initialPosition.y);
    }

    @Test
    public void pressing_escape_exits_edit_mode() {
        given().the_text_tool_is_selected();
        when().the_user_clicks_on_the_canvas_at(200,200)
                .and().the_user_types("Pass Rot, Pop Green, Watch The Rots")
                .and().the_user_presses_the_escape_key();

        then().the_figure_should_not_be_in_edit_mode();

    }

    @After
    public void tearDown() {
        if (window != null) {
            window.cleanUp();
        }
    }

    
}
