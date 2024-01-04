package org.jhotdraw.samples.svg.figures;

public enum PROPERTY {
    /**
     * Identifies the {@code arcWidth} JavaBeans property.
     */
    ARC_WIDTH("arcWidth"),

    /**
     * Identifies the {@code arcHeight} JavaBeans property.
     */
    ARC_HEIGHT("arcHeight");

    private final String name;
    PROPERTY(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
