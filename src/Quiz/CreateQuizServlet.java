package Quiz;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/CreateQuizServlet")
public class CreateQuizServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final String RANDOM_ORDER = "Random Order";
	private static final String MULTI_PAGE = "Mutli Page";
	private static final String CORRECTION = "Immediate Correction";
	public static final String QUIZ_CREATED = "Quiz Created";
	private static final String QUESTION_TYPE = "Question Type";
	private static final String QUESTION = "Question";
	private static final String ANSWER = "Answer";
	private static final String IMAGE_URL = "Image URL";
	private static final String MC_CHOICES = "Multiple Choice Choices";
	private static final String STR_DELIM = ";";
	
	private static final String QUESTION_RESPONSE = "Question Response";
	private static final String PIC_RESPONSE = "Picture Response";
	private static final String FILL_BLANK = "Fill in the Blank";
	private static final String MULTIPLE_CHOICE = "Multiple Choice";
	private static final String MULTI_ANSWER = "Multiple Answer";
	private static final String MULTI_ANSWER_MULTI_CHOICE = "Multiple Choice with Mulitple Answers";
	
	private static final String NUM_ANSWERS = "Number of answers";

    public CreateQuizServlet(){}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		createQuizPage(response);
	}
	
	private void createQuizPage(HttpServletResponse response) throws IOException{
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE html>");
		out.println("<head>");
		out.println("<meta charset=\"UTF-8\" />");
		out.println("<title>Create Quiz</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<h1>Select Quiz Options</h1>");
		out.println("<form action=\"CreateQuizServlet\" method=\"post\">");
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
		out.println("</body>");
		out.println("</html>");
	}
	
	private void createQuestionPage(HttpServletRequest request, HttpServletResponse response) throws IOException{
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE html>");
		out.println("<head>");
		out.println("<meta charset=\"UTF-8\" />");
		out.println("<title>Create a Question</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<h1>Create a Question </h1>");
		out.println("<form action=\"CreateQuizServlet\" method=\"post\">");
		out.println("<br>What type of question would you like to add?<br>");
		out.println("<select name=\""+QUESTION_TYPE+"\">");
		out.println("<option value=\""+QUESTION_RESPONSE+"\">"+QUESTION_RESPONSE+"</option>");
		out.println("<option value=\""+FILL_BLANK+"\">"+FILL_BLANK+"</option>");
		out.println("<option value=\""+PIC_RESPONSE+"\">"+PIC_RESPONSE+"</option>");
		out.println("<option value=\""+MULTIPLE_CHOICE+"\">"+MULTIPLE_CHOICE+"</option>");
		out.println("<option value=\""+MULTI_ANSWER+"\">"+MULTI_ANSWER+"</option>");
		out.println("<option value=\""+MULTI_ANSWER_MULTI_CHOICE+"\">"+MULTI_ANSWER_MULTI_CHOICE+"</option>");
		out.println("</select>");
		out.println("<br><input type=\"submit\" value=\"Add Question\"></form>");
		out.println("<form action=\"BasicQuizServlet\" method=\"get\">");
		out.println("If you are done adding questions please press the \"Complete Quiz\" button.<br>");
		out.println("<input type=\"submit\" value=\"Complete Quiz\"></form>");
		out.println("</body>");
		out.println("</html>");	
	}
	
	private void ShowQuestionResponse(HttpServletRequest request, HttpServletResponse response) throws IOException{
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE html>");
		out.println("<head>");
		out.println("<meta charset=\"UTF-8\" />");
		out.println("<title>Create a Question</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<h1>"+ QUESTION_RESPONSE+"</h1>");
		out.println("Please fill in the question and answer fields below.");
		out.println("If the question has multiple answers please separate each one by a \";\".");
		out.println("<form action=\"CreateQuizServlet\" method=\"post\">");
		out.println("Question: <input type=\"text\" name=\""+QUESTION+"\"><br>");
		out.println("Answers: <input type=\"text\" name=\""+ANSWER+"\"><br>");
		out.println("<br><input type=\"submit\" value=\"Complete Question\"></form>");
		out.println("</body>");
		out.println("</html>");
	}
	
	private void ShowFillBlank(HttpServletRequest request, HttpServletResponse response) throws IOException{
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE html>");
		out.println("<head>");
		out.println("<meta charset=\"UTF-8\" />");
		out.println("<title>Create a Question</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<h1>"+FILL_BLANK+"</h1>");
		out.println("Please fill in the question and answer fields below.");
		out.println("If the question has multiple answers please separate each one by a \";\".");
		out.println("<form action=\"CreateQuizServlet\" method=\"post\">");
		out.println("Question part one: <input type=\"text\" name=\""+QUESTION+"\"><br>");
		out.println("Answers: <input type=\"text\" name=\""+ANSWER+"\"><br>");
		out.println("Question part two: <input type=\"text\" name=\""+QUESTION+2+"\"><br>");
		out.println("<br><input type=\"submit\" value=\"Complete Question\"></form>");
		out.println("</body>");
		out.println("</html>");
	}
	
	private void ShowPictureResponse(HttpServletRequest request, HttpServletResponse response) throws IOException{
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE html>");
		out.println("<head>");
		out.println("<meta charset=\"UTF-8\" />");
		out.println("<title>Create a Question</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<h1>"+PIC_RESPONSE+"</h1>");
		out.println("Please fill in the question and answer fields below.");
		out.println("If the question has multiple answers please separate each one by a \";\".");
		out.println("<form action=\"CreateQuizServlet\" method=\"post\">");
		out.println("Question: <input type=\"text\" name=\""+QUESTION+"\"><br>");
		out.println("Image URL: <input type=\"text\" name=\""+IMAGE_URL+"\"><br>");
		out.println("Answers: <input type=\"text\" name=\""+ANSWER+"\"><br>");
		out.println("<br><input type=\"submit\" value=\"Complete Question\"></form>");
		out.println("</body>");
		out.println("</html>");
	}
	
	private void ShowMultipleChoice(HttpServletRequest request, HttpServletResponse response) throws IOException{
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE html>");
		out.println("<head>");
		out.println("<meta charset=\"UTF-8\" />");
		out.println("<title>Create a Question</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<h1>"+MULTIPLE_CHOICE+"</h1>");
		out.println("Please fill in the question, choice, and answer fields below. Please separate choices by a \";\".");
		out.println("The question should have one correct answer.");
		out.println("<form action=\"CreateQuizServlet\" method=\"post\">");
		out.println("Question: <input type=\"text\" name=\""+QUESTION+"\"><br>");
		out.println("Choices: <input type=\"text\" name=\""+MC_CHOICES+"\"><br>");
		out.println("Answers: <input type=\"text\" name=\""+ANSWER+"\"><br>");
		out.println("<br><input type=\"submit\" value=\"Complete Question\"></form>");
		out.println("</body>");
		out.println("</html>");
	}
	
	private void ShowMultiChoiceMultiAnswer(HttpServletRequest request, HttpServletResponse response) throws IOException{
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE html>");
		out.println("<head>");
		out.println("<meta charset=\"UTF-8\" />");
		out.println("<title>Create a Question</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<h1>"+MULTI_ANSWER_MULTI_CHOICE+"</h1>");
		out.println("Please fill in the question, choice, and answer fields below. Please separate choices by a \";\".");
		out.println("If the question has multiple answers please separate each one by a \";\".");
		out.println("<form action=\"CreateQuizServlet\" method=\"post\">");
		out.println("Question: <input type=\"text\" name=\""+QUESTION+"\"><br>");
		out.println("Choices: <input type=\"text\" name=\""+MC_CHOICES+"\"><br>");
		out.println("Answers: <input type=\"text\" name=\""+ANSWER+"\"><br>");
		out.println("<br><input type=\"submit\" value=\"Complete Question\"></form>");
		out.println("</body>");
		out.println("</html>");
	}
	
	private String[] stringToArray(String str){
		String[] arr = str.split(STR_DELIM);
		for(int i = 0; i<arr.length; i++){
			arr[i] = arr[i].trim();
		}
		return arr;
	}
	
	private String[] getAnswers(HttpServletRequest request){
		String qType = (String)request.getSession().getAttribute(QUESTION_TYPE);		
		String[] answers = null;
		
		if(qType.equals(MULTI_ANSWER)){
			
		}else{
			String ans = request.getParameter(ANSWER);
			answers = stringToArray(ans);
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
			Quiz quiz = new Quiz(randomOrder, multiPage, immediateCorrection);
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
				
			}
		}else{
			qType = (String)request.getSession().getAttribute(QUESTION_TYPE);
			Quiz quiz = (Quiz) request.getSession().getAttribute(QUIZ_CREATED);
			
			String question = request.getParameter(QUESTION);
			String answers[] = getAnswers(request);
			
			Question q = null;
			if(qType.equals(QUESTION_RESPONSE)){
				q = new QuestionResponse(question, answers);
			}else if(qType.equals(FILL_BLANK)){
				String restOfQuestion = request.getParameter(QUESTION+2);
				q = new FillBlankQuestion(question, answers, restOfQuestion);
			}else if(qType.equals(PIC_RESPONSE)){
				String url = request.getParameter(IMAGE_URL);
				q = new PictureResponseQuestion(question, url, answers);
			}else if(qType.equals(MULTIPLE_CHOICE)){
				String choiceStr = request.getParameter(MC_CHOICES);
				String[] choices = stringToArray(choiceStr);
				q = new MultipleChoiceQuestion(question, choices, answers[0]);
			}else if(qType.equals(MULTI_ANSWER_MULTI_CHOICE)){
				String choiceStr = request.getParameter(MC_CHOICES);
				String[] choices = stringToArray(choiceStr);
				q = new MultiChoiceMultiAnswerQuestion(question, choices, answers);
			}else if(qType.equals(MULTI_ANSWER)){
				
			}
			quiz.addQuestion(q);
			createQuestionPage(request, response);
		}
	}
}
