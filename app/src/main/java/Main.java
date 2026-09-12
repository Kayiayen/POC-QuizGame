package quiz;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;


public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception{

        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/menu.fxml"));

        Parent root = loader.load();
        
        Scene scene = new Scene(root, 800, 500);

        scene.getStylesheets().add(
            getClass().getResource("/style.css").toExternalForm()
        );

        stage.setTitle("Quiz Game");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);

    }
}