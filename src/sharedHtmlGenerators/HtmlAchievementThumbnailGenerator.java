package sharedHtmlGenerators;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpSession;

import quizsite.DatabaseConnection;

public class HtmlAchievementThumbnailGenerator {
	// Column names
	public static final String colAchId = "id";
	public static final String colAchName = "name";
	public static final String colIcon = "icon";
	
	// Class names
	public static final String classIcon = "nanoBadge";
	public static final String baseClass = "achievement-"; // All achievements have a class = achievement-ID, such as achievement-0 for amateur author
	public static final String classDiv = "tinyAchievements";
	
	/**
	 * Get HTML for a single thumbnail from the current row of the ResultSet
	 */
	public static String getThumbnail(ResultSet rs) {
		if (rs == null) return "";
		
		StringBuilder sb = new StringBuilder();
		String ls = System.getProperty("line.separator");
		
		// Get parameters
		Integer id = null;
		String name = null;
		String icon = null;
		try {
			id = rs.getInt(colAchId);
			name = rs.getString(colAchName);
			icon = rs.getString(colIcon);
		} catch (SQLException e) {
			return "";
		}
		
		// Ensure that parameters are valid
		if (id == null || id < 0 || name == null || name.isEmpty() || icon == null || icon.isEmpty()) return "";
		
		// Append HTML
		String badgeClass = classIcon + " " + baseClass + id.toString();
		sb.append("\t<span><img src=\"" + icon + "\" alt=\"" + name + "\" class=\"" + badgeClass + "\" /></span>" + ls);
		
		return sb.toString();
	}
	
	/**
	 * Given a ResultSetof achievements, convert all entries of the ResultSet into a block of HTML
	 * @param args
	 */
	public static String getHtml(ResultSet rs) {
		StringBuilder sb = new StringBuilder();
		String ls = System.getProperty("line.separator");
		
		sb.append("\t<div class=\"" + classDiv + "\">" + ls);
		try {
			if (rs.first()) {
				rs.beforeFirst();
				while(true) {
					rs.next();
					sb.append(getThumbnail(rs));
					if (rs.isLast()) break;
				}
			}
		} catch (SQLException e) {
			System.out.println("\t\tAchievement thumbnail generator: SQLException. Returning partial results");
		} finally {
			sb.append("\t</div>" + ls);
		}
		System.out.println(sb.toString());
		return sb.toString();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
