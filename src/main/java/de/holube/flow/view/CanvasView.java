package de.holube.flow.view;

import de.holube.flow.controller.CanvasController;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

public class CanvasView extends StackPane {

    @Getter
    private final CanvasController canvasController;

    @Getter
    private final Canvas mainCanvas;
    @Getter
    private final Canvas debugCanvas;

    private final Set<Canvas> canvases = new HashSet<>();

    private final IntegerProperty canvasWidth = new SimpleIntegerProperty(100);
    private final IntegerProperty canvasHeight = new SimpleIntegerProperty(100);
    private final DoubleProperty canvasRatio = new SimpleDoubleProperty(1.0);

    public CanvasView() {
        widthProperty().addListener((observable, oldValue, newValue) -> repositionCanvases());
        heightProperty().addListener((observable, oldValue, newValue) -> repositionCanvases());
        canvasWidth.addListener((observable, oldValue, newValue) -> {
            canvases.forEach(canvas -> canvas.setWidth(newValue.doubleValue()));
            canvasRatio.set(canvasWidth.get() / (double) canvasHeight.get());
        });
        canvasHeight.addListener((observable, oldValue, newValue) -> {
            canvases.forEach(canvas -> canvas.setHeight(newValue.doubleValue()));
            canvasRatio.set(canvasWidth.get() / (double) canvasHeight.get());
        });
        canvasRatio.addListener((observable, oldValue, newValue) -> repositionCanvases());

        mainCanvas = new Canvas();
        canvases.add(mainCanvas);
        getChildren().add(mainCanvas);
        debugCanvas = new Canvas();
        getChildren().add(debugCanvas);
        canvases.add(debugCanvas);
        repositionCanvases();

        setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        canvasController = new CanvasController(this);
    }

    /**
     * This method repositions the canvases to the size of the parent pane. It trys to fill the parent and positions the
     * canvases accordingly.
     */
    public void repositionCanvases() {
        final double width = getWidth();
        final double height = getHeight();
        final double cr = canvasRatio.get();

        double calculatedWidth;
        double calculatedHeight;
        double calculatedScaleX;
        double calculatedScaleY;

        if (width / height < cr) {
            calculatedWidth = width;
            calculatedHeight = width / cr;
        } else {
            calculatedWidth = height * cr;
            calculatedHeight = height;
        }

        calculatedScaleX = calculatedWidth / canvasWidth.get();
        calculatedScaleY = calculatedHeight / canvasHeight.get();

        canvases.forEach(canvas -> {
            canvas.setScaleX(calculatedScaleX);
            canvas.setScaleY(calculatedScaleY);
            // TODO center the canvas
            canvas.setLayoutX((calculatedWidth - canvas.getWidth()) / 2);
            canvas.setLayoutY((calculatedHeight - canvas.getHeight()) / 2);
        });

        debugCanvas.toFront();
    }


    //###########################
    // Getters and Setters
    //###########################

    public int getCanvasWidth() {
        return canvasWidth.get();
    }

    public IntegerProperty canvasWidthProperty() {
        return canvasWidth;
    }

    public void setCanvasWidth(int canvasWidth) {
        this.canvasWidth.set(canvasWidth);
    }

    public int getCanvasHeight() {
        return canvasHeight.get();
    }

    public IntegerProperty canvasHeightProperty() {
        return canvasHeight;
    }

    public void setCanvasHeight(int canvasHeight) {
        this.canvasHeight.set(canvasHeight);
    }

    public double getCanvasRatio() {
        return canvasRatio.get();
    }

    public DoubleProperty canvasRatioProperty() {
        return canvasRatio;
    }

    public void setCanvasRatio(double canvasRatio) {
        this.canvasRatio.set(canvasRatio);
    }
}
