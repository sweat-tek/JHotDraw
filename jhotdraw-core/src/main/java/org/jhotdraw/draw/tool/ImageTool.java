/*
 * @(#)ImageTool.java
 *
 * Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.draw.tool;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutionException;
import javax.swing.*;
import org.jhotdraw.draw.AttributeKey;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.figure.ImageHolderFigure;

/**
 * A tool to create new figures that implement the ImageHolderFigure
 * interface, such as ImageFigure. The figure to be created is specified by a
 * prototype.
 * <p>
 * Immediately, after the ImageTool has been activated, it opens a JFileChooser,
 * letting the user specify an image file. The the user then performs
 * the following mouse gesture:
 * <ol>
 * <li>Press the mouse button and drag the mouse over the DrawingView.
 * This defines the bounds of the created figure.</li>
 * </ol>
 *
 * <hr>
 * <b>Design Patterns</b>
 *
 * <p>
 * <em>Prototype</em><br>
 * The {@code ImageTool} creates new figures by cloning a prototype
 * {@code ImageHolderFigure} object.<br>
 * Prototype: {@link ImageHolderFigure}; Client: {@link ImageTool}.
 * <hr>
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class ImageTool extends CreationTool {

    private static final long serialVersionUID = 1L;
    protected FileDialog fileDialog;
    protected JFileChooser fileChooser;
    protected boolean useFileDialog;

    /**
     * Creates a new instance.
     */
    public ImageTool(ImageHolderFigure prototype) {
        super(prototype);
    }

    /**
     * Creates a new instance.
     */
    public ImageTool(ImageHolderFigure prototype, Map<AttributeKey<?>, Object> attributes) {
        super(prototype, attributes);
    }

    public void setUseFileDialog(boolean newValue) {
        useFileDialog = newValue;
        if (useFileDialog) {
            fileChooser = null;
        } else {
            fileDialog = null;
        }
    }

    public boolean isUseFileDialog() {
        return useFileDialog;
    }

    @Override
    public void activate(DrawingEditor editor) {
        super.activate(editor);
        final DrawingView v = getView();
        if (v == null) {
            return;
        }
        
        File file = selectImageFile(v);
        if (file == null) {
            handleNoFileSelected();
            return;
        }
        
        loadImageAsync(file, v);
    }

    /**
     * Prompts the user to select an image file using either native FileDialog
     * or Swing JFileChooser depending on configuration.
     * 
     * @param v the DrawingView component used as parent for dialogs
     * @return the selected File, or null if the user cancelled
     */
    private File selectImageFile(DrawingView v) {
        return useFileDialog ? selectImageFileWithFileDialog() : selectImageFileWithChooser(v);
    }

    /**
     * Shows native FileDialog for image selection.
     * 
     * @return the selected File, or null if cancelled
     */
    private File selectImageFileWithFileDialog() {
        getFileDialog().setVisible(true);
        if (getFileDialog().getFile() == null) {
            return null;
        }
        return new File(getFileDialog().getDirectory(), getFileDialog().getFile());
    }

    /**
     * Shows Swing JFileChooser for image selection.
     * 
     * @param v the DrawingView component used as parent
     * @return the selected File, or null if cancelled
     */
    private File selectImageFileWithChooser(DrawingView v) {
        int result = getFileChooser().showOpenDialog(v.getComponent());
        if (result == JFileChooser.APPROVE_OPTION) {
            return getFileChooser().getSelectedFile();
        }
        return null;
    }

    /**
     * Handles the case when no file was selected by the user.
     */
    private void handleNoFileSelected() {
        if (isToolDoneAfterCreation()) {
            fireToolDone();
        }
    }

    /**
     * Initiates asynchronous image loading in a background thread.
     * 
     * @param file the image file to load
     * @param v the DrawingView for error message display
     */
    private void loadImageAsync(final File file, final DrawingView v) {
        final ImageHolderFigure loaderFigure = ((ImageHolderFigure) prototype.clone());
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                loaderFigure.loadImage(file);
                return null;
            }

            @Override
            protected void done() {
                try {
                    // This will throw an ExecutionException if something went wrong in doInBackground
                    get();
                    applyLoadedImage(loaderFigure);
                } catch (ExecutionException ex) {
                    handleExecutionError(ex, v);
                } catch (InterruptedException ex) {
                    handleInterruptionError(ex, v);
                } catch (IOException ex) {
                    handleIOError(ex, v);
                }
            }
        }.execute();
    }

    /**
     * Handles errors during image loading execution.
     * 
     * @param ex the ExecutionException that occurred
     * @param v the DrawingView for error message display
     */
    private void handleExecutionError(ExecutionException ex, DrawingView v) {
        showErrorDialog(v, ex);
    }

    /**
     * Handles thread interruption during image loading.
     * 
     * @param ex the InterruptedException that occurred
     * @param v the DrawingView for error message display
     */
    private void handleInterruptionError(InterruptedException ex, DrawingView v) {
        showErrorDialog(v, ex);
        if (createdFigure != null) {
            getDrawing().remove(createdFigure);
        }
        fireToolDone();
    }

    /**
     * Handles I/O errors during image setting.
     * 
     * @param ex the IOException that occurred
     * @param v the DrawingView for error message display
     */
    private void handleIOError(IOException ex, DrawingView v) {
        showErrorDialog(v, ex);
        if (createdFigure != null) {
            getDrawing().remove(createdFigure);
        }
        fireToolDone();
    }

    /**
     * Applies the loaded image data to the appropriate figure (prototype or created figure).
     * 
     * @param loaderFigure the figure containing the loaded image
     * @throws IOException if setting the image fails
     */
    private void applyLoadedImage(ImageHolderFigure loaderFigure) throws IOException {
        ImageHolderFigure targetFigure = (createdFigure == null) 
            ? (ImageHolderFigure) prototype 
            : (ImageHolderFigure) createdFigure;
        targetFigure.setImage(loaderFigure.getImageData(), loaderFigure.getBufferedImage());
    }

    /**
     * Displays an error dialog to the user.
     * 
     * @param v the DrawingView for dialog parent
     * @param ex the exception to display
     */
    private void showErrorDialog(DrawingView v, Exception ex) {
        JOptionPane.showMessageDialog(v.getComponent(),
                ex.getMessage(),
                null,
                JOptionPane.ERROR_MESSAGE);
    }

    private JFileChooser getFileChooser() {
        if (fileChooser == null) {
            fileChooser = new JFileChooser();
        }
        return fileChooser;
    }

    private FileDialog getFileDialog() {
        if (fileDialog == null) {
            fileDialog = new FileDialog(new Frame());
        }
        return fileDialog;
    }
}
