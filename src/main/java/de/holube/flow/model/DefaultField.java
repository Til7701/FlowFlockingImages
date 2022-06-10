package de.holube.flow.model;

import javafx.scene.canvas.GraphicsContext;

public class DefaultField extends Field {

    protected DefaultField(int width, int height) {
        super(width, height);
    }

    @Override
    protected void earlyUpdate() {
        // do nothing
    }

    @Override
    protected void boidEarlyUpdate(Boid boid) {
        // do nothing
    }

    @Override
    protected void boidLateUpdate(Boid boid) {
        boid.getPosition().mod(width, height);
    }

    @Override
    protected void lateUpdate() {
        // do nothing
    }

    @Override
    public void drawDebug(GraphicsContext gc, int level) {
        // do nothing
    }
}
