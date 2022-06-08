package de.holube.flow;

import de.holube.flow.model.Boid;
import de.holube.flow.model.FlockField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.PixelWriter;

import java.util.concurrent.Semaphore;

public class HelloController {

    @FXML
    private Canvas canvas;

    public void update(FlockField flockField) {
        runAndWait(() -> {
            PixelWriter pw = canvas.getGraphicsContext2D().getPixelWriter();
            for (Boid boid : flockField.getBoids()) {
                pw.setColor((int) boid.getPosition().getX(), (int) boid.getPosition().getY(), DrawingConfiguration.getBoidColor(boid));
            }
        });
    }

    public static void runAndWait(Runnable action) {
        if (action == null) {
            throw new NullPointerException("action");
        }

        if (Platform.isFxApplicationThread()) {
            action.run();
            return;
        }

        final Semaphore lock = new Semaphore(0);
        Platform.runLater(() -> {
            try {
                action.run();
            } finally {
                lock.release();
            }
        });
        lock.acquireUninterruptibly();
    }
}