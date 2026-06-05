# Lab 5 — Testing (Unit Tests for the Undo/Redo Feature)

> **Project:** JHotDraw · **Feature / CR:** Undo/Redo
> **Unit under test:** `UndoRedoManager` (`org.jhotdraw.undo`) — the edit-stack engine that is the core domain logic of the feature.

## Introduction

Unit testing verifies individual units of source code in isolation to determine they behave as intended. In object-oriented code a unit is usually a class or a method. By testing the smallest units first, then their compound behaviours, we build up confidence in the whole feature.

## Objectives

- Understand the importance of testing.
- Implement unit tests for the feature's most important domain logic.

## What was tested and why

The feature's domain logic lives in **`UndoRedoManager`** (located in Lab 1, marked CHANGED in the impact analysis). The menu actions (`UndoAction`/`RedoAction`) are thin UI controllers that only delegate, so the behaviour worth verifying — the edit stack, the undo/redo operations, and the in-progress guard — is all in the manager.

## Classwork — how each requirement was met

| Lab step | Done |
|----------|------|
| 1. Add JUnit 4 dependency | Added `junit:junit:4.13.2` (test scope) to `jhotdraw-utils/pom.xml`. |
| 2. JUnit 4 tests for domain logic | `UndoRedoManagerTest` in `jhotdraw-utils/src/test/java/org/jhotdraw/undo/`. |
| 3. Best-case tests | add → undo → redo flow (see below). |
| 4. Boundary-case tests | empty-stack undo, nothing-to-redo, re-entrant edit during undo. |
| 4a. Mocks/stubs for dependencies | `mockito-core:5.11.0` used to stub a `UndoableEdit` so the manager is tested in isolation. |
| 5. Java assertions for invariants | a `assert` statement checks an undo/redo balance invariant (Surefire runs tests with `-ea`). |

### Test inventory

**Best case**
- `newManagerHasNothingToUndoOrRedo` — a fresh manager cannot undo/redo.
- `addingSignificantEditEnablesUndo` — adding an edit enables undo and sets `hasSignificantEdits`.
- `undoReversesLastEdit` — `undo()` delegates to the edit once; redo becomes available.
- `redoReappliesUndoneEdit` — `redo()` delegates once; undo becomes available again.
- `discardAllEditsClearsHistory` — history and the significant-edits flag reset.

**Boundary**
- `undoOnEmptyStackThrows` — `undo()` on an empty stack throws `CannotUndoException` (`@Test(expected = …)`).
- `redoWithNothingToRedoThrows` — `redo()` with nothing undone throws `CannotRedoException`.
- `editsArrivingWhileUndoInProgressAreIgnored` — an edit fired *during* an undo is ignored (the `undoOrRedoInProgress` guard), leaving nothing to undo.

**Isolation (Mockito)**
- `undoDelegatesReversalToTheEdit` — stubs `UndoableEdit` (`isSignificant()`/`canUndo()` → `true`) and `verify(edit).undo()` confirms the manager delegates the actual reversal to the edit, without depending on a concrete edit class.

**Invariant (Java assertion)**
- `undoRedoCountsStayBalanced` — uses `assert edit.redoCount <= edit.undoCount + 1` to enforce an invariant that should never break, in addition to the JUnit assertions on the exact counts.

### Note on assertions (lab step 5)

Two kinds of assertion are used deliberately, per the brief:
- **JUnit assertions** (`assertEquals`, `assertTrue`, …) verify the *expected outcome* of a test and fail the test when violated.
- A **Java `assert`** checks an *invariant* — "something that should never happen." If violated it halts execution (an `AssertionError`), unlike an exception which is part of normal control flow. Surefire enables assertions by default, so the `assert` is active during the test run.

### Isolating dependencies (lab step 4a)

A unit test should exercise a single code-path through a single method; when execution leaves that method you have a dependency that should be stubbed. Two techniques are shown:
- **Hand-written stub** — `CountingEdit extends AbstractUndoableEdit` records undo/redo counts (used where real stack semantics are needed).
- **Mockito mock** — `mock(UndoableEdit.class)` with programmed behaviour (used to verify pure delegation in isolation).

## Verification — results

| Command | Result |
|---------|--------|
| `mvn -pl jhotdraw-utils test` | **BUILD SUCCESS** — `Tests run: 14, Failures: 0, Errors: 0, Skipped: 0` (10 new JUnit + 4 existing TestNG). |
| `mvn install` (full reactor, with tests) | **BUILD SUCCESS** — all module tests pass. |

> **Coexistence note:** the module already used **TestNG** (`BezierPathNGTest`). Adding JUnit 4 did not displace it — Surefire ran both, so the existing tests keep passing alongside the new ones.

## How the feature was verified (summary for the portfolio)

The Undo/Redo feature's core behaviour is verified by automated JUnit 4 tests on `UndoRedoManager`: the best-case add/undo/redo cycle, the error boundaries (empty stack, nothing to redo), and the re-entrancy guard. Dependencies on concrete edits are removed with a Mockito stub so each test isolates one code-path, and a Java assertion guards a core invariant. All 14 module tests pass, and the full project build is green — giving objective evidence the feature behaves as designed.
