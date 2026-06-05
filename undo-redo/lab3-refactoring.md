# Lab 3 — Refactoring (remove the code smells)

> **Project:** JHotDraw · **Feature / CR:** Undo/Redo
> **Input:** the smells found in [Lab 2](lab2-code-smells-sonarlint.md) · **Phase:** prefactoring (clean up *before* implementing the change)

## Objective

Apply one or more suitable **refactoring patterns** to remove bad code smells in the change-request classes, keeping behaviour unchanged (verified by the build + tests).

## 1. Refactoring patterns applied

| Pattern (Fowler) | Target | Smell(s) removed |
|------------------|--------|------------------|
| **Extract Superclass** (+ Pull Up Method / Pull Up Field) | `UndoAction` & `RedoAction` → new `AbstractUndoRedoAction` | #12 duplicated classes |
| **Rename Field** | `redoActionPropertyListener` → `actionPropertyListener` | #10 misleading name |
| **Replace Anonymous Class with Lambda / Method Reference** | the `PropertyChangeListener` → `this::realActionPropertyChanged` | #9 `java:S1604` |
| **Replace Wildcard Imports with Explicit Imports** | imports in all three action files | #8 `java:S2208` |
| Correct documentation during pull-up | copy-pasted "redo" wording in `UndoAction` Javadoc | #11 |

> Scope: the three action classes in `org.jhotdraw.action.edit`. The smells in `UndoRedoManager` and `DrawView` are left for a later pass (or the actualization step), so this refactoring stays small and reviewable.

## 2. What changed

**New file:** `jhotdraw-actions/.../org/jhotdraw/action/edit/AbstractUndoRedoAction.java`
Holds the shared delegation logic that used to be duplicated: the property listener, `updateEnabledState`, `updateView`, `install/uninstallViewListeners`, `actionPerformed`, and `getRealAction()`. The only thing a subclass varies is the **action ID**, passed up through the constructor.

**`UndoAction` / `RedoAction`** shrink from ~115 lines each to a thin subclass:

```java
public class UndoAction extends AbstractUndoRedoAction {
    private static final long serialVersionUID = 1L;
    public static final String ID = "edit.undo";

    public UndoAction(Application app, View view) {
        super(app, view, ID);
    }
}
```

(`RedoAction` is identical except `ID = "edit.redo"`.)

### Behaviour preserved
- The public API is unchanged: both classes keep their `public static final String ID` and their `(Application, View)` constructor, so existing callers such as `DrawView` (`UndoAction.ID`, `RedoAction.ID`) are unaffected.
- The delegation logic is byte-for-byte the same, just pulled up; only the per-class `id` differs.

## 3. Verification

| Check | Result |
|-------|--------|
| `mvn -DskipTests install` | **BUILD SUCCESS** (compiles) |
| `mvn install` (full, with tests) | **BUILD SUCCESS**, all tests pass (`Tests run: … Failures: 0, Errors: 0`) |

## 4. Why this is the right prefactoring for the CR

The headline smell (#12) is **duplication between `UndoAction` and `RedoAction`**. The worked change request — *add Undo All / Redo All* — would otherwise tempt a **third** copy-paste (`UndoAllAction`/`RedoAllAction`). By extracting `AbstractUndoRedoAction` first, the new actions can extend the shared base and supply only their ID, so the feature is added without re-introducing the smell. This is prefactoring done for exactly the reason the methodology prescribes: clean the code on the change's path *before* changing it.
