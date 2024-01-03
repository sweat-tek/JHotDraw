package org.jhotdraw.samples.svg;
import org.jhotdraw.samples.svg.jgivenstages.GivenImage;
import org.jhotdraw.samples.svg.jgivenstages.ThenImage;
import org.jhotdraw.samples.svg.jgivenstages.WhenImage;
import org.junit.Test;
import com.tngtech.jgiven.junit.ScenarioTest;

public class ImageFeatureBehaviourTest extends ScenarioTest<GivenImage, WhenImage, ThenImage>{

    @Test
    public void drawImage() {
        given().anEmptyDrawing();
        when().anImageIsDrawn();
        then().theDrawingCointansTheDrawnImage();
    }

    @Test
    public void resizeImage() {
        given().anEmptyDrawing()
                .and().anImageIsAdded();
        when().theImageIsResized();
        then().theImageHasTheNewSize();
    }

}
