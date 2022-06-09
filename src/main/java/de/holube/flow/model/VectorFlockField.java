package de.holube.flow.model;

import de.holube.flow.util.OpenSimplex2S;
import de.holube.flow.util.UtilMethods;
import de.holube.flow.util.Vector2;

import java.util.Collection;
import java.util.Random;

public class VectorFlockField extends FlockField {

    private static final Random random = new Random();

    private float time = 0;
    private float timeInc = 0.001F;
    private float inc = 0.05F;
    private static final float noiseRadius = 0.5F;

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

        float xoff = 0;
        for (int i = 0; i < vectors.length; i++) {
            float yoff = 0;
            Vector2[] row = vectors[i];
            for (int j = 0, rowLength = row.length; j < rowLength; j++) {
                Vector2 vector = row[j];
                float angle = noise((int) xoff, (int) yoff);
                angle = UtilMethods.map(angle, 0, 1F, 0, (float) Math.PI * 2);
                vector.setAngle(angle);
                vector.normalize();
                yoff += inc;
            }
            xoff += inc;
        }
        time += timeInc;
    }

    private float noise(int x, int y) {
        return noise2(x, y);
    }

    private float noise1(int x, int y) {
        return OpenSimplex2S.noise4_ImproveXY_ImproveZW(0,
                noiseRadius * Math.sin(x), noiseRadius * Math.cos(x),
                noiseRadius * Math.sin(y), noiseRadius * Math.cos(y));
    }

    private float noise2(int x, int y) {
        return OpenSimplex2S.noise2_ImproveX(0, x, y);
    }

    private void onUpdate(Boid boid, Collection<Boid> boids) {
        int i = (int) (boid.getPosition().getX());
        int j = (int) (boid.getPosition().getY());
        boid.applyForce(vectors[i][j]);
    }

}
