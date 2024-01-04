package org.jhotdraw.samples.svg.figures;

import org.jhotdraw.draw.handle.Handle;
import org.jhotdraw.draw.handle.ResizeHandleKit;

import java.util.Collection;
import java.util.LinkedList;

public class ZeroDetailLevelHandleCreator extends HandleCreator{
    @Override
    public Collection<Handle> createHandles(SVGFigure figure) {
        LinkedList<Handle> handles = new LinkedList<>();
        ResizeHandleKit.addResizeHandles(figure, handles);
        handles.add(new SVGRectRadiusHandle(figure));
        handles.add(new LinkHandle(figure));
        return handles;
    }
}
