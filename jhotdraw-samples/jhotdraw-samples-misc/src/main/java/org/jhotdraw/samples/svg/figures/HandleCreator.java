package org.jhotdraw.samples.svg.figures;

import org.jhotdraw.draw.handle.Handle;

import java.util.Collection;

public abstract class HandleCreator {
    public abstract Collection<Handle> createHandles(SVGFigure figure);
}
