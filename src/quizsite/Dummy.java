package quizsite;

import java.util.*;

public class Dummy {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<String> test = new ArrayList<String>();
		
		String rightMessage = "Right message 4. Matt was here. So was Gino. Dan was here.";
		
		test.add("Wrong message 1");
		test.add("Wrong message 2");
		test.add("Wrong message 3");
		test.add(rightMessage);
		test.add("Wrong message 5");
		
		System.out.println(test.get(3));
	}

}
