package quiz;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;

public class QuizController {

	private Question currentQuestion;

	@FXML 
	private Label questionText;

	@FXML
	public void initialize(){
		currentQuestion = new Question(
		"What is 2 + 2?","4"
		);

		questionText.setText(currentQuestion.getText());
	}
	
	@FXML
	private void handleAnswer(ActionEvent event) {
		Button clickedButton = (Button)
		event.getSource();

		String clickedAnswer = clickedButton.getText();
	

		if (clickedAnswer.equals(currentQuestion.getAnswer())) {

			System.out.println("Correct!");
		} else {
			System.out.println("Wrong!");
		} //else try again or exit

	}

}