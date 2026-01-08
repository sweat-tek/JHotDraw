package org.jhotdraw.samples.svg.gui;

import org.jhotdraw.gui.action.ButtonFactory;
import org.jhotdraw.gui.plaf.palette.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.SliderUI;
import javax.swing.text.DefaultFormatterFactory;

import org.jhotdraw.draw.AttributeKey;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.action.*;
import org.jhotdraw.draw.event.*;
import org.jhotdraw.draw.gui.*;
import org.jhotdraw.gui.JPopupButton;
import static org.jhotdraw.samples.svg.SVGAttributeKeys.*;
import org.jhotdraw.text.ColorFormatter;
import org.jhotdraw.formatter.JavaNumberFormatter;
import org.jhotdraw.util.*;

public class StrokeToolBar extends AbstractToolBar {

    private static final long serialVersionUID = 1L;
    private SelectionComponentDisplayer displayer;

    public StrokeToolBar() {
        ResourceBundleUtil labels =
                ResourceBundleUtil.getBundle("org.jhotdraw.samples.svg.Labels");
        setName(labels.getString("stroke.toolbar"));
        setDisclosureStateCount(3);
    }

    @Override
    public void setEditor(DrawingEditor newValue) {
        if (displayer != null) {
            displayer.dispose();
            displayer = null;
        }
        super.setEditor(newValue);
        if (newValue != null) {
            displayer = new SelectionComponentDisplayer(editor, this);
        }
    }

    // REFACTORED METHOD

    @Override
    protected JComponent createDisclosedComponent(int state) {
        JPanel panel = createBasePanel();
        if (editor == null) {
            return panel;
        }

        switch (state) {
            case 1:
                buildCompactStrokeControls(panel);
                break;
            case 2:
                buildDetailedStrokeControls(panel);
                break;
            default:
                break;
        }
        return panel;
    }

    //  HELPER METHODS

    private JPanel createBasePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(5, 5, 5, 8));
        return panel;
    }

    private void buildCompactStrokeControls(JPanel panel) {
        ResourceBundleUtil labels =
                ResourceBundleUtil.getBundle("org.jhotdraw.samples.svg.Labels");

        addStrokeColorButton(panel, labels);
        addStrokeOpacitySlider(panel, labels);
        addStrokeWidthSlider(panel, labels);
        addStrokeCapJoinDashButtons(panel, labels);
    }

    private void buildDetailedStrokeControls(JPanel panel) {
        ResourceBundleUtil labels =
                ResourceBundleUtil.getBundle("org.jhotdraw.samples.svg.Labels");

        addStrokeColorField(panel, labels);
        addStrokeOpacityField(panel, labels);
        addStrokeWidthField(panel, labels);
        addDashOffsetField(panel, labels);
        addStrokeCapJoinDashButtons(panel, labels);
    }

    //  UI COMPONENT BUILDERS

    private void addStrokeColorButton(JPanel panel, ResourceBundleUtil labels) {
        Map<AttributeKey<?>, Object> defaults = new HashMap<>();
        STROKE_GRADIENT.put(defaults, null);

        AbstractButton button =
                ButtonFactory.createSelectionColorChooserButton(
                        editor, STROKE_COLOR, "attribute.strokeColor",
                        labels, defaults, new Rectangle(3, 3, 10, 10),
                        PaletteColorChooserUI.class, disposables);

        button.setUI((PaletteButtonUI) PaletteButtonUI.createUI(button));
        ((JPopupButton) button).setAction(null, null);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        panel.add(button, gbc);
    }

    private void addStrokeOpacitySlider(JPanel panel, ResourceBundleUtil labels) {
        JPopupButton popup = new JPopupButton();
        JAttributeSlider slider =
                new JAttributeSlider(JSlider.VERTICAL, 0, 100, 100);

        popup.add(slider);
        labels.configureToolBarButton(popup, "attribute.strokeOpacity");
        popup.setUI((PaletteButtonUI) PaletteButtonUI.createUI(popup));

        slider.setUI((SliderUI) PaletteSliderUI.createUI(slider));
        slider.setScaleFactor(100d);

        disposables.add(new FigureAttributeEditorHandler<>(
                STROKE_OPACITY, slider, editor));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(3, 0, 0, 0);
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        panel.add(popup, gbc);
    }

    private void addStrokeWidthSlider(JPanel panel, ResourceBundleUtil labels) {
        JPopupButton popup = new JPopupButton();
        JAttributeSlider slider =
                new JAttributeSlider(JSlider.VERTICAL, 0, 50, 1);

        slider.setUI((SliderUI) PaletteSliderUI.createUI(slider));
        popup.add(slider);
        labels.configureToolBarButton(popup, "attribute.strokeWidth");
        popup.setUI((PaletteButtonUI) PaletteButtonUI.createUI(popup));

        disposables.add(new FigureAttributeEditorHandler<>(
                STROKE_WIDTH, slider, editor));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(3, 0, 0, 0);
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        panel.add(popup, gbc);
    }

    private void addStrokeCapJoinDashButtons(JPanel panel, ResourceBundleUtil labels) {
        AbstractButton joinBtn =
                ButtonFactory.createStrokeJoinButton(editor, labels, disposables);
        joinBtn.setUI((PaletteButtonUI) PaletteButtonUI.createUI(joinBtn));

        AbstractButton capBtn =
                ButtonFactory.createStrokeCapButton(editor, labels, disposables);
        capBtn.setUI((PaletteButtonUI) PaletteButtonUI.createUI(capBtn));

        AbstractButton dashBtn =
                ButtonFactory.createStrokeDashesButton(
                        editor,
                        new double[][]{
                                null, {4d,4d}, {2d,2d}, {4d,2d},
                                {2d,4d}, {8d,2d}, {6d,2d,2d,2d}},
                        labels, disposables);
        dashBtn.setUI((PaletteButtonUI) PaletteButtonUI.createUI(dashBtn));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        panel.add(joinBtn, gbc);
        panel.add(capBtn, gbc);
        panel.add(dashBtn, gbc);
    }

    private void addStrokeColorField(JPanel panel, ResourceBundleUtil labels) {
        JAttributeTextField<Color> field = new JAttributeTextField<>();
        field.setColumns(7);
        field.setUI((PaletteFormattedTextFieldUI)
                PaletteFormattedTextFieldUI.createUI(field));
        field.setFormatterFactory(
                ColorFormatter.createFormatterFactory(
                        ColorFormatter.Format.RGB_INTEGER_SHORT,
                        false, false));

        disposables.add(new FigureAttributeEditorHandler<>(
                STROKE_COLOR, field, editor));

        panel.add(field);
    }

    private void addStrokeOpacityField(JPanel panel, ResourceBundleUtil labels) {
        JAttributeTextField<Double> field = new JAttributeTextField<>();
        JavaNumberFormatter formatter =
                new JavaNumberFormatter(0d, 100d, 100d, false, "%");
        field.setFormatterFactory(new DefaultFormatterFactory(formatter));

        disposables.add(new FigureAttributeEditorHandler<>(
                STROKE_OPACITY, field, editor));

        panel.add(field);
    }

    private void addStrokeWidthField(JPanel panel, ResourceBundleUtil labels) {
        JAttributeTextField<Double> field = new JAttributeTextField<>();
        JavaNumberFormatter formatter =
                new JavaNumberFormatter(0d, 100d, 1d);
        field.setFormatterFactory(new DefaultFormatterFactory(formatter));

        disposables.add(new FigureAttributeEditorHandler<>(
                STROKE_WIDTH, field, editor));

        panel.add(field);
    }

    private void addDashOffsetField(JPanel panel, ResourceBundleUtil labels) {
        JAttributeTextField<Double> field = new JAttributeTextField<>();
        field.setFormatterFactory(
                JavaNumberFormatter.createFormatterFactory(-1000d, 1000d, 1d));

        disposables.add(new FigureAttributeEditorHandler<>(
                STROKE_DASH_PHASE, field, editor));

        panel.add(field);
    }

    //  METADATA

    @Override
    protected String getID() {
        return "stroke";
    }

    @Override
    protected int getDefaultDisclosureState() {
        return 1;
    }
}
