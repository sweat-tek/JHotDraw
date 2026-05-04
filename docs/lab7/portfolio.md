# TestLab 1 — Portfolio: Unit Testing the Bezier Tool Feature

## Feature Under Test

**Bezier Tool** (`org.jhotdraw.draw`): drawing and editing Bézier curves through `BezierFigure` — the domain model that manages nodes, segments, open/closed paths, and hit-testing.

---

## Testing Strategy

### Scope

Pure domain logic in `BezierFigure`. The class has no Swing/UI dependencies at the unit-test level (no `Graphics2D`, no `JComponent`), making it ideal for fast, isolated unit tests. UI rendering and tool interaction are excluded from this test suite.

### Framework

- **JUnit 4.13.2** — chosen to match the existing copy/paste test suite on the `lab7-testing` branch
- **Mockito 3.12.4** — added as a dependency (available for future use with UI-dependent classes)
- Both dependencies were added to `jhotdraw-core/pom.xml`

### Test Cases (24 total)

#### Invariant Checks

| Test | What it verifies |
|------|-----------------|
| `testConstructor_openFigure_isNotClosed` | A figure created with `isClosed=false` is indeed open |
| `testConstructor_closedFigure_isClosed` | A figure created with `isClosed=true` is indeed closed |
| `testConstructor_figureHasZeroNodes` | A new figure starts with no nodes |

#### Best-Case Scenarios

| Test | What it verifies |
|------|-----------------|
| `testAddNode_increasesNodeCount` | Appending nodes increments the count |
| `testAddNodeAtSpecificIndex` | Inserting at a specific index places the node correctly |
| `testGetNode_returnsClone` | `getNode()` returns a defensive copy, not the internal reference |
| `testSetClosed_switchesState` | `setClosed(true/false)` flips the state correctly |
| `testSetPoint_movesNode` | `setPoint(index, point)` updates the node coordinates |
| `testSetPointWithCoord_movesControlPoint` | `setPoint(index, coord, point)` updates a specific control point |
| `testRemoveNode_decreasesCount` | Removing a node reduces the count |
| `testSetStartPoint_addsNodesIfNeeded` | `setStartPoint` auto-adds nodes when the figure is empty |
| `testSetEndPoint_movesLastNode` | `setEndPoint` updates the last node's coordinates |
| `testClone_createsIndependentCopy` | `clone()` produces a deep copy; modifying the copy does not affect the original |
| `testGetBounds_withNodes` | Bounding box is computed correctly when nodes exist |
| `testGetBounds_withNoNodes` | Bounding box does not throw on an empty figure |
| `testCreateHandles_detailLevelZero` | Handle creation works at the default detail level |

#### Boundary & Error Cases

| Test | What it verifies |
|------|-----------------|
| `testGetNode_fromEmptyFigure_throws` | `getNode(0)` on an empty figure throws `IndexOutOfBoundsException` |
| `testRemoveNode_fromEmptyFigure_throws` | `removeNode(0)` on an empty figure throws `IndexOutOfBoundsException` |
| `testGetPointOnPath_withNoNodes_doesNotThrow` | `getPointOnPath` returns `null` for an empty figure |
| `testGetBezierPath_returnsIndependentClone` | The returned `BezierPath` is independent (mutating it does not affect the figure) |
| `testSetBezierPath_replacesPath` | Setting a new path replaces all nodes |
| `testFindSegment_returnsMinusOneOnEmptyFigure` | `findSegment` returns `-1` on an empty figure |
| `testHandleMouseClick_singleClick_doesNotSplit` | A single click does not inadvertently split segments |
| `testContains_withNoNodes` | `contains()` returns `false` on an empty figure |

---

## Test Results

```
Tests run: 24, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

All 24 tests pass with no failures or errors.

---

## Reflection

### What was tested well

- **Core state transitions**: open ↔ closed, add/remove nodes, path replacement
- **Defensive copying**: `getNode()` and `getBezierPath()` return clones, protecting internal state
- **Precondition violations**: empty-figure edge cases are handled with exceptions or graceful fallbacks (`null`, `-1`)
- **Clone correctness**: deep copy independence was verified

### What was not tested

- **Rendering**: `draw(Graphics2D)` was omitted — would require mocking `Graphics2D`, adding little value for a unit test
- **Tool interaction**: `BezierTool` was excluded because it mixes Swing event handling with domain logic; testing it would require mocking `DrawingEditor`, `MouseEvent`, etc.
- **Handle interaction**: `BezierNodeHandle`, `BezierControlPointHandle`, etc. were excluded — these are interaction-layer classes that are better tested with integration or snapshot tests
- **Undo/redo**: The undoable edits produced by bezier actions were not tested

### Future work

- Add property-change listener tests to verify that node mutations fire the correct events
- Extract the Swing-heavy `BezierTool` logic into a testable strategy, or write integration tests using `ComponentTest` or `Jemmy`
- Explore mutation testing (PIT) to measure test-suite quality
