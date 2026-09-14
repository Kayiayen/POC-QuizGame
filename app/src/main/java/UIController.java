package quiz;

import java.io.IOException;

import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Toggle;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.fxml.FXML;

public class UIController {
	// the logic of the buttons should be implemented here
    @FXML
    private Button play;

    @FXML
    private Button settings;
    
    @FXML
    private Button exit; 

    @FXML
    private ToggleButton easyToggle;

    @FXML
    private ToggleButton intermediateToggle;

    @FXML
    private ToggleButton hardToggle;

    @FXML
    private Button startButton;

    @FXML 
    private VBox settingsOverlay;

    @FXML
    private ToggleGroup difficultyGroup = new ToggleGroup();

    @FXML
    private TextArea descArea;

    @FXML
    private void initialize () {
        difficultyGroup.selectedToggleProperty().addListener(
            (obs, oldToggle, newToggle) -> updateDescription(newToggle)
        );
    }

    @FXML
    private void menuAction(ActionEvent event) throws IOException {
        
        String id = ((Node) event.getSource()).getId();
        
        switch (id) {
        case "playButton"     -> playAction(event);
        case "settingsButton" -> openSettings();
        case "settingsClose"  -> closeSettings();
        case "helpButton"     -> helpInfo();
        case "exitButton"     -> exitGame(event);
        }
    }

    
    private void playAction(ActionEvent event) throws IOException {
        Parent difficultyRoot =
                    FXMLLoader.load(getClass().getResource("/difficulty.fxml"));

                Scene scene = ((Button) event.getSource()).getScene();
                scene.setRoot(difficultyRoot);
    }

    private void openSettings() {
        settingsOverlay.setVisible(true);
        settingsOverlay.setManaged(true);
    }

    private void closeSettings() {
        settingsOverlay.setVisible(false);
        settingsOverlay.setManaged(false);
    }

        private void helpInfo () {
       System.out.println("Insert Game Mechanics");
    }
    
    private void exitGame (ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void difficultyStart(ActionEvent event) throws IOException {
        Toggle selected = difficultyGroup.getSelectedToggle();

        if (selected == null) {
            System.out.println("Select difficulty");
            return;
        }

        String difficulty = ((ToggleButton) selected).getText();
        Scene scene = ((ToggleButton) selected).getScene();

            String fxml = switch (difficulty) {
            case "Easy"         -> "/quiz.fxml";
            case "Intermediate" -> "/Intermediate-ui.fxml";
            case "Hard"         -> "/hard-ui.fxml";
            default             -> null;
            };

        if (fxml != null) {
        scene.setRoot(FXMLLoader.load(getClass().getResource(fxml)));
        }
    }

    private void updateDescription (Toggle selected) {
        if (selected == null) {
            descArea.setText("");
        } else {
            ToggleButton btn = (ToggleButton) selected;
            switch (btn.getText()) {
            case "Easy"         -> descArea.setText("Insert Easy Description");
            case "Intermediate" -> descArea.setText("Insert Intermediate Description");
            case "Hard"         -> descArea.setText("Insert Hard Description");
            }
        }
    }


    
}