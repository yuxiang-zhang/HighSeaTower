package HighSeaTowerGame;

import javafx.scene.paint.Color;

public abstract class PlatformType {
    public final Color color;

    public final double cumulativeProbability;

    protected PlatformType(double cumulativeProbability, Color color) {
        this.cumulativeProbability = cumulativeProbability;
        this.color = color;
    }

    protected final boolean intersectsX(Platform surface, Jellyfish unit) {

        boolean unitLastOnRightOfSurfaceLeftBound = unit.lastPosX + Jellyfish.WIDTH > surface.posX.get();
        boolean unitLastOnLeftOfSurfaceRightBound = unit.lastPosX < surface.posX.get() + surface.widthProperty().get();

        boolean unitOnRightOfSurfaceLeftBound = unit.posX.get() + Jellyfish.WIDTH > surface.posX.get();
        boolean unitOnLeftOfSurfaceRightBound = unit.posX.get() < surface.posX.get() + surface.widthProperty().get();

        return unitLastOnRightOfSurfaceLeftBound && unitLastOnLeftOfSurfaceRightBound
                && unitOnRightOfSurfaceLeftBound && unitOnLeftOfSurfaceRightBound;
    }

    protected final boolean intersectsYFromAbove(Platform surface, Jellyfish unit) {
        boolean unitTouchesSurfaceFromAbove = Math.abs(unit.posY.get() + Jellyfish.HEIGHT - surface.posY.get()) < Platform.heightProperty().get();

        boolean unitLastAboveSurface = unit.lastPosY + Jellyfish.HEIGHT < surface.posY.get();
        boolean unitNowBelowSurface = unit.posY.get() > surface.posY.get();

        return (unitLastAboveSurface && unitNowBelowSurface) || unitTouchesSurfaceFromAbove;
    }

    protected final boolean intersectsYFromBelow(Platform surface, Jellyfish unit) {
        boolean unitTouchesSurfaceFromBelow = Math.abs(unit.posY.get() - surface.posY.get()) < Platform.heightProperty().get();

        boolean unitLastBelowSurface = unit.lastPosY + Jellyfish.HEIGHT > surface.posY.get();
        boolean unitNowAboveSurface = unit.posY.get() < surface.posY.get();

        return (unitLastBelowSurface && unitNowAboveSurface) || unitTouchesSurfaceFromBelow;
    }

    public abstract boolean testCollision(Platform surface, Jellyfish unit);
}

class SimplePlatformType extends PlatformType {
    public SimplePlatformType() {
        super(1.00, Color.rgb(230, 134, 58));
    }

    @Override
    public boolean testCollision(Platform surface, Jellyfish unit) {

        if (intersectsX(surface, unit) && intersectsYFromAbove(surface, unit) && unit.speedY.get() >= 0) {

            unit.setOnFloor(true);
            unit.posY.set(surface.posY.get() - Jellyfish.HEIGHT);
            unit.speedY.set(0.0);
            return true;

        }

        return false;
    }
}

class BouncingPlatformType extends PlatformType {
    public BouncingPlatformType() {
        super(0.35, Color.rgb(144, 238, 144));
    }

    @Override
    public boolean testCollision(Platform surface, Jellyfish unit) {

        if (intersectsX(surface, unit) && intersectsYFromAbove(surface, unit) && unit.speedY.get() >= 0) {

            unit.posY.set(surface.posY.get() - Jellyfish.HEIGHT);
            unit.speedY.set(Math.min(-100, -1.5 * unit.speedY.get()));

        }

        return false;
    }
}

class AcceleratingPlatformType extends PlatformType {
    public AcceleratingPlatformType() {
        super(0.15, Color.rgb(230, 221, 58));
    }

    @Override
    public boolean testCollision(Platform surface, Jellyfish unit) {

        if (intersectsX(surface, unit) && intersectsYFromAbove(surface, unit) && unit.speedY.get() >= 0) {

            unit.setOnFloor(true);
            unit.posY.set(surface.posY.get() - Jellyfish.HEIGHT);
            unit.speedY.set(0.0);

            Game.setAccelerating(true);

            return true;

        }

        return false;
    }
}

class SolidPlatformType extends PlatformType {
    public SolidPlatformType() {
        super(0.05, Color.rgb(184, 15, 36));
    }

    @Override
    public boolean testCollision(Platform surface, Jellyfish unit) {

        if (intersectsX(surface, unit)) {

            if (intersectsYFromBelow(surface, unit)) {

                unit.posY.set(surface.posY.get() + Platform.heightProperty().get());
                unit.speedY.set(600.0);

            } else if (intersectsYFromAbove(surface, unit)) {

                unit.setOnFloor(true);
                unit.posY.set(surface.posY.get() - Jellyfish.HEIGHT);
                unit.speedY.set(Math.min(0.0, unit.speedY.get()));
                return true;

            }
        }

        return false;
    }
}
