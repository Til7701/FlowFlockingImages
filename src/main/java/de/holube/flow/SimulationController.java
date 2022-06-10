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

    private volatile boolean running = true;

    private int fps = 0;
    private long lastFpsUpdate = 0;
    private int fpsCounter = 0;

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
                    controller.update(field, fps);
                }

                long currentTime = System.nanoTime();
                long delta = currentTime - lastFpsUpdate;
                if (delta > 1000000000) {
                    fps = fpsCounter;
                    fpsCounter = 0;
                    lastFpsUpdate = currentTime;
                }
                fpsCounter++;
            }
        }, 0, 1000 / 60);
    }

}
