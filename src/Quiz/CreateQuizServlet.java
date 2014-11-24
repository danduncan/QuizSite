package Quiz;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import quizsite.DatabaseConnection;
import quizsite.MyDBInfo;
import users.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quizsite.SiteManager;
import connection.QuizConnection;

@WebServlet("/CreateQuizServlet")
public class CreateQuizServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String STR_DELIM = ";";
	private static final String ARRAY_DELIM = "&&&";
	
	public static final String ORDER_MATTERS = "Order Matters";
	public static final String RANDOM_ORDER = "Random Order";
	public static final String MULTI_PAGE = "Mutli Page";
	public static final String CORRECTION = "Immediate Correction";
	public static final String QUIZ_CREATED = "Quiz Created";
	public static final String QUESTION_TYPE = "Question Type";
	public static final String QUESTION = "Question";
	public static final String ANSWER = "Answer";
	public static final String IMAGE_URL = "Image URL";
	public static final String MC_CHOICES = "Multiple Choice Choices";
	public static final String NUM_ANSWERS = "Number of answers";
	public static final String DESCRIPTION = "Description";
	public static final String QUIZ_NAME = "Quiz name";
	public static final String QUESTION_RESPONSE = "Question Response";
	public static final String PIC_RESPONSE = "Picture Response";
	public static final String FILL_BLANK = "Fill in the Blank";
	public static final String MULTIPLE_CHOICE = "Multiple Choice";
	public static final String MULTI_ANSWER = "Multiple Answer";
	public static final String MULTI_ANSWER_MULTI_CHOICE = "Multiple Choice with Mulitple Answers";
	

	public CreateQuizServlet(){}

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		forwardRequest(request, response, "createquizpage.jsp");
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String randOrd = request.getParameter(RANDOM_ORDER);
		String mPage = request.getParameter(MULTI_PAGE);
		String correction = request.getParameter(CORRECTION);
		String qType = request.getParameter(QUESTION_TYPE);
		
		if(randOrd!=null && mPage != null && correction != null){
			boolean randomOrder = Boolean.parseBoolean(randOrd);
			boolean multiPage = Boolean.parseBoolean(mPage);
			boolean immediateCorrection = Boolean.parseBoolean(correction);	
			String name = request.getParameter(QUIZ_NAME);
			String description = request.getParameter(DESCRIPTION);
			
			User user = (User) request.getSession().getAttribute("user");
			
			Integer practicemode = 0;			
			
			//get question type table and db connection
			ServletContext sc = request.getServletContext();
			ArrayList<QuestionType> qtypes = (ArrayList<QuestionType>) sc.getAttribute("questiontypes");
			DatabaseConnection dc = (DatabaseConnection) sc.getAttribute("DatabaseConnection");
			
			SiteManager sm = (SiteManager) request.getServletContext().getAttribute("SiteManager");
			int quizid = sm.popNextQuizID();
			
			Quiz quiz = new Quiz(quizid,user.id, name, description, practicemode,  multiPage, randomOrder, immediateCorrection, new QuizConnection(dc,qtypes));
			
			request.getSession().setAttribute(QUIZ_CREATED, quiz);
			forwardRequest(request, response, "createquestionpage.jsp");
		}else if(qType != null){			
			request.getSession().setAttribute(QUESTION_TYPE, qType);	
			if(qType.equals(QUESTION_RESPONSE)){
				forwardRequest(request, response, "createquestionresponse.jsp");
			}else if(qType.equals(FILL_BLANK)){
				forwardRequest(request, response, "createfillblank.jsp");
			}else if(qType.equals(PIC_RESPONSE)){
				forwardRequest(request, response, "createpictureresponse.jsp");
			}else if(qType.equals(MULTIPLE_CHOICE)){
				forwardRequest(request, response, "createmultiplechoice.jsp");
			}else if(qType.equals(MULTI_ANSWER_MULTI_CHOICE)){
				forwardRequest(request, response, "createmultichoicemultianswer.jsp");
			}else if(qType.equals(MULTI_ANSWER)){
				String numAnswerFields = request.getParameter(NUM_ANSWERS);
				int fields = 0;
				if(numAnswerFields != null) fields = Integer.parseInt(numAnswerFields);
				ShowMultiAnswer(request, response, fields);
			}
		}else{
			qType = (String)request.getSession().getAttribute(QUESTION_TYPE);
			Quiz quiz = (Quiz) request.getSession().getAttribute(QUIZ_CREATED);
			String question = request.getParameter(QUESTION);
			String answers[] = getAnswers(request);
			
			SiteManager sm = (SiteManager) request.getServletContext().getAttribute("SiteManager");
			Integer qID = sm.popNextQuestionID();
			

			//need input for question time
			Integer questiontime = 10;
			
			Question q = null;
			if(qType.equals(QUESTION_RESPONSE)){
				q = new QuestionResponse(qID, quiz.id, quiz.questions.size(), quiz.questions.size(), questiontime, MyDBInfo.QUESTIONRESPONSE, question, answers);
			}else if(qType.equals(FILL_BLANK)){
				String restOfQuestion = request.getParameter(QUESTION+2);
				q = new FillBlankQuestion(qID, quiz.id, quiz.questions.size(), quiz.questions.size(), questiontime, MyDBInfo.QUESTIONFILLBLANK, question, restOfQuestion, answers);
			}else if(qType.equals(PIC_RESPONSE)){
				String url = request.getParameter(IMAGE_URL);
				q = new PictureResponseQuestion(qID, quiz.id, quiz.questions.size(), quiz.questions.size(), questiontime, MyDBInfo.QUESTIONPICTURE, question, answers, url);
			}else if(qType.equals(MULTIPLE_CHOICE)){
				String choiceStr = request.getParameter(MC_CHOICES);
				String[] choices = stringToArray(choiceStr, STR_DELIM);
				//need input for randomized order
				q = new MultipleChoiceQuestion(qID, quiz.id, quiz.questions.size(), quiz.questions.size(), questiontime, MyDBInfo.QUESTIONMULTIPLECHOICE, question, answers[0], choices, false);
			}else if(qType.equals(MULTI_ANSWER_MULTI_CHOICE)){
				String choiceStr = request.getParameter(MC_CHOICES);
				String[] choices = stringToArray(choiceStr, STR_DELIM);
				//need input for randomized order
				q = new MultiChoiceMultiAnswerQuestion(qID, quiz.id, quiz.questions.size(), quiz.questions.size(), questiontime, MyDBInfo.QUESTIONMULTIPLECHOICEMULTIPLEANSWER, question, answers, choices, false);
			}else if(qType.equals(MULTI_ANSWER)){
				String orderMatters = request.getParameter(ORDER_MATTERS);
				List<String[]> answerList = new LinkedList<String[]>();
				for(int i=0; i<answers.length; i++){
					if(answers[i] == null) continue; 
					answerList.add(stringToArray(answers[i], STR_DELIM));
				}
				String[][] answerArray = answerList.toArray(new String[][]{});
				q = new MultiAnswerQuestion(qID, quiz.id, quiz.questions.size(), quiz.questions.size(), questiontime, MyDBInfo.QUESTIONMULTIPLEANSWER,question, answerArray, Boolean.parseBoolean(orderMatters));
			}
			quiz.addQuestion(q);
			request.getSession().setAttribute("Quiz", quiz);
			forwardRequest(request, response, "createquestionpage.jsp");
		}
	}
	
	private void forwardRequest(HttpServletRequest request, HttpServletResponse response, String dest) throws ServletException, IOException{
		RequestDispatcher dispatch = request.getRequestDispatcher(dest);
		dispatch.forward(request, response);
	}
	
	private void ShowMultiAnswer(HttpServletRequest request, HttpServletResponse response, int numAnswerFields) throws IOException, ServletException{
		request.getSession().setAttribute(NUM_ANSWERS, numAnswerFields);
		if(numAnswerFields == 0){
			forwardRequest(request, response, "createmultipleanswer1.jsp");
		}else{
			forwardRequest(request, response, "createmultipleanswer2.jsp");
		}
	}
	
	private String[] stringToArray(String str, String delim){
		String[] arr = str.split(delim);
		for(int i = 0; i<arr.length; i++){
			arr[i] = arr[i].trim();
		}
		return arr;
	}
	
	private String[] getAnswers(HttpServletRequest request){
		String qType = (String)request.getSession().getAttribute(QUESTION_TYPE);		
		String[] answers = null;
		
		if(qType.equals(MULTI_ANSWER)){
			Integer numAnswerFields = (Integer)request.getSession().getAttribute(NUM_ANSWERS);
			String ans = "";
			for(int i=0; i<numAnswerFields; i++){
				ans += request.getParameter(ANSWER+i);
				ans += ARRAY_DELIM;
			}
			answers = stringToArray(ans, ARRAY_DELIM);
		}else{
			String ans = request.getParameter(ANSWER);
			answers = stringToArray(ans, STR_DELIM);
		}
		return answers;
	}
}
