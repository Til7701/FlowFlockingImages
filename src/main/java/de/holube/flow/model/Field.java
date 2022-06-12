package de.holube.flow.model;

import javafx.scene.canvas.GraphicsContext;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public abstract class Field {

    @Getter
    protected final int width;
    @Getter
    protected final int height;

    @Getter
    protected final List<Boid> boids;

    protected Field(int width, int height) {
        this.width = width;
        this.height = height;
        this.boids = new ArrayList<>();
    }

    public void update() {
        earlyUpdate();
        for (Boid boid : boids) {
            boidEarlyUpdate(boid);
            boid.update(this);
            boidLateUpdate(boid);
        }
        lateUpdate();
    }

    protected abstract void earlyUpdate();

    protected abstract void boidEarlyUpdate(Boid boid);

    protected abstract void boidLateUpdate(Boid boid);

    protected abstract void lateUpdate();

    public abstract void drawDebug(GraphicsContext gc, int level);
}
