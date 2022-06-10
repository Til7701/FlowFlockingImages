package de.holube.flow.model.field;

import de.holube.flow.model.Boid;
import de.holube.flow.model.DefaultField;
import de.holube.flow.util.OpenSimplex2S;
import de.holube.flow.util.UtilMethods;
import de.holube.flow.util.Vector2;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ScaledVectorField extends DefaultField {

    private final int scl = 20;
    private final int rows;
    private final int cols;

    private float time = 0;
    private float timeInc = 0.001F;
    private float inc = 0.1F;

    private final Vector2[][] vectors;

    public ScaledVectorField(int width, int height) {
        super(width, height);

        rows = width / scl;
        cols = height / scl;

        vectors = new Vector2[rows][cols];
        for (int i = 0; i < vectors.length; i++) {
            for (int j = 0; j < vectors[i].length; j++) {
                vectors[i][j] = new Vector2(1, 0);
            }
        }
    }

    @Override
    public void boidEarlyUpdate(Boid boid) {
        int x = (int) (boid.getPosition().getX() / scl) % (width / scl);
        int y = (int) (boid.getPosition().getY() / scl) % (height / scl);
        boid.applyForce(vectors[x][y]);
    }

    @Override
    public void lateUpdate() {
        float xOff = 0;
        for (Vector2[] row : vectors) {
            float yOff = 0;
            for (Vector2 vector : row) {
                float angle = OpenSimplex2S.noise3_ImproveXY(0, xOff, yOff, time);
                angle = UtilMethods.map(angle, 0, 1F, 0, (float) Math.PI * 2);
                vector.setAngle(angle);
                vector.normalize();
                yOff += inc;
            }
            xOff += inc;
        }
        time += timeInc;
    }

    @Override
    public void drawDebug(GraphicsContext gc, int level) {
        if (level == 1) {
            drawDebug1(gc);
        } else if (level == 2) {
            drawDebug2(gc);
        }
    }

    private void drawDebug1(GraphicsContext gc) {
        final int lineLength = scl / 2;

        for (int i = 0; i < vectors.length; i++) {
            for (int j = 0; j < vectors[i].length; j++) {
                Vector2 vector = vectors[i][j];

                int x1 = (int) ((i * scl) - vector.getX() * lineLength);
                int y1 = (int) ((j * scl) - vector.getY() * lineLength);
                int x2 = (int) ((i * scl) + vector.getX() * lineLength);
                int y2 = (int) ((j * scl) + vector.getY() * lineLength);
                gc.strokeLine(x1, y1, x2, y2);
            }
        }
    }

    private void drawDebug2(GraphicsContext gc) {
        for (int i = 0; i < vectors.length; i++) {
            for (int j = 0; j < vectors[i].length; j++) {
                gc.setFill(getColorForAngle(vectors[i][j].getAngle()));
                gc.fillRect(i * scl, j * scl, scl, scl);
            }
        }
    }

    private Color getColorForAngle(float angle) {
        final int value = (int) (angle * 255 / (Math.PI * 2));
        return Color.rgb(value, value, value);
    }

}
