package de.holube.flow;

import de.holube.flow.model.Field;
import de.holube.flow.model.boid.DefaultBoid;
import de.holube.flow.model.boid.FlockBoid;
import de.holube.flow.model.field.ScaledVectorField;
import de.holube.flow.simulation.Simulation;
import de.holube.flow.util.Vector2;
import de.holube.flow.view.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Random;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        final int width = 1920;
        final int height = 1080;

        MainView mainView = new MainView();
        Scene scene = new Scene(mainView, width + 100.0, height + 100.0);
        primaryStage.setTitle("FlowFlockingImages");
        primaryStage.setScene(scene);
        primaryStage.show();

        mainView.getCanvasView().getCanvasController().setCanvasSize(width, height);

        Random random = new Random();

        Field field = new ScaledVectorField(width, height);
        for (int i = 0; i < 100; i++) {
            DefaultBoid boid = new FlockBoid(new Vector2(random.nextInt(width), random.nextInt(height)));
            boid.getVelocity().setX(random.nextFloat());
            boid.getVelocity().setY(random.nextFloat());
            field.getBoids().add(boid);
        }

        Simulation simulation = Simulation.getInstance();
        simulation.setController(mainView.getMainController());
        simulation.setField(field);
        simulation.start();
    }

    public static void main(String[] args) {
        launch();
    }
}