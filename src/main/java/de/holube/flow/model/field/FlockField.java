package de.holube.flow.model.field;

import de.holube.flow.model.Boid;
import de.holube.flow.model.DefaultField;
import de.holube.flow.util.Vector2;

import java.util.Collection;

public class FlockField extends DefaultField {

    private static final float ALIGN_VALUE = 0.5f;
    private static final float COHESION_VALUE = 1f;
    private static final float SEPARATION_VALUE = 1.2f;

    public FlockField(int width, int height) {
        super(width, height);
    }

    @Override
    public void boidEarlyUpdate(Boid boid) {
        flock(boid, boids);
    }

    protected void flock(Boid boid, Collection<Boid> boids) {
        Vector2 alignment = align(boid, boids);
        Vector2 cohesion = cohesion(boid, boids);
        Vector2 separation = separation(boid, boids);

        alignment.multi(ALIGN_VALUE);
        cohesion.multi(COHESION_VALUE);
        separation.multi(SEPARATION_VALUE);

        boid.applyForce(alignment);
        boid.applyForce(cohesion);
        boid.applyForce(separation);
    }

    protected Vector2 align(Boid boid, Collection<Boid> boids) {
        int perceptionRadius = 50;
        Vector2 steering = new Vector2();
        int total = 0;
        for (Boid other : boids) {
            float d = Vector2.dist(boid.getPosition(), other.getPosition());
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
            float d = Vector2.dist(boid.getPosition(), other.getPosition());
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
            float d = Vector2.dist(boid.getPosition(), other.getPosition());
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
