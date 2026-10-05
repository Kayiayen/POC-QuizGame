package quiz;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.nio.file.Paths;

public class Main extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;

        Parent root = FXMLLoader.load(getClass().getResource("/menu.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(
            getClass().getResource("/style.css").toExternalForm()
        );

        stage.setTitle("Quiz Game");
        stage.setScene(scene);
        stage.setWidth(800);
        stage.setHeight(500);
        stage.setResizable(false);
        stage.show();
        music();
    }

    MediaPlayer mediaPlayer;

    public void music() {
        String s = getClass().getResource("/music/bgm.wav").toExternalForm();

        Media h = new Media(s);

        mediaPlayer = new MediaPlayer(h);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        Settings.musicOn.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
            mediaPlayer.play();
            } else {
                mediaPlayer.pause();
            }
        });

        if (Settings.musicOn.get()) {
            mediaPlayer.play();
        }
    }

    public static Stage getStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}