package org.jhotdraw.samples.svg.figures;

import org.jhotdraw.draw.handle.BoundsOutlineHandle;
import org.jhotdraw.draw.handle.Handle;

import java.util.Collection;
import java.util.LinkedList;

public class MouseHoverHandleCreator extends HandleCreator {

    @Override
    public Collection<Handle> createHandles(SVGFigure figure) {
        LinkedList<Handle> handles = new LinkedList<>();
        handles.add(new BoundsOutlineHandle(figure, false, true));
        return handles;
    }
}
