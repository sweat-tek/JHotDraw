package org.jhotdraw.samples.svg.figures;

import org.junit.Test;

public class TestRunner extends ChangeTextTest<SVGTextFigure> {

    @Test
    public void moveTextTest(){
        super.moveFigure(new SVGTextFigure());
    }
}
