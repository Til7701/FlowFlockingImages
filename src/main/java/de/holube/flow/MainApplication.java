package de.holube.flow;

import de.holube.flow.fx.MainController;
import de.holube.flow.model.Field;
import de.holube.flow.model.boid.DefaultBoid;
import de.holube.flow.model.boid.FlockBoid;
import de.holube.flow.model.field.ScaledVectorField;
import de.holube.flow.simulation.SimulationController;
import de.holube.flow.util.Vector2;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Random;

public class MainApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        final int width = 1920;
        final int height = 1080;

        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("mainView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), width, height);
        stage.setTitle("Have Fun!");
        stage.setScene(scene);
        stage.show();
        if (fxmlLoader.getController() instanceof MainController c)
            c.setCanvasSize(width, height);

        Random random = new Random();

        Field field = new ScaledVectorField(width, height);
        for (int i = 0; i < 100; i++) {
            DefaultBoid boid = new FlockBoid(new Vector2(random.nextInt(width), random.nextInt(height)));
            boid.getVelocity().setX(random.nextFloat());
            boid.getVelocity().setY(random.nextFloat());
            field.getBoids().add(boid);
        }

        SimulationController simulationController = SimulationController.getInstance();
        simulationController.setController(fxmlLoader.getController());
        simulationController.setField(field);
        simulationController.start();
    }

    public static void main(String[] args) {
        launch();
    }
}