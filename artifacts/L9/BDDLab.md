### **BDD Lab Portfolio (Behavior Driven Testing)**

#### **1. User Story and Mapping**

* **User Story:** As a graphic designer, I want to draw a rectangle on the canvas so that I can create block diagrams
* **Mapped BDD Scenario:** * **Given** an empty JHotDraw drawing canvas
    * **When** I select the Rectangle Tool and drag from coordinates (10, 10) to (100, 100)
    * **Then** a rectangle figure should appear on the canvas

#### **2. JGiven and AssertJ-Swing Automation**

To implement this in JHotDraw (which is a Swing application), we need to combine JGiven's testing structure with AssertJ-Swing's UI interaction capabilities.

**JGiven Test Class:**
```java
import com.tngtech.jgiven.junit.ScenarioTest;
import org.junit.Test;

public class DrawRectangleScenarioTest extends ScenarioTest<GivenCanvas, WhenDrawing, ThenCanvasState> {

    @Test
    public void drawing_a_rectangle_adds_it_to_the_canvas() {
        given().an_empty_drawing_canvas();
        when().i_select_the_rectangle_tool_and_drag_from_to(10, 10, 100, 100);
        then().a_rectangle_figure_should_appear_on_the_canvas();
    }
}
```

**JGiven Stages (with AssertJ-Swing):**
```java
import com.tngtech.jgiven.Stage;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.core.GenericTypeMatcher;
import javax.swing.JPanel;
import org.jhotdraw.draw.DrawingView;

// GIVEN Stage
public class GivenCanvas extends Stage<GivenCanvas> {
    FrameFixture window;

    public GivenCanvas an_empty_drawing_canvas() {
        // Initialize JHotDraw main application frame here using AssertJ-Swing
        // window = new FrameFixture(robot(), new DefaultDrawApplication());
        // window.show();
        return self();
    }
}

// WHEN Stage
public class WhenDrawing extends Stage<WhenDrawing> {
    FrameFixture window;

    public WhenDrawing i_select_the_rectangle_tool_and_drag_from_to(int x1, int y1, int x2, int y2) {
        // Use AssertJ-swing to automate the Scenarios 
        // 1. Click the rectangle tool button
        window.button("RectangleTool").click();
        
        // 2. Simulate dragging on the drawing view
        window.panel(new GenericTypeMatcher<DrawingView>(DrawingView.class) {
            @Override
            protected boolean isMatching(DrawingView component) {
                return true;
            }
        }).robot().moveMouse(window.panel().target(), x1, y1);
        
        window.panel().robot().pressMouse(java.awt.event.InputEvent.BUTTON1_DOWN_MASK);
        window.panel().robot().moveMouse(window.panel().target(), x2, y2);
        window.panel().robot().releaseMouse(java.awt.event.InputEvent.BUTTON1_DOWN_MASK);
        
        return self();
    }
}

// THEN Stage
public class ThenCanvasState extends Stage<ThenCanvasState> {
    DrawingView view; // Assumed injected or retrieved from the frame

    public ThenCanvasState a_rectangle_figure_should_appear_on_the_canvas() {
        // For domain specific assertions use the Assert.J library 
        org.assertj.core.api.Assertions.assertThat(view.getDrawing().getChildCount())
            .as("Canvas should have exactly 1 figure after drawing")
            .isEqualTo(1);
        return self();
    }
}
```
