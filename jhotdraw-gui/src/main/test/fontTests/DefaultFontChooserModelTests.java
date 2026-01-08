package fontTests;

import org.jhotdraw.gui.JFontChooser;
import org.jhotdraw.gui.fontchooser.DefaultFontChooserModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import static org.junit.Assert.*;

public class DefaultFontChooserModelTests {

     @BeforeEach
     void setUp() {
          MockitoAnnotations.openMocks(this);
     }


     @Test
     // Invariant test
     // Regardless of which fonts are set, the models root node has the amount of children as there are groups in the yaml.
     public void setFontsTest() {
          DefaultFontChooserModel model = new DefaultFontChooserModel();
          int expected = 1 + model.getConfig().getGroups().size() + 1;

          model.setFonts(new Font[] { new Font("Arial", Font.PLAIN, 12) });
          Assertions.assertEquals(expected, model.getChildCount(model.getRoot()));

          model.setFonts(GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts());
          assertEquals(expected, model.getChildCount(model.getRoot()));

          model.setFonts(new Font[] { new Font("Times New Roman", Font.BOLD, 14) });
          assertEquals(expected, model.getChildCount(model.getRoot()));


     }



}
