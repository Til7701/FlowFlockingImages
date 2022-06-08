package de.holube.flow;

import de.holube.flow.model.FlockField;
import lombok.Getter;
import lombok.Setter;

import java.util.Timer;
import java.util.TimerTask;

@Getter
@Setter
public class SimulationController extends Thread {

    private final Timer timer;

    private HelloController controller;
    private FlockField flockField;

    volatile boolean running = true;

    public SimulationController() {
        timer = new Timer(true);
        setDaemon(true);
    }

    @Override
    public void run() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (running) {
                    flockField.update();
                    controller.update(flockField);
                }
            }
        }, 0, 1000 / 60);
    }

}
