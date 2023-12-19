package org.jhotdraw.samples.svg.figures;

import org.jhotdraw.draw.handle.Handle;
import org.jhotdraw.draw.handle.TransformHandleKit;

import java.util.Collection;
import java.util.LinkedList;

public class OneDetailLevelHandleCreator extends HandleCreator{
    @Override
    public Collection<Handle> createHandles(SVGFigure figure) {
        LinkedList<Handle> handles = new LinkedList<>();
        TransformHandleKit.addTransformHandles(figure, handles);
        return handles;
    }
}
