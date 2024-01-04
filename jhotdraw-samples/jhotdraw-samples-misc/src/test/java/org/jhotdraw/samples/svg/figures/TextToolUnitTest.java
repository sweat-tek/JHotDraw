/**
 * Class: TextToolUnitTest
 *
 * Description:
 * This class contains unit tests for the SVGTextFigure class and its functionalities.
 *
 * Test Organization:
 * - Test for moving the text figure.
 * - Test for initializing the SVGTextFigure with default and custom text input.
 *
 * Test Cases:
 * 1. Test moveTextFigure():
 *    - Purpose: Verify that the moveTextFigure() method correctly moves the text figure by a specified amount.
 *    - Steps: Set up the initial bounds of the text figure, move it, and check if the new bounds reflect the correct movement.
 *
 * 2. Test initializeDefault():
 *    - Purpose: Ensure that the SVGTextFigure initializes with default text "Text".
 *    - Steps: Create an SVGTextFigure instance and check if the default text is set correctly.
 *
 * 3. Test initializeWithTextInput():
 *    - Purpose: Validate that the SVGTextFigure initializes with the provided custom text input.
 *    - Steps: Create an SVGTextFigure instance with a custom text input and check if it matches the provided text.
 *
 * Testing Strategy:
 * This class follows a unit testing approach using JUnit. Each test method focuses on specific functionalities
 * of the SVGTextFigure class, ensuring its correct behavior.
 *
 * Dependencies:
 * - JUnit for running the tests.
 * - AffineTransform class for handling transformations.
 *
 * Special Notes:
 * - The moveTextFigure() method uses AffineTransform to perform translations on the text figure.
 * - The initializeDefault() and initializeWithTextInput() tests check the initialization behavior of SVGTextFigure.
 */
package org.jhotdraw.samples.svg.figures;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class TextToolUnitTest {
    private SVGTextFigure textFigure;
    private AffineTransform TransformationObject;

    @Before
    public void setUp() {
        textFigure = new SVGTextFigure();
        TransformationObject = new AffineTransform();
        textFigure.setBounds(new Point2D.Double(0, 0), new Point2D.Double(20, 20));
    }

    private void moveTextFigure(double x, double y) {
        TransformationObject.translate(x, y);
        textFigure.transform(TransformationObject);
    }
    @Test
    public void moveTextFigure(){
        Rectangle2D.Double oldBounds = (Rectangle2D.Double) textFigure.getBounds().clone();
        moveTextFigure(10, 10);
        Rectangle2D.Double newBounds = textFigure.getBounds();
        Assert.assertTrue((newBounds.x - oldBounds.x) == 10 && (newBounds.y - oldBounds.y) == 10);
    }

    @Test
    public void initializeDefault(){
        Assert.assertEquals("Text", textFigure.getText());
    }
    @Test
    public void initializeWithTextInput(){
        String sampleText = "Sample text";
        Assert.assertEquals(sampleText, new SVGTextFigure(sampleText).getText());
    }
}
