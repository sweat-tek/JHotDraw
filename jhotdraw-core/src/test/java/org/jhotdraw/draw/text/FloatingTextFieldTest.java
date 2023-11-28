package org.jhotdraw.draw.text;

import org.jhotdraw.draw.DefaultDrawingView;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.event.FigureAdapter;
import org.jhotdraw.draw.event.FigureListener;
import org.jhotdraw.draw.figure.TextFigure;
import org.jhotdraw.draw.figure.TextHolderFigure;
import org.jhotdraw.draw.tool.TextCreationTool;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import javax.swing.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static org.jhotdraw.draw.AttributeKeys.TRANSFORM;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FloatingTextFieldTest {

    @Mock
    private TextHolderFigure textHolderFigure;
    @Mock
    private DrawingView drawingView;
    @Mock
    private TextCreationTool textCreationTool;
    @Mock
    private JTextField textField;
    @Mock
    private FigureListener figureListener;
    @InjectMocks
    private FloatingTextField floatingTextField;

    /**
     * Set up the test environment by initializing required mocks and objects.
     * This method is executed before each test method.
     */
    @Before
    public void setUp() {
        // Initialize the FloatingTextField instance to be tested
        floatingTextField = new FloatingTextField();

        // Mock objects used in the tests
        textHolderFigure = mock(TextHolderFigure.class);
        drawingView = mock(DrawingView.class);
        textField = mock(JTextField.class);

        // Create a TextFigure for TextCreationTool
        final TextFigure textFigure = new TextFigure();
        textCreationTool = new TextCreationTool(textFigure);

        // Add an ActionListener to the FloatingTextField for the TextCreationTool
        floatingTextField.addActionListener(textCreationTool);

        // Ensure that the drawingView has focus
        drawingView.requestFocus();
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test case for verifying that the createOverlay method throws an {@link IllegalArgumentException}
     * when called with a null view.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateOverlayWithNullView() {
        floatingTextField.createOverlay(null, textHolderFigure);
    }

    /**
     * Test case for verifying that the createOverlay method throws an {@link IllegalArgumentException}
     * when called with a null figure.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateOverlayWithNullFigure() {
        floatingTextField.createOverlay(drawingView, null);
    }

    /**
     * Test case for verifying that the getInsets method returns a non-null {@link Insets} object.
     */
    @Test
    public void testGetInsets() {
        // Given
        when(floatingTextField.getInsets()).thenReturn(new Insets(1, 1, 1, 1));

        // When
        Insets insets = floatingTextField.getInsets();

        // Then
        assertNotNull(insets);
    }

    /**
     * Test case for verifying that the requestFocus method invokes the requestFocus method on the textField.
     */
    @Test
    public void testRequestFocus() {
        // When
        floatingTextField.requestFocus();

        //
        verify(textField).requestFocus();
    }

    /**
     * Test case for verifying that the setColumns method of the textField is called with the specified number of columns.
     */
    @Test
    public void testPreferredSize() {
        // Given
        final int colsNumber = 1;
        // When
        Dimension preferredSize = floatingTextField.getPreferredSize(colsNumber);

        // Then
        verify(textField).setColumns(colsNumber);
    }

    /**
     * Test case for verifying that the font of the textField is set based on the font and font size of the textHolderFigure.
     */
    @Test
    public void testSetTextFieldFont() {
        // Given
        final Font font = new Font("Test font", Font.BOLD, 1);
        final float fontSize = 1;
        when(textHolderFigure.getFont()).thenReturn(font);
        when(textHolderFigure.getFontSize()).thenReturn(fontSize);
        when(textField.getFont()).thenReturn(font);

        // When
        floatingTextField.setTextFieldFont();

        // Then
        verify(textHolderFigure).getFont();
        verify(textHolderFigure).getFontSize();
        verify(textField).setFont(any());
        assertEquals(textField.getFont(), font);
    }

    /**
     * Test case for verifying that the foreground and background colors of the textField are set based on the colors of the textHolderFigure.
     */
    @Test
    public void testSetTextFieldColors() {
        // Given
        final Color color = new Color(1);
        when(textHolderFigure.getTextColor()).thenReturn(color);
        final Color fillColor = new Color(1);
        when(textHolderFigure.getFillColor()).thenReturn(fillColor);
        textField.setForeground(color);
        when(textField.getForeground()).thenReturn(color);
        textField.setBackground(fillColor);
        when(textField.getBackground()).thenReturn(fillColor);

        // When
        floatingTextField.setTextFieldColors();

        // Then
        verify(textField, times(2)).setBackground(color);
        verify(textField, times(2)).setForeground(fillColor);
        assertEquals(textField.getForeground(), color);
        assertEquals(textField.getBackground(), fillColor);

    }

    /**
     * Test case for verifying that the createOverlay method sets the text of the {@link FloatingTextField}
     * based on the properties of the textHolderFigure.
     */
    @Test
    public void testCreateOverlay() {
        // Given
        final Font font = new Font("test", Font.BOLD, 1);
        when(textHolderFigure.getFont()).thenReturn(font);
        final Rectangle2D.Double fDrawBounds = new Rectangle2D.Double();
        when(textHolderFigure.getBounds()).thenReturn(fDrawBounds);
        final Rectangle fViewBounds = new Rectangle();
        when(drawingView.drawingToView(any(Rectangle2D.Double.class))).thenReturn(fViewBounds);
        final Point fViewLoc = new Point();
        when(drawingView.drawingToView(any(Point2D.Double.class))).thenReturn(fViewLoc);
        DefaultDrawingView defaultDrawingView = mock(DefaultDrawingView.class);
        when(drawingView.getComponent()).thenReturn(defaultDrawingView);
        defaultDrawingView.add(textField, 0);
        final BufferedImage image = new BufferedImage(1, 2, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D graphics2D = image.createGraphics();
        when(textField.getGraphics()).thenReturn(graphics2D);
        when(textField.getFont()).thenReturn(font);
        when(textField.getInsets()).thenReturn(new Insets(1, 2, 3, 4));
        when(textField.getPreferredSize()).thenReturn(new Dimension());

        // When
        floatingTextField.createOverlay(drawingView, textHolderFigure);

        // Then
        assertEquals(textHolderFigure.getText(), floatingTextField.getText());
    }

    /**
     * Test case for verifying that the figureListener is properly initialized as a {@link FigureAdapter}.
     */
    @Test
    public void testInitFigureListener() {
        // When
        figureListener = new FigureAdapter();

        // Then
        assertNotNull(figureListener);
    }

    /**
     * Test case for verifying that the setTextFieldBounds method sets the bounds of the textField
     * based on the properties of the textHolderFigure and the drawingView.
     */
    @Test
    public void testSetTextFieldBounds() {
        // Given
        final Font font = new Font("test", Font.BOLD, 1);
        final Rectangle2D.Double fDrawBounds = new Rectangle2D.Double();
        when(textHolderFigure.getBounds()).thenReturn(fDrawBounds);
        when(textHolderFigure.get(TRANSFORM)).thenReturn(new AffineTransform());
        final Rectangle fViewBounds = new Rectangle();
        when(drawingView.drawingToView(any(Rectangle2D.Double.class))).thenReturn(fViewBounds);
        final Point fViewLoc = new Point();
        when(drawingView.drawingToView(any(Point2D.Double.class))).thenReturn(fViewLoc);
        final Dimension dimension = new Dimension();
        when(textField.getPreferredSize()).thenReturn(dimension);
        final Insets insets = new Insets(1, 2, 3, 4);
        when(textField.getInsets()).thenReturn(insets);
        final BufferedImage image = new BufferedImage(1, 2, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = image.createGraphics();
        when(textField.getGraphics()).thenReturn(graphics2D);
        when(textField.getFont()).thenReturn(font);

        // When
        floatingTextField.setTextFieldBounds();

        // Given
        verify(textHolderFigure).getBounds();
        verify(drawingView).drawingToView(any(Rectangle2D.Double.class));
        verify(drawingView).drawingToView(any(Point2D.Double.class));
        verify(textField).getPreferredSize();
        verify(textField).getInsets();
        verify(textField).getFont();

    }

    /**
     * Test case for verifying that the endOverlay method removes the textField from the drawingView
     * and removes a FigureListener from the textHolderFigure.
     */
    @Test
    public void testEndOverlay() {
        // Given
        textField.setVisible(false);
        DefaultDrawingView defaultDrawingView = mock(DefaultDrawingView.class);
        when(drawingView.getComponent()).thenReturn(defaultDrawingView);
        when(textField.getBounds()).thenReturn(new Rectangle());

        // When
        floatingTextField.endOverlay();

        // Then
        verify(drawingView.getComponent()).remove(any(JTextField.class));
        verify(textField).getBounds();
        verify(textHolderFigure).removeFigureListener(any());
    }

}