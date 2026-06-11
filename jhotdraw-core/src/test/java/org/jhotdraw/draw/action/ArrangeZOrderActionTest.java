package org.jhotdraw.draw.action;

import java.util.Arrays;
import java.util.Collections;
import org.jhotdraw.draw.DefaultDrawingView;
import org.jhotdraw.draw.figure.Figure;
import static org.jhotdraw.draw.ArrangeTestSupport.addFigure;
import static org.jhotdraw.draw.ArrangeTestSupport.assertOrder;
import static org.jhotdraw.draw.ArrangeTestSupport.createDefaultView;
import org.junit.Test;

public class ArrangeZOrderActionTest {

    @Test
    public void sendToBackPreservesRelativeOrderForMultipleSelectedFigures() {
        DefaultDrawingView view = createDefaultView();
        Figure a = addFigure(view, 0);
        Figure b = addFigure(view, 10);
        Figure c = addFigure(view, 20);
        Figure d = addFigure(view, 30);
        Figure e = addFigure(view, 40);

        SendToBackAction.sendToBack(view, Arrays.asList(b, d));

        assertOrder(view, b, d, a, c, e);
    }

    @Test
    public void bringToFrontPreservesRelativeOrderForMultipleSelectedFigures() {
        DefaultDrawingView view = createDefaultView();
        Figure a = addFigure(view, 0);
        Figure b = addFigure(view, 10);
        Figure c = addFigure(view, 20);
        Figure d = addFigure(view, 30);
        Figure e = addFigure(view, 40);

        BringToFrontAction.bringToFront(view, Arrays.asList(b, d));

        assertOrder(view, a, c, e, b, d);
    }

    @Test
    public void sendToBackMovesSingleSelectedFigureToBack() {
        DefaultDrawingView view = createDefaultView();
        Figure a = addFigure(view, 0);
        Figure b = addFigure(view, 10);
        Figure c = addFigure(view, 20);
        Figure d = addFigure(view, 30);

        SendToBackAction.sendToBack(view, Collections.singletonList(c));

        assertOrder(view, c, a, b, d);
    }

    @Test
    public void sendToBackWithEmptySelectionDoesNotChangeOrder() {
        DefaultDrawingView view = createDefaultView();
        Figure a = addFigure(view, 0);
        Figure b = addFigure(view, 10);
        Figure c = addFigure(view, 20);

        SendToBackAction.sendToBack(view, Collections.emptyList());

        assertOrder(view, a, b, c);
    }

    @Test
    public void sendToBackWithAllFiguresSelectedPreservesOrder() {
        DefaultDrawingView view = createDefaultView();
        Figure a = addFigure(view, 0);
        Figure b = addFigure(view, 10);
        Figure c = addFigure(view, 20);
        Figure d = addFigure(view, 30);

        SendToBackAction.sendToBack(view, Arrays.asList(a, b, c, d));

        assertOrder(view, a, b, c, d);
    }
}
