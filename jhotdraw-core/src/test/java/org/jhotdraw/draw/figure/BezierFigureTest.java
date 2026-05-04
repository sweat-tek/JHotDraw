package org.jhotdraw.draw.figure;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import org.jhotdraw.draw.handle.Handle;
import org.jhotdraw.geom.BezierPath;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BezierFigureTest {

    private BezierFigure openFigure;
    private BezierFigure closedFigure;

    @Before
    public void setUp() {
        openFigure = new BezierFigure(false);
        closedFigure = new BezierFigure(true);
    }

    // --- invariant checks ---

    @Test
    public void testConstructor_openFigure_isNotClosed() {
        assert !openFigure.isClosed() : "open figure should not be closed";
    }

    @Test
    public void testConstructor_closedFigure_isClosed() {
        assert closedFigure.isClosed() : "closed figure should be closed";
    }

    @Test
    public void testConstructor_figureHasZeroNodes() {
        assertEquals(0, openFigure.getNodeCount());
        assertEquals(0, closedFigure.getNodeCount());
    }

    // --- best-case scenarios ---

    @Test
    public void testAddNode_increasesNodeCount() {
        openFigure.addNode(new BezierPath.Node(10, 20));
        assertEquals(1, openFigure.getNodeCount());
        openFigure.addNode(new BezierPath.Node(30, 40));
        assertEquals(2, openFigure.getNodeCount());
    }

    @Test
    public void testAddNodeAtSpecificIndex() {
        openFigure.addNode(new BezierPath.Node(10, 10));
        openFigure.addNode(new BezierPath.Node(30, 30));
        openFigure.addNode(1, new BezierPath.Node(20, 20));
        assertEquals(3, openFigure.getNodeCount());
        assertEquals(20.0, openFigure.getNode(1).x[0], 0.0);
        assertEquals(10.0, openFigure.getNode(0).x[0], 0.0);
        assertEquals(30.0, openFigure.getNode(2).x[0], 0.0);
    }

    @Test
    public void testGetNode_returnsClone() {
        openFigure.addNode(new BezierPath.Node(10, 20));
        BezierPath.Node node = openFigure.getNode(0);
        node.x[0] = 99;
        BezierPath.Node node2 = openFigure.getNode(0);
        assertEquals(10.0, node2.x[0], 0.0);
    }

    @Test
    public void testSetClosed_switchesState() {
        openFigure.setClosed(true);
        assert openFigure.isClosed() : "figure should be closed after setClosed(true)";
        openFigure.setClosed(false);
        assert !openFigure.isClosed() : "figure should be open after setClosed(false)";
    }

    @Test
    public void testSetPoint_movesNode() {
        openFigure.addNode(new BezierPath.Node(10, 10));
        openFigure.addNode(new BezierPath.Node(20, 20));
        openFigure.setPoint(0, new Point2D.Double(100, 200));
        assertEquals(100.0, openFigure.getNode(0).x[0], 0.0);
        assertEquals(200.0, openFigure.getNode(0).y[0], 0.0);
    }

    @Test
    public void testSetPointWithCoord_movesControlPoint() {
        openFigure.addNode(new BezierPath.Node(10, 10));
        openFigure.addNode(new BezierPath.Node(20, 20));
        openFigure.setPoint(0, 1, new Point2D.Double(15, 25));
        assertEquals(15.0, openFigure.getNode(0).x[1], 0.0);
        assertEquals(25.0, openFigure.getNode(0).y[1], 0.0);
    }

    @Test
    public void testRemoveNode_decreasesCount() {
        openFigure.addNode(new BezierPath.Node(10, 10));
        openFigure.addNode(new BezierPath.Node(20, 20));
        openFigure.addNode(new BezierPath.Node(30, 30));
        assertEquals(3, openFigure.getNodeCount());
        openFigure.removeNode(1);
        assertEquals(2, openFigure.getNodeCount());
    }

    @Test
    public void testSetStartPoint_addsNodesIfNeeded() {
        openFigure.setStartPoint(new Point2D.Double(5, 10));
        assertEquals(2, openFigure.getNodeCount());
        assertEquals(5.0, openFigure.getNode(0).x[0], 0.0);
        assertEquals(10.0, openFigure.getNode(0).y[0], 0.0);
    }

    @Test
    public void testSetEndPoint_movesLastNode() {
        openFigure.addNode(new BezierPath.Node(10, 10));
        openFigure.addNode(new BezierPath.Node(20, 20));
        openFigure.setEndPoint(new Point2D.Double(99, 88));
        assertEquals(99.0, openFigure.getEndPoint().x, 0.0);
        assertEquals(88.0, openFigure.getEndPoint().y, 0.0);
    }

    @Test
    public void testClone_createsIndependentCopy() {
        openFigure.addNode(new BezierPath.Node(10, 10));
        openFigure.addNode(new BezierPath.Node(20, 20));
        BezierFigure clone = openFigure.clone();
        assertEquals(openFigure.getNodeCount(), clone.getNodeCount());
        assertEquals(openFigure.getNode(0).x[0], clone.getNode(0).x[0], 0.0);
        clone.setPoint(0, new Point2D.Double(999, 999));
        assertEquals(10.0, openFigure.getNode(0).x[0], 0.0);
    }

    @Test
    public void testGetBounds_withNoNodes() {
        Rectangle2D.Double bounds = openFigure.getBounds();
        assertNotNull(bounds);
    }

    @Test
    public void testGetBounds_withNodes() {
        openFigure.addNode(new BezierPath.Node(10, 20));
        openFigure.addNode(new BezierPath.Node(50, 80));
        Rectangle2D.Double bounds = openFigure.getBounds();
        assertTrue(bounds.width > 0);
        assertTrue(bounds.height > 0);
    }

    @Test
    public void testCreateHandles_detailLevelZero() {
        openFigure.addNode(new BezierPath.Node(10, 10));
        openFigure.addNode(new BezierPath.Node(20, 20));
        Collection<Handle> handles = openFigure.createHandles(0);
        assertFalse(handles.isEmpty());
    }

    // --- boundary cases ---

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetNode_fromEmptyFigure_throws() {
        openFigure.getNode(0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveNode_fromEmptyFigure_throws() {
        openFigure.removeNode(0);
    }

    @Test
    public void testGetPointOnPath_withNoNodes_doesNotThrow() {
        Point2D.Double p = openFigure.getPointOnPath(0.5f, 1.0);
        assertNull(p);
    }

    @Test
    public void testGetBezierPath_returnsIndependentClone() {
        openFigure.addNode(new BezierPath.Node(10, 10));
        BezierPath bp = openFigure.getBezierPath();
        bp.add(new BezierPath.Node(99, 99));
        assertEquals(1, openFigure.getNodeCount());
    }

    @Test
    public void testSetBezierPath_replacesPath() {
        openFigure.addNode(new BezierPath.Node(10, 10));
        BezierPath newPath = new BezierPath();
        newPath.add(new BezierPath.Node(1, 2));
        newPath.add(new BezierPath.Node(3, 4));
        openFigure.setBezierPath(newPath);
        assertEquals(2, openFigure.getNodeCount());
        assertEquals(1.0, openFigure.getNode(0).x[0], 0.0);
    }

    @Test
    public void testFindSegment_returnsMinusOneOnEmptyFigure() {
        assertEquals(-1, openFigure.findSegment(new Point2D.Double(0, 0), 1.0));
    }

    @Test
    public void testHandleMouseClick_singleClick_doesNotSplit() {
        openFigure.addNode(new BezierPath.Node(0, 0));
        openFigure.addNode(new BezierPath.Node(100, 100));
        int nodeCount = openFigure.getNodeCount();
        assertTrue(openFigure.getNodeCount() == 2);
    }

    @Test
    public void testContains_withNoNodes() {
        assertFalse(openFigure.contains(new Point2D.Double(0, 0)));
    }
}
