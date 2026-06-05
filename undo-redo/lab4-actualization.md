# Lab 4 — Actualization (Clean Architecture & Clean Code)

> **Project:** JHotDraw · **Feature / CR:** *Add an "Undo All / Redo All" command*
> **Phase:** Actualization — implement the new functionality, incorporate it into the old code, and propagate the change to every place that needs a secondary modification.
> **Builds on:** the impact set from [Lab 1](lab1-concept-location-and-impact-analysis.md) `{ UndoRedoManager, DrawView }` + the action classes, and the prefactoring in [Lab 3 — Refactoring](lab3-refactoring.md).

## Introduction

The **actualization** phase consists of implementing the new functionality, incorporating it into the old code, and **change propagation** that seeks out and updates all places in the old code that require secondary modification. The quality of that incorporation is governed by **Clean Architecture** (where code goes and which way dependencies point) and **Clean Code** principles (how each unit is written).

## Objectives

- Understand and explain **Clean Architecture** in the context of actualization.
- Understand and explain **Clean Code / SOLID principles** in the context of actualization.

---

## Part 1 — SOLID principles in the case study

Each principle is grounded in the actual undo/redo code (and in the prefactoring already applied).

### S — Single Responsibility Principle (SRP)

> *A class should have one reason to change.*

The design **separates the gesture from the operation**:

- `UndoAction` / `RedoAction` (`org.jhotdraw.action.edit`) — responsibility: **translate a menu gesture** into a call on the view's real action. They do **not** know how undo works.
- `UndoRedoManager` (`org.jhotdraw.undo`) — responsibility: **manage the edit stack** and perform `undo()`/`redo()`.

**Actualization impact:** because responsibilities are split, *Undo All* is added in two well-defined places — a new manager method (stack policy) and a new menu action (gesture) — without one bleeding into the other.

**Counter-example / smell:** `UndoRedoManager.setLocale()` (an instance method) writes the **static** `labels` field (Lab 2 smell #4, `java:S2696`) — mixing per-instance behaviour with global locale state. That is an SRP/encapsulation violation worth noting.

### O — Open/Closed Principle (OCP)

> *Open for extension, closed for modification.*

This is the principle the **prefactoring (Lab 3 Refactoring) unlocked**. After **Extract Superclass**, `AbstractUndoRedoAction` holds the shared delegation logic, and a new command is added by **subclassing and supplying an ID** — no edit to existing code:

```java
public class UndoAllAction extends AbstractUndoRedoAction {
    public static final String ID = "edit.undoAll";
    public UndoAllAction(Application app, View view) { super(app, view, ID); }
}
```

`UndoRedoManager` is likewise open to new `UndoableEdit` types: it operates against the `UndoableEdit` abstraction, so new edit kinds need no manager change.

### L — Liskov Substitution Principle (LSP)

> *Subtypes must be usable wherever their base type is expected.*

- `UndoRedoManager extends javax.swing.undo.UndoManager` and stays substitutable: it overrides `undo()`, `redo()`, `addEdit()` while preserving the contract (still throws `CannotUndoException`, still returns the right `boolean`).
- After prefactoring, `UndoAction`/`RedoAction` are substitutable wherever an `AbstractViewAction` / `Action` is expected — `DrawView` and the menu builder treat them uniformly.

**Caveat:** `UndoRedoManager.redo()` declares `throws CannotUndoException` rather than `CannotRedoException` — a small contract wrinkle that slightly weakens strict LSP and is worth flagging in actualization.

### I — Interface Segregation Principle (ISP)

> *Clients should not depend on methods they do not use.*

The collaboration is built on **small, focused interfaces**:

- `UndoableEditListener` — a single method (`undoableEditHappened`); `UndoRedoManager` implements exactly what it needs to receive edits.
- `PropertyChangeListener` — one method; used to keep the menu action's name/enabled state in sync (now a lambda after prefactoring).
- `UndoableEdit` exposes a cohesive set (`undo`, `redo`, `canUndo`, `canRedo`, `die`) that every client genuinely uses.

### D — Dependency Inversion Principle (DIP)

> *Depend on abstractions, not concretions.*

The menu controller **never references the concrete undo implementation**:

```java
// UndoAction delegates through the abstraction javax.swing.Action,
// resolved by ID from the active view's ActionMap — not a hard reference
// to UndoRedoManager$UndoAction.
Action realAction = getActiveView().getActionMap().get(ID);
```

The concrete real action is **injected** by the view (`DrawView` registers `undo.getUndoAction()` under the ID). High-level UI depends on the `Action` abstraction; the low-level concrete action is supplied at runtime through the `ActionMap` (a string-keyed port).

---

## Part 2 — Clean Architecture in the case study

Robert C. Martin's Clean Architecture organises code into concentric layers with **the Dependency Rule: source-code dependencies point only inward**. Mapping the undo/redo feature onto those layers:

```
        ┌──────────────────────────────────────────────┐
        │  Frameworks & Drivers (outer)                  │
        │  Swing: AbstractAction, UndoManager, JMenu;    │
        │  DrawView, application model / menu wiring      │
        │   ┌────────────────────────────────────────┐   │
        │   │  Interface Adapters / Controllers       │   │
        │   │  UndoAction / RedoAction                │   │
        │   │  (AbstractUndoRedoAction)               │   │
        │   │   ┌────────────────────────────────┐    │   │
        │   │   │  Use Cases / Application        │    │   │
        │   │   │  UndoRedoManager.undo()/redo()  │    │   │
        │   │   │   ┌────────────────────────┐    │    │   │
        │   │   │   │  Entities (core)        │    │    │   │
        │   │   │   │  edit stack,            │    │    │   │
        │   │   │   │  UndoableEdit objects   │    │    │   │
        │   │   │   └────────────────────────┘    │    │   │
        │   │   └────────────────────────────────┘    │   │
        │   └────────────────────────────────────────┘   │
        └──────────────────────────────────────────────┘
                 dependencies point inward ↑
```

| Clean-Architecture layer | Undo/redo code | Role |
|--------------------------|----------------|------|
| **Entities** (enterprise policy) | `UndoableEdit` / `AbstractUndoableEdit` / `CompositeEdit` and the edit stack | Encapsulate a change and how to reverse/reapply it — the most stable policy. |
| **Use cases** (application policy) | `UndoRedoManager.undo()/redo()/addEdit()` | Orchestrate the edit stack to carry out the undo/redo operation. |
| **Interface adapters** | `UndoAction` / `RedoAction` → `AbstractUndoRedoAction` | Translate a GUI gesture into a use-case call; adapt Swing's `Action` world to the manager. |
| **Frameworks & drivers** | Swing (`AbstractAction`, `UndoManager`, menus), `DrawView`, application model | The volatile outer ring — UI toolkit and view wiring. |

**The Dependency Rule holds here.** The core `UndoRedoManager` knows nothing about menus or `DrawView`; the outward menu action depends *inward* on the `Action` abstraction. The crossing of the boundary uses a **port** — the `ActionMap` keyed by a string ID — so the UI never statically depends on the undo implementation (this is the architectural face of DIP above). Each document gets its **own per-view manager**, so a use-case instance is scoped to its document rather than being a global singleton.

### How Clean Architecture guides the actualization

Implementing *Undo All / Redo All* slots cleanly into the layers — and the **change propagates outward, never inward**:

1. **Entities/Use cases:** add `undoAll()` / `redoAll()` to `UndoRedoManager` (loop `undo()` while `canUndo()`), bracketed by the existing `undoOrRedoInProgress` guard. *(core — CHANGED in impact analysis)*
2. **Interface adapters:** add `UndoAllAction` / `RedoAllAction` extending `AbstractUndoRedoAction` with new IDs (OCP — no existing adapter modified).
3. **Frameworks & drivers:** register the real actions in `DrawView`'s `ActionMap` and add the menu items in the application model. *(view — CHANGED in impact analysis)*

No inner layer is forced to know about an outer one, which is exactly why the impact set stayed small.

### Clean Code principles applied during actualization

- **DRY** — the **Extract Superclass** prefactoring removed the `UndoAction`/`RedoAction` duplication so the feature does not copy-paste a third time.
- **Meaningful names** — **Rename Field** fixed the misleading `redoActionPropertyListener` inside `UndoAction`.
- **Small, single-purpose units** — `AbstractUndoRedoAction` does one thing (delegate); subclasses only declare identity.
- **Prefer abstractions over console I/O** — Lab 2 flagged `System.out`/`printStackTrace` (`java:S106`/`S1148`); replacing them with a logger is the remaining Clean-Code cleanup.

---

## Summary

Actualization is not just "write the feature" — it is incorporating it so that **dependencies still point inward** (Clean Architecture) and each unit stays **single-purpose, DRY, and well-named** (Clean Code / SOLID). For *Undo All / Redo All*, the prefactoring (OCP via Extract Superclass) means the new commands are added at the adapter layer plus a single core method, with the change propagating outward through `DrawView` only — matching the `{ UndoRedoManager, DrawView }` impact set predicted in Lab 1.
