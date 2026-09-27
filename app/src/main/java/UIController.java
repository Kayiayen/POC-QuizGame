package quiz;

import java.io.IOException;

import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class UIController {

    // ===================== FXML fields =====================
    @FXML private VBox settingsOverlay;
    @FXML private StackPane helpOverlay;
    @FXML private TextArea descArea;

    @FXML private JFXButton easyToggle;
    @FXML private JFXButton intermediateToggle;
    @FXML private JFXButton hardToggle;

    // ===================== State =====================
    private JFXButton[] difficultyButtons;
    private JFXButton selectedDifficulty;

    private static final String CSS_SELECTED = "difficulty-selected";

    // ===================== Lifecycle =====================
    @FXML
    private void initialize() {
        difficultyButtons = new JFXButton[]{ easyToggle, intermediateToggle, hardToggle };
    }

    // ===================== Menu routing =====================
    @FXML
    private void menuPressed(ActionEvent event) throws IOException {
        String id = ((javafx.scene.Node) event.getSource()).getId();

        switch (id) {
            case "playButton"     -> {enterPlay(event);
                                        Sfx.play("click-sfx");}
            case "settingsButton" -> {openSettings();
                                        Sfx.play("click-sfx");}
            case "settingsClose"  -> {closeSettings();
                                        Sfx.play("click-sfx");}
            case "helpButton"     -> {helpShow();
                                        Sfx.play("click-sfx");}
            case "exitButton"     -> {exitGame(event);
                                        Sfx.play("click-sfx");}
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
        stage.close();
    }

    @FXML
    void helpClose(ActionEvent event) {
        helpOverlay.setVisible(false);
        helpOverlay.setManaged(false);
        Sfx.play("click-sfx");
    }

    @FXML
    void backMenu(ActionEvent event) throws IOException {
        Navigation.goTo("/menu.fxml");
        Sfx.play("click-sfx");
    }

    // ===================== Difficulty selection =====================
    /**
     * Simulates toggle-group behaviour on plain JFXButtons by adding/
     * removing a CSS class. JFXButton has no :selected pseudo-class,
     * so we drive the visual state ourselves.
     */
    @FXML
    private void difficultySelected(ActionEvent event) {
        JFXButton clicked = (JFXButton) event.getSource();

        // Clear previous selection
        for (JFXButton b : difficultyButtons) {
            b.getStyleClass().remove(CSS_SELECTED);
        }

        // Mark this one selected (persists until another is clicked)
        clicked.getStyleClass().add(CSS_SELECTED);
        selectedDifficulty = clicked;

        updateDescription(clicked.getText());
    }

    private void updateDescription(String difficulty) {
        switch (difficulty) {
            case "Easy"         -> {descArea.setText("Insert Easy Description");
                                    Sfx.play("click-sfx");}
            case "Medium"       -> {descArea.setText("Insert Medium Description");
                                    Sfx.play("click-sfx");}
            case "Hard"         -> {descArea.setText("Insert Hard Description");
                                    Sfx.play("click-sfx");}
            default             -> {descArea.setText("");
                                    Sfx.play("click-sfx");}
        }
    }

    @FXML
    private void difficultyStart(ActionEvent event) throws IOException {
        Sfx.play("click-sfx");
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