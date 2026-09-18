package quiz;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public final class Navigation {

    private Navigation() {}

    
    public static void goTo(String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(Navigation.class.getResource(fxmlPath));
            Scene scene = Main.getStage().getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}