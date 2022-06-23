package de.holube.flow.model.boid;

import de.holube.flow.model.Boid;
import de.holube.flow.model.Field;
import de.holube.flow.util.Vector2;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

public class FlockBoid extends DefaultBoid {

    private static final float ALIGN_VALUE = 0.5f;
    private static final float COHESION_VALUE = 1f;
    private static final float SEPARATION_VALUE = 1.3f;

    public FlockBoid(Vector2 position) {
        super(position);
    }

    @Getter
    @Setter
    protected float maxForce = 1F;

    @Override
    public void update(Field field) {
        flock(field.getBoids());
        acceleration.limit(maxForce);
        super.update(field);
    }

    protected void flock(Collection<Boid> boids) {
        Vector2 alignment = align(boids);
        Vector2 cohesion = cohesion(boids);
        Vector2 separation = separation(boids);

        alignment.multi(ALIGN_VALUE);
        cohesion.multi(COHESION_VALUE);
        separation.multi(SEPARATION_VALUE);

        applyForce(alignment);
        applyForce(cohesion);
        applyForce(separation);
    }

    protected Vector2 align(Collection<Boid> boids) {
        int perceptionRadius = 50;
        Vector2 steering = new Vector2();
        int total = 0;
        for (Boid other : boids) {
            float d = Vector2.dist(getPosition(), other.getPosition());
            if (this != other && d < perceptionRadius) {
                steering.add(other.getVelocity());
                total++;
            }
        }
        if (total > 0) {
            steering.div(total);
            steering.setMag(getMaxSpeed());
            steering.sub(getVelocity());
            steering.limit(getMaxForce());
        }
        return steering;
    }

    protected Vector2 separation(Collection<Boid> boids) {
        int perceptionRadius = 50;
        Vector2 steering = new Vector2();
        int total = 0;
        for (Boid other : boids) {
            float d = Vector2.dist(getPosition(), other.getPosition());
            if (this != other && d < perceptionRadius) {
                Vector2 diff = Vector2.sub(getPosition(), other.getPosition());
                diff.div(d * d);
                steering.add(diff);
                total++;
            }
        }
        if (total > 0) {
            steering.div(total);
            steering.setMag(getMaxSpeed());
            steering.sub(getVelocity());
            steering.limit(getMaxForce());
        }
        return steering;
    }

    protected Vector2 cohesion(Collection<Boid> boids) {
        int perceptionRadius = 100;
        Vector2 steering = new Vector2();
        int total = 0;
        for (Boid other : boids) {
            float d = Vector2.dist(getPosition(), other.getPosition());
            if (this != other && d < perceptionRadius) {
                steering.add(other.getPosition());
                total++;
            }
        }
        if (total > 0) {
            steering.div(total);
            steering.sub(getPosition());
            steering.setMag(getMaxSpeed());
            steering.sub(getVelocity());
            steering.limit(getMaxForce());
        }
        return steering;
    }

}
