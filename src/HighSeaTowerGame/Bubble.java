package HighSeaTowerGame;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.paint.Color;

import java.util.Random;

public class Bubble extends Model2D {
    static final Color COLOR = new Color(0, 0, 1, 0.4);

    private DoubleProperty radius;

    public Bubble() {
        super();
        radius = new SimpleDoubleProperty();
    }

    @Override
    public void update(double dt) {
        posY.set(posY.get() - speedY.get() * dt);

    }

    DoubleProperty radiusProperty() {
        return radius;
    }


}

class BubbleGroup {

    public Bubble[] bubblesList;

    double age;

    private static final double LIFETIME = 3.0;

    private static final int GROUP_SIZE = 5;

    private static final double POS_X_DISTANCE = 20.0;

    private static final double MIN_RADIUS = 10.0;

    private static final double RADIUS_OFFSET = 30.0;

    private static final double MIN_SPEED = 350.0;

    private static final double SPEED_OFFSET = 100.0;

    private static Random rand = new Random(System.currentTimeMillis());

    public BubbleGroup() {
        bubblesList = new Bubble[GROUP_SIZE];
        age = rand.nextDouble() * LIFETIME;
        for (int i = 0; i < GROUP_SIZE; i++) {
            bubblesList[i] = new Bubble();
        }
    }

    public void update(double dt) {
        if (dt > 0.05) {
            return;
        }

        age += dt;

        for (Bubble b : bubblesList) {
            b.update(dt);
        }

        if (age > LIFETIME) {
            age = 0.0;

            double basePosX = rand.nextDouble() * (GameApp.WINDOW_WIDTH + 1);

            for (Bubble b : bubblesList) {
                b.radiusProperty().set(MIN_RADIUS + rand.nextDouble() * (RADIUS_OFFSET + 1));

                if (rand.nextInt(5) > 1) {
                    b.speedY.set(MIN_SPEED + rand.nextDouble() * (SPEED_OFFSET + 1));
                } else {
                    b.speedY.set(0.0);
                }

                double clampedPosX = Utility.clamp(b.radiusProperty().get(),
                        basePosX - POS_X_DISTANCE + rand.nextDouble() * POS_X_DISTANCE * 2,
                        GameApp.WINDOW_WIDTH - b.radiusProperty().get());
                b.posX.set(clampedPosX);

                b.posY.set(GameApp.WINDOW_HEIGHT + b.radiusProperty().get() * 2);

            }

        }
    }
}