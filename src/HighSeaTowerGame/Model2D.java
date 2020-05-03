package HighSeaTowerGame;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.util.Random;

public abstract class Model2D {
    protected DoubleProperty posX, posY;

    protected DoubleProperty speedX, speedY;

    protected DoubleProperty accelX, accelY;

    protected static double posYOffset;

    protected static Random rand = new Random(System.currentTimeMillis());

    protected Model2D() {
        posX = new SimpleDoubleProperty();
        posY = new SimpleDoubleProperty();
        speedX = new SimpleDoubleProperty();
        speedY = new SimpleDoubleProperty();
        accelX = new SimpleDoubleProperty();
        accelY = new SimpleDoubleProperty();
    }

    public abstract void update(double dt);

    public static void setPosYOffset(double posYOffset) {
        Model2D.posYOffset = posYOffset;
    }

    public DoubleProperty posXProperty() {
        return posX;
    }

    public DoubleProperty posYProperty() {
        return posY;
    }

}
