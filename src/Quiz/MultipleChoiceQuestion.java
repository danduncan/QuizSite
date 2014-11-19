package Quiz;

import java.io.PrintWriter;

public class MultipleChoiceQuestion extends Question {
	public String[] answerChoices;
	public boolean randomizeAnswers;

	public MultipleChoiceQuestion(Integer ID, Integer QuizID, Integer QNumber, Integer PageNumber, Integer QuestionTime, Integer NumAttempted, Integer NumCorrect, String Type, String question, String Answer, String[] AnswerChoices, boolean random){
		super(ID,QuizID,QNumber,PageNumber,QuestionTime,NumAttempted,NumCorrect, Type,question, Answer, Question.MULTIPLE_CHOICE);
		answerChoices = AnswerChoices;
		randomizeAnswers = random;
	}
	
	public MultipleChoiceQuestion(Integer ID, Integer QuizID, Integer QNumber, Integer PageNumber, Integer QuestionTime, String Type, String question, String Answer, String[] AnswerChoices, boolean random){
		super(ID,QuizID,QNumber,PageNumber,QuestionTime,Type, question, Answer, Question.MULTIPLE_CHOICE);
		answerChoices = AnswerChoices;
		randomizeAnswers = random;
	}
	
	public MultipleChoiceQuestion(String question, String[] answers, String answer){
		super(Question.MULTIPLE_CHOICE, question, answer);
	}
	
	public void printToJSP(PrintWriter out, int i){
		out.println(getQuestionStr() + "<br>");
		for(int j = 0 ; j < answerChoices.length; j++){
			out.println("<input type=\"radio\" name=\""+Question.ANSWER+i+""+0+"\" value="+ answerChoices[j] +">" + answerChoices[j] + "<br>");
		}
	}

}
