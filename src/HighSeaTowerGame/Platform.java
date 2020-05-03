package HighSeaTowerGame;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class Platform extends Model2D {

    private static final DoubleProperty height = new SimpleDoubleProperty(10.0);

    private final DoubleProperty width;

    private static DoubleProperty unifiedSpeed;

    private PlatformType type;

    private static Platform lastProperties = new Platform(0.0);

    public static final double VERTICAL_SPACING = 100.0;
    private static final double MIN_WIDTH = 80.0;
    private static final double WIDTH_OFFSET = 175.0 - MIN_WIDTH;

    public Platform() {
        super();
        width = new SimpleDoubleProperty();
        genProperties();
    }

    private Platform(double init) {
        width = new SimpleDoubleProperty(MIN_WIDTH);
        posX = new SimpleDoubleProperty(MIN_WIDTH);
        posY = new SimpleDoubleProperty(GameApp.WINDOW_HEIGHT);
        unifiedSpeed = new SimpleDoubleProperty();
        type = new SolidPlatformType();
    }

    public static void reset() {
        lastProperties = new Platform(0.0);
    }

    public void genProperties() {
        width.set(MIN_WIDTH + rand.nextDouble() * WIDTH_OFFSET);

        posX.set(rand.nextDouble() * (GameApp.WINDOW_WIDTH - width.get()));
        posY.set(lastProperties.posY.get() - VERTICAL_SPACING);

        selectPlatformType();

        lastProperties = this;
    }

    private void selectPlatformType() {
        double probability = rand.nextDouble();

        if (lastProperties.type.getClass() == SolidPlatformType.class) {
            probability += 0.05;
        }

        if (probability < 0.05 && width.get() <= lastProperties.width.get()) {
            this.type = new SolidPlatformType();
        } else if (probability < 0.15) {
            this.type = new AcceleratingPlatformType();
        } else if (probability < 0.35) {
            this.type = new BouncingPlatformType();
        } else {
            this.type = new SimplePlatformType();
        }

    }

    public boolean testCollision(Jellyfish unit) {
        return type.testCollision(this, unit);
    }

    @Override
    public void update(double dt) {
        if (posY.get() > GameApp.WINDOW_HEIGHT) {
            genProperties();
        } else {
            posY.set(posY.get() + unifiedSpeed.get() * dt + posYOffset);
        }
    }

    public static double getUnifiedSpeed() {
        return unifiedSpeed.get();
    }

    public static void setUnifiedSpeed(double speed) {
        unifiedSpeed.set(speed);
    }

    public static DoubleProperty heightProperty() {
        return height;
    }

    public DoubleProperty widthProperty() {
        return width;
    }

    public PlatformType getType() {
        return type;
    }
}
