package org.jhotdraw.undo;

public enum LabelPath {
    UNDO_TEXT("edit.undo.text"),
    REDO_TEXT("edit.redo.text"),
    JHOTDRAW_UNDO_LABELS("org.jhotdraw.undo.Labels");

    private String path;
    LabelPath(String path) {
        this.path = path;
    }
    public String getPath(){
        return path;
    }
}

