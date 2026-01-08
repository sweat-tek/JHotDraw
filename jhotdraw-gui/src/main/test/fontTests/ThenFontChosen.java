package fontTests;

import com.tngtech.jgiven.Stage;


import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.jhotdraw.gui.JFontChooser;


import static org.assertj.core.api.Assertions.assertThat;

import java.awt.*;

public class ThenFontChosen extends Stage<ThenFontChosen> {

     @ExpectedScenarioState
             Font font;
     @ExpectedScenarioState
     JFontChooser fontChooser;
     public ThenFontChosen the_font_should_be_chosen(Font font) {
          assertThat(fontChooser.getFont()).isEqualTo(font);
          return this;
     }


}
