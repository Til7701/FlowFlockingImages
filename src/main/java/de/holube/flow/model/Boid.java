package de.holube.flow.model;

import de.holube.flow.util.Vector2;
import lombok.Getter;
import lombok.Setter;


public class Boid {


    @Getter
    private final Vector2 position;
    @Getter
    private final Vector2 velocity;
    @Getter
    private final Vector2 acceleration;

    @Getter
    @Setter
    private float maxSpeed = 2.5F;

    public Boid(Vector2 position) {
        this.position = position;
        velocity = new Vector2(0, 0);
        acceleration = new Vector2(0, 0);
    }

    void applyForce(Vector2 force) {
        this.acceleration.add(force);
    }

    void update() {
        position.add(velocity);
        velocity.add(acceleration);
        velocity.limit(maxSpeed);
        acceleration.multi(0);
    }

}
