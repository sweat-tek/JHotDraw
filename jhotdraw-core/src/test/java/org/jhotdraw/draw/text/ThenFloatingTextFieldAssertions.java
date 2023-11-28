package org.jhotdraw.draw.text;

import com.tngtech.jgiven.Stage;

public class ThenFloatingTextFieldAssertions extends Stage<ThenFloatingTextFieldAssertions> {

    public ThenFloatingTextFieldAssertions the_entered_text_should_be_visible_in_the_drawing_area() {
        return self();
    }

    public ThenFloatingTextFieldAssertions the_text_creator_overlay_should_close() {
        return self();
    }

    public ThenFloatingTextFieldAssertions text_creator_overlay_should_appear() {
        return self();
    }
}
