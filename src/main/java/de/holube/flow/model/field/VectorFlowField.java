package de.holube.flow.model.field;

import de.holube.flow.model.Boid;
import de.holube.flow.model.DefaultField;
import de.holube.flow.util.OpenSimplex2S;
import de.holube.flow.util.UtilMethods;
import de.holube.flow.util.Vector2;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

public class VectorFlowField extends DefaultField {

    private float time = 0;
    private float timeInc = 0.001F;
    private float inc = 0.005F;

    private final Vector2[][] vectors;

    public VectorFlowField(int width, int height) {
        super(width, height);

        vectors = new Vector2[width][height];
        for (int i = 0; i < vectors.length; i++) {
            for (int j = 0; j < vectors[i].length; j++) {
                vectors[i][j] = new Vector2(1, 1);
            }
        }
    }

    @Override
    public void boidEarlyUpdate(Boid boid) {
        int i = (int) (boid.getPosition().getX());
        int j = (int) (boid.getPosition().getY());
        boid.applyForce(vectors[i][j]);
    }

    @Override
    public void lateUpdate() {
        float noiseRadius = 0.5f;

        float xOff = 0;
        for (Vector2[] row : vectors) {
            float yOff = 0;
            for (Vector2 vector : row) {
                //float angle = OpenSimplex2S.noise3_ImproveXY(0, xOff, yOff, time);
                float angle = OpenSimplex2S.noise4_ImproveXY_ImproveZW((long) (Math.random() * 10000), noiseRadius * Math.sin(xOff), noiseRadius * Math.cos(xOff), noiseRadius * Math.sin(yOff), noiseRadius * Math.cos(yOff));
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
        final int spacing = 25;
        final int lineLength = spacing / 5;

        for (int i = spacing; i < vectors.length; i += spacing) {
            for (int j = spacing; j < vectors[i].length; j += spacing) {
                Vector2 vector = vectors[i][j];

                int x1 = (int) (i - vector.getX() * lineLength);
                int y1 = (int) (j - vector.getY() * lineLength);
                int x2 = (int) (i + vector.getX() * lineLength);
                int y2 = (int) (j + vector.getY() * lineLength);
                gc.strokeLine(x1, y1, x2, y2);
            }
        }
    }

    private void drawDebug2(GraphicsContext gc) {
        PixelWriter pw = gc.getPixelWriter();
        for (int i = 0; i < vectors.length; i++) {
            for (int j = 0; j < vectors[i].length; j++) {
                pw.setColor(i, j, getColorForAngle(vectors[i][j].getAngle()));
            }
        }
    }

    private Color getColorForAngle(float angle) {
        final int value = (int) (angle * 255 / (Math.PI * 2));
        return Color.rgb(value, value, value);
    }

}
