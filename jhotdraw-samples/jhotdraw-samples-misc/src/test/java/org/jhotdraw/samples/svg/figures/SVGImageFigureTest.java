package org.jhotdraw.samples.svg.figures;

import org.junit.Test;
import java.awt.geom.AffineTransform;
import static org.jhotdraw.draw.AttributeKeys.TRANSFORM;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


public class SVGImageFigureTest{
    @Test
    public void testTransformCanBeApplied() {
        SVGImageFigure figure = new SVGImageFigure();

        AffineTransform transform = new AffineTransform(5, 10, 5, 10, 5, 10);
        figure.transform(transform);

        assertEquals(transform, figure.get(TRANSFORM));
    }

    @Test
   public void transformShouldSetTransformWhenNoExistingTransform() {
        // Arrange
        SVGImageFigure imageFigure = new SVGImageFigure();
        AffineTransform nonExistingTransform = imageFigure.get(TRANSFORM);

        assertNull(nonExistingTransform);
    }

    @Test
    public void transformShouldSetBoundsWhenSpecialConditionIsMet() {
        SVGImageFigure imageFigure1 = new SVGImageFigure();
        AffineTransform transform1 = new AffineTransform(2, 1, 2, 1, 2, 1);
        AffineTransform transform2 = new AffineTransform(1, 2, 1, 2, 1, 2);

        imageFigure1.transform(transform2);
        imageFigure1.transform(transform1);

        AffineTransform expectedTransform = new AffineTransform(6, 3, 6, 3, 8, 4);
        assertEquals(expectedTransform, imageFigure1.get(TRANSFORM));


    }
}