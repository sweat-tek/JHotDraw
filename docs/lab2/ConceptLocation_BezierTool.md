# Concept Location Results: Bezier Tool Feature

**Method:** Static code analysis (code tracing) of the Bezier tool feature in JHotDraw.

**Selected Feature:** Bezier Curve Drawing Tool

## Domain Classes and Responsibilities

| # | Domain Class | Responsibility |
|---|---|---|
| 1 | `BezierFigure` | Represents a bezier path figure on the drawing; stores nodes and control points; handles rendering (stroke/fill), hit-testing, bounding box, and XML serialization. |
| 2 | `BezierTool` | Controller that manages the interactive creation of bezier figures; handles mouse events (press, drag, release, click, move) to place nodes, fit curves, and finalize the figure. |
| 3 | `BezierPath` | Core geometric data structure (extends `ArrayList<Node>` implementing `Shape`); stores the sequence of nodes with their control points; provides path operations (split, join, transform, arc calculations). |
| 4 | `BezierPath.Node` | A single node in the bezier path containing three coordinate pairs (C0 anchor, C1 incoming control point, C2 outgoing control point) and a mask indicating which control points are active. |
| 5 | `BezierNodeHandle` | Interactive handle for selecting and dragging a node point on the bezier path; supports node deletion on double-click and modifier-key-based node type changes. |
| 6 | `BezierControlPointHandle` | Interactive handle for dragging a control point (C1 or C2) of a node to adjust the curve tangent; supports colinear locking of opposite control points. |
| 7 | `BezierOutlineHandle` | Non-interactive handle that renders the outline path of the bezier figure for selection visualization. |
| 8 | `BezierScaleHandle` | Interactive handle for scaling and rotating a bezier figure as a whole. |
| 9 | `BezierNodeEdit` | Undoable edit event that records node position/mask changes for undo/redo support. |
| 10 | `Bezier` | Static utility class containing curve-fitting algorithms (Schneider's algorithm) to fit a smooth bezier curve to digitized mouse points. |

## Supporting Infrastructure Classes

| # | Domain Class | Responsibility |
|---|---|---|
| 11 | `BezierPathIterator` | Iterates over a `BezierPath` producing standard `PathIterator` segments (MOVETO, LINETO, QUADTO, CUBICTO) for use with Java2D rendering. |
| 12 | `ChopBezierConnector` | Connector that calculates connection points on the boundary of a bezier figure for drawing connecting lines. |
| 13 | `BezierPointLocator` | Locator that returns the position of a specific node or control point on a bezier figure (used for attaching labels/decorations). |
| 14 | `BezierLabelLocator` | Locator that places a label at a relative distance along the bezier path with an angular offset. |
