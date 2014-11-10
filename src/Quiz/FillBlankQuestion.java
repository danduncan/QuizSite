package Quiz;

import java.io.PrintWriter;

public class FillBlankQuestion extends Question{
	private String qStr1;
	private String qStr2;
	
	public FillBlankQuestion(String qStr1, String answerStr, String qStr2){
		this(qStr1, new String[]{answerStr}, qStr2);
	}
	
	public FillBlankQuestion(String qStr1, String[] answerStrs, String qStr2){
		super(Question.FILL_IN_BLANK, qStr1 + Question.DELIM + qStr2, answerStrs);
		this.qStr1 = qStr1;
		this.qStr2 = qStr2;
	}
	
	public void printToJSP(PrintWriter out, int i){
		out.println(qStr1 + " <input type=\"text\" name=\""+Question.ANSWER+i+""+0+"\"> " + qStr2);
	}
}
