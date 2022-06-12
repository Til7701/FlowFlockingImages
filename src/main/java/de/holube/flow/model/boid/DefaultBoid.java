package de.holube.flow.model.boid;

import de.holube.flow.model.Boid;
import de.holube.flow.model.Field;
import de.holube.flow.util.Vector2;
import lombok.Getter;
import lombok.Setter;


public class DefaultBoid extends Boid {

    @Getter
    @Setter
    protected float maxSpeed = 2.5F;

    public DefaultBoid(Vector2 position) {
        super(position);
    }

    @Override
    public void update(Field field) {
        position.add(velocity);
        velocity.add(acceleration);
        velocity.limit(maxSpeed);
        acceleration.multi(0);
    }

}
