# Concept Location Notes — Automatic Selection Feature (JHotDraw)

**Change Request:** Automatic Selection
**Technique:** Dynamic Program Analysis using the IDE Debugger
**Sample Application:** `org.jhotdraw.samples.draw.Main`

---

## Scenario A — Select All

### Trigger
Edit → Select All (menu item)

### Breakpoints
| File | Line | Method |
|------|------|--------|
| `jhotdraw-actions/.../SelectAllAction.java` | 76 | `actionPerformed()` |
| `jhotdraw-core/.../DefaultDrawingView.java` | 844 | `selectAll()` |
| `jhotdraw-core/.../DefaultDrawingView.java` | 1062 | `fireSelectionChanged()` |

### Debugger Steps
1. F5 to launch the Draw sample.
2. Draw a few figures on the canvas.
3. Click **Edit → Select All** → debugger stops in `SelectAllAction.actionPerformed()`.
4. F10 to step over to the line `((EditableComponent) c).selectAll();`.
5. F11 to step into `DefaultDrawingView.selectAll()`.
6. F10 through the loop — observe `selectedFigures` in the Variables panel growing as each `Figure` from `drawing.getChildren()` is added (only those with `isSelectable() == true`).
7. F11 at `fireSelectionChanged(...)` to enter event dispatch.
8. Inspect Call Stack panel.

### Observed Call Chain
```
SelectAllAction.actionPerformed()
  └─► DefaultDrawingView.selectAll()
        - selectedFigures.clear()
        - for each Figure in drawing.getChildren():
              if figure.isSelectable() → selectedFigures.add(figure)
        - invalidateHandles()
        - fireSelectionChanged(old, new)
              └─► FigureSelectionEvent → all FigureSelectionListeners
        - repaint()
```

---

## Scenario B — Deselect All (Clear Selection)

### Trigger
Edit → Clear Selection (menu item)

### Breakpoints
| File | Line | Method |
|------|------|--------|
| `jhotdraw-actions/.../ClearSelectionAction.java` | 75 | `actionPerformed()` |
| `jhotdraw-core/.../DefaultDrawingView.java` | 862 | `clearSelection()` |
| `jhotdraw-core/.../DefaultDrawingView.java` | 1062 | `fireSelectionChanged()` |

### Debugger Steps
1. F5 to launch.
2. Draw figures and click one to select it (so the action is enabled).
3. Click **Edit → Clear Selection** → debugger stops in `ClearSelectionAction.actionPerformed()`.
4. F11 at `((EditableComponent) c).clearSelection();` → enters `DefaultDrawingView.clearSelection()`.
5. Watch `selectedFigures` in Variables panel — it shrinks to an empty set.
6. F11 at `fireSelectionChanged(...)` → enters event dispatch.
7. Inspect Call Stack panel.

### Observed Call Chain
```
ClearSelectionAction.actionPerformed()
  └─► DefaultDrawingView.clearSelection()
        - if selectedFigures not empty:
            - oldSelection = copy of selectedFigures
            - selectedFigures.clear()
            - invalidateHandles()
            - fireSelectionChanged(old, empty)
                  └─► FigureSelectionEvent → all FigureSelectionListeners
```

---

## Scenario C — Select Same

### Trigger
Toolbar button "Select Same" (requires at least one figure already selected)

### Breakpoints
| File | Line | Method |
|------|------|--------|
| `jhotdraw-core/.../action/SelectSameAction.java` | 37 | `actionPerformed()` |
| `jhotdraw-core/.../action/SelectSameAction.java` | 41 | `selectSame()` |
| `jhotdraw-core/.../DefaultDrawingView.java` | 740 | `addToSelection(Figure)` |

### Debugger Steps
1. F5 to launch.
2. Draw, e.g., two rectangles and one ellipse.
3. Click on one rectangle to select it (the Select Same toolbar button becomes enabled).
4. Click the **Select Same** button → debugger stops in `SelectSameAction.actionPerformed()`.
5. F11 into `selectSame()`.
6. Step over the first `for` loop — observe `selectedClasses` now contains the class `RectangleFigure`.
7. Step into the second loop — at `getView().addToSelection(f)` press F11 → enters `DefaultDrawingView.addToSelection()`.
8. Watch `selectedFigures` grow as each rectangle is added; the ellipse is skipped because its class is not in `selectedClasses`.
9. Each call to `addToSelection` triggers `fireSelectionChanged()` — record the call stack.

### Observed Call Chain
```
SelectSameAction.actionPerformed()
  └─► SelectSameAction.selectSame()
        - selectedClasses ← classes of getView().getSelectedFigures()
        - for each Figure f in getDrawing().getChildren():
              if selectedClasses.contains(f.getClass())
                  └─► DefaultDrawingView.addToSelection(f)
                        - selectedFigures.add(figure)
                        - figure.createHandles(detailLevel)
                        - fireSelectionChanged(old, new)
                              └─► FigureSelectionEvent → all FigureSelectionListeners
```

---

## Consolidated Initial Set of Classes — Concept Location Result

| Domain Class | Responsibility |
|---|---|
| `SelectAllAction` | Controller entry point for the "Select All" menu item; resolves the focused `EditableComponent` and invokes its `selectAll()` method. |
| `ClearSelectionAction` | Controller entry point for the "Clear Selection" menu item; resolves the focused `EditableComponent` and invokes its `clearSelection()` method. |
| `SelectSameAction` | Controller entry point for the "Select Same" toolbar action; collects the classes of currently selected figures and adds every figure of those classes to the selection. |
| `AbstractSelectionAction` | Abstract base class for `SelectAllAction` and `ClearSelectionAction`; manages action enablement based on the target component's selection state. |
| `AbstractSelectedAction` | Abstract base class for `SelectSameAction`; listens to `DrawingEditor` for the active view and enables the action only when at least one figure is selected. |
| `EditableComponent` | Contract interface declaring `selectAll()` and `clearSelection()`; implemented by `DefaultDrawingView` to make the drawing canvas behave as an editable component. |
| `DrawingView` | Contract interface declaring the full selection API: `addToSelection()`, `removeFromSelection()`, `selectAll()`, `clearSelection()`, `getSelectedFigures()`. |
| `DefaultDrawingView` | Core domain class that owns the `selectedFigures` set and implements `selectAll()`, `clearSelection()`, and `addToSelection()`; fires `FigureSelectionEvent` after every change. |
| `Drawing` | Domain model that holds all figures; provides `getChildren()` which is iterated by `selectAll()` and `selectSame()`. |
| `Figure` | Domain entity representing each shape; queried via `isSelectable()` (Select All) and `getClass()` (Select Same) to decide whether it joins the selection. |
| `DrawingEditor` | Mediator that exposes the active `DrawingView` to figure-aware actions such as `SelectSameAction`. |
| `FigureSelectionEvent` | Event object carrying the old and new selection sets; created by `DefaultDrawingView.fireSelectionChanged()`. |
| `FigureSelectionListener` | Observer interface; every registered listener receives `selectionChanged(FigureSelectionEvent)` whenever the selection changes. |
| `AbstractAttributeEditorHandler` | Observer that reacts to selection changes by refreshing attribute editor panels (color, stroke, font, etc.). |
| `SelectionComponentDisplayer` | Observer that shows or hides toolbar UI components depending on whether figures are selected. |
| `SelectionComponentRepainter` | Observer that repaints toolbar UI components to reflect the attributes of the new selection. |
