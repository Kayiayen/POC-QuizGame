package quiz;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Toggle;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.scene.Node;




public class QuizController {

	private Question currentQuestion;

	@FXML
    private Button answer1;

    @FXML
    private Button answer2;

    @FXML
    private Button answer3;

    @FXML
    private Button answer4;

	@FXML 
	private Label questionText;

	@FXML
	private VBox optionsOverlay;

	@FXML
    private StackPane helpOverlay;

	@FXML
	public void initialize() {
    	currentQuestion = new Question("Why did David want to build a house for God in 2 Samuel 7?", "B");
    	questionText.setText(currentQuestion.getText());

		Platform.runLater(() -> optionsOverlay.getScene().setOnKeyPressed(this::showOptions));
	}

    @FXML
	private void submitAnswer(ActionEvent event) {
		Button clickedButton = (Button)
		event.getSource();

		String clickedAnswer = clickedButton.getText();
	

		if (clickedAnswer.equals(currentQuestion.getAnswer())) {

			System.out.println("Correct!");
		} else {
			System.out.println("Wrong!");
		}

	}

	private void showOptions(KeyEvent event) {
    	if (event.getCode() == KeyCode.ESCAPE) {
		optionsOverlay.setVisible(true);
        optionsOverlay.setManaged(true);
    	}
	}

    @FXML
    private void closePressed(MouseEvent event) {
        optionsOverlay.setVisible(false);
        optionsOverlay.setManaged(false);
    }


    @FXML
    void backDifficulty(MouseEvent event) throws IOException {
			Navigation.goTo("/difficulty.fxml");
    }

    @FXML
   	void helpShow (MouseEvent event) {
   		optionsOverlay.setVisible(false);
        optionsOverlay.setManaged(false);
        helpOverlay.setVisible(true);
        helpOverlay.setManaged(true);
    }

    @FXML
    void helpClose(MouseEvent event) {
        helpOverlay.setVisible(false);
        helpOverlay.setManaged(false);
        optionsOverlay.setVisible(true);
        optionsOverlay.setManaged(true);
    }
}