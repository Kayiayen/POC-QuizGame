package quiz;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
    }

    public static Stage getStage() {     
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}