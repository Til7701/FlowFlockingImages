package de.holube.flow;

import de.holube.flow.model.Field;
import lombok.Getter;
import lombok.Setter;

import java.util.Timer;
import java.util.TimerTask;

@Getter
@Setter
public class SimulationController extends Thread {

    private final Timer timer;

    private HelloController controller;
    private Field field;

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
                    field.update();
                    controller.update(field);
                }
            }
        }, 0, 1000 / 60);
    }

}
