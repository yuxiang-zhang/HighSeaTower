package HighSeaTowerGame;

import javafx.scene.image.Image;


public class Jellyfish extends Model2D {

    public static final double WIDTH = 50.0;

    public static final double HEIGHT = 50.0;

    public static final Image figure = new Image("/res/jellyfish.png");

    private boolean onFloor;

    public double lastPosX, lastPosY;

    public Jellyfish() {
        super();
    }

    public void reset() {
        posX.set(GameApp.WINDOW_WIDTH / 2.0);
        posY.set(GameApp.WINDOW_HEIGHT - HEIGHT);
        lastPosY = posY.get();
        accelY.set(1200.0);
        onFloor = true;
    }

    public void move(DIRECTION dir) {
        if (dir == DIRECTION.UP) {
            if (onFloor) {
                speedY.set(-600.0);
                accelX.set(0.0);
                onFloor = false;
            }
        }
        if (dir == DIRECTION.LEFT) {
            if (speedX.get() > -60.0) {
                speedX.set(-60.0);
            }
            accelX.set(-1200.0);

        } else if (dir == DIRECTION.RIGHT) {
            if (speedX.get() < 60.0) {
                speedX.set(60.0);
            }
            accelX.set(1200.0);

        }
    }

    @Override
    public void update(double dt) {
        if ((accelX.get() > 0 && speedX.get() < 0) || (accelX.get() < 0 && speedX.get() > 0)) {
            speedX.set(0.0);
        }
        speedX.set(speedX.get() + accelX.get() * dt);
        speedY.set(speedY.get() + accelY.get() * dt);

        lastPosX = posX.get();
        lastPosY = posY.get();

        posX.set(posX.get() + speedX.get() * dt);
        posY.set(posY.get() + speedY.get() * dt + posYOffset);

        if (posX.get() + WIDTH > GameApp.WINDOW_WIDTH || posX.get() < 0.0) {
            speedX.set(-speedX.get());
        }

        posX.set(Utility.clamp(0.0, posX.get(), GameApp.WINDOW_WIDTH - WIDTH));
        posY.set(Utility.clamp(0.0, posY.get(), GameApp.WINDOW_HEIGHT + HEIGHT));

        speedX.set(speedX.get() * 0.99);
        accelX.set(accelX.get() / 1.05);

    }

    public boolean absorbedByOcean() {
        return posY.get() > GameApp.WINDOW_HEIGHT;
    }

    public void setOnFloor(boolean onFloor) {
        this.onFloor = onFloor;
    }

    public boolean isOnFloor() {
        return onFloor;
    }

}
