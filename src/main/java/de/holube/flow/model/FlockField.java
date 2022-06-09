package de.holube.flow.model;

import de.holube.flow.util.UtilMethods;
import de.holube.flow.util.Vector2;
import javafx.scene.canvas.GraphicsContext;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;

public class FlockField {

    private static final float alignValue = 0.5f;
    private static final float cohesionValue = 1f;
    private static final float seperationValue = 1.2f;

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
            flock(boid, boids);
            boid.update();
            boid.getPosition().mod(width, height);
        }
    }

    public void drawDebug(GraphicsContext gc) {

    }

    protected void flock(Boid boid, Collection<Boid> boids) {
        Vector2 alignment = align(boid, boids);
        Vector2 cohesion = cohesion(boid, boids);
        Vector2 separation = separation(boid, boids);

        alignment.multi(alignValue);
        cohesion.multi(cohesionValue);
        separation.multi(seperationValue);

        boid.applyForce(alignment);
        boid.applyForce(cohesion);
        boid.applyForce(separation);
    }

    protected Vector2 align(Boid boid, Collection<Boid> boids) {
        int perceptionRadius = 50;
        Vector2 steering = new Vector2();
        int total = 0;
        for (Boid other : boids) {
            float d = UtilMethods.dist(boid.getPosition(), other.getPosition());
            if (other != boid && d < perceptionRadius) {
                steering.add(other.getVelocity());
                total++;
            }
        }
        if (total > 0) {
            steering.div(total);
            steering.setMag(boid.getMaxSpeed());
            steering.sub(boid.getVelocity());
            steering.limit(boid.getMaxForce());
        }
        return steering;
    }

    protected Vector2 separation(Boid boid, Collection<Boid> boids) {
        int perceptionRadius = 50;
        Vector2 steering = new Vector2();
        int total = 0;
        for (Boid other : boids) {
            float d = UtilMethods.dist(boid.getPosition(), other.getPosition());
            if (other != boid && d < perceptionRadius) {
                Vector2 diff = Vector2.sub(boid.getPosition(), other.getPosition());
                diff.div(d * d);
                steering.add(diff);
                total++;
            }
        }
        if (total > 0) {
            steering.div(total);
            steering.setMag(boid.getMaxSpeed());
            steering.sub(boid.getVelocity());
            steering.limit(boid.getMaxForce());
        }
        return steering;
    }

    protected Vector2 cohesion(Boid boid, Collection<Boid> boids) {
        int perceptionRadius = 100;
        Vector2 steering = new Vector2();
        int total = 0;
        for (Boid other : boids) {
            float d = UtilMethods.dist(boid.getPosition(), other.getPosition());
            if (other != boid && d < perceptionRadius) {
                steering.add(other.getPosition());
                total++;
            }
        }
        if (total > 0) {
            steering.div(total);
            steering.sub(boid.getPosition());
            steering.setMag(boid.getMaxSpeed());
            steering.sub(boid.getVelocity());
            steering.limit(boid.getMaxForce());
        }
        return steering;
    }
}
