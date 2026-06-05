# Concept Location via Dynamic Analysis â€” JHotDraw

> **Goal of this exercise**
> Use the IDE Debugger to find â€” at runtime â€” every class that participates in the **Automatic Selection** feature of JHotDraw, and produce an *initial set of classes* table for the portfolio.

---

## Part 1 â€” One-Time Setup

Do these once before you start.

### Step 1. Build the project

Open a PowerShell terminal in VS Code (`` Ctrl+` ``) and run:

```powershell
cd "c:\Users\alans\Desktop\SM\JHotDraw"
mvn install -DskipTests
```

Wait until you see `BUILD SUCCESS`.

### Step 2. Confirm the launch configuration exists

The file [.vscode/launch.json](.vscode/launch.json) must contain a configuration named **"Debug JHotDraw Draw Sample"**. If it doesn't, create it with this content:

```json
{
    "version": "0.2.0",
    "configurations": [
        {
            "type": "java",
            "name": "Debug JHotDraw Draw Sample",
            "request": "launch",
            "mainClass": "org.jhotdraw.samples.draw.Main",
            "projectName": "jhotdraw-samples-misc"
        }
    ]
}
```

### Step 3. Learn the four debugger keys

| Key | What it does |
|---|---|
| **F5** | Continue running until next breakpoint |
| **F10** | Step Over â€” execute current line, stay in same method |
| **F11** | Step Into â€” go inside the method called on this line |
| **Shift+F11** | Step Out â€” finish current method and return to caller |

Memorise these panels in the **Run and Debug** view (`Ctrl+Shift+D`):
- **Variables** â€” current values of local variables and fields
- **Call Stack** â€” chain of methods that led to the current line *(this is what you copy into your portfolio table)*
- **Breakpoints** â€” list of all breakpoints (you can disable/remove them here)

---

## Part 2 â€” Scenario A: Select All

**Goal:** Trace what happens when the user clicks **Edit â†’ Select All**.

### Step 1. Set the breakpoints

Open each file below, click in the gutter (left of the line number) on the listed line. A red dot appears.

| File | Line | Method |
|---|---|---|
| [SelectAllAction.java](jhotdraw-actions/src/main/java/org/jhotdraw/action/edit/SelectAllAction.java#L74) | 74 | `actionPerformed()` |
| [DefaultDrawingView.java](jhotdraw-core/src/main/java/org/jhotdraw/draw/DefaultDrawingView.java#L844) | 844 | `selectAll()` |
| [DefaultDrawingView.java](jhotdraw-core/src/main/java/org/jhotdraw/draw/DefaultDrawingView.java#L1062) | 1062 | `fireSelectionChanged()` |

### Step 2. Launch the app

Press **F5**. The JHotDraw window opens.

### Step 3. Create test data

Inside the running app:
1. Click the **Rectangle** tool in the toolbar.
2. Draw 2 rectangles on the canvas.
3. Click the **Ellipse** tool.
4. Draw 1 ellipse.

### Step 4. Trigger the feature

Click **Edit â†’ Select All** in the menu bar.

### Step 5. Observe in the debugger

The debugger pauses at line 74 of `SelectAllAction`.

1. Look at the **Variables** panel â€” note the value of `c` (it should be a `DefaultDrawingView`).
2. Press **F10** until the highlighted line is `((EditableComponent) c).selectAll();`.
3. Press **F11** â€” you jump into `DefaultDrawingView.selectAll()`.
4. In the **Variables** panel expand `selectedFigures`. It is currently empty.
5. Press **F10** through the `for` loop. After each iteration, watch `selectedFigures` grow by one figure.
6. After the loop, the highlighted line is `fireSelectionChanged(oldSelection, newSelection);`. Press **F11**.
7. You are now inside `fireSelectionChanged()`. Look at the **Call Stack** panel â€” it shows the full chain.

### Step 6. Record the call stack

Open the **Call Stack** panel and write down every class name from top to bottom. You should see:

```
DefaultDrawingView.fireSelectionChanged()      â† current
DefaultDrawingView.selectAll()
SelectAllAction.actionPerformed()
... (Swing menu plumbing â€” ignore these)
```

### Step 7. Stop

Press the red **Stop** button (or **Shift+F5**).

### What you just confirmed

```
SelectAllAction.actionPerformed()
  â””â”€â–º DefaultDrawingView.selectAll()
        - selectedFigures.clear()
        - for each Figure in drawing.getChildren():
              if figure.isSelectable() â†’ selectedFigures.add(figure)
        - invalidateHandles()
        - fireSelectionChanged(old, new)
              â””â”€â–º FigureSelectionEvent â†’ all FigureSelectionListeners
        - repaint()
```

---

## Part 3 â€” Scenario B: Deselect All (Clear Selection)

**Goal:** Trace what happens when the user clicks **Edit â†’ Clear Selection**.

### Step 1. Disable the Scenario A breakpoints

In the **Breakpoints** panel, uncheck the three breakpoints from Scenario A (don't delete them â€” you may want them again).

### Step 2. Set the new breakpoints

| File | Line | Method |
|---|---|---|
| [ClearSelectionAction.java](jhotdraw-actions/src/main/java/org/jhotdraw/action/edit/ClearSelectionAction.java#L74) | 74 | `actionPerformed()` |
| [DefaultDrawingView.java](jhotdraw-core/src/main/java/org/jhotdraw/draw/DefaultDrawingView.java#L862) | 862 | `clearSelection()` |
| [DefaultDrawingView.java](jhotdraw-core/src/main/java/org/jhotdraw/draw/DefaultDrawingView.java#L1062) | 1062 | `fireSelectionChanged()` |

### Step 3. Launch the app

Press **F5**.

### Step 4. Create test data and select something

1. Draw 2 rectangles.
2. Click the **Selection** tool (the arrow).
3. Click on one rectangle to select it. (You will see selection handles appear around it.)

> Without this step the menu item is disabled and your breakpoint will never hit.

### Step 5. Trigger the feature

Click **Edit â†’ Clear Selection**.

### Step 6. Observe in the debugger

1. Debugger pauses in `ClearSelectionAction.actionPerformed()`. Press **F10** until the highlighted line is `((EditableComponent) c).clearSelection();`.
2. Press **F11** â€” you jump into `DefaultDrawingView.clearSelection()`.
3. In the **Variables** panel, `selectedFigures` contains 1 figure.
4. Press **F10** past `selectedFigures.clear();` â€” now it is empty.
5. Press **F11** at `fireSelectionChanged(...)`.
6. In the **Call Stack** panel, record every class.

### Step 7. Stop

Press **Shift+F5**.

### What you just confirmed

```
ClearSelectionAction.actionPerformed()
  â””â”€â–º DefaultDrawingView.clearSelection()
        - if selectedFigures not empty:
              - oldSelection = copy of selectedFigures
              - selectedFigures.clear()
              - invalidateHandles()
              - fireSelectionChanged(old, empty)
                    â””â”€â–º FigureSelectionEvent â†’ all FigureSelectionListeners
```

---

## Part 4 â€” Scenario C: Select Same

**Goal:** Trace what happens when the user clicks the **Select Same** toolbar button.

### Step 1. Disable the Scenario B breakpoints

Uncheck them in the **Breakpoints** panel.

### Step 2. Set the new breakpoints

| File | Line | Method |
|---|---|---|
| [SelectSameAction.java](jhotdraw-core/src/main/java/org/jhotdraw/draw/action/SelectSameAction.java#L36) | 36 | `actionPerformed()` |
| [SelectSameAction.java](jhotdraw-core/src/main/java/org/jhotdraw/draw/action/SelectSameAction.java#L41) | 41 | `selectSame()` |
| [DefaultDrawingView.java](jhotdraw-core/src/main/java/org/jhotdraw/draw/DefaultDrawingView.java#L742) | 742 | `addToSelection(Figure)` |

### Step 3. Launch the app

Press **F5**.

### Step 4. Create test data and pre-select

1. Draw **2 rectangles** and **1 ellipse**.
2. Click the **Selection** tool.
3. Click **one rectangle** (only one figure must be selected for the toolbar button to enable).

### Step 5. Trigger the feature

Click the **Select Same** button on the toolbar (its tooltip reads "Select Same").

### Step 6. Observe in the debugger

1. Debugger pauses in `SelectSameAction.actionPerformed()`. Press **F11** to step into `selectSame()`.
2. After the first `for` loop, expand `selectedClasses` in **Variables** â€” it contains exactly one class: `RectangleFigure`.
3. Press **F10** through the second loop. For each `Figure f`:
   - When `f` is a rectangle â†’ execution enters the `if` block. Press **F11** at `getView().addToSelection(f)` to step into `DefaultDrawingView.addToSelection()`.
   - When `f` is the ellipse â†’ the `if` is skipped (the ellipse class is not in `selectedClasses`).
4. Inside `addToSelection()`, watch `selectedFigures` grow.
5. Each time, `fireSelectionChanged()` is invoked â†’ check the **Call Stack**.

### Step 7. Stop

Press **Shift+F5**.

### What you just confirmed

```
SelectSameAction.actionPerformed()
  â””â”€â–º SelectSameAction.selectSame()
        - selectedClasses â† classes of getView().getSelectedFigures()
        - for each Figure f in getDrawing().getChildren():
              if selectedClasses.contains(f.getClass())
                  â””â”€â–º DefaultDrawingView.addToSelection(f)
                        - selectedFigures.add(figure)
                        - figure.createHandles(detailLevel)
                        - fireSelectionChanged(old, new)
                              â””â”€â–º FigureSelectionEvent â†’ all FigureSelectionListeners
```

---

## Part 5 â€” Portfolio Deliverable: Initial Set of Classes

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

---

## Part 6 â€” Troubleshooting

| Symptom | Cause | Fix |
|---|---|---|
| Breakpoint never hits | The menu/button is disabled, so `actionPerformed` is never called | Pre-select a figure (Scenarios B & C); make sure the canvas has focus |
| "Source not found" when stepping | Module not built | Re-run `mvn install -DskipTests` |
| App won't launch | Wrong `projectName` in launch.json | Confirm it is `jhotdraw-samples-misc` |
| Too many breakpoints firing | Old breakpoints from prior scenario still active | Uncheck them in the **Breakpoints** panel |
| Lost in the call stack | Stepped into Swing internals | Press **Shift+F11** repeatedly until you are back in a `org.jhotdraw.*` frame |

---

## Part 7 â€” Key Take-aways

- **Controllers** in JHotDraw are `*Action` classes under `jhotdraw-actions` (generic) and `jhotdraw-core/.../draw/action` (drawing-specific).
- The `EditableComponent` interface decouples the generic edit menu actions from the concrete drawing canvas (`DefaultDrawingView`).
- `DefaultDrawingView` is the **single source of truth** for the current selection (`selectedFigures` set).
- Selection changes are propagated through the **Observer pattern** via `FigureSelectionEvent` / `FigureSelectionListener`, enabling toolbars and attribute editors to react automatically.
- `SelectSameAction` differs structurally from the other two: it extends `AbstractSelectedAction` (drawing-aware, requires non-empty selection) instead of `AbstractSelectionAction` (generic Swing-component-aware).



---

# Part 8 — Change Impact Analysis (Marking Algorithm)

> **Goal of this chapter**
> Take the classes you found by concept location and decide, one by one, which ones a *real change request* would affect. We follow the marking algorithm from Rajlich, *Software Engineering: The Current Practice*, Figure 7.9.

> **Concrete change request used in this exercise**
> *After Select All / Deselect All / Select Same, show a "N figure(s) selected" message in a status bar at the bottom of the Draw window, and keep it in sync with every later selection change.*

---

## Step 1 — Understand the four marks

Every class will end up with exactly one of these labels:

| Mark | Meaning |
|---|---|
| **BLANK** | Not yet visited. |
| **NEXT** | A neighbor of an already-marked class; it has to be inspected next. |
| **UNCHANGED** | After inspection: the change request does NOT affect this class. Dead end. |
| **PROPAGATES** | After inspection: the change passes through this class (it is on the path) but the class itself does not need source edits. |
| **CHANGED** | After inspection: this class must actually be edited. |

The **Estimated Impact Set** at the end = every class marked **CHANGED** plus every class marked **PROPAGATES**.

---

## Step 2 — Seed the algorithm with the three starting classes

These are the classes concept location already gave you. Mark them **CHANGED** without inspection:

| Starting class | Why it is the entry point |
|---|---|
| `org.jhotdraw.action.edit.SelectAllAction` | Controller for *Select All* |
| `org.jhotdraw.action.edit.ClearSelectionAction` | Controller for *Deselect All* |
| `org.jhotdraw.draw.action.SelectSameAction` | Controller for *Select Same* |

> Note on the package name: the assignment text says `org.jhotdraw.app.action.edit`, but in this codebase the package was renamed to `org.jhotdraw.action.edit` (module `jhotdraw-actions`). Use the real names you see in the source.

Now mark every BLANK neighbor of these three as **NEXT**.

---

## Step 3 — How to find neighbors of a class (in VS Code)

For each class you are inspecting, do this every time:

1. Open the `.java` file.
2. Look at the `extends` / `implements` clauses › those are *inheritance neighbors*.
3. Look at every `import org.jhotdraw.*` line › those are *reference neighbors*.
4. Right-click the class name in the editor › **Find All References** › that gives you *callers*.
5. Inside the class body, look at every method call on a non-local variable › those are *callees*.

Write the neighbors down before moving on. **Do not invent neighbors you have not verified in the source.**

---

## Step 4 — How to inspect one NEXT class

For each NEXT class, ask yourself this single question:

> *"To satisfy the change request (show selection count in a status bar), do I need to edit THIS file?"*

Then pick exactly one of three answers and write a one-sentence reason:

- **UNCHANGED** — "No edits, and the change does not flow through here."
- **PROPAGATES** — "No edits, but my class is on the call/observer path between an edited class and another class that may need editing."
- **CHANGED** — "Yes, I have to open this file and modify it."

If the answer is **PROPAGATES** or **CHANGED**, mark every still-BLANK neighbor of that class as **NEXT**.
If the answer is **UNCHANGED**, stop — its neighbors are NOT added.

Keep doing Step 4 until there are zero NEXT classes left.

---

## Step 5 — Apply the algorithm to JHotDraw (visit-order trace)

Visit the classes in this order. Each row shows the mark and the one-sentence justification.

| # | Class | Mark | Reason |
|---|---|---|---|
| 1 | `SelectAllAction` | CHANGED | Seeded — entry point of *Select All*. |
| 2 | `ClearSelectionAction` | CHANGED | Seeded — entry point of *Deselect All*. |
| 3 | `SelectSameAction` | CHANGED | Seeded — entry point of *Select Same*. |
| 4 | `AbstractSelectionAction` | UNCHANGED | Parent of #1 and #2; only manages the action's enabled state, no selection-count work happens here. |
| 5 | `AbstractSelectedAction` | UNCHANGED | Parent of #3; same story — uses `FigureSelectionEvent` only to refresh enablement. |
| 6 | `EditableComponent` | UNCHANGED | Already exposes everything the status bar needs (`SELECTION_EMPTY_PROPERTY`); no new method required. |
| 7 | `DrawingEditor` | PROPAGATES | The new status-bar listener has to follow the *active view* via `ACTIVE_VIEW_PROPERTY`, so the change passes through this interface — but the interface itself does not change. Adds neighbor › `DrawingView`. |
| 8 | `Figure` | UNCHANGED | The CR is about counting figures, not about per-figure behavior. |
| 9 | `DefaultApplicationModel` | UNCHANGED | Only registers `SelectAllAction.ID` / `ClearSelectionAction.ID` in the action map — wiring stays the same. |
| 10 | `DefaultDrawingEditor` | UNCHANGED | Wires keystrokes for `SelectAllAction.ID`; key bindings are unchanged. |
| 11 | `ButtonFactory` | UNCHANGED | Builds the toolbar button for `SelectSameAction`; button construction is unchanged. |
| 12 | `DefaultMenuBuilder` | UNCHANGED | Only assembles the Edit menu by ID; menu structure is unchanged. |
| 13 | `DrawingPanel` (Draw sample) | UNCHANGED | Only a layout host for tools/attributes; status bar lives one level up in `DrawView`. |
| 14 | `DrawingView` (interface) | UNCHANGED | API already provides `getSelectionCount()`, `getSelectedFigures()`, `addFigureSelectionListener()`. Adds neighbor › `DrawView`. |
| 15 | `DrawView` (Draw sample) | CHANGED | This is the `AbstractView` subclass that owns the scroll pane + `DrawingView`; the new status-bar component, label, and `FigureSelectionListener` go here. |
| 16 | `AbstractView` | UNCHANGED | Generic life-cycle base; the status bar is added in the concrete `DrawView`, not in the base class. |

When you finish row 16 there are no more NEXT classes › **STOP**.

---

## Step 6 — Write down the Estimated Impact Set

The Estimated Impact Set is just the rows whose mark is **CHANGED** or **PROPAGATES**:

| Mark | Class | Module |
|---|---|---|
| CHANGED | `SelectAllAction` | `jhotdraw-actions` |
| CHANGED | `ClearSelectionAction` | `jhotdraw-actions` |
| CHANGED | `SelectSameAction` | `jhotdraw-core` |
| CHANGED | `DrawView` | `jhotdraw-samples-misc` |
| PROPAGATES | `DrawingEditor` | `jhotdraw-core` |

**Impact set size: 5 existing classes** (plus one new helper class — e.g. `SelectionCountStatusLabel implements FigureSelectionListener` — that you will *create*; new classes are not part of the marking algorithm).

---

## Step 7 — Group the visited classes by package (Table 1)

| Package | # classes visited | Comments |
|---|---:|---|
| `org.jhotdraw.action.edit` (module `jhotdraw-actions`) | 3 | App-layer Edit-menu controllers. Hosts `SelectAllAction`, `ClearSelectionAction` and their abstract base. They are component-agnostic: they delegate to whatever focused `EditableComponent` exists, which is what makes the same Edit menu work for both the canvas and a `JTextComponent`. |
| `org.jhotdraw.api.gui` (module `jhotdraw-api`) | 1 | Defines `EditableComponent` (`selectAll`, `clearSelection`, `SELECTION_EMPTY_PROPERTY`). It is the seam between the generic edit actions and the concrete canvas — the reason the change does not propagate further into the action hierarchy. |
| `org.jhotdraw.draw` (module `jhotdraw-core`) | 2 | Core drawing framework. `DrawingEditor` / `DefaultDrawingEditor` track the active `DrawingView`, own the action map, and bind keystrokes — the bridge between the *Select Same* path and the underlying selection state. |
| `org.jhotdraw.draw.action` (module `jhotdraw-core`) | 2 | Drawing-aware action layer. `SelectSameAction` (3rd entry point) plus its abstract base `AbstractSelectedAction`, which already listens for `FigureSelectionEvent` to keep its enabled state in sync — confirming the Observer wiring needed by the new status bar is already in place. |
| `org.jhotdraw.draw.figure` (module `jhotdraw-core`) | 1 | Domain entity package containing `Figure`. Visited as a neighbor of `SelectSameAction`; UNCHANGED because the CR is about counting, not per-figure behavior. |
| `org.jhotdraw.app` (module `jhotdraw-app`) | 3 | Application shell. `DefaultApplicationModel` registers the actions, `DefaultMenuBuilder` builds the Edit menu, `AbstractView` is the base class of `DrawView`. None change — the CR is satisfied at the View level. |
| `org.jhotdraw.gui.action` (module `jhotdraw-gui`) | 1 | `ButtonFactory` constructs the SVG-style selection toolbar. UNCHANGED — the new behavior is not button-construction-time. |
| `org.jhotdraw.samples.draw` (module `jhotdraw-samples-misc`) | 2 | The Draw sample itself. `DrawingPanel` is a layout host (UNCHANGED); `DrawView` owns the scroll pane and the `DrawingView` and is the natural home for the new status bar (CHANGED). |

**Total classes visited: 15** (3 seeded + 12 inspected).

---

## Step 8 — Sanity checks before you submit

Tick these one by one:

- [ ] Every class in the Impact Set was actually inspected (not assumed).
- [ ] Every class is counted exactly once even if it was reached as a neighbor of more than one other class.
- [ ] Each row in the visit-order trace has its own one-sentence justification.
- [ ] The CR is stated explicitly at the top of the chapter (so the marks can be re-evaluated if the CR changes).
- [ ] Any class whose dependencies you could not verify in the source is flagged as such, instead of being guessed.

---

## Step 9 — Key take-aways from this analysis

- JHotDraw absorbs this change cheaply because the **Observer pattern is already in place**: `FigureSelectionEvent` / `FigureSelectionListener` over `DrawingView` publishes exactly what the new status bar needs.
- The two paths split cleanly along the layer boundary:
  - **App-layer path** (Select All / Clear Selection) terminates at `EditableComponent` and never reaches the drawing core.
  - **Draw-layer path** (Select Same) propagates through `DrawingEditor` to `DrawingView`, then stops because the API is already sufficient.
- The only real source edit beyond the three seeded actions lives in `DrawView` — that is where the status-bar component, the listener registration, and the label-text update belong.
