package org.jhotdraw.draw.action.bdd;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import org.jhotdraw.draw.DefaultDrawingEditor;
import static org.jhotdraw.draw.ArrangeTestSupport.addFigure;
import static org.jhotdraw.draw.ArrangeTestSupport.createDefaultView;
import static org.jhotdraw.draw.ArrangeTestSupport.createQuadTreeView;
import static org.jhotdraw.draw.ArrangeTestSupport.select;

public class ArrangeGivenStage extends Stage<ArrangeGivenStage> {

    @ProvidedScenarioState
    private final ArrangeScenarioState state = new ArrangeScenarioState();

    public ArrangeGivenStage a_default_drawing_with_five_figures() {
        state.editor = new DefaultDrawingEditor();
        state.view = createDefaultView();
        state.editor.add(state.view);
        state.editor.setActiveView(state.view);
        addFiveFigures();
        return self();
    }

    public ArrangeGivenStage a_quad_tree_drawing_with_five_figures() {
        state.editor = new DefaultDrawingEditor();
        state.view = createQuadTreeView();
        state.editor.add(state.view);
        state.editor.setActiveView(state.view);
        addFiveFigures();
        return self();
    }

    public ArrangeGivenStage the_figures_b_and_d_are_selected() {
        select(state.view, state.b, state.d);
        return self();
    }

    public ArrangeGivenStage the_figures_a_and_c_are_selected() {
        select(state.view, state.a, state.c);
        return self();
    }

    private void addFiveFigures() {
        state.a = addFigure(state.view, 0);
        state.b = addFigure(state.view, 10);
        state.c = addFigure(state.view, 20);
        state.d = addFigure(state.view, 30);
        state.e = addFigure(state.view, 40);
    }
}
