/**
 * This class takes an input and converts it into a formatted HTML table complete with class names
 * 	Acceptable inputs include: MySQL ResultSet OR String[][] table OR String[][] tableData, String[] colNames OR String[] tableData, String[] colNames
 * 
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
	// Class names for the <table> tag
	public static final String universalTableClass = "qwizardtable";
	
	// Class names for the <tr> tag
	public static final String headerrow = "headerrow";
	public static final String firstrow = "firstrow";
	public static final String innerrow = "innerrow";
	public static final String lastrow = "lastrow";
	public static final String evenrow = "evenrow";
	public static final String oddrow = "oddrow";
	
	// Class names for the <th> and <td> tags
	public static final String firstcol = "firstcol";
	public static final String innercol = "innercol";
	public static final String lastcol = "lastcol";

	
	public HtmlTableGenerator() {
	}
	
	/**
	 * Convert a ResultSet into an HTML table. All other arguments are optional and can be replaced with null
	 * @param rs - MySQL ResultSet
	 * @param colNames - String[] array of the names of columns you want in the table. Must match names present in the ResultSet
	 * @param printedColNames - String[] array of the way you wish for the column names to be printed at the top of the table
	 * @param tableClass - If specified, the value gets put into the table's HTML class name (i.e. <table class="myClassName">)
	 * @return
	 */
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
		if (tableClass != null)	{
			tableClass = universalTableClass + " " + tableClass;
		} else {
			tableClass = universalTableClass;
		}
		if (tableClass != null && !tableClass.isEmpty()) {
			sb.append("<table class=\"" + tableClass + "\">" + ls);
		} else {
			sb.append("<table>" + ls);
		}
		
		// Add the row of column names
		sb.append("\t" + "<tr class=\"" + headerrow + "\">" + ls);
		for (int i = 0; i < printedColNames.length; i++) {
			String c = "";
			if (i == 0) c = c + firstcol + " ";
			if (i == printedColNames.length - 1) c = c + lastcol + " ";
			if (i > 0 && i < printedColNames.length - 1) c = c + innercol + " ";
			
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
			if (r == 1) cls = cls + firstrow + " ";
			if (r > 1 && r < rowCount) cls = cls + innerrow + " ";
			if (r == rowCount) cls = cls + lastrow + " ";
			if (r % 2 == 1) {
				cls = cls + oddrow + " ";
			} else {
				cls = cls + evenrow + " ";
			}
			sb.append("\t" + "<tr class=\"" + cls.trim() + "\">" + ls);
			
			// Add data cells to row
			for (int c = 0; c < colNames.length; c++) {
				cls = "";
				if (c == 0) cls = cls + firstcol + " ";
				if (c == colNames.length - 1) cls = cls + lastcol + " ";
				if (c > 0 && c < colNames.length - 1) cls = cls + innercol + " ";
				
				sb.append("\t\t" + "<td class=\"" + cls.trim() + "\">");
				try {
					String cur = rs.getString(colNames[c]);
					if (cur == null || cur.equalsIgnoreCase("null")) cur = ""; // Clear out any null strings
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
		// First check if there is a ResultSet
		if (rs == null) return false;
		
		// Next, check if colNames contains valid column names
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
	
	/** 
	 * Converts a String[row][col] grid into an HTML table
	 * @param table
	 * @param tableClass = optional class specifier (can be replaced with null)
	 * @return
	 */
	public static String getHtml(String[][] table, String tableClass) {
		// Check validity of inputs
		if (table == null || table.length < 1 || table[0].length < 1) return "";
		int rowCount = table.length;
		int colCount = table[0].length;
		for (int i = 1; i < rowCount; i++) {
			if (table[i].length != colCount) return "";
		}
		
		// Now begin building the table
		StringBuilder sb = new StringBuilder();
		String ls = System.getProperty("line.separator");
		
		// Begin the table
		if (tableClass == null) {
			tableClass = universalTableClass;
		} else {
			tableClass = universalTableClass + " " + tableClass;
		}
		if (tableClass != null && !tableClass.isEmpty()) {
			sb.append("<table class=\"" + tableClass + "\">" + ls);
		} else {
			sb.append("<table>" + ls);
		}

		// Add the row of column names
		sb.append("\t" + "<tr class=\"" + headerrow + "\">" + ls);
		for (int i = 0; i < colCount; i++) {
			String c = "";
			if (i == 0) c = c + firstcol + " ";
			if (i == colCount - 1) c = c + lastcol + " ";
			if (i > 0 && i < colCount - 1) c = c + innercol + " ";

			if (!c.isEmpty()) {
				sb.append("\t\t" + "<th class=\"" + c.trim() + "\">" + table[0][i] + "</th>" + ls);
			} else {
				sb.append("\t\t" + "<th>" + table[0][i] + "</th>" + ls);
			}
		}
		sb.append("\t" + "</tr>" + ls);

		// Add all data rows (rows are 1-indexed)
		for (int r = 1; r < rowCount; r++) {
			// Begin new row
			String cls = "";
			if (r == 1) cls = cls + firstrow + " ";
			if (r > 1 && r < rowCount - 1) cls = cls + innerrow + " ";
			if (r == rowCount - 1) cls = cls + lastrow + " ";
			if (r % 2 == 1) {
				cls = cls + oddrow + " ";
			} else {
				cls = cls + evenrow + " ";
			}
			sb.append("\t" + "<tr class=\"" + cls.trim() + "\">" + ls);

			// Add data cells to row
			for (int c = 0; c < colCount; c++) {
				cls = "";
				if (c == 0) cls = cls + firstcol + " ";
				if (c == colCount - 1) cls = cls + lastcol + " ";
				if (c > 0 && c < colCount - 1) cls = cls + innercol + " ";

				sb.append("\t\t" + "<td class=\"" + cls.trim() + "\">");
				String cur = table[r][c];
				if (cur == null) cur = "";
				sb.append(cur);
				sb.append("</td>" + ls);
			}

			// End row
			sb.append("\t" + "</tr>" + ls);
		}

		// End the table
		sb.append("</table>" + ls);

		// Return the table
		return sb.toString();
	}
	public static String getHtml(String[][] table) {
		return getHtml(table,(String) null);
	}
	
	/** 
	 * Converts a String[][] array of table data and a separate String[] array of column names into an HTML table
	 * @param table
	 * @return
	 */
	public static String getHtml(String[][] tableData, String[] colNames, String tableClass) {
		// First, verify validity
		if (tableData == null || colNames == null || colNames.length == 0) return "";
		
		// Combine tableData and colNames into a single array
		String[][] table = new String[tableData.length + 1][];
		table[0] = colNames;
		for (int i = 0; i < tableData.length; i++) {
			table[i+1] = tableData[i];
		}
		
		// Parse the result into an HTML table
		return getHtml(table,tableClass);
	}
	public static String getHtml(String[][] tableData, String[] colNames) {
		return getHtml(tableData,colNames,null);
	}
	
	/**
	 * These methods allow a table with only one row of data to be passed in as a String[]
	 * @param tableData
	 * @param colNames
	 * @param tableClass
	 * @return
	 */
	public static String getHtml(String[] tableData, String[] colNames, String tableClass) {
		String[][] newTableData = new String[1][];
		newTableData[0] = tableData;
		return getHtml(newTableData,colNames,tableClass);
	}
	public static String getHtml(String[] tableData, String[] colNames) {
		return getHtml(tableData,colNames,null);
	}
	
	
	// For testing purposes only
	public static void main(String[] args) {
		// Get a ResultSet from the database
		quizsite.DatabaseConnection dc = new quizsite.DatabaseConnection();
		
		// Specify setup for test
		String query = "SELECT * from friends;";
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
		
		String tableHtml = getHtml(rs);
		if (tableHtml.isEmpty()) {
			System.out.println("Table is empty!");
		} else {
			System.out.println(tableHtml);
		}
		
//		// Test the functionality for a String[][] table
//		String[][] table = new String[4][];
//		String[] labels = {"Next User","Next Quiz","Next Question","Next Message","Next Quiz Taken"};
//		String[] values1 = {"123", "456", "789", "987", "654"};
//		String[] values2 = {"a", "b", "c", "d", "e"};
//		String[] values3 = {"f", "g", "h", "i", "j"};
//		
//		table[0] = labels;
//		table[1] = values1;
//		table[2] = values2;
//		table[3] = values3;
//		String tableHtml2 = getHtml(table,tableClass);
//		System.out.println(tableHtml2);
//		System.out.println(getHtml(values1,labels,tableClass + " secondClass"));
		
	}

}
