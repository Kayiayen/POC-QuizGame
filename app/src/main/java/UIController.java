package quiz;

import java.io.IOException;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class UIController {

    @FXML private VBox settingsOverlay;
    @FXML private StackPane helpOverlay;
    @FXML private TextArea descArea;

    @FXML private JFXButton easyToggle;
    @FXML private JFXButton intermediateToggle;
    @FXML private JFXButton hardToggle;
    @FXML private JFXToggleButton musicToggle;

    private JFXButton[] difficultyButtons;
    private JFXButton selectedDifficulty;

    private static final String CSS_SELECTED = "difficulty-selected";

    @FXML
    private void initialize() {
        difficultyButtons = new JFXButton[]{ easyToggle, intermediateToggle, hardToggle };
        if (musicToggle != null) musicToggle.selectedProperty().bindBidirectional(Settings.musicOn);
    }

    @FXML
    private void menuPressed(ActionEvent event) throws IOException {
        String id = ((javafx.scene.Node) event.getSource()).getId();

        switch (id) {
            case "playButton"     -> enterPlay(event);
            case "settingsButton" -> openSettings();
            case "settingsClose"  -> closeSettings();
            case "helpButton"     -> helpShow();
            case "exitButton"     -> exitGame(event);
        }
    }

    private void enterPlay(ActionEvent event) throws IOException {
        Navigation.goTo("/difficulty.fxml");
    }

    private void openSettings() {
        settingsOverlay.setVisible(true);
        settingsOverlay.setManaged(true);
        helpOverlay.setVisible(false);
        helpOverlay.setManaged(false);
    }

    private void closeSettings() {
        settingsOverlay.setVisible(false);
        settingsOverlay.setManaged(false);
    }

    private void helpShow() {
        helpOverlay.setVisible(true);
        helpOverlay.setManaged(true);
        settingsOverlay.setVisible(false);
        settingsOverlay.setManaged(false);
    }

    private void exitGame(ActionEvent event) {
        javafx.stage.Stage stage = (javafx.stage.Stage)
            ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        AudioManager.stopAll();
        stage.close();
    }

    @FXML
    void helpClose(ActionEvent event) {
        helpOverlay.setVisible(false);
        helpOverlay.setManaged(false);
    }

    @FXML
    void backMenu(ActionEvent event) throws IOException {
        Navigation.goTo("/menu.fxml");
    }

    @FXML
    private void difficultySelected(ActionEvent event) {
        JFXButton clicked = (JFXButton) event.getSource();

        for (JFXButton b : difficultyButtons) {
            b.getStyleClass().remove(CSS_SELECTED);
        }

        clicked.getStyleClass().add(CSS_SELECTED);
        selectedDifficulty = clicked;

        updateDescription(clicked.getText());
    }

    private void updateDescription(String difficulty) {
        switch (difficulty) {
            case "Easy"         -> descArea.setText("Contains common knowledge that can be considered as easy.");
            case "Medium"       -> descArea.setText("The somewhere in between the general.");
            case "Hard"         -> descArea.setText("Its easy...");
            default             -> descArea.setText("");
        }
    }

    @FXML
    private void difficultyStart(ActionEvent event) throws IOException {
        if (selectedDifficulty == null) {
            descArea.setText("Please Select Difficulty");
            return;
        }

        String difficulty = selectedDifficulty.getText();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/quiz.fxml"));
        Parent root = loader.load();

        QuizController quiz = loader.getController();
        quiz.setDifficulty(difficulty);

        Navigation.goTo(root);
    }
}