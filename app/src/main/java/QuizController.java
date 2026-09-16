package quiz;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Toggle;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;




public class QuizController {

	private Question currentQuestion;

	@FXML
    private ToggleButton answer1;

    @FXML
    private ToggleButton answer2;

    @FXML
    private ToggleButton answer3;

    @FXML
    private ToggleButton answer4;

	@FXML 
	private Label questionText;

	@FXML
	private VBox optionsOverlay;

	@FXML // load the questions here?
	public void initialize(){
		currentQuestion = new Question(
		"What is 2 + 2?","D"
		);

		questionText.setText(currentQuestion.getText());
	}

	@FXML
    private ToggleGroup toggleAnswerGroup = new ToggleGroup();

    @FXML // the ui logic of identifying the selected answer
    void submitAnswer(MouseEvent event) {
        Toggle selected = toggleAnswerGroup.getSelectedToggle();

        if (selected == null) {
            System.out.println("Please Select an Answer");
            return;

        }


        String clickedAnswer = ((ToggleButton) selected).getText();
		
		if (clickedAnswer.equals(currentQuestion.getAnswer())) {
		
		System.out.println("Correct!");
		} else {
		System.out.println("Wrong!");
		}
    }

	@FXML // todo when pressed escape show options 
	void showOptions(KeyEvent event) {
		if(event.getCode() == KeyCode.ESCAPE) {
    			System.out.println("You have escaped");
    			optionsOverlay.setVisible(false);
        		optionsOverlay.setManaged(false);
    	}
    }

    @FXML
    private void closePressed() {
        optionsOverlay.setVisible(false);
        optionsOverlay.setManaged(false);
    }
}