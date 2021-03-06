package Quiz;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;

public class MultiChoiceMultiAnswerQuestion extends Question {
	public String[] answerChoices;
	public boolean randomizeAnswers;
	
	public MultiChoiceMultiAnswerQuestion(Integer ID, Integer QuizID, Integer QNumber, Integer PageNumber, Integer QuestionTime, Integer NumAttempted,
										  Integer NumCorrect, String Type, String question, String[] Answer, String[] AnswerChoices, boolean RandomizeAnswers){
		super(ID,QuizID,QNumber,PageNumber,QuestionTime,NumAttempted,NumCorrect, Type, question, Answer, Question.MULTI_CHOICE_MULTI_ANSWER);
		answerChoices = AnswerChoices;
		randomizeAnswers = RandomizeAnswers;
		
	}
	
	public MultiChoiceMultiAnswerQuestion(Integer ID, Integer QuizID, Integer QNumber, Integer PageNumber, Integer QuestionTime, 
										  String Type, String question, String[] Answer, String[] AnswerChoices, boolean RandomizeAnswers){
		super(ID,QuizID,QNumber,PageNumber,QuestionTime, Type, question, Answer, Question.MULTI_CHOICE_MULTI_ANSWER);
		answerChoices = AnswerChoices;
		randomizeAnswers = RandomizeAnswers;
		
	}
	
	@Override
	public String[] getResponses(HttpServletRequest request, int i){
		List<String> answers = new ArrayList<String>();
		for(int j = 0; j<answerChoices.length; j++){
			String ans = request.getParameter(ANSWER+i+""+j);
			if(ans != null) answers.add(ans);
		}
		return(String[]) answers.toArray(new String[]{});
	}
	
	@Override
	public int numCorrect(String[] responseStrs){
		Set<String> answers = new HashSet<String>();
		for(int k = 0; k<answerStrs.length; k++){
			for(int j=0; j<answerStrs[k].length; j++){
				answers.add(answerStrs[k][j]);
			}
		}
		
		Set<String> responses = new HashSet<String>();
		for(int i = 0; i<responseStrs.length; i++){
			responses.add(responseStrs[i]);
		}
		
		int numCorrect = 0;
		if(responses.equals(answers)) numCorrect = 1;
		
		return numCorrect;
	}
	
	public void printToJSP(JspWriter out, int i) throws IOException {
		out.println(getQuestionStr() + "<br>");
		for(int j = 0 ; j < answerChoices.length; j++){
			out.println("<input type=\"checkbox\" name=\""+Question.ANSWER+i+""+j+"\" value=\""+ answerChoices[j] +"\">" + answerChoices[j] + "<br>");
		}
	}
}
