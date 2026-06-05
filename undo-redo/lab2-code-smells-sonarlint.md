# Lab 2 — Code Smells (SonarLint / SonarQube for IDE)

> **Project:** JHotDraw · **Feature / CR:** Undo/Redo (same classes located in [Lab 1](lab1-concept-location-and-impact-analysis.md))
> **Tool:** SonarQube for IDE (SonarLint) — VS Code extension, **Connected Mode** to SonarQube Cloud (project `domjab23`)

## Objective

Find code smells in JHotDraw **scoped to the change request** — the undo/redo classes located in Lab 1 — using SonarLint static analysis in the IDE, and record them in table form for the portfolio.

## 1. How the analysis was run

1. Installed the **SonarQube for IDE (SonarLint)** extension and bound it in **Connected Mode** to SonarQube Cloud project `domjab23`.
2. Opened each CR class from Lab 1 so SonarLint analyzes it on open:
   - `jhotdraw-utils/.../org/jhotdraw/undo/UndoRedoManager.java`
   - `jhotdraw-actions/.../org/jhotdraw/action/edit/UndoAction.java`
   - `jhotdraw-actions/.../org/jhotdraw/action/edit/RedoAction.java`
   - `jhotdraw-samples-misc/.../org/jhotdraw/samples/draw/DrawView.java`
3. Read findings from three places:
   - **inline squiggles** in each file,
   - the **Problems panel** (`Ctrl+Shift+M`), filtered to `sonar`,
   - the **SonarLint view** — each finding shows a rule id such as `java:S2208`.

> **Scope:** the project-wide warning count was ~592; this lab reports only the subset in the CR classes — those are the smells relevant to the undo/redo change.

## 2. Findings (CR-scoped)

> Severity uses SonarQube's labels. Because SonarLint is in **Connected Mode**, the active **quality profile** of `domjab23` is authoritative — confirm each rule id/severity against what your IDE actually reports.

| # | Class (file) | Line(s) | Rule (id) | Severity | Smell — what & why |
|:-:|--------------|---------|-----------|----------|--------------------|
| 1 | `UndoRedoManager` | 10–15 | `java:S2208` Wildcard imports should not be used | Minor | 6 star-imports (`java.awt.event.*`, `java.beans.*`, `java.util.*`, `javax.swing.*`, …) hide what is actually used and risk name clashes. |
| 2 | `UndoRedoManager` | 86, 113, 191, 226 | `java:S106` Standard outputs should not be used to log | Major | Logs via `System.out`/`System.err` instead of a logger — no levels, no formatting, not configurable. |
| 3 | `UndoRedoManager` | 87 | `java:S1148` `printStackTrace()` should not be called | Minor | `e.printStackTrace()` in the `catch` writes raw traces to console. |
| 4 | `UndoRedoManager` | 142–144 | `java:S2696` Instance methods should not write to static fields | Major | `setLocale()` (instance method) reassigns the **static** `labels` field — hidden global state shared across all managers. |
| 5 | `UndoRedoManager` | 28 + `if (DEBUG)` at 190, 225 | dead conditional / unused logging guard | Minor | `private static final boolean DEBUG = false;` makes the `if (DEBUG)` blocks unreachable dead code. |
| 6 | `UndoRedoManager` | 24 | `java:S125` Commented-out code should be removed | Minor | Trailing `//javax.swing.undo.UndoManager {` left in the class declaration. |
| 7 | `UndoRedoManager` | 82–88 vs 108–114 | inconsistent exception handling | Minor | `undo()`'s catch logs to `System.err` + stack trace; `redo()`'s catch only `System.out.println`, no trace — asymmetric handling of the same kind of failure. |
| 8 | `UndoAction` & `RedoAction` | 10–16 | `java:S2208` Wildcard imports | Minor | Same star-import smell in both action classes. |
| 9 | `UndoAction` & `RedoAction` | 39–49 / 40–50 | `java:S1604` Anonymous class should be a lambda | Minor | `new PropertyChangeListener() { … }` is a single-method interface → should be a lambda (Java 8+). |
| 10 | `UndoAction` | 39 | misleading name (copy-paste smell) | Minor | The field is named **`redoActionPropertyListener`** inside **`UndoAction`** — copied from `RedoAction`; it actually listens to the *undo* action. Misleading identifier. |
| 11 | `UndoAction` | 21–22 (Javadoc) | copy-paste documentation error | Info | Doc says *"returns a project specific undo action when invoking `getActionMap("redo")`"* — wrong ID in the prose (`"redo"` instead of `"undo"`), copied from `RedoAction`. |
| 12 | `UndoAction` vs `RedoAction` | whole class | `java:S4144` / duplicated code | Major | The two classes are near-identical (same structure, listener, install/uninstall logic) — duplicated logic that could share a base class. |

### 2a. `DrawView.java` — confirmed SonarLint output

Transcribed verbatim from the SonarQube-for-IDE Problems panel (`DrawView.java`, **10 issues**). These are the authoritative results from the connected `domjab23` profile.

| # | Location | Rule (id) | Message |
|:-:|----------|-----------|---------|
| 13 | Ln 64, Col 27 | `java:S1948` | Make non-static `"editor"` transient or serializable. *(non-serializable field in a `Serializable` class)* |
| 14 | Ln 78, Col 44 | `java:S1604` | Make this anonymous inner class a lambda. |
| 15 | Ln 98, Col 50 | `java:S3252` | Use static access with `"javax.swing.ScrollPaneConstants"` for `"LOWER_LEFT_CORNER"`. |
| 16 | Ln 160, Col 17 | `java:S1141` | Extract this nested `try` block into a separate method. *(+1 location)* |
| 17 | Ln 172, Col 46 | `java:S1604` | Make this anonymous inner class a lambda. |
| 18 | Ln 181, Col 18 | `java:S2142` | Either re-interrupt this method or rethrow the `InterruptedException` that can be caught here. |
| 19 | Ln 219, Col 46 | `java:S1604` | Make this anonymous inner class a lambda. |
| 20 | Ln 230, Col 18 | `java:S2147` | Combine this `catch` with the one at line 228, which has the same body. *(+1 location)* |
| 21 | Ln 230, Col 18 | `java:S2142` | Either re-interrupt this method or rethrow the `InterruptedException` that can be caught here. |
| 22 | Ln 247, Col 18 | `java:S2177` | Rename this method; there is a `private` method in the parent class with the same name. |

> **Recurring themes in `DrawView`:** anonymous classes that should be lambdas (`S1604` ×3), swallowed `InterruptedException` (`S2142` ×2), and duplicated `catch` bodies (`S2147`). The `S2177` hidden-method and `S1141` nested-try findings are the higher-value maintainability ones.

## 3. Notes for the portfolio

- **Scope honestly.** Report smells in the classes the CR touches; mention the project-wide total (~592) only as context.
- **Connected Mode matters.** The quality profile of `domjab23` decides which rules fire and at what severity, so your exact list/severities are authoritative over the estimates above.
- **Tie smells back to the CR.** The `UndoAction`/`RedoAction` duplication (#12) is directly relevant to the worked CR (*add Undo All / Redo All*): adding `UndoAllAction`/`RedoAllAction` would tempt a third copy-paste, so the smell argues for extracting a shared superclass **before** implementing the feature.
