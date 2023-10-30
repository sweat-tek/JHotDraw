package org.jhotdraw.draw.action;

import org.jhotdraw.draw.*;
import org.jhotdraw.draw.figure.EllipseFigure;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jhotdraw.draw.figure.Figure;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;


public class ArrangeActionTest2 {
    private DrawingView view;
    private Drawing drawing;
    private DrawingView view1;
    private Drawing drawing1;
    private DrawingView view2;
    private Drawing drawing2;
    private Figure figure1;
    private Figure figure2;
    private Figure figure3;

    private Figure figure4;

    private Figure figure5;
    private Figure figure6;
    private Figure figure7;
    private Figure figure;
    private Set<Figure> selectedFigure;
    private Set<Figure> selectedFigures;
    private Set<Figure> selectedFiguresMulti;

    @Before
    public void setUp() {
        view = mock(DrawingView.class);
        drawing = new DefaultDrawing();
        figure = new EllipseFigure();
        drawing.add(figure);

        when(view.getDrawing()).thenReturn(drawing);

        selectedFigure = new LinkedHashSet<>();
        selectedFigure.add(figure);
        when(view.getSelectedFigures()).thenReturn(selectedFigure);

        //other drawing
        view1 = mock(DrawingView.class);
        //drawing = new QuadTreeDrawing();//for større drawings
        drawing1 = new DefaultDrawing();//for mindre drawings

        // create figures and add them to the drawing
        figure1 = new EllipseFigure();
        figure2 = new EllipseFigure();
        figure3 = new EllipseFigure();
        drawing1.add(figure3);
        drawing1.add(figure2);
        drawing1.add(figure1);

        when(view1.getDrawing()).thenReturn(drawing1);

        selectedFigures = new LinkedHashSet<>();
        selectedFigures.add(figure2);
        when(view1.getSelectedFigures()).thenReturn(selectedFigures);

        //other drawing
        view2 = mock(DrawingView.class);
        drawing2 = new DefaultDrawing();
        when(view2.getDrawing()).thenReturn(drawing2);

        Figure figure4 = new EllipseFigure();
        Figure figure5 = new EllipseFigure();
        Figure figure6 = new EllipseFigure();
        Figure figure7 = new EllipseFigure();

        drawing2.add(figure7);
        drawing2.add(figure6);
        drawing2.add(figure5);
        drawing2.add(figure4);

        /*System.out.println(figure5 + " " + figure6 + " " + figure7 + " " + figure8);
        System.out.println(d.getFiguresFrontToBack());
        System.out.println(v.getDrawing());*/

        selectedFiguresMulti = new LinkedHashSet<>();
        selectedFiguresMulti.add(figure5);
        selectedFiguresMulti.add(figure6);
        when(view2.getSelectedFigures()).thenReturn(selectedFiguresMulti);
    }

    @After
    public void tearDown() throws Exception {
        drawing.remove(figure);

        drawing1.remove(figure1);
        drawing1.remove(figure2);
        drawing1.remove(figure3);

        drawing2.remove(figure4);
        drawing2.remove(figure5);
        drawing2.remove(figure6);
        drawing2.remove(figure7);

        selectedFigure.clear();
        selectedFigures.clear();
        selectedFiguresMulti.clear();
    }

    @Test
    public void actionPerformed() {
    }

    @Test
    public void arrangeFiguresBestCaseOneFigure() {
        //Bring to front
        //figure in the front
        Figure frontFigure = drawing.getFiguresFrontToBack().get(0);
        assertSame(figure, frontFigure);

        ArrangeAction.arrangeFigures(view, selectedFigure, ArrangeAction.BRING_TO_FRONT); //send figure to the front

        //figure in the front
        Figure frontFigureNew = drawing.getFiguresFrontToBack().get(0);
        assertSame(figure, frontFigureNew);

        //Send to back
        // figure at the back
        Figure backFigure = drawing.getFiguresFrontToBack().get(drawing.getFiguresFrontToBack().size() - 1);
        assertSame(figure, backFigure);

        ArrangeAction.arrangeFigures(view, selectedFigure, ArrangeAction.SEND_TO_BACK); //send figure to the back

        // figure at the back
        Figure backFigureNew = drawing.getFiguresFrontToBack().get(drawing.getFiguresFrontToBack().size() - 1);
        assertSame(figure, backFigureNew);
    }

    @Test
    public void arrangeFiguresBestCaseAlreadyArranged() {
        //Bring to front
        //figure1 in the front
        Figure frontFigure = drawing1.getFiguresFrontToBack().get(0);
        assertSame(figure1, frontFigure);

        Set<Figure> selectedFigure1 = new LinkedHashSet<>();
        selectedFigure1.add(figure1);
        when(view1.getSelectedFigures()).thenReturn(selectedFigure1);

        ArrangeAction.arrangeFigures(view1, selectedFigure1, ArrangeAction.BRING_TO_FRONT); //send figure to the front

        //figure1 in the front
        Figure frontFigureNew = drawing1.getFiguresFrontToBack().get(0);
        assertSame(figure1, frontFigureNew);

        //Send to back
        // figure3 at the back
        Figure backFigure = drawing1.getFiguresFrontToBack().get(drawing1.getFiguresFrontToBack().size() - 1);
        assertSame(figure3, backFigure);

        Set<Figure> selectedFigure3 = new LinkedHashSet<>();
        selectedFigure1.add(figure3);
        when(view1.getSelectedFigures()).thenReturn(selectedFigure3);

        ArrangeAction.arrangeFigures(view1, selectedFigure3, ArrangeAction.SEND_TO_BACK); //send figure to the back

        // figure at the back
        Figure backFigureNew = drawing1.getFiguresFrontToBack().get(drawing1.getFiguresFrontToBack().size() - 1);
        assertSame(figure3, backFigureNew);
    }

    @Test
    public void getResourceBundle() {
    }

    @Test
    public void testArrangeFiguresBoundaryCaseBringToFront() {
        testBringToFront(view1, drawing1, selectedFigures, figure1, figure2);

        //figure1 in the front
        /*Figure frontFigure = drawing1.getFiguresFrontToBack().get(0);
        assertSame(figure1, frontFigure);

        ArrangeAction.arrangeFigures(view1, selectedFigures, ArrangeAction.BRING_TO_FRONT); //send figure2 to the front

        //figure2 in the front
        Figure frontFigureNew = drawing1.getFiguresFrontToBack().get(0);
        assertSame(figure2, frontFigureNew);*/

        /*// figure1 in the front
        Figure frontFigure = drawing.getFiguresFrontToBack().iterator().next(); // Z-order
        assertSame(figure1, frontFigure);

        ArrangeAction.arrangeFigures(view, selectedFigures, ArrangeAction.BRING_TO_FRONT); //send figure2 to the front
        //figure2 in the front
        Figure frontFigureNew = drawing.getFiguresFrontToBack().iterator().next(); // Z-order
        assertSame(figure2, frontFigureNew);*/
    }

    @Test
    public void testArrangeFiguresBoundaryCaseSendToBack() {
        testSendToBack(view1, drawing1, selectedFigures, figure3, figure2);
        // figure3 at the back
        /*Figure backFigure = drawing1.getFiguresFrontToBack().get(drawing1.getFiguresFrontToBack().size() - 1);
        assertSame(figure3, backFigure);

        ArrangeAction.arrangeFigures(view1, selectedFigures, ArrangeAction.SEND_TO_BACK); //send figure2 to the back

        // figure2 at the back
        Figure backFigureNew = drawing1.getFiguresFrontToBack().get(drawing1.getFiguresFrontToBack().size() - 1);
        assertSame(figure2, backFigureNew);*/

        /*// figure at the back
        Figure backFigure = drawing.getFiguresFrontToBack().get(0); // Z-order, from back to front.
        assertSame(figure1, backFigure);

        ArrangeAction.arrangeFigures(view, selectedFigures, ArrangeAction.SEND_TO_BACK); //send figure2 to the back

        //
        Figure backFigureNew = drawing.getFiguresFrontToBack().get(0); // Z-order, from back to front.
        assertSame(figure1, backFigureNew);*/

    }

    @Test
    public void testArrangeFiguresBoundaryCaseMultipleSelectedFigures() {

        ArrangeAction.arrangeFigures(view2, selectedFiguresMulti, ArrangeAction.BRING_TO_FRONT);

        // check if the figures 5 and 6 are in the front
        List<Figure> figuresFromTheDrawing = new ArrayList<>(drawing2.getFiguresFrontToBack());
        int indexForFigure = figuresFromTheDrawing.indexOf(selectedFiguresMulti.iterator().next());

        //shows that figure 5 and 6 are in the front after the ArrangeAction
        for (Figure f : selectedFiguresMulti) {
            assertSame(f, figuresFromTheDrawing.get(indexForFigure));
            indexForFigure++;
        }
    }

    private void testSendToBack(DrawingView view, Drawing drawing, Set<Figure> selectedFigures, Figure beforeArrange, Figure afterArrange){
        Figure backFigure = drawing.getFiguresFrontToBack().get(drawing.getFiguresFrontToBack().size() - 1);
        assertSame(backFigure, backFigure);

        ArrangeAction.arrangeFigures(view, selectedFigures, ArrangeAction.SEND_TO_BACK);

        Figure backFigureNew = drawing.getFiguresFrontToBack().get(drawing.getFiguresFrontToBack().size() - 1);
        assertSame(afterArrange, backFigureNew);
    }

    private void testBringToFront(DrawingView view, Drawing drawing, Set<Figure> selectedFigures, Figure beforeArrange, Figure afterArrange){
        Figure frontFigure = drawing.getFiguresFrontToBack().get(0);
        assertSame(beforeArrange, frontFigure);

        ArrangeAction.arrangeFigures(view, selectedFigures, ArrangeAction.BRING_TO_FRONT);

        Figure frontFigureNew = drawing.getFiguresFrontToBack().get(0);
        assertSame(afterArrange, frontFigureNew);
    }
}