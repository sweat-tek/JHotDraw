package fontTests;

import org.jhotdraw.gui.fontchooser.FontFaceNode;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import static org.junit.Assert.*;

public class FontFaceNodeTests {

     @Mock
     private ActionListener listener;

     @BeforeEach
     void setUp() {
          MockitoAnnotations.openMocks(this);
     }

     @Test
     // Best case scenario
     public void beautifyNameTest() {
          Font testFont = new Font("Dialog", Font.PLAIN, 12);
          FontFaceNode faceNode = new FontFaceNode(testFont);
          assertEquals("Dialog.plain", faceNode.toString());
     }

     // Test boundary case scenario for FontFaceNode
     @Test
     public void beautifyNameTestBoundaryCase() {
          Font testFont = new Font("", Font.PLAIN, 12);
          FontFaceNode faceNode = new FontFaceNode(testFont);
          assertEquals("Dialog.plain", faceNode.toString());
     }

     // Invariant test for fontfacenode. Must always be a leaf.
     @Test
     public void isLeafTest() {
          FontFaceNode node = new FontFaceNode(new Font("Arial", 0, 12));
          assertTrue(node.isLeaf());
          assertEquals(0, node.getChildCount());
     }
}
