package HighSeaTowerGame;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TimelineBuilder;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private Pane area;

    @FXML
    private Group background, playground;

    @FXML
    private Group foreground;

    Game game = new Game();

    public static double lastFrameTime = 0.0;

    // THE TIMELINE, RUNS EVERY 10MS
    private final Timeline heartbeat = TimelineBuilder.create()
            .keyFrames(new KeyFrame(new Duration(10.0), evt -> {
                game.update((System.currentTimeMillis() - lastFrameTime) / 1000);
                lastFrameTime = System.currentTimeMillis();
            }))
            .cycleCount(Timeline.INDEFINITE).build();

    private void bindShapesToSceneGraph() {
        background.getChildren().addAll(game.getBackground());
        background.toBack();
        playground.getChildren().addAll(game.getPlayground());
        playground.toFront();
        foreground.getChildren().addAll(game.getForeground());
        foreground.toFront();
    }

    @FXML
    private void bindKeyEvents() {
        area.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @FXML
            @Override
            public void handle(KeyEvent event) {

                switch (event.getCode()) {
                    case UP:
                    case SPACE:
                        if (!game.hasStarted()) {
                            game.start();
                        }
                        game.move(DIRECTION.UP);
                        break;
                    case LEFT:
                        game.move(DIRECTION.LEFT);
                        break;
                    case RIGHT:
                        game.move(DIRECTION.RIGHT);
                        break;
                    case ESCAPE:
                        System.out.println("Terminated. ");
                        Platform.exit();
                        break;
                    case F2:
                        game.reset();
                        break;
                    default:
                        break;
                }

            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bindKeyEvents();
        bindShapesToSceneGraph();
        heartbeat.play();
    }
}
