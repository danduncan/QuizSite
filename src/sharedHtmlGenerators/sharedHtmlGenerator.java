package sharedHtmlGenerators;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class sharedHtmlGenerator {
	//private static String sharedHTMLPath = "src/sharedHtmlGenerators/";
	
	public sharedHtmlGenerator() {	
	}
	
	/**
	 * Read in a local html file and return it as a string
	 * @param filename
	 * @return
	 */
	public static String getHTML(String filename) {
		return getHTML(filename,0);
	}
	
	/**
	 * Read in a local html file as a string.
	 * Add numTabs \t's to the beginning of each line
	 * @param filename
	 * @return
	 */
	public static String getHTML(String filename, int numTabs) {
		return readHTMLFile(filename, numTabs);
	}
	
	private static String readHTMLFile(String filename, int numTabs) {
		//System.out.println("Reading file \"" + filename + "\"...");
		
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(filename));
		} catch(FileNotFoundException e) {
			System.out.println("SharedHTMLGenerator: Error: File \"" + filename + "\" not found!");
			return "";
		}
		
		String line = null;
		StringBuilder sb = new StringBuilder("");
		String ls = System.getProperty("line.separator");
		
		try {
			while( (line = reader.readLine() ) != null) {
				for (int i = 0; i < numTabs; i++) {
					sb.append("\t");
				}
				sb.append(line);
				sb.append(ls);
			}
			reader.close();
		} catch (IOException e) {
			System.out.println("getHTML: Error: IOException");
			return sb.toString();
		}
		
		return sb.toString();
	}
	
	public static void main(String args[]) {
		String pkgPath = "/src/sharedHtmlGenerators/";
		String file = "sharedpagehead_TESTING.html";
		
		String pwd = "";
		try {
			pwd = new java.io.File( "." ).getCanonicalPath();
		} catch(Exception ignored) {};

		file = pwd + pkgPath + file;
		System.out.println("File path = " + pwd + pkgPath + file);
		System.out.println(getHTML(file,1));
	}
	
	
}
