import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 */

/**
 * Assignment 1 Main.java
 * 
 * @author Sanika Joshi Ruchita Kshirsagar
 * 
 * 
 */
public class Main {

	/**
	 * @param args
	 */
	private void checkString(String strOfOnesAndZeros, String strOfAlpha) {
		String patternfor0 = "(A+)";
		String patternfor1 = "(A+|B+)";
		StringBuilder strB = new StringBuilder();
		boolean valid = true;

		for (int i = 0; i < strOfOnesAndZeros.length(); i++) {
			int j = Character.getNumericValue(strOfOnesAndZeros.charAt(i));
			if (j == 1) {
				strB.append(patternfor1);
			} else if (j == 0) {
				strB.append(patternfor0);
			}
		}

		Pattern p = Pattern.compile(strB.toString());
		Matcher m = p.matcher(strOfAlpha);
       
		if (m.matches()) {
			System.out.println("Yes");
		} else {
			System.out.println("No");
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<String> arr = new ArrayList<String>();
		int i = 0;
		String sCurrentLine = "";
		try {
			String filePath = args[0];
			File file = new File(filePath);
			//File file = new File(args[0]);
			BufferedReader br = new BufferedReader(new FileReader(file));
			while ((sCurrentLine = br.readLine()) != null) {
				Collections.addAll(arr, sCurrentLine.split(" "));
				if ((((String) arr.get(i)).length() < 150)
						&& ((String) arr.get(i + 1)).length() < 1000) {
					(new Main()).checkString(arr.get(i), arr.get(i + 1));
					i = i + 2;
				} else {
					System.out.println("Invalid length of string");
					break;
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
