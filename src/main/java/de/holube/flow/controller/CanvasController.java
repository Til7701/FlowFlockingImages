package de.holube.flow.controller;

import de.holube.flow.model.DrawingConfiguration;
import de.holube.flow.simulation.Simulation;
import de.holube.flow.view.CanvasView;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import lombok.Getter;

public class CanvasController {

    @Getter
    private final CanvasView canvasView;

    public CanvasController(CanvasView canvasView) {
        this.canvasView = canvasView;
    }

    public void update(Simulation simulation) {
        PixelWriter pw = canvasView.getMainCanvas().getGraphicsContext2D().getPixelWriter();
        simulation.getField().getBoids()
                .forEach(boid ->
                        pw.setColor((int) boid.getPosition().getX(), (int) boid.getPosition().getY(), DrawingConfiguration.getBoidColor(boid))
                );

        clearDebugCanvas();
        GraphicsContext gc = canvasView.getDebugCanvas().getGraphicsContext2D();
        simulation.getField().drawDebug(gc, 1);
    }

    public void setCanvasSize(int width, int height) {
        canvasView.setCanvasWidth(width);
        canvasView.setCanvasHeight(height);
    }

    public void clearDebugCanvas() {
        canvasView.getDebugCanvas().getGraphicsContext2D().clearRect(0, 0, canvasView.getDebugCanvas().getWidth(), canvasView.getDebugCanvas().getHeight());
    }


}
