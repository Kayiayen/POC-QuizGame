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

    private static final String[][] EASY_QUESTIONS = {
        {"What does IT stand for?", "Internet Technology", "Information Technology", "Information Transfer", "Internet Transfer"},
        {"What is the capital of the Philippines?", "Cebu", "Manila", "Davao", "Baguio"},
        {"What organ pumps blood?", "Brain", "Lungs", "Heart", "Stomach"},
        {"What gas do humans need to breathe?", "Oxygen", "Helium", "Carbon", "Hydrogen"},
        {"Which device is mainly used to type text?", "Mouse", "Printer", "Monitor", "Keyboard"},
        {"What is the opposite of hot?", "Cold", "Warm", "Dry", "Bright"},
        {"What is the internet?", "A computer part", "A global network", "A file", "A keyboard"},
        {"What do bees produce?", "Milk", "Honey", "Juice", "Oil"},
        {"What is 5 x 4?", "15", "45", "20", "30"},
        {"Which is commonly used to make presentations?", "Calculator", "Powerpoint", "Paint", "Notepad"}
    };

    private static final String[][] MEDIUM_QUESTIONS = {
        {"What process changes water into vapor?", "Freezing", "Melting", "Evaporation", "Condensation"},
        {"What does LAN stand for?", "Local Access Network", "Local Area Network", "Local Application Number", "Long Area Node"},
        {"Which language is commonly used for web page styling?", "CSS", "SQL", "Python", "Java"},
        {"What do red blood cells carry?", "Oxygen", "Food", "Water", "Bones"},
        {"What is malware?", "Computer hardware", "A database", "A browser", "Harmful software"},
        {"Which planet is famous for its Great Red Spot?", "Mars", "Jupiter", "Saturn", "Uranus"},
        {"Which vitamin is primarily produced by the skin in response to sunlight?", "Vitamin A", "Vitamin B12", "Vitamin C", "Vitamin D"},
        {"Which language has the most native speakers worldwide?", "English", "Spanish", "Mandarin Chinese", "French"},
        {"What is cloud storage?", "Online file storage", "Physical storage only", "Computer memory", "A type of keyboard"},
        {"Which instrument measures wind speed?", "Barometer", "Anemometer", "Thermometer", "Seismometer"}
    };

    private static final String[][] HARD_QUESTIONS = {
        {"Which active Philippine volcano had a massive 1991 eruption?", "Taal", "Mayon", "Mount Pinatubo", "Mount Kanlaon"},
        {"In what year did Vincent van Gogh paint The Starry Night?", "1888", "1889", "1890", "1891"},
        {"Who was Jose Rizal's eldest sister?", "Saturnina", "Paciano", "Narcisa", "Soledad"},
        {"How many teeth does an adult human typically have?", "30", "31", "32", "33"},
        {"Which African country was formerly known as Abyssinia?", "Ghana", "Zimbabwe", "Ethiopia", "Sudan"},
        {"What is the most abundant gas in Earth's atmosphere?", "Nitrogen", "Oxygen", "Carbon Dioxide", "Argon"},
        {"What is the main functional unit of the human liver?", "Nephron", "Lobule", "Alveolus", "Islet"},
        {"What is the capital of New Zealand?", "Auckland", "Christchurch", "Dunedin", "Wellington"},
        {"What is the SI unit of magnetic flux?", "Tesla", "Henry", "Weber", "Lumen"},
        {"What is the loopback IPv4 address for a local host?", "192.168.1.1", "127.0.0.1", "10.0.0.1", "255.255.255.255"}
    };

    private static final int[] EASY_ANSWERS   = {1, 1, 2, 0, 3, 0, 1, 1, 2, 1};
    private static final int[] MEDIUM_ANSWERS = {2, 1, 0, 0, 3, 1, 3, 2, 0, 1};
    private static final int[] HARD_ANSWERS   = {2, 1, 0, 2, 2, 0, 1, 3, 2, 1};

    private static final int EASY_SECONDS   = 30;
    private static final int MEDIUM_SECONDS = 20;
    private static final int HARD_SECONDS   = 10;

    private String difficulty = "Easy";
    private int currentIndex = 0;
    private int score = 0;
    private int timeLeft;
    private Timeline timer;
    private JFXButton[] answerButtons;

    private static final long FEEDBACK_MILLIS = 1500;

    private static final String CSS_CORRECT = "answer-correct";
    private static final String CSS_WRONG   = "answer-wrong";

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

    @FXML
    public void initialize() {
        answerButtons = new JFXButton[]{ answer1, answer2, answer3, answer4 };

        for (int i = 0; i < answerButtons.length; i++) {
            answerButtons[i].setUserData(i);
        }

        questionText.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.getRoot().addEventFilter(KeyEvent.KEY_PRESSED, this::handleKey);
            }
        });

        loadQuestion();
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
        this.currentIndex = 0;
        this.score = 0;
        loadQuestion();
    }

    private String[][] questions() {
        return switch (difficulty) {
            case "Medium" -> MEDIUM_QUESTIONS;
            case "Hard" -> HARD_QUESTIONS;
            default -> EASY_QUESTIONS;
        };
    }

    private int[] answers() {
        return switch (difficulty) {
            case "Medium" -> MEDIUM_ANSWERS;
            case "Hard" -> HARD_ANSWERS;
            default -> EASY_ANSWERS;
        };
    }

    private int secondsForDifficulty() {
        return switch (difficulty) {
            case "Medium" -> MEDIUM_SECONDS;
            case "Hard" -> HARD_SECONDS;
            default -> EASY_SECONDS;
        };
    }

    private void loadQuestion() {
        String[][] qs = questions();

        if (currentIndex >= qs.length) {
            endGame();
            return;
        }

        String[] q = qs[currentIndex];

        questionText.setText((currentIndex + 1) + ". " + q[0]);

        for (int i = 0; i < answerButtons.length; i++) {
            answerButtons[i].setText(q[i + 1]);
            answerButtons[i].setDisable(false);
        }

        updateScoreLabel();
        startTimer(secondsForDifficulty());
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
                advance();
            }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    @FXML
    private void submitAnswer(ActionEvent event) {
        if (timer != null) timer.stop();

        JFXButton clicked = (JFXButton) event.getSource();
        int chosen = (int) clicked.getUserData();
        int correctIndex = answers()[currentIndex];

        for (JFXButton b : answerButtons) b.setDisable(true);

        if (chosen == correctIndex) {
            score++;
            clicked.getStyleClass().add(CSS_CORRECT);
        } else {
            clicked.getStyleClass().add(CSS_WRONG);
            answerButtons[correctIndex].getStyleClass().add(CSS_CORRECT);
        }

        updateScoreLabel();

        Timeline feedback = new Timeline(new KeyFrame(
            Duration.millis(FEEDBACK_MILLIS),
            e -> {
                clearFeedbackStyles();
                advance();
            }
        ));
        feedback.play();
    }

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

    private void handleKey(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE) {
            if (resultsOverlay.isVisible()) return;
            if (helpOverlay.isVisible()) {
                helpClose(null);
                return;
            }
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

    @FXML
    private void backDifficulty(ActionEvent event) throws IOException {
        if (timer != null) timer.stop();
        Navigation.goTo("/difficulty.fxml");
    }
}