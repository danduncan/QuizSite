package Quiz;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class MultiAnswerQuestion extends Question {
	public boolean ordered;
	
	public MultiAnswerQuestion(Integer ID, Integer QuizID, Integer QNumber, Integer PageNumber, Integer QuestionTime, 
						Integer NumAttempted, Integer NumCorrect, String Type,String question, String[][] Answer, boolean Ordered){
		super(ID,QuizID,QNumber,PageNumber,QuestionTime,NumAttempted,NumCorrect, Type, question, Answer, Question.MULTI_ANSWER);
		ordered = Ordered;
	}
	
	
	public MultiAnswerQuestion(Integer ID, Integer QuizID, Integer QNumber, Integer PageNumber, Integer QuestionTime,
							String Type, String question, String[][] Answer, boolean Ordered){
		super(ID,QuizID,QNumber,PageNumber,QuestionTime, Type, question, Answer, Question.MULTI_ANSWER);
		ordered = Ordered;
	}
	
	
	public MultiAnswerQuestion(Integer ID, Integer QuizID, Integer QNumber, Integer PageNumber, Integer QuestionTime,
						Integer NumAttempted, Integer NumCorrect, String Type,String question, String[] Answer, boolean Ordered, int type){
		super(ID,QuizID,QNumber,PageNumber,QuestionTime,NumAttempted,NumCorrect, Type, question, Answer, type);
		ordered = Ordered;
	}
	
	
	public MultiAnswerQuestion(Integer ID, Integer QuizID, Integer QNumber, Integer PageNumber, Integer QuestionTime,
								String Type, String question, String[] Answer, boolean Ordered, int type){
		super(ID,QuizID,QNumber,PageNumber,QuestionTime, Type, question, Answer, type);
		ordered = Ordered;
	}
	
	@Override
	public int numCorrect(String[] responseStrs){
		if(ordered == true){
			return super.numCorrect(responseStrs);
		}else{			
			List<Set<String>> setList = new ArrayList<Set<String>>();
			for(int i = 0; i<answerStrs.length; i++){
				Set<String> answerSet = new HashSet<String>();
				for(int j=0; j<answerStrs[i].length; j++){
					if(answerStrs[i][j] != null) answerSet.add(answerStrs[i][j]);
				}
				setList.add(answerSet);
			}
			boolean[] booleanArr = new boolean[setList.size()];
			
			int correctAnswers = 0;
			for(int i = 0; i<responseStrs.length; i++){
				if(responseStrs[i]== null) continue;
				String response = responseStrs[i];
				for(int j = 0; j<setList.size(); j++){
					if(setList.get(j).contains(response) && booleanArr[j] == false){
						booleanArr[j] = true;
						correctAnswers++;
						break;
					}
				}
			}
			return correctAnswers;
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
