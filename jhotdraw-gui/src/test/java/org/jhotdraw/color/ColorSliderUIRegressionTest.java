package org.jhotdraw.color;

import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.fail;
import org.jhotdraw.color.ColorSliderModel;
import org.jhotdraw.color.DefaultColorSliderModel;
import org.jhotdraw.color.HSVColorSpace;


public class ColorSliderUIRegressionTest {

    @Test
    void paintColorTrack_doesNotCallImageUpdate_andDoesNotCrash() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            // Custom slider that fails if Swing calls imageUpdate on it.
            // After our fix (drawImage observer = null), this should never be called.
            JSlider slider = new JSlider(JSlider.HORIZONTAL) {
                @Override
                public boolean imageUpdate(Image img, int infoflags, int x, int y, int w, int h) {
                    fail("imageUpdate() was called. The color track should be drawn with a null ImageObserver.");
                    return super.imageUpdate(img, infoflags, x, y, w, h);
                }
            };

            // Install JHotDraw custom slider UI
            slider.setUI((ColorSliderUI) ColorSliderUI.createUI(slider));

            // Configure like in the real chooser panels (model + client properties)
            ColorSliderModel model = new DefaultColorSliderModel(HSVColorSpace.getInstance());
            model.configureSlider(2, slider);

            // Give it a size so painting runs
            slider.setSize(300, 50);
            slider.doLayout();

            // Paint into an offscreen image (headless-friendly)
            BufferedImage img = new BufferedImage(300, 50, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = img.createGraphics();
            try {
                slider.paint(g2);
            } finally {
                g2.dispose();
            }
        });
    }
}
