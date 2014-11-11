package Quiz;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;

public abstract class Question {
	public static final int QUESTION_RESPONSE = 0;
	public static final int FILL_IN_BLANK = 1;
	public static final int MULTIPLE_CHOICE = 2;
	public static final int PICTURE_RESPONSE = 3;
	public static final int MULTI_ANSWER = 4;
	public static final int MULTI_CHOICE_MULTI_ANSWER = 5;
	
	public static final String ANSWER = "Answer";
	public static final String STRING_DELIM = "&&&";
	public static final String ARRAY_DELIM = "%%%";
	
	protected int type;
	protected String questionStr;
	protected String[][] answerStrs;
	protected int possiblePoints;
	
	
	public Question(int type, String questionStr, String answerStr){
		this(type, questionStr, new String[]{answerStr});
	}
	
	public Question(int type, String questionStr, String[] answerStrs){
		this(type, questionStr, new String[][]{answerStrs});
	}
	
	public Question(int type, String questionStr, String[][] answerStrs){
		this.type = type;
		this.questionStr = questionStr;
		this.answerStrs = answerStrs;
		this.possiblePoints = answerStrs.length;
	}
	
	
	
	public int numCorrect(String[] responseStrs){
		if(responseStrs == null) return 0;
		int numCorrect = 0;
		for(int i = 0 ; i<responseStrs.length; i++){
			boolean correct = false;
			if(responseStrs[i] == null) continue;
			for(int j = 0; j<answerStrs[i].length; j++){
				if(responseStrs[i].equals(answerStrs[i][j])) correct = true;
			}
			if(correct) numCorrect++;
		}
		return numCorrect;
	}
	
	public int numCorrect(String responseStr){
		return numCorrect(new String[]{responseStr});
	}
	
	public boolean isCorrect(String[] responseStrs){
		return (numCorrect(responseStrs) == numAttempted());
	}
	
	public boolean isCorrect(String responseStr){
		return isCorrect(new String[]{responseStr});
	}
	
	
	public int numAttempted(){
		return possiblePoints;
	}
	
	public String getQuestionStr(){
		return questionStr;
	}
	
	public String[] getAnswerStrs(){
		String[] answers = new String[possiblePoints];
		for(int j = 0; j<possiblePoints; j++){
			String answerStr = "";
			for(int i = 0; i<answerStrs[j].length; i++){
				answerStr += answerStrs[j][i];
				if(i < answerStrs[j].length-1) answerStr += ", ";
			}
			answers[j] = answerStr;
		}
		return answers;
	}
	
	public int getType(){
		return type;
	}
	
	public String[] getResponses(HttpServletRequest request, int i){
		String[] answer = new String[possiblePoints];
		for(int j = 0; j<possiblePoints; j++){
			answer[j] = request.getParameter(ANSWER+i+""+j);
		}
		return answer;
	}
	
	public abstract void printToJSP(PrintWriter out, int i);
}
