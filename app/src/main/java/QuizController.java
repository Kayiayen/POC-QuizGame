package quiz;

import java.io.IOException;

import com.jfoenix.controls.JFXButton;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class QuizController {

    // ===================== Question data =====================
    // Row layout: [question, choiceA, choiceB, choiceC, choiceD]
    // The "A. " prefix is added by the UI when displaying, not stored here.

    private static final String[][] EASY_QUESTIONS = {
        {" 1 + 1 = ?", "2", "5", "45", "1"},
        {" 2 x 2 = ?", "2", "12", "4", "6"},
        {" 2 + 4 = ?", "12", "10", "6", "34"},
        {" 3 + 1 = ?", "14", "8", "9", "4"},
        {" 1 + 2 = ?", "1", "3", "5", "7"},
        {" 5 + 3 = ?", "6", "7", "8", "9"},
        {" 10 - 4 = ?", "5", "6", "7", "8"},
        {" 3 x 3 = ?", "6", "7", "8", "9"},
        {" 8 - 2 = ?", "4", "5", "6", "7"},
        {" 4 + 5 = ?", "7", "8", "9", "10"}
    };

    private static final String[][] MEDIUM_QUESTIONS = {
        {" What is the capital city of the Philippines?", "Cebu City", "Manila", "Davao City", "Quezon City"},
        {" Which city is known as the 'Queen City of the South'?", "Cebu City", "Manila", "Baguio City", "Iloilo City"},
        {" Which city is known for its cool climate and is called the 'Summer Capital of the Philippines'?", "Tagaytay City", "Baguio City", "Davao City", "Pasay City"},
        {" Which city is the largest city in Mindanao by population?", "Cagayan de Oro", "General Santos", "Davao City", "Zamboanga City"},
        {" Which city is famous for the Chocolate Hills nearby?", "Tagbilaran City", "Cebu City", "Iloilo City", "Vigan City"},
        {" Which city is known for the Dinagyang Festival?", "Bacolod City", "Iloilo City", "Cebu City", "Manila"},
        {" Which city is famous for the MassKara Festival?", "Bacolod City", "Davao City", "Baguio City", "Pasig City"},
        {" Which city is famous for its historic Calle Crisologo?", "Vigan City", "Laoag City", "Naga City", "Legazpi City"},
        {" Which city is known as the 'Durian Capital of the Philippines'?", "Davao City", "Cagayan de Oro", "General Santos", "Zamboanga City"},
        {" Which city is famous for the Mayon Volcano nearby?", "Legazpi City", "Baguio City", "Batangas City", "Tagaytay City"}
    };

    private static final String[][] HARD_QUESTIONS = {
        {" What is the chemical symbol for Gold?", "Ag", "Au", "Gd", "Go"},
        {" Which planet has the most moons?", "Earth", "Mars", "Saturn", "Venus"},
        {" What is the largest organ in the human body?", "Heart", "Liver", "Brain", "Skin"},
        {" What gas do plants absorb during photosynthesis?", "Oxygen", "Nitrogen", "Carbon Dioxide", "Hydrogen"},
        {" What is the hardest natural substance?", "Iron", "Diamond", "Gold", "Quartz"},
        {" Which part of the cell contains genetic material?", "Nucleus", "Ribosome", "Cytoplasm", "Cell Wall"},
        {" What is the closest star to Earth?", "Sirius", "Polaris", "Alpha Centauri", "The Sun"},
        {" Which blood cells help fight infections?", "Red Blood Cells", "White Blood Cells", "Platelets", "Plasma"},
        {" What force keeps planets in orbit around the Sun?", "Friction", "Magnetism", "Gravity", "Electricity"},
        {" What is the process by which water changes from liquid to gas?", "Condensation", "Freezing", "Melting", "Evaporation"}
    };

    // Correct choice index: 0=A, 1=B, 2=C, 3=D
    private static final int[] EASY_ANSWERS   = {0, 2, 2, 3, 1, 2, 1, 3, 2, 2};
    private static final int[] MEDIUM_ANSWERS = {1, 0, 1, 2, 0, 1, 0, 0, 0, 0};
    private static final int[] HARD_ANSWERS   = {1, 2, 3, 2, 1, 0, 3, 1, 2, 3};

    // ===================== State =====================
    private String difficulty = "Easy";
    private int currentIndex = 0;
    private int score = 0;
    private int timeLeft;
    private Timeline timer;
    private JFXButton[] answerButtons;

    private static final int SECONDS_PER_QUESTION = 30;
    private static final long FEEDBACK_MILLIS = 2000;
    private static final String[] LETTERS = { "A. ", "B. ", "C. ", "D. " };

    // CSS classes for inline per-question feedback
    private static final String CSS_CORRECT = "answer-correct";
    private static final String CSS_WRONG   = "answer-wrong";

    // ===================== FXML fields =====================
    @FXML private JFXButton answer1;
    @FXML private JFXButton answer2;
    @FXML private JFXButton answer3;
    @FXML private JFXButton answer4;
    @FXML private Label questionText;
    @FXML private Label scoreLabel;
    @FXML private Label timerLabel;

    @FXML private VBox optionsOverlay;
    @FXML private StackPane helpOverlay;
    @FXML private StackPane resultsOverlay;
    @FXML private Label resultsLabel;

    // ===================== Lifecycle =====================
    @FXML
    public void initialize() {
        answerButtons = new JFXButton[]{ answer1, answer2, answer3, answer4 };
    
        for (int i = 0; i < answerButtons.length; i++) {
            answerButtons[i].setUserData(i);
            answerButtons[i].setRipplerFill(javafx.scene.paint.Color.TRANSPARENT); // optional
        }
    
        // Wire ESC once the root is attached to a Scene. Navigation's fade
        // attaches the root ~220ms after initialize(), so we can't do this
        // synchronously or via Platform.runLater().
        questionText.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.getRoot().addEventFilter(KeyEvent.KEY_PRESSED, this::handleKey);
            }
       });
    
        loadQuestion();
    }

    /** Called by UIController.difficultyStart() after loading quiz.fxml. */
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
        this.currentIndex = 0;
        this.score = 0;
        loadQuestion();
    }

    // ===================== Difficulty lookup =====================
    private String[][] questions() {
        return switch (difficulty) {
            case "Intermediate", "Medium" -> MEDIUM_QUESTIONS;
            case "Hard" -> HARD_QUESTIONS;
            default -> EASY_QUESTIONS;
        };
    }

    private int[] answers() {
        return switch (difficulty) {
            case "Intermediate", "Medium" -> MEDIUM_ANSWERS;
            case "Hard" -> HARD_ANSWERS;
            default -> EASY_ANSWERS;
        };
    }

    // ===================== Question flow =====================
    private void loadQuestion() {
        String[][] qs = questions();

        if (currentIndex >= qs.length) {
            endGame();
            return;
        }

        String[] q = qs[currentIndex];
        questionText.setText(q[0]);

        for (int i = 0; i < answerButtons.length; i++) {
            answerButtons[i].setText(LETTERS[i] + q[i + 1]); // +1 skips question text
            answerButtons[i].setDisable(false);
        }

        updateScoreLabel();
        startTimer(SECONDS_PER_QUESTION);
    }

    private void startTimer(int seconds) {
        if (timer != null) timer.stop();

        timeLeft = seconds;
        timerLabel.setText("Time: " + timeLeft);

        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            timeLeft--;
            timerLabel.setText("Time: " + timeLeft);

            if (timeLeft <= 0) {
                timer.stop();
                // Timeout: no color feedback, advance straight away.
                advance();
            }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    @FXML
    private void submitAnswer(ActionEvent event) {
        // Stop the timer so the player isn't penalized during the feedback pause.
        if (timer != null) timer.stop();

        JFXButton clicked = (JFXButton) event.getSource();
        int chosen = (int) clicked.getUserData();
        int correctIndex = answers()[currentIndex];

        // Disable all four buttons immediately — no double-click allowed.
        for (JFXButton b : answerButtons) b.setDisable(true);

        // Inline visual feedback (CSS classes; no popup, no overlay, no modal).
        if (chosen == correctIndex) {
            score++;
            clicked.getStyleClass().add(CSS_CORRECT);
        } else {
            clicked.getStyleClass().add(CSS_WRONG);
            answerButtons[correctIndex].getStyleClass().add(CSS_CORRECT);
        }

        // Score reflects the click right away.
        updateScoreLabel();

        // Keep colors visible ~@x ms, then clean up and move on.
        Timeline feedback = new Timeline(new KeyFrame(
            Duration.millis(FEEDBACK_MILLIS),
            e -> {
                clearFeedbackStyles();
                advance();
            }
        ));
        feedback.play();
    }

    /** Remove any inline feedback classes from all four answer buttons. */
    private void clearFeedbackStyles() {
        for (JFXButton b : answerButtons) {
            b.getStyleClass().removeAll(CSS_CORRECT, CSS_WRONG);
        }
    }

    private void advance() {
        currentIndex++;
        loadQuestion();
    }

    private void updateScoreLabel() {
        scoreLabel.setText("Score: " + score + " / " + questions().length);
    }

    // ===================== End of quiz =====================
    private void endGame() {
        if (timer != null) timer.stop();

        int total = questions().length;
        resultsLabel.setText(
            difficulty + "\nFinal Score: " + score + " / " + total
        );

        resultsOverlay.setVisible(true);
        resultsOverlay.setManaged(true);
    }

    @FXML
    private void playAgain(ActionEvent event) {
        resultsOverlay.setVisible(false);
        resultsOverlay.setManaged(false);
        score = 0;
        currentIndex = 0;
        loadQuestion();
    }

    @FXML
    private void resultsToMenu(ActionEvent event) throws IOException {
        Navigation.goTo("/menu.fxml");
    }

    // ===================== Overlays =====================
    private void handleKey(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE) {
            optionsOverlay.setVisible(true);
            optionsOverlay.setManaged(true);
        }
    }

    @FXML
    private void closePressed(ActionEvent event) {
        optionsOverlay.setVisible(false);
        optionsOverlay.setManaged(false);
    }

    @FXML
    private void helpShow(ActionEvent event) {
        optionsOverlay.setVisible(false);
        optionsOverlay.setManaged(false);
        helpOverlay.setVisible(true);
        helpOverlay.setManaged(true);
    }

    @FXML
    private void helpClose(ActionEvent event) {
        helpOverlay.setVisible(false);
        helpOverlay.setManaged(false);
        optionsOverlay.setVisible(true);
        optionsOverlay.setManaged(true);
    }

    // ===================== Navigation =====================
    @FXML
    private void backDifficulty(ActionEvent event) throws IOException {
        if (timer != null) timer.stop();
        Navigation.goTo("/difficulty.fxml");
    }
}