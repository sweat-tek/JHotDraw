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
package org.jhotdraw.samples.svg.figures.jgiven;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.samples.svg.figures.SVGEllipseFigure;

import java.util.List;

import static org.junit.Assert.assertTrue;
/**
 *
 * @author Lorenzo Fagiani
 */
public class ThenEllipse extends Stage<ThenEllipse> {

    @ProvidedScenarioState
    Drawing drawing;

    public void an_ellipse_shape_should_be_created() {
        List<Figure> figures = drawing.getFiguresFrontToBack();
        assertTrue(figures.stream().anyMatch(figure -> figure instanceof SVGEllipseFigure));
    }
}
