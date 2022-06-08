package de.holube.flow.model;

import de.holube.flow.util.Vector2;

import java.util.Collection;
import java.util.Random;

public class VectorFlockField extends FlockField {

    private static final Random random = new Random();

    private final Vector2[][] vectors;

    public VectorFlockField(int width, int height) {
        super(width, height);

        vectors = new Vector2[width][height];
        for (int i = 0; i < vectors.length; i++) {
            for (int j = 0; j < vectors[i].length; j++) {
                vectors[i][j] = new Vector2(random.nextFloat(), random.nextFloat());
                vectors[i][j].normalize();
            }
        }

        onUpdate = this::onUpdate;
    }

    @Override
    public void update() {
        super.update();

        for (Vector2[] row : vectors) {
            for (Vector2 vector : row) {
                vector.setX(random.nextBoolean() ? (-1 * random.nextFloat()) : (1 * random.nextFloat()));
                vector.setY(random.nextBoolean() ? (-1 * random.nextFloat()) : (1 * random.nextFloat()));
                vector.normalize();
            }
        }
    }

    private void onUpdate(Boid boid, Collection<Boid> boids) {
        int i = (int) (boid.getPosition().getX());
        int j = (int) (boid.getPosition().getY());
        boid.applyForce(vectors[i][j]);
    }

}
