package sharedHtmlGenerators;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpSession;

import java.sql.ResultSetMetaData;

import quizsite.DatabaseConnection;

public class HtmlQuizThumbnailGenerator {
	// Strings for generating the link to a user's profile
	public static final String profileURL = "/QuizSite/profile.jsp";
	public static final String profileQueryString = "id";
	public static final String quizURL = "/QuizSite/QuizHomepageServlet";
	public static final String quizQueryString = "quizid";
	
	// Strings of class names in the HTML
	public static final String classSearchResults = "quizsearchresults";
	public static final String classQuizThumbnail = "quizthumbnail";
	public static final String classQuizName = "quizName";
	public static final String classStats = "quizthumbnailstats";
	public static final String classQuizDescription = "quizthumbnaildescription";
	
	// ResultSet column names
	public static final String colQuizId = "quizid";
	public static final String colAuthorId = "authorid";
	public static final String colUsername = "username";
	public static final String colDateMade = "datemade";
	public static final String colQuizName = "name";
	public static final String colQuizDescription = "description";
	public static final String colPracticeMode = "practicemode";
	public static final String colNumQuestions = "numquestions";
	public static final String colNumTaken = "numtaken";
	
	// Text to include in labels and buttons
	public static final String labelCreated = "Created by: ";
	public static final String labelDateMade = "Created on: ";
	public static final String labelNumQuestions = "Questions: ";
	public static final String labelNumTaken = "Taken: ";
	public static final String labelPractice = "Practice Mode: ";
			
	
	public HtmlQuizThumbnailGenerator() {
	}
	
	/**
	 * Given a ResultSet, convert the current row of the ResultSet to a string quiz thumbnail
	 * @param rs
	 * @param dc
	 * @param session
	 * @return
	 */
	public static String getThumbnail(ResultSet rs) {
		// Verify that inputs are valid
		if (rs == null) return "";
		int row = -1;
		try {
			row = rs.getRow();
		} catch (SQLException e) {
			return "";
		}
		if (row < 1) return "";
				
		// Get the relevant data about the user from rs
		Integer quizId = null;
		Integer authorId = null;
		String username = null;
		String dateMade = null;
		String quizName = null;
		String description = null;
		Integer practiceMode = null;
		Integer numQuestions = null;
		Integer numTaken = null;
		try {
			// Populate all fields
			quizId = rs.getInt(colQuizId);
			authorId = rs.getInt(colAuthorId);
			username = rs.getString(colUsername);
			dateMade = rs.getString(colDateMade);
			quizName = rs.getString(colQuizName);
			description = rs.getString(colQuizDescription);
			practiceMode = rs.getInt(colPracticeMode);
			numQuestions = rs.getInt(colNumQuestions);
			numTaken = rs.getInt(colNumTaken);
		} catch (SQLException e) {
			System.out.println("HtmlQuizThumbnailGenerator: Invalid column access in ResultSet");
			return "";
		}
		
		// Return empty string if any required fields are null
		if (quizId == null || authorId == null || username == null || quizName == null) return "";
		
		// Return empty string if any required fields are empty
		if (username.isEmpty() || quizName.isEmpty()) return "";
		if (quizId < 0 || authorId < 1) {
			System.out.println("HtmlQuizThumbnailGenerator.getHtml(): Error: Got quizid=" + quizId + " and authorId = " + authorId + ". IDs must be >= 1");
			return "";
		}
				
		// Format the user's join date here
		String[] dateMadeFormatted = quizsite.FormatDateTime.getUserDateTime(dateMade);
		dateMade = dateMadeFormatted[0];
		
		// Get the practice string here
		String practiceString = "";
		if (practiceMode == 0) {
			practiceString = "disabled";
		} else {
			practiceString = "enabled";
		}
		
		// Generate HTML!
		StringBuilder sb = new StringBuilder();
		String ls = System.getProperty("line.separator");
		
		// Start the profile thumbnail
		sb.append("<div class =\"" + classQuizThumbnail + "\">" + ls);
		
		// Add the quiz name
		String thisQuizUrl = quizURL + "?" + quizQueryString + "=" + quizId;
		sb.append("\t<a href=\"" + thisQuizUrl + "\">" + ls);
		sb.append("\t\t<div class=\"" + classQuizName + "\">" + quizName + "</div>" + ls);
		sb.append("\t</a>" + ls);
		
		// Add the quiz stats
		sb.append("\t<div class=\"" + classStats + "\">" + ls);
		sb.append("\t\t<span><strong>" + labelCreated + "<a href=\"" + profileURL + "?" + profileQueryString + "=" + authorId + "\">" + username + "</a></strong></span>" + ls);
		sb.append("\t\t<span><strong>" + labelDateMade + "</strong>" + dateMade + "</span>" + ls);
		sb.append("\t\t<span><strong>" + labelNumQuestions + "</strong>" + numQuestions + "</span>" + ls);
		sb.append("\t\t<span><strong>" + labelNumTaken + "</strong>" + numTaken + " times</span>" + ls);
		sb.append("\t\t<span><strong>" + labelPractice + "</strong> <em>" + practiceString + "</em></span>" + ls);
		sb.append("\t</div>");
		
		// Add the quiz description
		sb.append("\t<div class=\"" + classQuizDescription + "\">" + ls);
		sb.append("\t\t<strong><em>Description:&nbsp&nbsp&nbsp</em></strong>" + description + ls);
		sb.append("\t</div>" + ls);
		
		// Close out the thumbnail
		sb.append("</div>" + ls);
		return sb.toString();
	}
	
	/**
	 * Given a ResultSet, convert all its entries into one big set of quiz thumbnails
	 */
	public static String getHtml(ResultSet rs) {
		if (rs == null) return "";

		StringBuilder sb = new StringBuilder("");
		String ls = System.getProperty("line.separator");
		sb.append("<div class=\"" + classSearchResults + "\">" + ls);

		// Check if rs is empty
		try {
			if (!rs.first()) {
				sb.append("<div class=\"noSearchResults\">No results found :(</div>" + ls);
				sb.append("</div>" + ls);
				return sb.toString();
			}
		} catch (SQLException e) {
			return "";
		}
		
		
		try {
			rs.beforeFirst();
			while (true) {
				rs.next();
				sb.append(getThumbnail(rs));
				
				if (rs.isLast()) break;
			}
		} catch (SQLException e) {
			System.out.println("HtmlQuizThumbnailGenerator.getHtml(): Error occurred partway through HTML generation. Returning partial results.");
			sb.append("</div>" + ls);
			return sb.toString();
		}
		
		sb.append("</div>" + ls);
		return sb.toString();
	}
}
