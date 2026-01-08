package fontTests;


import com.tngtech.jgiven.junit5.ScenarioTest;
import org.junit.jupiter.api.Test;

import java.awt.*;

public class BDDTests extends ScenarioTest<GivenFont, WhenChoosingFont, ThenFontChosen> {

     private Font font1 = new Font("Arial", Font.PLAIN, 12);

     @Test
     public void the_font_should_be_chosen() {
          given().the_font();
          when().choosing_font(font1);
          then().the_font_should_be_chosen(font1);
     }

}
