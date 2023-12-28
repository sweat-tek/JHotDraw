/*
 * Copyright (C) 2023 JHotDraw.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package org.jhotdraw.samples.svg.figures;

import com.tngtech.jgiven.junit.ScenarioTest;
import org.jhotdraw.samples.svg.figures.jgiven.GivenEllipse;
import org.jhotdraw.samples.svg.figures.jgiven.ThenEllipse;
import org.jhotdraw.samples.svg.figures.jgiven.WhenEllipse;
import org.junit.Test;

/**
 *
 * @author Lorenzo Fagiani
 */
public class SVGEllipseFigureBDDScenario extends ScenarioTest<GivenEllipse, WhenEllipse, ThenEllipse> {
    @Test
    public void testEllipseDrawing() {
        given().the_user_selected_the_ellipse_drawing_tool();
        when().the_user_uses_the_mouse_to_create_an_ellipse();
        then().an_ellipse_shape_should_be_created();
    }
}
