package Quiz;

import java.io.PrintWriter;

public class MultipleChoiceQuestion extends Question {
	public String answer;
	public String question;
	public String[] answerChoices;
	public boolean randomizeAnswers;
	private String[] answers;
	
	public MultipleChoiceQuestion(Integer ID, Integer QuizID, Integer QNumber, Integer PageNumber, Integer QuestionTime, Integer NumAttempted, Integer NumCorrect, String Type, String Question, String Answer, String[] AnswerChoices, boolean random){
		super(ID,QuizID,QNumber,PageNumber,QuestionTime,NumAttempted,NumCorrect, Type);
		answer = Answer;
		question = Question;
		answerChoices = AnswerChoices;
		randomizeAnswers = random;
	}
	
	public MultipleChoiceQuestion(Integer ID, Integer QuizID, Integer QNumber, Integer PageNumber, Integer QuestionTime, String Type, String Question, String Answer, String[] AnswerChoices, boolean random){
		super(ID,QuizID,QNumber,PageNumber,QuestionTime,Type);
		answer = Answer;
		question = Question;
		answerChoices = AnswerChoices;
		randomizeAnswers = random;
	}
	
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
