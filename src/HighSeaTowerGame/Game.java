package HighSeaTowerGame;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

enum DIRECTION {
    LEFT, RIGHT, UP, DOWN
}

public class Game {

    private boolean started;

    private double overallPos;

    private double overallSpeed;

    private static final double OVERALL_ACCEL_Y = 2.0;

    private static boolean accelerating;

    private static final int NUM_OF_BUBBLE_GROUPS = 3;
    private static final int NUM_OF_BUBBLES_IN_A_GROUP = 5;

    private static final int NUM_OF_RECT = (int) Math.ceil(GameApp.WINDOW_HEIGHT / Platform.VERTICAL_SPACING);

    @FXML
    private Circle[][] circles = new Circle[NUM_OF_BUBBLE_GROUPS][NUM_OF_BUBBLES_IN_A_GROUP];

    private BubbleGroup[] bubbles = new BubbleGroup[NUM_OF_BUBBLE_GROUPS];

    @FXML
    private Rectangle[] rectangles = new Rectangle[NUM_OF_RECT];

    private Platform[] platforms = new Platform[NUM_OF_RECT];

    @FXML
    private ImageView imageView = new ImageView();

    private Jellyfish jellyfish;

    @FXML
    private Text score = new Text("0m");

    private int highScore;

    private List<Node> background = new LinkedList<>();

    private List<Node> playground = new LinkedList<>();

    private List<Node> foreground = new LinkedList<>();

    public Game() {
        initScore();
        initBubbles();
        initPlatforms();
        initJellyfish();
        reset();
    }

    public void start() {
        started = true;
    }

    public void update(double dt) {
        if (!started) return;

        if (jellyfish.absorbedByOcean()) {
            started = false;
            highScore = Math.max(highScore, (int) overallPos);
            score.setText("Good Game.\nPress F2 to replay.\nYour Score: " + (int) overallPos + "m\nHighest Score: " + highScore + "m");
            score.setLayoutX(0.5 * GameApp.WINDOW_WIDTH - score.getLayoutBounds().getWidth() / 2);
            return;
        }


        // Update screen scroll speed and position
        overallSpeed += OVERALL_ACCEL_Y * dt;

        if (accelerating) {
            Platform.setUnifiedSpeed(overallSpeed * 3);
        } else {
            Platform.setUnifiedSpeed(overallSpeed);
        }

        overallPos += Platform.getUnifiedSpeed() * dt;

        score.setText((int) overallPos + "m");
        score.setLayoutX(0.5 * GameApp.WINDOW_WIDTH - score.getLayoutBounds().getWidth() / 2);


        // Check if jellyfish gets higher than 75% of screen height
        double posOffset = Math.max(0.25 * GameApp.WINDOW_HEIGHT - jellyfish.posY.get(), 0.0);

        Model2D.setPosYOffset(posOffset);


        // Update models internal properties
        jellyfish.update(dt);

        boolean onFloor = false;

        setAccelerating(false);

        for (int i = 0; i < NUM_OF_RECT; ++i) {

            platforms[i].update(dt);
            rectangles[i].setFill(platforms[i].getType().color);

            onFloor |= platforms[i].testCollision(jellyfish);
            jellyfish.setOnFloor(onFloor);
        }

        // Update background
        for (BubbleGroup bubbleGroup : bubbles) {
            bubbleGroup.update(dt);
        }
    }

    public void move(DIRECTION dir) {
        jellyfish.move(dir);
    }

    public void reset() {
        started = false;
        overallSpeed = 50.0;
        overallPos = 0;


        score.setText("0m");
        score.setLayoutY(0.25 * GameApp.WINDOW_HEIGHT);
        score.setLayoutX(0.5 * GameApp.WINDOW_WIDTH - score.getLayoutBounds().getWidth() / 2);


        jellyfish.reset();

        Platform.reset();

        for (int i = 0; i < NUM_OF_RECT; ++i) {
            platforms[i].genProperties();
            rectangles[i].setFill(platforms[i].getType().color);
        }
    }

    private void initScore() {
        highScore = 0;
        score.setId("score");
        foreground.add(score);
    }

    private void initJellyfish() {
        jellyfish = new Jellyfish();
        imageView.setImage(Jellyfish.figure);
        imageView.xProperty().bind(jellyfish.posXProperty());
        imageView.yProperty().bind(jellyfish.posYProperty());
        imageView.setFitHeight(Jellyfish.HEIGHT);
        imageView.setFitWidth(Jellyfish.WIDTH);
        playground.add(imageView);
    }

    private void initBubbles() {
        for (int i = 0; i < NUM_OF_BUBBLE_GROUPS; i++) {
            bubbles[i] = new BubbleGroup();
            for (int j = 0; j < NUM_OF_BUBBLES_IN_A_GROUP; j++) {
                circles[i][j] = new Circle();

                circles[i][j].centerXProperty().bind(bubbles[i].bubblesList[j].posXProperty());
                circles[i][j].centerYProperty().bind(bubbles[i].bubblesList[j].posYProperty());
                circles[i][j].radiusProperty().bind(bubbles[i].bubblesList[j].radiusProperty());
                circles[i][j].setFill(Bubble.COLOR);
            }
            background.addAll(Arrays.asList(circles[i]));
        }
    }

    private void initPlatforms() {
        for (int i = 0; i < NUM_OF_RECT; ++i) {
            rectangles[i] = new Rectangle();
            platforms[i] = new Platform();

            rectangles[i].heightProperty().bind(Platform.heightProperty());
            rectangles[i].widthProperty().bind(platforms[i].widthProperty());
            rectangles[i].xProperty().bind(platforms[i].posXProperty());
            rectangles[i].yProperty().bind(platforms[i].posYProperty());
        }
        playground.addAll(Arrays.asList(rectangles));
    }

    public boolean hasStarted() {
        return started;
    }

    public static void setAccelerating(boolean accelerating) {
        Game.accelerating = accelerating;
    }

    public List<Node> getBackground() {
        return background;
    }

    public List<Node> getPlayground() {
        return playground;
    }

    public List<Node> getForeground() {
        return foreground;
    }

}
