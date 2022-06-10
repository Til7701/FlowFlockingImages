package de.holube.flow;

import de.holube.flow.model.Boid;
import de.holube.flow.model.FlockField;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class HelloController {

    @FXML
    private AnchorPane parent;

    @FXML
    private Canvas canvas;
    @FXML
    private Canvas debugCanvas;

    private double canvasRatio;

    public void setCanvasSize(int width, int height) {
        // TODO make this option available to the user
        canvas.setWidth(width);
        canvas.setHeight(height);
        debugCanvas.setWidth(width);
        debugCanvas.setHeight(height);
        canvasRatio = canvas.getWidth() / canvas.getHeight();
    }

    @FXML
    private void initialize() {
        canvasRatio = canvas.getWidth() / canvas.getHeight();

        parent.widthProperty().addListener((observable, oldValue, newValue) -> resizeCanvas());
        parent.heightProperty().addListener((observable, oldValue, newValue) -> resizeCanvas());
    }

    public void update(FlockField flockField) {
        PlatformExt.runAndWait(() -> {
            PixelWriter pw = canvas.getGraphicsContext2D().getPixelWriter();
            for (Boid boid : flockField.getBoids()) {
                pw.setColor((int) boid.getPosition().getX(), (int) boid.getPosition().getY(), DrawingConfiguration.getBoidColor(boid));
            }

            GraphicsContext gc = debugCanvas.getGraphicsContext2D();
            gc.setFill(Color.TRANSPARENT);
            gc.clearRect(0, 0, debugCanvas.getWidth(), debugCanvas.getHeight());
            flockField.drawDebug(gc, 2);
        });
    }

    private void resizeCanvas() {
        double calculatedWidth;
        double calculatedHeight;
        double calculatedScaleX;
        double calculatedScaleY;

        if (parent.getWidth() / parent.getHeight() < canvasRatio) {
            calculatedWidth = parent.getWidth();
            calculatedHeight = parent.getWidth() / canvasRatio;
        } else {
            calculatedWidth = parent.getHeight() * canvasRatio;
            calculatedHeight = parent.getHeight();
        }

        calculatedScaleX = calculatedWidth / canvas.getWidth();
        calculatedScaleY = calculatedHeight / canvas.getHeight();

        canvas.setScaleX(calculatedScaleX);
        canvas.setScaleY(calculatedScaleY);
        canvas.setLayoutX((calculatedWidth - canvas.getWidth()) / 2);
        canvas.setLayoutY((calculatedHeight - canvas.getHeight()) / 2);

        debugCanvas.setScaleX(calculatedScaleX);
        debugCanvas.setScaleY(calculatedScaleY);
        debugCanvas.setLayoutX((calculatedWidth - debugCanvas.getWidth()) / 2);
        debugCanvas.setLayoutY((calculatedHeight - debugCanvas.getHeight()) / 2);
    }

}