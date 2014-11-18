package Quiz;

import java.io.PrintWriter;

public class FillBlankQuestion extends Question{
	public String qStr1;
	public String qStr2;
	public String[] answer;
	
	public FillBlankQuestion(Integer ID, Integer QuizID, Integer QNumber, Integer PageNumber, Integer QuestionTime, Integer NumAttempted, Integer NumCorrect, String Type,String QStr1, String QStr2, String[] Answer){
		super(ID,QuizID,QNumber,PageNumber,QuestionTime,NumAttempted,NumCorrect, Type);
		qStr1 = QStr1;
		qStr2 = QStr2;
		answer = Answer;
	}
	
//	public FillBlankQuestion(String qStr1, String answerStr, String qStr2){
//		this(qStr1, new String[]{answerStr}, qStr2);
//	}
	
	public FillBlankQuestion(String qStr1, String[] answerStrs, String qStr2){
		super(Question.FILL_IN_BLANK, qStr1 + Question.STRING_DELIM + qStr2, answerStrs);
		this.qStr1 = qStr1;
		this.qStr2 = qStr2;
	}
	
	public void printToJSP(PrintWriter out, int i){
		out.println(qStr1 + " <input type=\"text\" name=\""+Question.ANSWER+i+""+0+"\"> " + qStr2);
	}
}
