package de.holube.flow.simulation;

import de.holube.flow.controller.MainController;
import de.holube.flow.model.Field;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

// TODO redesign to reduce volatile accesses
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Simulation {

    private static final Simulation INSTANCE = new Simulation();

    public static Simulation getInstance() {
        return INSTANCE;
    }

    private final AtomicReference<SimThread> simulationThread = new AtomicReference<>();
    private final AtomicReference<SimulationSettings> simulationSettings = new AtomicReference<>(new SimulationSettings());
    private final AtomicReference<SimulationStatistics> simulationStatistics = new AtomicReference<>(new SimulationStatistics());

    private final AtomicReference<Field> field = new AtomicReference<>();
    private final AtomicReference<MainController> controller = new AtomicReference<>();

    public void start() {
        stop();
        SimThread newThread = new SimThread(this);
        simulationThread.set(newThread);
        newThread.start();
    }

    public void stop() {
        SimThread thread = simulationThread.get();
        if (thread != null)
            thread.interrupt();
    }

    private class SimThread extends Thread {

        private static final AtomicInteger threadCount = new AtomicInteger(0);

        private final Simulation simulation;

        private long lastUpdate = 0;
        private long lastFpsUpdate = 0;
        private int fpsCounter = 0;

        public SimThread(Simulation simulation) {
            this.simulation = simulation;

            setDaemon(true);
            setName("SimulationThread-" + threadCount.getAndIncrement());
        }

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                update();
                ensureFPS();
                updateFPS();
            }
        }

        /**
         * Updates the simulation.
         */
        private void update() {
            Field currentField = field.get();
            currentField.update();
            controller.get().update(simulation);
        }

        /**
         * Ensures that the simulation runs at the target FPS. If the simulation is running faster than the target FPS,
         * it will sleep for the difference. If the simulation is running slower than the target FPS, it will not sleep.
         * TODO: make this accurate (currently it is not at all)
         */
        private void ensureFPS() {
            long targetFrameTimeMillis = (long) (1000 / simulationSettings.get().getTargetFPS());
            long now = System.nanoTime();
            if (toMillis(now - lastUpdate) < targetFrameTimeMillis) {
                try {
                    final long timeToSleep = targetFrameTimeMillis - toMillis(now - lastUpdate);
                    Thread.sleep(timeToSleep);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            lastUpdate = now;
        }

        private void updateFPS() {
            long now = System.nanoTime();
            long delta = now - lastFpsUpdate;
            if (delta > 1_000_000_000) {
                SimulationStatistics statistics = simulationStatistics.get();
                statistics.setCurrentFPS(fpsCounter);
                simulationStatistics.compareAndSet(statistics, statistics);
                fpsCounter = 0;
                lastFpsUpdate = now;
            }
            fpsCounter++;
        }

        private static long toMillis(long nanos) {
            return nanos / 1_000_000;
        }

    }

    //###########################
    // Getters and Setters
    //###########################

    public MainController getController() {
        return controller.get();
    }

    public void setController(MainController newController) {
        controller.set(newController);
    }

    public Field getField() {
        return field.get();
    }

    public void setField(Field newField) {
        field.set(newField);
    }

}
