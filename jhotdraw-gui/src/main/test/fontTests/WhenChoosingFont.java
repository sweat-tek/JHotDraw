package fontTests;

import com.tngtech.jgiven.Stage;


import com.tngtech.jgiven.annotation.ExpectedScenarioState;

import org.jhotdraw.gui.JFontChooser;


import java.awt.*;

public class WhenChoosingFont extends Stage<WhenChoosingFont> {
     @ExpectedScenarioState
     JFontChooser fontChooser;
     Font font;

     public WhenChoosingFont choosing_font(Font font) {
          this.font = font;
          fontChooser.setFont(font);
          return self();
     }
}
