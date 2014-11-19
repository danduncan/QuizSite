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
	
	private static final String ORDER_MATTERS = "Order Matters";
	private static final String RANDOM_ORDER = "Random Order";
	private static final String MULTI_PAGE = "Mutli Page";
	private static final String CORRECTION = "Immediate Correction";
	public static final String QUIZ_CREATED = "Quiz Created";
	private static final String QUESTION_TYPE = "Question Type";
	private static final String QUESTION = "Question";
	private static final String ANSWER = "Answer";
	private static final String IMAGE_URL = "Image URL";
	private static final String MC_CHOICES = "Multiple Choice Choices";
	private static final String NUM_ANSWERS = "Number of answers";
	private static final String DESCRIPTION = "Description";
	private static final String QUIZ_NAME = "Quiz name";
	private static final String STR_DELIM = ";";
	private static final String ARRAY_DELIM = "&&&";
	
	private static final String QUESTION_RESPONSE = "Question Response";
	private static final String PIC_RESPONSE = "Picture Response";
	private static final String FILL_BLANK = "Fill in the Blank";
	private static final String MULTIPLE_CHOICE = "Multiple Choice";
	private static final String MULTI_ANSWER = "Multiple Answer";
	private static final String MULTI_ANSWER_MULTI_CHOICE = "Multiple Choice with Mulitple Answers";

    public CreateQuizServlet(){}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		createQuizPage(response);
	}
	
	private PrintWriter writeHeader(HttpServletResponse response, String title, String headline) throws IOException{
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE html>");
		out.println("<head>");
		out.println("<meta charset=\"UTF-8\" />");
		out.println("<title>"+title+"</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<h1>"+headline+"</h1>");
		return out;
	}
	
	private void createQuizPage(HttpServletResponse response) throws IOException{
		PrintWriter out = writeHeader(response, "Create Quiz", "Select Quiz Options");
		out.println("<form action=\"CreateQuizServlet\" method=\"post\">");
		out.println("Quiz name: <input type=\"text\" name=\""+QUIZ_NAME+"\"><br>");
		out.println("Description: <input type=\"text\" name=\""+DESCRIPTION+"\"><br>");
		out.println("Should the questions be displayed in random order?<br>");
		out.println("<select name=\""+RANDOM_ORDER+"\">");
		out.println("<option value=\"false\">No</option>");
		out.println("<option value=\"true\">Yes</option>");
		out.println("</select>");
		out.println("<br>Should the quiz be displayed on multiple pages?<br>");
		out.println("<select name=\""+MULTI_PAGE+"\">");
		out.println("<option value=\"false\">No</option>");
		out.println("<option value=\"true\">Yes</option>");
		out.println("</select>");
		out.println("<br>Should the quiz display correct answers?<br>");
		out.println("<select name=\""+CORRECTION+"\">");
		out.println("<option value=\"false\">No</option>");
		out.println("<option value=\"true\">Yes</option>");
		out.println("</select>");
		out.println("<br><input type=\"submit\" value=\"Create Questions\"></form>");
		out.println("</body></html>");
	}
	
	private void createQuestionPage(HttpServletRequest request, HttpServletResponse response) throws IOException{
		PrintWriter out = writeHeader(response, "Create a Question", "Create a Question");
		out.println("<form action=\"CreateQuizServlet\" method=\"post\">");
		out.println("<br>What type of question would you like to add to your quiz?<br>");
		out.println("<select name=\""+QUESTION_TYPE+"\">");
		out.println("<option value=\""+QUESTION_RESPONSE+"\">"+QUESTION_RESPONSE+"</option>");
		out.println("<option value=\""+FILL_BLANK+"\">"+FILL_BLANK+"</option>");
		out.println("<option value=\""+PIC_RESPONSE+"\">"+PIC_RESPONSE+"</option>");
		out.println("<option value=\""+MULTIPLE_CHOICE+"\">"+MULTIPLE_CHOICE+"</option>");
		out.println("<option value=\""+MULTI_ANSWER+"\">"+MULTI_ANSWER+"</option>");
		out.println("<option value=\""+MULTI_ANSWER_MULTI_CHOICE+"\">"+MULTI_ANSWER_MULTI_CHOICE+"</option>");
		out.println("</select>");
		out.println("<br><input type=\"submit\" value=\"Add Question\"></form>");
		out.println("<form action=\"ShowQuizServlet\" method=\"get\">");
		out.println("If you are done adding questions please press the \"Complete Quiz\" button below.<br>");
		out.println("<input type=\"submit\" value=\"Complete Quiz\"></form>");
		out.println("</body></html>");
	}
	
	private void ShowQuestionResponse(HttpServletRequest request, HttpServletResponse response) throws IOException{
		PrintWriter out = writeHeader(response, "Create a Question", QUESTION_RESPONSE);
		out.println("Please fill in the question and answer fields below.");
		out.println("If the question has multiple answers then please separate each one by a \";\".");
		out.println("<form action=\"CreateQuizServlet\" method=\"post\">");
		out.println("Question: <input type=\"text\" name=\""+QUESTION+"\"><br>");
		out.println("Answers: <input type=\"text\" name=\""+ANSWER+"\"><br>");
		out.println("<br><input type=\"submit\" value=\"Complete Question\"></form>");
		out.println("</body></html>");
	}
	
	private void ShowFillBlank(HttpServletRequest request, HttpServletResponse response) throws IOException{
		PrintWriter out = writeHeader(response, "Create a Question", FILL_BLANK);
		out.println("Please fill in the question and answer fields below.");
		out.println("If the question has multiple answers then please separate each one by a \";\".");
		out.println("<form action=\"CreateQuizServlet\" method=\"post\">");
		out.println("Question before blank: <input type=\"text\" name=\""+QUESTION+"\"><br>");
		out.println("Answers for blank: <input type=\"text\" name=\""+ANSWER+"\"><br>");
		out.println("Question after blank: <input type=\"text\" name=\""+QUESTION+2+"\"><br>");
		out.println("<br><input type=\"submit\" value=\"Complete Question\"></form>");
		out.println("</body></html>");
	}
	
	private void ShowPictureResponse(HttpServletRequest request, HttpServletResponse response) throws IOException{
		PrintWriter out = writeHeader(response, "Create a Question", PIC_RESPONSE);
		out.println("Please fill in the question, image URL, and answer fields below.");
		out.println("If the question has multiple answers then please separate each one by a \";\".");
		out.println("<form action=\"CreateQuizServlet\" method=\"post\">");
		out.println("Question: <input type=\"text\" name=\""+QUESTION+"\"><br>");
		out.println("Image URL: <input type=\"text\" name=\""+IMAGE_URL+"\"><br>");
		out.println("Answers: <input type=\"text\" name=\""+ANSWER+"\"><br>");
		out.println("<br><input type=\"submit\" value=\"Complete Question\"></form>");
		out.println("</body></html>");
	}
	
	private void ShowMultipleChoice(HttpServletRequest request, HttpServletResponse response) throws IOException{
		PrintWriter out = writeHeader(response, "Create a Question", MULTIPLE_CHOICE);
		out.println("Please fill in the question, choices, and answer fields below. Please separate choices by a \";\".");
		out.println("This question type may only have one correct answer.");
		out.println("<form action=\"CreateQuizServlet\" method=\"post\">");
		out.println("Question: <input type=\"text\" name=\""+QUESTION+"\"><br>");
		out.println("Choices: <input type=\"text\" name=\""+MC_CHOICES+"\"><br>");
		out.println("Answers: <input type=\"text\" name=\""+ANSWER+"\"><br>");
		out.println("<br><input type=\"submit\" value=\"Complete Question\"></form>");
		out.println("</body></html>");
	}
	
	private void ShowMultiChoiceMultiAnswer(HttpServletRequest request, HttpServletResponse response) throws IOException{
		PrintWriter out = writeHeader(response, "Create a Question", MULTI_ANSWER_MULTI_CHOICE);
		out.println("Please fill in the question, choices, and answer fields below. Please separate choices by a \";\".");
		out.println("If the question has multiple answers then please separate each one by a \";\".");
		out.println("<form action=\"CreateQuizServlet\" method=\"post\">");
		out.println("Question: <input type=\"text\" name=\""+QUESTION+"\"><br>");
		out.println("Choices: <input type=\"text\" name=\""+MC_CHOICES+"\"><br>");
		out.println("Answers: <input type=\"text\" name=\""+ANSWER+"\"><br>");
		out.println("<br><input type=\"submit\" value=\"Complete Question\"></form>");
		out.println("</body></html>");
	}
	
	private void ShowMultiAnswer(HttpServletRequest request, HttpServletResponse response, int numAnswerFields) throws IOException{
		request.getSession().setAttribute(NUM_ANSWERS, numAnswerFields);
		PrintWriter out = writeHeader(response, "Create a Question", MULTI_ANSWER);
		if(numAnswerFields == 0){
			out.println("<form action=\"CreateQuizServlet\" method=\"post\">");
			out.println("Does the order of the answers matter? <br>");
			out.println("<select name=\""+ORDER_MATTERS+"\">");
			out.println("<option value=\"false\">No</option>");
			out.println("<option value=\"true\">Yes</option>");
			out.println("</select><br>");
			out.println("How many answers does the question have?");
			out.println("<select name=\""+NUM_ANSWERS+"\">");
			out.println("<option value=\"1\">1</option>");
			out.println("<option value=\"2\">2</option>");
			out.println("<option value=\"3\">3</option>");
			out.println("<option value=\"4\">4</option>");
			out.println("<option value=\"5\">5</option>");
			out.println("<option value=\"6\">6</option>");
			out.println("<option value=\"7\">7</option>");
			out.println("<option value=\"8\">8</option>");
			out.println("</select><br>");
			out.println("<input type=\"hidden\" name=\""+QUESTION_TYPE+"\" value=\""+MULTI_ANSWER+"\">");
			out.println("<input type=\"submit\" value=\"Update Settings\"></form>");
		}else{
			String orderMatters = request.getParameter(ORDER_MATTERS);
			out.println("Please fill in the question and answer fields below.");
			out.println("If multiple answers are acceptable for an answer field then please separate each one by a \";\".");
			out.println("<form action=\"CreateQuizServlet\" method=\"post\">");
			out.println("Question: <input type=\"text\" name=\""+QUESTION+"\"><br>");
			for(int i=0; i<numAnswerFields; i++){
				out.println("Answers: <input type=\"text\" name=\""+ANSWER+i+"\"><br>");	
			}
			out.println("<input type=\"hidden\" name=\""+ORDER_MATTERS+"\" value=\""+orderMatters+"\">");
			out.println("<br><input type=\"submit\" value=\"Complete Question\"></form>");
			out.println("</body></html>");
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
			
			//create quiz using random id for now
			Date date = new Date();
			DateFormat df = new SimpleDateFormat("yyyyMMdd");			
			Random generator = new Random(); 
			int id = generator.nextInt(1000) + 1;
			Integer practicemode = 0;
			
			
			//get question type table and db connection
			ServletContext sc = request.getServletContext();
			ArrayList<QuestionType> qtypes = (ArrayList<QuestionType>) sc.getAttribute("questiontypes");
			DatabaseConnection dc = (DatabaseConnection) sc.getAttribute("DatabaseConnection");
			
			SiteManager sm = (SiteManager) request.getServletContext().getAttribute("sitemanager");
			int quizid = sm.popNextQuizID();
			
			Quiz quiz = new Quiz(quizid,user.id, name, description, practicemode,  multiPage, randomOrder, immediateCorrection, new QuizConnection(dc,qtypes));

			//Quiz quiz = new Quiz(randomOrder, multiPage, immediateCorrection);
			
			request.getSession().setAttribute(QUIZ_CREATED, quiz);
			createQuestionPage(request, response);
		}else if(qType != null){			
			request.getSession().setAttribute(QUESTION_TYPE, qType);	
			if(qType.equals(QUESTION_RESPONSE)){
				ShowQuestionResponse(request, response);
			}else if(qType.equals(FILL_BLANK)){
				ShowFillBlank(request, response);
			}else if(qType.equals(PIC_RESPONSE)){
				ShowPictureResponse(request, response);
			}else if(qType.equals(MULTIPLE_CHOICE)){
				ShowMultipleChoice(request, response);
			}else if(qType.equals(MULTI_ANSWER_MULTI_CHOICE)){
				ShowMultiChoiceMultiAnswer(request, response);
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
			
			Random generator = new Random(); 
			int id = generator.nextInt(1000) + 1;
			Integer questiontime = 10;
			
			Question q = null;
			if(qType.equals(QUESTION_RESPONSE)){
				q = new QuestionResponse(id, quiz.id, quiz.questions.size(), quiz.questions.size(), questiontime, MyDBInfo.QUESTIONRESPONSE, question, answers);
			}else if(qType.equals(FILL_BLANK)){
				String restOfQuestion = request.getParameter(QUESTION+2);
				q = new FillBlankQuestion(id, quiz.id, quiz.questions.size(), quiz.questions.size(), questiontime, MyDBInfo.QUESTIONFILLBLANK, question, restOfQuestion, answers);
			}else if(qType.equals(PIC_RESPONSE)){
				String url = request.getParameter(IMAGE_URL);
				q = new PictureResponseQuestion(id, quiz.id, quiz.questions.size(), quiz.questions.size(), questiontime, MyDBInfo.QUESTIONPICTURE, question, answers, url);
			}else if(qType.equals(MULTIPLE_CHOICE)){
				String choiceStr = request.getParameter(MC_CHOICES);
				String[] choices = stringToArray(choiceStr, STR_DELIM);
				//need input for randomized order
				q = new MultipleChoiceQuestion(id, quiz.id, quiz.questions.size(), quiz.questions.size(), questiontime, MyDBInfo.QUESTIONMULTIPLECHOICE, question, answers[0], choices, false);
			}else if(qType.equals(MULTI_ANSWER_MULTI_CHOICE)){
				String choiceStr = request.getParameter(MC_CHOICES);
				String[] choices = stringToArray(choiceStr, STR_DELIM);
				//need input for randomized order
				q = new MultiChoiceMultiAnswerQuestion(id, quiz.id, quiz.questions.size(), quiz.questions.size(), questiontime, MyDBInfo.QUESTIONMULTIPLECHOICEMULTIPLEANSWER, question, answers, choices, false);
			}else if(qType.equals(MULTI_ANSWER)){
				String orderMatters = request.getParameter(ORDER_MATTERS);
				List<String[]> answerList = new LinkedList<String[]>();
				for(int i=0; i<answers.length; i++){
					if(answers[i] == null) continue; 
					answerList.add(stringToArray(answers[i], STR_DELIM));
				}
				String[][] answerArray = answerList.toArray(new String[][]{});
				q = new MultiAnswerQuestion(id, quiz.id, quiz.questions.size(), quiz.questions.size(), questiontime, MyDBInfo.QUESTIONMULTIPLEANSWER,question, answerArray, Boolean.parseBoolean(orderMatters));
			}
			quiz.addQuestion(q);
			createQuestionPage(request, response);
		}
	}
}
