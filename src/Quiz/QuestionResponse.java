package Quiz;


public class QuestionResponse extends MultiAnswerQuestion {
	public QuestionResponse(String questionStr, String answerStr){
		super(Question.QUESTION_RESPONSE, questionStr, answerStr);
	}
	
	public QuestionResponse(String questionStr, String[] answerStrs){
		super(Question.QUESTION_RESPONSE, questionStr, answerStrs);
	}	
}
