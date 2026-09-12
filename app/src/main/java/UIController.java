package quiz;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

public class UIController {
	// the logic of the buttons should be implemented here
    @FXML
    private Button play;

    @FXML
    private Button settings;
    
    @FXML
    private Button exit; 

    @FXML
    private Button easy;

    @FXML
    private Button intermediate;

    @FXML
    private Button hard;

    @FXML
    private Button answer1; //example only

    @FXML // Difficulty selector scene
    private void handlePlay(ActionEvent event) throws IOException {

        Parent difficultyRoot =
            FXMLLoader.load(getClass().getResource("/difficulty.fxml"));

        Scene scene = ((Button) event.getSource()).getScene();

        scene.setRoot(difficultyRoot);
    }

    @FXML // Difficulty info scene
    private void handleDifficulty(ActionEvent event) throws IOException {

    Button clickedButton = (Button) event.getSource();
    String difficulty = clickedButton.getText();
    Scene scene = clickedButton.getScene();

    String fxml;

        switch (difficulty) {
        case "Easy":   fxml = "/easy-ui.fxml";   break;
        case "Medium": fxml = "/medium-ui.fxml"; break;
        case "Hard":   fxml = "/hard-ui.fxml";   break;
        default:       return;
        }

        scene.setRoot(FXMLLoader.load(getClass().getResource(fxml)));
    }

     @FXML // Difficulty selector scene
    private void easyStart(ActionEvent event) throws IOException {

        Parent difficultyRoot =
            FXMLLoader.load(getClass().getResource("/quiz.fxml"));

        Scene scene = ((Button) event.getSource()).getScene();

        scene.setRoot(difficultyRoot);
    }
    
}