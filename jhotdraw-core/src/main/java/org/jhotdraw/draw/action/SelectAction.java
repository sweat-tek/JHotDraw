package org.jhotdraw.draw.action;

import dk.sdu.mmmi.featuretracer.lib.FeatureEntryPoint;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.figure.CompositeFigure;
import org.jhotdraw.draw.figure.GroupFigure;
import org.jhotdraw.util.ResourceBundleUtil;

abstract public class SelectAction extends AbstractSelectedAction {
  private static final long serialVersionUID = 1L;
  public static final String ID = "edit.groupSelection";
  protected CompositeFigure prototype;
  /**
   * If this variable is true, this action groups figures.
   * If this variable is false, this action ungroups figures.
   */
  private boolean isGroupingAction;

  /**
   * Creates a new instance.
   */
  @FeatureEntryPoint(value = "Group")
  public SelectAction(DrawingEditor editor) {
    this(editor, new GroupFigure(), true);
  }

  @FeatureEntryPoint(value = "Group")
  public SelectAction(DrawingEditor editor, CompositeFigure prototype) {
    this(editor, prototype, true);
  }

  @FeatureEntryPoint(value = "Group")
  public SelectAction(DrawingEditor editor, CompositeFigure prototype,
                      boolean isGroupingAction) {
    super(editor);
    this.prototype = prototype;
    this.isGroupingAction = isGroupingAction;
    ResourceBundleUtil labels
            = ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
    labels.configureAction(this, ID);
    updateEnabledState();
  }

  @Override
  protected abstract void updateEnabledState();
}
