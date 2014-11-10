package Quiz;

import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;


public class MultiAnswerQuestion extends Question {
	private boolean orderedResponses;
	
	public MultiAnswerQuestion(String questionStr, String[][] answerStrs, boolean orderedResponses){
		super(Question.MULTI_ANSWER, questionStr, answerStrs);
		this.orderedResponses = orderedResponses;
	}
	
	public MultiAnswerQuestion(int type, String questionStr, String answerStr){
		super(type, questionStr, answerStr);
		orderedResponses = false;
	}
	
	public MultiAnswerQuestion(int type, String questionStr, String[] answerStrs){
		super(type, questionStr, answerStrs);
		orderedResponses = false;
	}
	
	@Override
	public int numCorrect(String[] responseStrs){
		if(orderedResponses == true){
			return super.numCorrect(responseStrs);
		}else{
			Set<String> responseSet = new HashSet<String>();
			for(int i = 0; i<responseStrs.length ; i++){
				if(responseStrs[i] != null) responseSet.add(responseStrs[i]);
			}
			
			Set<String> answerSet = new HashSet<String>();
			for(int i = 0; i<answerStrs.length; i++){
				for(int j=0; j<answerStrs[i].length; j++){
					if(answerStrs[i][j] != null) answerSet.add(answerStrs[i][j]);
				}
			}
			
			answerSet.retainAll(responseSet);			
			return answerSet.size();
		}
	}
	
	
	public void printToJSP(PrintWriter out, int i){
		out.println(getQuestionStr() + "<br>");
		for(int j = 0; j<possiblePoints; j++){
			if(possiblePoints != 1){
				char c = (char) ('a'+j);
				out.println(c+".)");
			}
			out.println(" <input type=\"text\" name=\""+Question.ANSWER+i+""+j+"\"><br>");
		}
	}
	
}
