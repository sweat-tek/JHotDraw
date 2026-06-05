# Lab 1 — Concept Location & Impact Analysis (Undo/Redo)

> **Project:** JHotDraw · **Sample app:** `org.jhotdraw.samples.draw.Main`
> **Feature under study:** the Undo/Redo concept
> **Worked change request (CR):** *Add an "Undo All / Redo All" command*

This document has two parts:

1. **[Part A — Concept Location](#part-a--concept-location)** — find the code that implements Undo/Redo by debugging the running app.
2. **[Part B — Impact Analysis](#part-b--impact-analysis)** — starting from the located class, run the change-propagation algorithm (Figure 7.9) to estimate which classes a change would touch.

---

## Part A — Concept Location

### A.0 Lab brief

**Introduction.** Dynamic program analysis is the analysis of software performed by *executing* the program on a real or virtual processor. To be effective, the target program must be run with sufficient test inputs to produce interesting behavior. Coverage measures help ensure that an adequate slice of the program's possible behaviors has been observed.

**Objectives.**
- Apply the IDE debugger to locate feature concepts **at runtime**.
- Produce the **initial set of classes** that results from concept location.

**Classwork.** Use the IDE debugger to localize the classes involved in the Change Request. Features usually start from **controller** classes. If the feature spans many classes, localize only the **domain** classes tied to the domain concepts.

> **This submission:** Feature = **Undo/Redo**; the gesture traced is **Edit → Undo** in `org.jhotdraw.samples.draw.Main`. The controller entry point is `UndoAction.actionPerformed` (see A.2–A.5). The resulting initial set of classes is the **Portfolio deliverable in [A.6](#a6-portfolio-deliverable--initial-set-of-classes)**.

### A.1 Setup for this feature

1. Run `org.jhotdraw.samples.draw.Main` in **Debug** mode and let the app open fully.
2. In the running app, **draw a figure** (so there is something to undo).
3. Trigger **Edit → Undo**.

> **Not in `Main.java`.** `Main.main()` runs once at startup and never again, so a breakpoint there tells you nothing about undo/redo. Let `Main` run normally and put the breakpoint in the class that *handles the Undo gesture*.

### A.2 The key finding

There are **two `UndoAction` classes**, connected by a **delegation pattern**. Discovering this link is the main goal of the debugging session.

| Role | Location | What it does |
|------|----------|--------------|
| **Menu controller** | `jhotdraw-actions/.../org/jhotdraw/action/edit/UndoAction.java` | Does **not** perform the undo. Looks up the real action from the active view's `ActionMap` under the ID `"edit.undo"` and delegates: `realUndoAction.actionPerformed(e)`. |
| **Real action** | inner `UndoAction` of `jhotdraw-utils/.../org/jhotdraw/undo/UndoRedoManager.java` | Registered by the view via `getActionMap().put(UndoAction.ID, undo.getUndoAction())` in `DrawView.java`. Calls `UndoRedoManager.undo()`. |

(Do the same for **Redo** in `RedoAction.java`.)

### A.3 Where to set breakpoints

**Primary breakpoint** — first line inside `actionPerformed` of the *menu* controller
`jhotdraw-actions/src/main/java/org/jhotdraw/action/edit/UndoAction.java`:

```java
@Override
public void actionPerformed(ActionEvent e) {
    Action realUndoAction = getRealUndoAction();   // ← put the breakpoint here
    if (realUndoAction != null && realUndoAction != this) {
        realUndoAction.actionPerformed(e);
    }
}
```

**Optional breakpoints** — to also see how edits get *recorded* (the other half of the concept):

- `addEdit(...)` in `UndoRedoManager` (`jhotdraw-utils/.../org/jhotdraw/undo/UndoRedoManager.java`)
- `fireUndoableEditHappened(...)` in `AbstractFigure`

### A.4 How to step through it

1. Run `Main` in Debug mode; let the app open.
2. Draw a figure.
3. Click **Edit → Undo** → execution stops at the primary breakpoint.
4. **Step Into (F7)**: `getRealUndoAction()` returns the action stored in the view; stepping into `realUndoAction.actionPerformed(e)` jumps into `UndoRedoManager`'s inner `UndoAction`, then into `UndoRedoManager.undo()`. The **call-stack panel** records each class as you go.

### A.5 Execution paths

**Recording (an edit is created and stored):**

```
figure changes
  → AbstractFigure.fireUndoableEditHappened
    → Drawing.fireUndoableEditHappened
      → UndoRedoManager.undoableEditHappened   (UndoableEditListener)
        → UndoRedoManager.addEdit
```

**Undo trigger (a stored edit is reversed):**

```
menu UndoAction.actionPerformed
  → UndoRedoManager$UndoAction.actionPerformed   (delegated real action)
    → UndoRedoManager.undo()
      → UndoableEdit.undo()   (reverses the figure change)
```

### A.6 Portfolio deliverable — Initial set of classes

> **Portfolio Work.** Per the lab brief, the result of concept location is the initial set of classes in the required `Domain Class | Responsibility` table format below.

| Domain Class | Responsibility |
|--------------|----------------|
| **`UndoAction` / `RedoAction`** (`org.jhotdraw.action.edit`) | Menu/toolbar controller for Undo/Redo; a thin wrapper that **delegates** to the real action registered in the active view. |
| **`AbstractViewAction`** | Parent of the menu actions; resolves the currently active view so the command targets the right document. |
| **`View` / `DrawView`** (sample) | Owns a per-view `UndoRedoManager`, listens to its drawing for edits, and registers the real undo/redo actions in its `ActionMap`. |
| **`UndoRedoManager`** (`org.jhotdraw.undo`) | **Core of the concept.** Extends Swing's `UndoManager`, holds the edit stack, exposes the real undo/redo actions, and performs `undo()`/`redo()`. |
| **`UndoRedoManager.UndoAction` / `.RedoAction`** (inner) | The **real** actions stored in the view; invoke `manager.undo()` / `manager.redo()` and keep their enabled state in sync. |
| **`Drawing` / `DefaultDrawing`** | The model; fires `UndoableEditEvent`s and is the source the manager subscribes to as an `UndoableEditListener`. |
| **`AbstractFigure`** (and composite figures) | Generate undoable edits via `fireUndoableEditHappened` whenever a figure is modified. |
| **`UndoableEdit` / `AbstractUndoableEdit` / `CompositeEdit`** | The edit objects that encapsulate a change and know how to reverse (`undo`) and reapply (`redo`) it. |

> **Trimming tip:** If your CR scopes undo/redo to one specific operation (e.g. undoing a *move* or a *group*), you can trim this to the controller (`UndoAction`), the `UndoRedoManager`, and the particular `UndoableEdit` subclass that operation creates.

**Rule of thumb:** the breakpoint goes on the **controller that handles the gesture you trigger**, not on the launcher. For undo/redo that is `UndoAction.actionPerformed`.

---

## Part B — Impact Analysis

This is the step *after* concept location. Start from the class you located (`UndoRedoManager` / `UndoAction`) and follow the **change-propagation algorithm of Figure 7.9** to estimate which other classes a change would touch.

### B.1 The algorithm (plain terms)

Build an interaction diagram of the classes that collaborate for the feature (most already come from Part A). Every class starts **BLANK**, then:

1. Mark the located class as **CHANGED** — this is your **seed**.
2. Mark all its BLANK neighbors as **NEXT** (the frontier to examine).
3. While any class is **NEXT**, pick one, inspect it, and give it one of three marks:
   - **CHANGED** — must be modified to make the change work.
   - **PROPAGATES** — not modified itself, but the change passes through it (a forwarded call, a relayed signature change), so its neighbors still need checking.
   - **UNCHANGED** — not modified and does not carry the change further.
4. If you marked it **CHANGED** or **PROPAGATES**, mark its BLANK neighbors as **NEXT**. If **UNCHANGED**, stop expanding from it.
5. Repeat until no **NEXT** classes remain.

> **Estimated Impact Set** = every class marked **CHANGED** or **PROPAGATES**. UNCHANGED classes were visited but excluded.

You find neighbors using both kinds of analysis the objective names:

- **Static** — IDE "Find Usages" / "Call Hierarchy" on each class.
- **Dynamic** — the debugger call stack from Part A (`UndoAction → UndoRedoManager → undo()`).

> **Caveat — the marks depend entirely on the CR.** "Fix a bug in undo" propagates very differently from "add Undo All." The worked pass below uses **CR = "Add Undo All / Redo All."** Substitute your own CR's judgments.

### B.2 Worked propagation pass — 7 steps

> Legend: 🟧 **CHANGED** · 🟦 **UNCHANGED** · 🟪 **NEXT** (frontier) · ⬜ **BLANK** (never reached)

| Step | Action | Result |
|:----:|--------|--------|
| **0** | Initialize | All classes **BLANK**. |
| **1** | **Seed.** `UndoRedoManager` was located in Part A; it needs new `undoAll()`/`redoAll()` methods. | `UndoRedoManager` → 🟧 **CHANGED**. Neighbors → 🟪 **NEXT**: `DrawView`, `UndoableEdit`, `DefaultDrawing`. |
| **2** | Examine **`DrawView`** (NEXT). It owns the manager and registers its actions into the `ActionMap` (≈ lines 131–132). New manager actions ⇒ must register them. | `DrawView` → 🟧 **CHANGED**. Mark BLANK neighbor `UndoAction` → 🟪 **NEXT**. |
| **3** | Examine **`UndoableEdit`** (NEXT). The `javax.swing.undo` interface from the JDK; the existing `undo()` contract is reused. | `UndoableEdit` → 🟦 **UNCHANGED**. No expansion. |
| **4** | Examine **`DefaultDrawing`** (NEXT). Fires `UndoableEditEvent`s via the standard `UndoableEditListener`; that mechanism is unchanged for Undo All. | `DefaultDrawing` → 🟦 **UNCHANGED**. No expansion → `AbstractFigure` stays ⬜ BLANK. |
| **5** | Examine **`UndoAction`** (NEXT). Delegates to `getActiveView().getActionMap().get(ID)`. The delegation pattern is unchanged — a new `UndoAllAction` would be a *separate* class following the same pattern, not a modification here. | `UndoAction` → 🟦 **UNCHANGED**. No expansion → `AbstractViewAction` stays ⬜ BLANK. |
| **6** | No 🟪 NEXT classes remain. | **Algorithm terminates.** |

**Estimated Impact Set = { `UndoRedoManager`, `DrawView` }** (the two 🟧 coral classes).

- 🟦 **Visited but unaffected:** `UndoAction`, `DefaultDrawing`, `UndoableEdit`.
- ⬜ **Never reached:** `AbstractFigure`, `AbstractViewAction`, and the rest — the change is well-encapsulated and does not ripple to them.

### B.3 Table 1 — packages visited

Keep the **Comments** column honest about what you *learned* by visiting each package — that is explicitly what the portfolio asks for. Re-decide every CHANGED/PROPAGATES/UNCHANGED mark against your real CR; the structure stays the same, but which packages land in the impact set will shift.

| Package | # classes visited | Mark | Comments |
|---------|:-----------------:|------|----------|
| `org.jhotdraw.action.edit` | 2 | CHANGED* | The Undo/Redo menu controllers — the feature's command surface. New commands are added or existing ones extended. *(In the worked Undo-All pass `UndoAction` itself stayed UNCHANGED because a parallel `UndoAllAction` is a new sibling class.)* |
| `org.jhotdraw.action` | 1 | PROPAGATES | `AbstractViewAction`, base for view-scoped actions. Not modified, but every undo/redo action inherits its active-view resolution. |
| `org.jhotdraw.undo` | 1–2 | CHANGED | `UndoRedoManager` (and `CompositeEdit`). Core of the feature; the actual undo/redo logic and edit stack live here. |
| `org.jhotdraw.samples.draw` | 2 | CHANGED | `DrawView`, `DrawApplicationModel`. The view owns the per-document manager and registers the real actions; the model builds the menu. |
| `org.jhotdraw.draw` | 2–3 | UNCHANGED | `Drawing`, `DefaultDrawing`, `DefaultDrawingView`. The model fires `UndoableEditEvent`s — the *source* the manager listens to, not something the change modifies. |
| `org.jhotdraw.draw.figure` | 1+ | UNCHANGED | `AbstractFigure` and figure subclasses. They generate edits when modified; visited to confirm the edit-creation path is unaffected. |
| `org.jhotdraw.app` / `org.jhotdraw.api.app` | 2–3 | PROPAGATES / UNCHANGED | Application/View framework (`SDIApplication`, `View`). Provides the wiring that hands the action its active view. |
| `javax.swing.undo` (external) | 3–4 | UNCHANGED | `UndoManager`, `UndoableEdit`, `AbstractUndoableEdit`, `CompoundEdit`. JDK library that `UndoRedoManager` extends; reused, never modified. |

### B.4 Takeaways

1. **A small impact set is a valid — and good — result.** It means the feature has **low coupling**; for the Undo-All CR only `UndoRedoManager` and `DrawView` need changing.
2. **The marks shift with the CR.** If a CR required modifying the `UndoableEdit` interface itself, it would flip to **CHANGED** and propagate outward through `DefaultDrawing` and `AbstractFigure`, giving a much larger impact set. The **algorithm is identical** either way — what changes is your judgment at each NEXT class about whether it needs modification.
3. **For the portfolio:** the two CHANGED classes plus the three UNCHANGED ones are the packages you "visited"; the gray (never-reached) ones you did not.

