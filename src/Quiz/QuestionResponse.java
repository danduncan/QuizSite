package Quiz;


public class QuestionResponse extends MultiAnswerQuestion {
	public QuestionResponse(Integer ID, Integer QuizID, Integer QNumber, Integer PageNumber, Integer QuestionTime, Integer NumAttempted, Integer NumCorrect, String Type,String Question, String[] Answer){
		super(ID,QuizID,QNumber,PageNumber,QuestionTime,NumAttempted,NumCorrect, Type, Question, new String[][]{Answer}, false);
	}
	
	public QuestionResponse(String questionStr, String answerStr){
		super(Question.QUESTION_RESPONSE, questionStr, answerStr);
	}
	
	public QuestionResponse(String questionStr, String[] answerStrs){
		super(Question.QUESTION_RESPONSE, questionStr, answerStrs);
	}	
}
