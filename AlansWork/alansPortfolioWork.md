# Portfolio Work

This document collects the portfolio deliverables for the Software Maintenance course.
Each chapter corresponds to one lab.

---

# Chapter: ChangeReqLab — Change Request

> **System under maintenance:** JHotDraw 7.6 (Java structured-graphics editor framework).
> **Selected feature:** Automatic Selection — covering three sub-features in the Edit menu / toolbar:
> - **Select All** — select every figure on the canvas.
> - **Deselect All (Clear Selection)** — empty the current selection.
> - **Select Same** — extend the current selection with every figure whose class matches a class already in the selection.

## User stories

The feature is described from the point of view of the end user of the Draw sample application, using the standard *"As a … I want … so that …"* template. Each story also lists its acceptance criteria.

### US-1 — Select All

> **As** a user editing a drawing,
> **I want** to select every figure on the canvas with a single command,
> **so that** I can apply an operation (move, delete, change attributes, copy) to the whole drawing without clicking each figure individually.

**Trigger:** menu *Edit → Select All*, or the keyboard shortcut `Ctrl+A`.

**Acceptance criteria:**
1. After the command, every selectable figure on the active canvas is part of the current selection.
2. Figures whose `isSelectable()` returns `false` (e.g. locked figures) are not added.
3. The selection handles are visible on every newly selected figure.
4. Any UI element that listens to selection changes (attribute panels, toolbar buttons) is refreshed.

### US-2 — Deselect All (Clear Selection)

> **As** a user editing a drawing,
> **I want** to clear the current selection in one step,
> **so that** I can start a fresh selection without having to click on empty canvas or unselect each figure individually.

**Trigger:** menu *Edit → Clear Selection*.

**Acceptance criteria:**
1. After the command, the current selection is empty.
2. All selection handles disappear from the canvas.
3. Actions whose enablement depends on a non-empty selection (e.g. *Select Same*) become disabled.
4. The command is a no-op (and visibly disabled) when the selection is already empty.

### US-3 — Select Same

> **As** a user editing a drawing that contains many shapes of different kinds,
> **I want** to extend the current selection to every figure of the same kind as the figures I already have selected,
> **so that** I can apply the same change (e.g. recolour all rectangles) to a whole category of shapes without picking them one by one.

**Trigger:** *Select Same* button on the selection toolbar.

**Acceptance criteria:**
1. The button is disabled when the current selection is empty.
2. After the command, the selection contains every figure on the canvas whose runtime class matches at least one class already in the previous selection.
3. Figures of other classes remain unselected.
4. The command works additively — figures already selected stay selected.

---

## Change request statement (chosen for the impact-analysis lab)

*After Select All / Deselect All / Select Same, the Draw sample window must display a status-bar message of the form `"N figure(s) selected"` reflecting the new selection size. The status bar must also stay in sync with any later selection change (mouse click, drag-rectangle, keyboard shortcut, etc.), not only with the three actions above.*

## Why this change request

- It exercises **all three** sub-features of the Automatic Selection feature, so concept location and impact analysis cover one coherent slice of the system.
- It is realistic — a status bar with the current selection count is a feature found in most graphical editors.
- It is non-trivial enough to propagate beyond the three controller classes (it reaches the View layer of the sample), but small enough to be feasible inside one lab.

---

# Chapter: CLLab — Concept Location

> **Technique used:** IDE Debugger (dynamic program analysis) on the Draw sample (`org.jhotdraw.samples.draw.Main`).
> Breakpoints were placed at suspected controller entry points; execution was followed via Step Over / Step Into; the **Call Stack** and **Variables** panels in VS Code were used to record every class that participates at runtime.
> Full step-by-step procedure is in [alans.md](alans.md) (Parts 1–4).

## Initial set of classes — result of concept location

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

**Total: 16 domain classes** identified by dynamic analysis.

---

# Chapter: AnalysisLab1 — Packages Visited After Concept Location

> **Feature analysed:** Automatic Selection (Select All / Deselect All / Select Same) in JHotDraw 7.6.
> **Change request used for the impact analysis:** *After Select All / Deselect All / Select Same, show a "N figure(s) selected" message in a status bar at the bottom of the Draw window, and keep it in sync with every later selection change.*

---

## Table 1 — Packages, classes visited, and what each package contributes

| Package name | # of classes visited | Comments |
|---|---:|---|
| `org.jhotdraw.action.edit` (module `jhotdraw-actions`) | 3 | App-layer Edit-menu controllers. Hosts `SelectAllAction`, `ClearSelectionAction` and their abstract base `AbstractSelectionAction`. **What I learned:** these actions are deliberately component-agnostic — they don't know about figures, they just delegate to whatever focused `EditableComponent` exists. **Contribution to the feature:** they are the controller entry points for two of the three selection scenarios (Select All, Deselect All) and resolve the focused component before forwarding the call. |
| `org.jhotdraw.api.gui` (module `jhotdraw-api`) | 1 | Defines the `EditableComponent` contract (`selectAll`, `clearSelection`, `SELECTION_EMPTY_PROPERTY`). **What I learned:** this single interface is the seam that lets the same Edit-menu actions work for the drawing canvas *and* for plain Swing text components. **Contribution to the feature:** it is the contract through which `SelectAllAction` and `ClearSelectionAction` reach the drawing view without depending on the drawing module. |
| `org.jhotdraw.draw` (module `jhotdraw-core`) | 2 | Core drawing framework. Visited classes: `DrawingEditor` and `DrawingView`. **What I learned:** `DrawingEditor` is a Mediator that exposes the active `DrawingView`, and `DrawingView` is the single source of truth for the current selection (it owns `selectedFigures`, `getSelectionCount()` and fires `FigureSelectionEvent`). **Contribution to the feature:** every selection change — whether triggered by a menu action, a toolbar button, or the mouse — ends up modifying `DrawingView` state and being broadcast from here. |
| `org.jhotdraw.draw.action` (module `jhotdraw-core`) | 2 | Drawing-aware action layer. Visited: `SelectSameAction` and its abstract base `AbstractSelectedAction`. **What I learned:** unlike the app-layer actions, these actions know about `DrawingEditor`, `DrawingView` and `Figure`, and `AbstractSelectedAction` already listens for `FigureSelectionEvent` to keep its enabled state in sync. **Contribution to the feature:** `SelectSameAction` is the third controller entry point; it inspects the currently selected figures and adds every figure of the same class to the selection. |
| `org.jhotdraw.draw.figure` (module `jhotdraw-core`) | 1 | Domain entity package containing `Figure`. **What I learned:** `SelectSameAction` only uses `Figure.getClass()` and `Figure.isSelectable()` — i.e. structural information, not behaviour. **Contribution to the feature:** figures are the things being counted/selected, but the feature does not require any new behaviour from `Figure` itself. |
| `org.jhotdraw.app` (module `jhotdraw-app`) | 3 | Application shell. Visited: `DefaultApplicationModel`, `DefaultMenuBuilder`, `AbstractView`. **What I learned:** action wiring is centralised — the model registers actions by ID, the menu builder looks them up by ID, and concrete views extend `AbstractView`. **Contribution to the feature:** this is what plugs the Edit-menu actions into every JHotDraw application without each sample having to wire them manually. |
| `org.jhotdraw.gui.action` (module `jhotdraw-gui`) | 1 | Visited: `ButtonFactory`. **What I learned:** the SVG-style toolbar with the *Select Same* button is assembled here, separate from the actions themselves. **Contribution to the feature:** it is the reason the *Select Same* button shows up on the toolbar of several samples without their panel code knowing about it. |
| `org.jhotdraw.samples.draw` (module `jhotdraw-samples-misc`) | 2 | Visited: `DrawingPanel` and `DrawView`. **What I learned:** `DrawingPanel` is just a layout container for the tools/attributes side panel, while `DrawView` is the `AbstractView` subclass that actually owns the scroll pane and the `DrawingView`. **Contribution to the feature:** `DrawView` is the natural place to host any per-window UI that reacts to selection (e.g. the status bar in the change request) because it has direct access to the `DrawingView` and its `FigureSelectionListener` registration. |

**Total packages visited: 8**
**Total classes visited: 15** (3 seeded by concept location + 12 inspected during the marking algorithm).

---

## What the table tells me overall

- The **automatic selection** feature is spread across **two clean layers**: a generic app-layer (`jhotdraw-actions` + `jhotdraw-api`) for menu-driven actions, and a drawing-aware layer (`jhotdraw-core` + `jhotdraw-gui`) for figure-aware actions. The split is what allows the same Edit menu to work for both text components and drawing canvases.
- The drawing core is wired with the **Observer pattern** (`FigureSelectionEvent` / `FigureSelectionListener` on `DrawingView`). This is why most packages I visited ended up *UNCHANGED* during the impact analysis — the API needed to react to selection changes is already exposed.
- The **only place that actually needs new code** for the change request lives in the sample (`org.jhotdraw.samples.draw.DrawView`), confirming that JHotDraw's separation between framework and sample applications works as intended.
