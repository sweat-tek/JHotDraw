package org.jhotdraw.samples.svg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.jhotdraw.draw.DefaultDrawing;
import org.jhotdraw.draw.DefaultDrawingEditor;
import org.jhotdraw.draw.DefaultDrawingView;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.figure.CompositeFigure;
import org.jhotdraw.draw.figure.ImageFigure;
import org.jhotdraw.draw.figure.ImageHolderFigure;
import org.junit.Ignore;
import org.junit.Test;

public class SVGCreateFromFileToolTest {
    /**
     * Method under test: {@link SVGCreateFromFileTool#SVGCreateFromFileTool(ImageHolderFigure, CompositeFigure, Map)}
     */
    @Test
    public void testConstructor() {
        ImageFigure imagePrototype = new ImageFigure();
        DefaultDrawing groupPrototype = new DefaultDrawing();
        SVGCreateFromFileTool actualSvgCreateFromFileTool = new SVGCreateFromFileTool(imagePrototype, groupPrototype,
                new HashMap<>());

        assertNull(actualSvgCreateFromFileTool.getActionMap());
        ImageHolderFigure imageHolderFigure = actualSvgCreateFromFileTool.imagePrototype;
        assertTrue(imageHolderFigure instanceof ImageFigure);
        assertTrue(actualSvgCreateFromFileTool.groupPrototype instanceof DefaultDrawing);
        assertNull(actualSvgCreateFromFileTool.getInputMap());
        assertSame(imageHolderFigure, actualSvgCreateFromFileTool.getPrototype());
        assertEquals(0, actualSvgCreateFromFileTool.getPropertyChangeListeners().length);
        assertTrue(actualSvgCreateFromFileTool.isToolDoneAfterCreation());
    }

    /**
     * Method under test: {@link SVGCreateFromFileTool#activate(DrawingEditor)}
     */
    @Test
    public void testActivate() {
        ImageFigure imagePrototype = new ImageFigure();
        SVGCreateFromFileTool svgCreateFromFileTool = new SVGCreateFromFileTool(imagePrototype, new DefaultDrawing());
        svgCreateFromFileTool.activate(new DefaultDrawingEditor());
        assertTrue(svgCreateFromFileTool.isActive());
    }

    /**
     * Method under test: {@link SVGCreateFromFileTool#activate(DrawingEditor)}
     */
    @Test
    @Ignore("TODO: Complete this test")
    public void testActivate2() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //       at org.jhotdraw.draw.tool.AbstractTool.activate(AbstractTool.java:91)
        //       at org.jhotdraw.draw.tool.CreationTool.activate(CreationTool.java:169)
        //       at org.jhotdraw.samples.svg.SVGCreateFromFileTool.activate(SVGCreateFromFileTool.java:93)
        //   In order to prevent activate(DrawingEditor)
        //   from throwing NullPointerException, add constructors or factory
        //   methods that make it easier to construct fully initialized objects used in
        //   activate(DrawingEditor).
        //   See https://diff.blue/R013 to resolve this issue.

        ImageFigure imagePrototype = new ImageFigure();
        (new SVGCreateFromFileTool(imagePrototype, new DefaultDrawing())).activate(null);
    }

    /**
     * Method under test: {@link SVGCreateFromFileTool#activate(DrawingEditor)}
     */
    @Test
    @Ignore("TODO: Complete this test")
    public void testActivate3() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.awt.HeadlessException
        //       at java.awt.GraphicsEnvironment.checkHeadless(GraphicsEnvironment.java:208)
        //       at java.awt.Window.<init>(Window.java:548)
        //       at java.awt.Frame.<init>(Frame.java:423)
        //       at java.awt.Frame.<init>(Frame.java:388)
        //       at javax.swing.SwingUtilities$SharedOwnerFrame.<init>(SwingUtilities.java:1919)
        //       at javax.swing.SwingUtilities.getSharedOwnerFrame(SwingUtilities.java:1995)
        //       at javax.swing.JOptionPane.getRootFrame(JOptionPane.java:1689)
        //       at javax.swing.JOptionPane.getWindowForComponent(JOptionPane.java:1630)
        //       at javax.swing.JOptionPane.getWindowForComponent(JOptionPane.java:1633)
        //       at javax.swing.JFileChooser.createDialog(JFileChooser.java:813)
        //       at javax.swing.JFileChooser.showDialog(JFileChooser.java:760)
        //       at javax.swing.JFileChooser.showOpenDialog(JFileChooser.java:666)
        //       at org.jhotdraw.samples.svg.SVGCreateFromFileTool.activate(SVGCreateFromFileTool.java:107)
        //   In order to prevent activate(DrawingEditor)
        //   from throwing HeadlessException, add constructors or factory
        //   methods that make it easier to construct fully initialized objects used in
        //   activate(DrawingEditor).
        //   See https://diff.blue/R013 to resolve this issue.

        ImageFigure imagePrototype = new ImageFigure();
        SVGCreateFromFileTool svgCreateFromFileTool = new SVGCreateFromFileTool(imagePrototype, new DefaultDrawing());

        DefaultDrawingEditor defaultDrawingEditor = new DefaultDrawingEditor();
        defaultDrawingEditor.add(new DefaultDrawingView());
        svgCreateFromFileTool.activate(defaultDrawingEditor);
    }

    /**
     * Method under test: {@link SVGCreateFromFileTool#activate(DrawingEditor)}
     */
    @Test
    @Ignore("TODO: Complete this test")
    public void testActivate4() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.awt.HeadlessException
        //       at java.awt.GraphicsEnvironment.checkHeadless(GraphicsEnvironment.java:208)
        //       at java.awt.Window.<init>(Window.java:548)
        //       at java.awt.Frame.<init>(Frame.java:423)
        //       at java.awt.Frame.<init>(Frame.java:388)
        //       at javax.swing.SwingUtilities$SharedOwnerFrame.<init>(SwingUtilities.java:1919)
        //       at javax.swing.SwingUtilities.getSharedOwnerFrame(SwingUtilities.java:1995)
        //       at javax.swing.JOptionPane.getRootFrame(JOptionPane.java:1689)
        //       at javax.swing.JOptionPane.getWindowForComponent(JOptionPane.java:1630)
        //       at javax.swing.JOptionPane.getWindowForComponent(JOptionPane.java:1633)
        //       at javax.swing.JFileChooser.createDialog(JFileChooser.java:813)
        //       at javax.swing.JFileChooser.showDialog(JFileChooser.java:760)
        //       at javax.swing.JFileChooser.showOpenDialog(JFileChooser.java:666)
        //       at org.jhotdraw.samples.svg.SVGCreateFromFileTool.activate(SVGCreateFromFileTool.java:107)
        //   In order to prevent activate(DrawingEditor)
        //   from throwing HeadlessException, add constructors or factory
        //   methods that make it easier to construct fully initialized objects used in
        //   activate(DrawingEditor).
        //   See https://diff.blue/R013 to resolve this issue.

        ImageFigure imagePrototype = new ImageFigure();
        SVGCreateFromFileTool svgCreateFromFileTool = new SVGCreateFromFileTool(imagePrototype, new DefaultDrawing());

        DefaultDrawingView defaultDrawingView = new DefaultDrawingView();
        defaultDrawingView.addToSelection(new DefaultDrawing());

        DefaultDrawingEditor defaultDrawingEditor = new DefaultDrawingEditor();
        defaultDrawingEditor.add(defaultDrawingView);
        svgCreateFromFileTool.activate(defaultDrawingEditor);
    }
}

