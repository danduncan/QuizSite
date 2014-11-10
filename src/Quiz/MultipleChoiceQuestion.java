package Quiz;

import java.io.PrintWriter;

public class MultipleChoiceQuestion extends Question {
	private String[] answers;
	
	public MultipleChoiceQuestion(String question, String[] answers, String answer){
		super(Question.MULTIPLE_CHOICE, question, answer);
		this.answers = answers;
	}
	
	public void printToJSP(PrintWriter out, int i){
		out.println(getQuestionStr() + "<br>");
		for(int j = 0 ; j < answers.length; j++){
			out.println("<input type=\"radio\" name=\""+Question.ANSWER+i+""+0+"\" value="+ answers[j] +">" + answers[j] + "<br>");
		}
	}

}
