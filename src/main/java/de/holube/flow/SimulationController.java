package de.holube.flow;

import de.holube.flow.model.Field;
import lombok.Getter;
import lombok.Setter;

public class SimulationController {

    private static final SimulationController INSTANCE = new SimulationController();

    private SimulationController() {

    }

    public static SimulationController getInstance() {
        return INSTANCE;
    }


    private Thread simulationThread;
    private int threadCount = 0;

    @Getter
    @Setter
    private MainController controller;
    @Getter
    @Setter
    private Field field;

    @Getter
    private volatile int targetFps = 60;
    private volatile int targetFrameTimeMillis = 1000 / targetFps;
    @Getter
    private int fps = 0;

    public void setTargetFps(int targetFps) {
        this.targetFps = targetFps;
        targetFrameTimeMillis = 1000 / targetFps;
    }

    public void start() {
        createNewSimulationThread();
        simulationThread.start();
    }

    public void pause() {
        simulationThread.interrupt();
    }

    private void createNewSimulationThread() {
        simulationThread = new SimThread();
        simulationThread.setName("SimulationThread-" + threadCount++);
        simulationThread.setDaemon(true);
    }

    private class SimThread extends Thread {

        long lastUpdate = 0;

        long lastFpsUpdate = 0;
        int fpsCounter = 0;

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {

                field.update();
                controller.update(field, fps);

                long now = System.nanoTime();
                System.out.println("FrameCount: " + fpsCounter);
                if (toMillis(now - lastUpdate) < targetFrameTimeMillis) {
                    try {
                        final long timeToSleep = targetFrameTimeMillis - toMillis(now - lastUpdate);
                        System.out.println(getName() + " Sleeping for " + timeToSleep + "ms FrameCount: " + fpsCounter);
                        Thread.sleep(timeToSleep);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                lastUpdate = now;
                updateFPS();
            }
        }

        private void updateFPS() {
            long delta = lastUpdate - lastFpsUpdate;
            if (delta > 1_000_000_000) {
                fps = fpsCounter;
                fpsCounter = 0;
                lastFpsUpdate = lastUpdate;
            }
            fpsCounter++;
        }

        private static long toMillis(long nanos) {
            return nanos / 1_000_000;
        }

    }

}
