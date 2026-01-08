package fontTests;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import org.jhotdraw.gui.JFontChooser;

import java.awt.*;

public class GivenFont extends Stage<GivenFont> {

     @ProvidedScenarioState
     JFontChooser fontChooser;
     public Font font;

     public GivenFont the_font() {
          fontChooser = new JFontChooser();
          font = new Font("Arial", Font.PLAIN, 12);
          return self();
     }

}
