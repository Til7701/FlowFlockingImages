package de.holube.flow.simulation;

import de.holube.flow.fx.MainController;
import de.holube.flow.model.Field;
import lombok.Getter;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class SimulationController {

    private static final SimulationController INSTANCE = new SimulationController();

    private SimulationController() {

    }

    public static SimulationController getInstance() {
        return INSTANCE;
    }


    private final AtomicReference<SimThread> simulationThread = new AtomicReference<>();
    private final AtomicInteger threadCount = new AtomicInteger(0);

    private final AtomicReference<MainController> controller = new AtomicReference<>();
    private final AtomicReference<Field> field = new AtomicReference<>();

    @Getter
    private volatile int targetFps = 60;
    @Getter
    private final AtomicInteger fps = new AtomicInteger(0);

    public void start() {
        createNewSimulationThread();
        simulationThread.get().start();
    }

    public void pause() {
        simulationThread.get().interrupt();
    }

    private void createNewSimulationThread() {
        SimThread newThread = new SimThread();
        newThread.setName("SimulationThread-" + threadCount.getAndIncrement());
        newThread.setDaemon(true);

        SimThread oldThread = simulationThread.get();
        while (!simulationThread.compareAndSet(oldThread, newThread)) {
            oldThread = simulationThread.get();
        }
    }

    private class SimThread extends Thread {

        private long lastUpdate = 0;

        private int targetFrameTimeMillis = 1000 / targetFps;
        private long lastFpsUpdate = 0;
        private int fpsCounter = 0;

        @Override
        public void run() {
            // TODO I don't know how to make loops :)
            while (!Thread.currentThread().isInterrupted()) {

                Field currentField = SimulationController.this.field.get();
                currentField.update();
                controller.get().update(currentField, fps.get());

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

        void setTargetFrameTimeMillis(int targetFrameTimeMillis) {
            this.targetFrameTimeMillis = targetFrameTimeMillis;
        }

        private void updateFPS() {
            long now = System.nanoTime();
            long delta = now - lastFpsUpdate;
            if (delta > 1_000_000_000) {
                fps.set(fpsCounter);
                fpsCounter = 0;
                lastFpsUpdate = now;
            }
            fpsCounter++;
        }

        private static long toMillis(long nanos) {
            return nanos / 1_000_000;
        }

    }

    public void setTargetFps(int targetFps) {
        this.targetFps = targetFps;
        simulationThread.get().setTargetFrameTimeMillis(1000 / targetFps);
    }

    public MainController getController() {
        return controller.get();
    }

    public void setController(MainController newController) {
        MainController oldController = controller.get();
        while (!controller.compareAndSet(oldController, newController)) {
            oldController = controller.get();
        }
    }

    public Field getField() {
        return field.get();
    }

    public void setField(Field newField) {
        Field oldField = field.get();
        while (!field.compareAndSet(oldField, newField)) {
            oldField = field.get();
        }
    }

}
