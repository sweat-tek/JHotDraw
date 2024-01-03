package org.jhotdraw.samples.svg.jgivenstages;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ScenarioState;
import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.samples.svg.figures.SVGImageFigure;

import java.awt.geom.Rectangle2D;
import java.util.List;

import static org.junit.Assert.*;

public class ThenImage extends Stage<ThenImage> {

    @ScenarioState
    Drawing drawing;

    public ThenImage theDrawingCointansTheDrawnImage() {
        List<Figure> figures = drawing.getFiguresFrontToBack();
        assertTrue(figures.stream().anyMatch(figure -> figure instanceof SVGImageFigure));
        return this;
    }

    public ThenImage theImageHasTheNewSize() {
        assertNotNull(drawing);
        List<Figure> figures = drawing.getFiguresFrontToBack();
        assertEquals(figures.size(), 1);
        Figure figure = figures.get(0);
        assertTrue(figure instanceof SVGImageFigure);
        SVGImageFigure ellipse = (SVGImageFigure) figure;
        Rectangle2D bounds = ellipse.getBounds();
        assertEquals(new Rectangle2D.Double(1, 2, 3, 4), bounds);
        return this;
    }
}
