package de.holube.flow.model;

import de.holube.flow.util.Vector2;
import lombok.Getter;

public abstract class Boid {

    @Getter
    protected final Vector2 position;
    @Getter
    protected final Vector2 velocity;
    @Getter
    protected final Vector2 acceleration;

    protected Boid(Vector2 position) {
        this.position = position;
        velocity = new Vector2(0, 0);
        acceleration = new Vector2(0, 0);
    }

    public void applyForce(Vector2 force) {
        this.acceleration.add(force);
    }

    public abstract void update(Field field);

}
