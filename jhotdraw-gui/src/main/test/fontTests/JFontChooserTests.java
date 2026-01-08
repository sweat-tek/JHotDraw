package fontTests;


import org.jhotdraw.gui.JFontChooser;
import org.jhotdraw.gui.fontchooser.DefaultFontChooserModel;
import org.jhotdraw.gui.fontchooser.FontFaceNode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;

public class JFontChooserTests {

     @Mock
     private ActionListener listener;
     private Font font1 = new Font("Arial", Font.PLAIN, 12);
     private Font font2 = new Font("Times New Roman", Font.BOLD, 14);

     @BeforeEach
     void setUp() {
          MockitoAnnotations.openMocks(this);
     }


     @Test
     public void addAndRemoveActionListener() {
          JFontChooser fontChooser = new JFontChooser();

          fontChooser.addActionListener(listener);
          assertEquals(1, fontChooser.getListeners(ActionListener.class).length);
          fontChooser.removeActionListener(listener);
          assertEquals(0, fontChooser.getListeners(ActionListener.class).length);


     }

     // Test that font is always chosen. One font must always be chosen, that is the invariant.
     @Test
     public void chooseFont() {
          JFontChooser fontChooser = new JFontChooser();

          fontChooser.setSelectedFont(font1);
          assertEquals(font1, fontChooser.getSelectedFont());

          fontChooser.setSelectedFont(font2);
          assertEquals(font2, fontChooser.getSelectedFont());
     }

     @Test
     public void chooseFakeFont() {
          JFontChooser fontChooser = new JFontChooser();

          Font font3 = new Font("BOGUSA AND FAKE FONT, NOT REAL 1293129391239###!!!!", Font.BOLD, 999999999);

          fontChooser.setSelectedFont(font1);
          assertEquals(font1, fontChooser.getSelectedFont());

          fontChooser.setSelectedFont(font3);
          assertEquals(font3, fontChooser.getSelectedFont());

     }


     // Test the invariant: approveSelection should always fire the correct command.
     @Test
     public void approveSelectionTest() {
          JFontChooser chooser = new JFontChooser();
          AtomicReference<String> command = new AtomicReference<>();

          chooser.addActionListener(e -> command.set(e.getActionCommand()));

          chooser.approveSelection();

          assertEquals(JFontChooser.APPROVE_SELECTION, command.get());
     }

     @Test
     void updateSelectionPath_setsCorrectTreePath_whenFontExists() {
          // Arrange
          JFontChooser chooser = new JFontChooser();

          Font font = new Font("Arial", Font.PLAIN, 12);
          Font[] fonts = new Font[] { font };

          DefaultFontChooserModel model = new DefaultFontChooserModel(fonts);
          chooser.setModel(model);

          chooser.setSelectedFont(font);

          TreePath selectionPath = chooser.getSelectionPath();
          assertNotNull(selectionPath);

          Object lastComponent = selectionPath.getLastPathComponent();
          assertTrue(lastComponent instanceof FontFaceNode);

          FontFaceNode faceNode = (FontFaceNode) lastComponent;
          assertEquals(font.getFontName(), faceNode.getFont().getFontName());
     }

}
