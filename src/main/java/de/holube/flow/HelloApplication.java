package de.holube.flow;

import de.holube.flow.model.Boid;
import de.holube.flow.model.Field;
import de.holube.flow.model.field.VectorFlowField;
import de.holube.flow.util.Vector2;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Random;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        final int width = 1024;
        final int height = 720;

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), width, height);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
        if (fxmlLoader.getController() instanceof HelloController c)
            c.setCanvasSize(width, height);

        Random random = new Random();

        Field field = new VectorFlowField(width, height);
        for (int i = 0; i < 100; i++) {
            Boid boid = new Boid(new Vector2(random.nextInt(width), random.nextInt(height)));
            boid.getVelocity().setX(random.nextFloat());
            boid.getVelocity().setY(random.nextFloat());
            field.getBoids().add(boid);
        }

        SimulationController simulationController = new SimulationController();
        simulationController.setController(fxmlLoader.getController());
        simulationController.setField(field);
        simulationController.start();
    }

    public static void main(String[] args) {
        launch();
    }
}