package HighSeaTowerGame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class GameApp extends Application {
    private static final String GAME_VIEW = "/res/game.fxml";
    private static final String STYLESHEET = "/res/style.css";
    public static final Image ICON = new Image(GameApp.class.getResourceAsStream("/res/jellyfish.png"));

    static final int WINDOW_WIDTH = 350;
    static final int WINDOW_HEIGHT = 480;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource(GAME_VIEW));
        Scene primaryScene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);


        primaryScene.getStylesheets().add(STYLESHEET);

        root.requestFocus();

        primaryStage.setTitle("High Sea Tower");
        primaryStage.getIcons().add(ICON);
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(primaryScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
