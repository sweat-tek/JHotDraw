package org.jhotdraw.draw;

import java.util.Arrays;
import java.util.List;
import org.jhotdraw.draw.action.BringToFrontAction;
import org.jhotdraw.draw.action.SendToBackAction;
import org.jhotdraw.draw.figure.Figure;
import static org.jhotdraw.draw.ArrangeTestSupport.addFigure;
import static org.jhotdraw.draw.ArrangeTestSupport.assertOrder;
import static org.jhotdraw.draw.ArrangeTestSupport.createQuadTreeView;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class QuadTreeDrawingArrangeTest {

    @Test
    public void sendToBackPreservesRelativeOrderForMultipleSelectedFiguresInQuadTreeDrawing() {
        DefaultDrawingView view = createQuadTreeView();
        Figure a = addFigure(view, 0);
        Figure b = addFigure(view, 10);
        Figure c = addFigure(view, 20);
        Figure d = addFigure(view, 30);
        Figure e = addFigure(view, 40);

        SendToBackAction.sendToBack(view, Arrays.asList(b, d));

        assertOrder(view, b, d, a, c, e);
    }

    @Test
    public void bringToFrontPreservesRelativeOrderForMultipleSelectedFiguresInQuadTreeDrawing() {
        DefaultDrawingView view = createQuadTreeView();
        Figure a = addFigure(view, 0);
        Figure b = addFigure(view, 10);
        Figure c = addFigure(view, 20);
        Figure d = addFigure(view, 30);
        Figure e = addFigure(view, 40);

        BringToFrontAction.bringToFront(view, Arrays.asList(b, d));

        assertOrder(view, a, c, e, b, d);
    }

    @Test
    public void sendToBackWithAllFiguresSelectedPreservesOrderInQuadTreeDrawing() {
        DefaultDrawingView view = createQuadTreeView();
        Figure a = addFigure(view, 0);
        Figure b = addFigure(view, 10);
        Figure c = addFigure(view, 20);
        Figure d = addFigure(view, 30);

        SendToBackAction.sendToBack(view, Arrays.asList(a, b, c, d));

        assertOrder(view, a, b, c, d);
    }

    @Test
    public void sortReturnsBackToFrontOrderAfterSendToBack() {
        DefaultDrawingView view = createQuadTreeView();
        Figure a = addFigure(view, 0);
        Figure b = addFigure(view, 10);
        Figure c = addFigure(view, 20);
        Figure d = addFigure(view, 30);
        Figure e = addFigure(view, 40);

        SendToBackAction.sendToBack(view, Arrays.asList(b, d));

        List<Figure> actual = view.getDrawing().sort(Arrays.asList(e, b, c, d));

        assertEquals(Arrays.asList(b, d, c, e), actual);
    }

    @Test
    public void getFiguresFrontToBackMatchesInverseOfSortedOrderAfterBringToFront() {
        DefaultDrawingView view = createQuadTreeView();
        Figure a = addFigure(view, 0);
        Figure b = addFigure(view, 10);
        Figure c = addFigure(view, 20);
        Figure d = addFigure(view, 30);
        Figure e = addFigure(view, 40);

        BringToFrontAction.bringToFront(view, Arrays.asList(b, d));

        List<Figure> actual = view.getDrawing().getFiguresFrontToBack();

        assertEquals(Arrays.asList(d, b, e, c, a), actual);
    }
}
