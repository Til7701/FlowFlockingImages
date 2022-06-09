package de.holube.flow.model;

import javafx.scene.canvas.GraphicsContext;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;

public class FlockField {

    @Getter
    protected final int width;
    @Getter
    protected final int height;

    @Getter
    protected final List<Boid> boids;

    @Getter
    @Setter
    protected BiConsumer<Boid, Collection<Boid>> onUpdate;

    public FlockField(int width, int height) {
        this.width = width;
        this.height = height;
        this.boids = new ArrayList<>();
    }

    public void update() {
        for (Boid boid : boids) {
            if (onUpdate != null) {
                onUpdate.accept(boid, boids);
            }
            boid.update();
            boid.getPosition().mod(width, height);
        }
    }

    public void drawDebug(GraphicsContext gc) {

    }
}
