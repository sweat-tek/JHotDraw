package org.jhotdraw.samples.svg.figures;

import com.tngtech.jgiven.junit.ScenarioTest;
import org.jhotdraw.samples.svg.figures.jgivenstages.GivenEllipseDrawing;
import org.jhotdraw.samples.svg.figures.jgivenstages.ThenEllipseDrawing;
import org.jhotdraw.samples.svg.figures.jgivenstages.WhenEllipseDrawing;
import org.junit.Test;

public class SVGEllipseFigureBDDTest extends ScenarioTest<GivenEllipseDrawing, WhenEllipseDrawing, ThenEllipseDrawing> {
    @Test
    public void testEllipseDrawing() {
        given().the_user_has_selected_the_ellipse_drawing_tool();
        when().the_user_clicks_and_drags_the_mouse_to_create_an_ellipse_on_the_drawing_canvas();
        then().an_ellipse_shape_should_be_created_with_the_specified_dimensions();
    }


}