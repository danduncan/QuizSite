/**
 * This class takes a MySQL ResultSet and converts it into an HTML table
 * It can automatically extract the column names from the ResultSet, printing them in their original order
 * Alternatively, you can give it an array of column names and it will output only those columns (case-sensitive)
 * You can also specify a second array of column names. The table will print those column names at the top of the column
 * instead of the default name used by the ResultSet
 * Lastly, you can specify a class for the table. This is useful if you want to provide your own custom styling for the table.
 * Included classes for rows and cells:
 * "headerrow", "firstrow", "lastrow", "evenrow", "oddrow" for rows
 * "firstcol", "lastcol", or "innercol" for cells
 */

package sharedHtmlGenerators;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.ResultSetMetaData;

public class HtmlTableGenerator {

	public HtmlTableGenerator() {
	}
	
	public static String getHtml(ResultSet rs, String[] colNames, String[] printedColNames, String tableClass) {
		// First, check that the names specified for the ResultSet are valid
		if (!checkValidity(rs,colNames,printedColNames)) {
			System.out.println("HtmlTableGenerator.getHtml(): Invalid column names specified. Returning empty string.");
			return "";
		}
		
		// If colNames are not provided, have them be all columns
		if (colNames == null) {
			ResultSetMetaData rmd = null;
			int colCount = -1;
			try {
				rmd = rs.getMetaData();
				colCount = rmd.getColumnCount();
				colNames = new String[colCount];
				for (int i = 1; i <= colCount; i++) {  // col indices start with 1, not 0
					colNames[i-1] = rmd.getColumnLabel(i);
				}
			} catch (SQLException e) {
				System.out.println("HtmlTableGenerator.getHtml(): SQLException occurred generating colNames");
				return "";
			}
		}		
		
		// If printColNames is not specified, have it match colNames
		if (printedColNames == null) printedColNames = colNames;
		
		// Get the number of rows and check if the set is empty
		int rowCount = 0;
		try {
			rs.last();
			rowCount = rs.getRow(); // Row numbers start with 1, not 0
			rs.first();
		} catch(SQLException e) {
			System.out.println("HtmlTableGenerator.getHtml(): SQLException occurred getting rowCount");
			return "";
		}
		if (rowCount == 0) return "";
		
		// Now begin building the table
		StringBuilder sb = new StringBuilder();
		String ls = System.getProperty("line.separator");
		
		// Begin the table
		if (tableClass != null && !tableClass.isEmpty()) {
			sb.append("<table class=\"" + tableClass + "\">" + ls);
		} else {
			sb.append("<table>" + ls);
		}
		
		// Add the row of column names
		sb.append("\t" + "<tr class=\"headerrow\">" + ls);
		for (int i = 0; i < printedColNames.length; i++) {
			String c = "";
			if (i == 0) c = c + "firstcol ";
			if (i == printedColNames.length - 1) c = c + "lastcol ";
			if (i > 0 && i < printedColNames.length - 1) c = c + "innercol ";
			
			if (!c.isEmpty()) {
				sb.append("\t\t" + "<th class=\"" + c.trim() + "\">" + printedColNames[i] + "</th>" + ls);
			} else {
				sb.append("\t\t" + "<th>" + printedColNames[i] + "</th>" + ls);
			}
		}
		sb.append("\t" + "</tr>" + ls);
		
		// Add all data rows (rows are 1-indexed)
		for (int r = 1; r <= rowCount; r++) {
			// Begin new row
			String cls = "";
			if (r == 1) cls = cls + "firstrow ";
			if (r == rowCount) cls = cls + "lastrow ";
			if (r % 2 == 1) {
				cls = cls + "oddrow ";
			} else {
				cls = cls + "evenrow ";
			}
			sb.append("\t" + "<tr class=\"" + cls.trim() + "\">" + ls);
			
			// Add data cells to row
			for (int c = 0; c < colNames.length; c++) {
				cls = "";
				if (c == 0) cls = cls + "firstcol ";
				if (c == colNames.length - 1) cls = cls + "lastcol";
				if (c > 0 && c < colNames.length - 1) cls = cls + "innercol";
				
				sb.append("\t\t" + "<td class=\"" + cls.trim() + "\">");
				try {
					String cur = rs.getString(colNames[c]);
					if (cur.equalsIgnoreCase("null")) cur = ""; // Clear out any null strings
					sb.append(cur);
				} catch(SQLException ignored) {
					System.out.println("HtmlTableGenerator.getHtml(): SQLException occured getting data for row " + r + " and column \"" + colNames[c] + "\"");
					sb.append("");
				}
				sb.append("</td>" + ls);
			}
			
			// End row
			sb.append("\t" + "</tr>" + ls);
			try {
				rs.next();
			} catch(SQLException ignored) {
				System.out.println("HtmlTableGenerator.getHtml(): Error: rowCount larger than ResultSet dimensions");
			}
		}
		
		// End the table
		sb.append("</table>" + ls);
		
		// Return the table
		return sb.toString();
	}

	public static String getHtml(ResultSet rs) {
		return getHtml(rs,null,null,null);
	}
	
	public static String getHtml(ResultSet rs, String[] colNames) {
		return getHtml(rs,colNames,null,null);
	}
	
	public static String getHtml(ResultSet rs, String[] colNames, String[] printedColNames) {
		return getHtml(rs,colNames,printedColNames,null);
	}
	
	private static boolean checkValidity(ResultSet rs, String[] colNames, String[] printedColNames) {
		// First, check if colNames contains valid column names
		if (colNames != null) {
			for (int i = 0; i < colNames.length; i++) {
				try {
					int n = rs.findColumn(colNames[i]);
				} catch (SQLException e) {
					// An exception is thrown if the row is not found
					return false;
				}
			}
		}
		
		// Next, check if printColNames matches colNames or rs in size
		if (printedColNames == null) return true;
		if (colNames != null && colNames.length != printedColNames.length) return false;
		
		if (colNames == null) {
			ResultSetMetaData rmd;
			int colCount = -1;
			try {
				rmd = rs.getMetaData();
				colCount = rmd.getColumnCount();
			} catch (SQLException e) {
				return false;
			}
			if (colCount != printedColNames.length) return false;
		}
		
		// All tests have passed!
		return true;
	}
	
	
	public static void main(String[] args) {
		// Get a ResultSet from the database
		quizsite.DatabaseConnection dc = new quizsite.DatabaseConnection();
		
		// Specify setup for test
		String query = "SELECT * from siteManager;";
		String[] colNames = {"nextuserid","nextquizid","nextquestionid","nextmessageid","nextquiztakenid"};
		String [] printedColNames = {"Next User","Next Quiz","Next Question","Next Message","Next Quiz Taken"};
		String tableClass = "myTable";
		
		
		
		ResultSet rs = dc.executeQuery(query);
		try {
			if(!rs.first()) {
				System.out.println("ResultSet is empty!");
				return;
			}
		} catch (SQLException e) {
			System.out.println("Error: Corrupted ResultSet");
			return;
		}
		
		String table = getHtml(rs,colNames,printedColNames,tableClass);
		if (table.isEmpty()) {
			System.out.println("Table is empty!");
		} else {
			System.out.println(table);
		}
	}

}
