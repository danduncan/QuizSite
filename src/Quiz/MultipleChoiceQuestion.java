package Quiz;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.jsp.JspWriter;

public class MultipleChoiceQuestion extends Question {
	public String[] answerChoices;
	public boolean randomizeAnswers;

	public MultipleChoiceQuestion(Integer ID, Integer QuizID, Integer QNumber, Integer PageNumber, Integer QuestionTime, Integer NumAttempted,
								  Integer NumCorrect, String Type, String question, String Answer, String[] AnswerChoices, boolean random){
		super(ID,QuizID,QNumber,PageNumber,QuestionTime,NumAttempted,NumCorrect, Type,question, Answer, Question.MULTIPLE_CHOICE);
		answerChoices = AnswerChoices;
		randomizeAnswers = random;
	}
	
	public MultipleChoiceQuestion(Integer ID, Integer QuizID, Integer QNumber, Integer PageNumber, Integer QuestionTime,
								String Type, String question, String Answer, String[] AnswerChoices, boolean random){	
		super(ID,QuizID,QNumber,PageNumber,QuestionTime,Type, question, Answer, Question.MULTIPLE_CHOICE);
		answerChoices = AnswerChoices;
		randomizeAnswers = random;
	}
	
	public void printToJSP(JspWriter out, int i) throws IOException{
		out.println(getQuestionStr() + "<br>");
		for(int j = 0 ; j < answerChoices.length; j++){
			out.println("<input type=\"radio\" name=\""+Question.ANSWER+i+""+0+"\" value=\""+ answerChoices[j] +"\">" + answerChoices[j] + "<br>");		}
	}

}
