package quiz;

public class Question {

    String text;
    String answer;

    public Question(String text, String answer) {
        this.text = text;
        this.answer = answer;
    }

    public String getAnswer(){
        return answer;
    }

    public String getText() {
        return text;
    }
}