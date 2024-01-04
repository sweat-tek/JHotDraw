package org.jhotdraw.samples.svg.BDDAlign;

import org.jhotdraw.draw.*;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.figure.LineFigure;
import org.jhotdraw.draw.io.ImageOutputFormat;
import org.jhotdraw.draw.io.SerializationInputOutputFormat;
import org.jhotdraw.draw.tool.CreationTool;
import org.jhotdraw.draw.tool.DnDTracker;
import org.jhotdraw.draw.tool.SelectionTool;
import org.jhotdraw.gui.action.ButtonFactory;
import org.jhotdraw.util.ResourceBundleUtil;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TestApplication extends JFrame {
    private static final long serialVersionUID = 1L;
    private JToolBar toolBar;
    private JPanel innerPane;
    private DrawingView drawingView;

    public TestApplication() {
        initializeComponents();
        configureFrame();
    }

    private void initializeComponents() {
        drawingView = createDrawingView();
        DrawingEditor editor = createDrawingEditor(drawingView);
        toolBar = createToolBar(editor);
        innerPane = createInnerPane(drawingView);
    }

    private DrawingView createDrawingView() {
        DrawingView view = new DefaultDrawingView();
        view.setDrawing(createDrawing());
        return view;
    }

    private DrawingEditor createDrawingEditor(DrawingView view) {
        DrawingEditor editor = new DefaultDrawingEditor();
        editor.add(view);
        return editor;
    }

    private JToolBar createToolBar(DrawingEditor editor) {
        ResourceBundleUtil labels = ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
        JToolBar tb = new JToolBar();
        addToolsToToolBar(tb, editor, labels);
        tb.setOrientation(JToolBar.VERTICAL);
        return tb;
    }

    private void addToolsToToolBar(JToolBar tb, DrawingEditor editor, ResourceBundleUtil labels) {
        SelectionTool selectionTool = new SelectionTool();
        selectionTool.setDragTracker(new DnDTracker());
        ButtonFactory.addSelectionToolTo(tb, editor, selectionTool);
        ButtonFactory.addToolTo(tb, editor, new CreationTool(new LineFigure()), "edit.createLine", labels);
        ButtonFactory.addAlignmentButtonsTo(tb, editor);
    }

    private JPanel createInnerPane(DrawingView view) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        JScrollPane scrollPane = new JScrollPane(view.getComponent());
        scrollPane.setPreferredSize(new Dimension(200, 200));
        panel.add(scrollPane);
        return panel;
    }

    private void configureFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().add(new JScrollPane(innerPane), BorderLayout.CENTER);
        getContentPane().add(toolBar, BorderLayout.WEST);
        setSize(600, 400);
    }

    private Drawing createDrawing() {
        DefaultDrawing drawing = new DefaultDrawing();
        drawing.addInputFormat(new SerializationInputOutputFormat());
        drawing.addOutputFormat(new SerializationInputOutputFormat());
        drawing.addOutputFormat(new ImageOutputFormat());
        return drawing;
    }

    public void setFiguresInView(List<Figure> figures) {
        Drawing drawing = getDrawing();
        if (drawing != null) {
            for (Figure fig : figures) {
                drawing.add(fig);
                drawingView.addToSelection(fig);
            }
        }
    }

    public Drawing getDrawing() {
        return drawingView != null ? drawingView.getDrawing() : null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TestApplication app = new TestApplication();
            app.setVisible(true);
        });
    }
}