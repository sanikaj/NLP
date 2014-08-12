import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.OrderedBidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

public class HuffmanEncoder {
	static ArrayList<TreeClass> listofTrees;
	private Object listofNodes;
	String charac;
	static int countOfMap, countOfCharac;
	static StringBuilder bufferOfNodes;
	static OutputStream writer2B;
	PrintWriter writer;
	static PrintWriter writer_To_Encoded_File, writer_To_InputFile,writer_To_Decoded_File;
	static FileOutputStream writer_To_Binary_File;

	static HashMap<Integer, String> mapOfBits, mapofReverseBits;
	static long time1;
	static long time2;
	static int totalChars = 0;
	static BufferedReader bfReaderForEncodedFile;
	static int countOfCharacProcessed = 0;

	public TreeClass buildInitialTree(String listKey, Integer stringValue) {
		TreeClass t = new TreeClass(listKey, stringValue, null, null);
		return t;
	}

	
	
	public void traverse(TreeClass root, String currBit) throws IOException { // Each child of a
															// tree is a root of
															// its subtree.
		if (root.left == null && root.right == null) {
			root.bitValue = bufferOfNodes.toString() + currBit;
			//String hex = String.format("%04x", (int) root.alphabet.charAt(0));
			//writer.write(hex + "," + root.bitValue + "\n");
			writer_To_Binary_File.write(root.alphabet.getBytes());
			writer_To_Binary_File.write(root.bitValue.getBytes());
			//mapOfBits.put(hex, root.bitValue);
			mapOfBits.put((Integer)((int)root.alphabet.charAt(0)), root.bitValue);
			return;
		}

		if (currBit != null)
			bufferOfNodes = bufferOfNodes.append(currBit);

		if (root.left != null) {
			traverse(root.left, "0");
		}

		if (root.right != null) {
			traverse(root.right, "1");

		}

		if (bufferOfNodes.length() > 0)
			bufferOfNodes.deleteCharAt(bufferOfNodes.length() - 1);
	}

	// Encode the characters
	public void encoder(StringBuffer lineBuffer) {
		// Replace all the characters with their equivalent bit values
		int r,i;
		char c;
		byte[] bytesValue = new byte[32*1024];
		String line = "";
		StringBuffer bufferOfBytes = new StringBuffer();
		StringBuffer bitValue = new StringBuffer();
		
		try {
			for(i = 0 ; i < lineBuffer.length() ; i++) {
				c = (char) lineBuffer.charAt(i);
				byte b = (byte) c;
				//String hex = String.format("%04x", b);
				/*String bitValue = (String) mapOfBits.get((Integer)((int)b));
				if(bitValue!= null) {
				  bytesValue = bitValue.getBytes();
				  if(bytesValue.length > 32*1024) {
					writer_To_Binary_File.write(bytesValue);
				    bytesValue = null;
				  	bytesValue = new byte[32*1024];
				  }
			  }*/
				String stringOfBits = (String) mapOfBits.get((Integer)((int)b));
				if(stringOfBits.length() > 0)
					bitValue.append(stringOfBits);
					if(bitValue.length() > 32*1024) {
							writer_To_Binary_File.write(bitValue.toString().getBytes());
                            bitValue.setLength(0);                
						}
					}
				
		    	
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String convertHexToString(String hex) {

		StringBuilder sb = new StringBuilder();
		StringBuilder temp = new StringBuilder();

		// 49204c6f7665204a617661 split into two characters 49, 20, 4c...
		for (int i = 0; i < hex.length() - 1; i += 2) {

			// grab the hex in pairs
			String output = hex.substring(i, (i + 2));
			// convert hex to decimal
			int decimal = Integer.parseInt(output, 16);
			// convert the decimal to character
			sb.append((char) decimal);

			temp.append(decimal);
		}
		System.out.println("Decimal : " + temp.toString());

		return sb.toString();
	}

	public void decoder() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader("binout.dat"));
		String line= new String();
		String[] arr = null;
		String bitSeq= new String();
		BidiMap<Character,String> map = new DualHashBidiMap<Character,String>();
		while((line = reader.readLine()) != null) {
			arr = line.split("33");
		    int i=0;
			System.out.println("Array Length" + arr[0].length());
		    while(i < arr[0].length()) {
		    	char c= arr[0].charAt(i);
		    	i++;
		    	System.out.println("Value of i" + i);
		    	
		    	while(i < arr[0].length() && (arr[0].charAt(i) == '1' || arr[0].charAt(i) == '0' )) {
		    		bitSeq = bitSeq + arr[0].charAt(i);
		    		i++;
		    	}
		    		map.put(c,bitSeq);
		    		bitSeq = new String();
		    	}
		    
		}
		for(int j=0 ; j < arr[1].length() ; j++) {
			bitSeq = bitSeq + arr[1].charAt(j); 
			if(map.containsValue(bitSeq)) {
			  	writer_To_Decoded_File.write(map.getKey(bitSeq));
			  	bitSeq  = new String();
			}
		}
		System.out.println(map.toString());
	}
	// Decode the string
	/*public void decoder() {
		Iterator mapIterator = mapOfBits.entrySet().iterator();
		StringBuffer buffer = new StringBuffer();
		int r;
		char c;

		while (mapIterator.hasNext()) {
			Map.Entry<String, String> mapEntry = (Map.Entry<String, String>) mapIterator
					.next();
			mapofReverseBits.put(mapEntry.getValue(), mapEntry.getKey());
		}

		try {
			BufferedReader bf = new BufferedReader(new FileReader(
					"encodedFile.txt"));
			int count = 0;
			String characterV;
			while ((r = bf.read()) != -1) {
				c = (char) r;
				buffer.append(c);

				String bytes = buffer.toString();
				if (mapofReverseBits.get(bytes) != null) {
					characterV = mapofReverseBits.get(bytes);
					if (characterV != "002e" || characterV != "000a")
						characterV = characterV.trim();

					// writer3.write(convertHexToString(characterV));
					buffer.setLength(0);
					count = 0;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

		}
	}
*/
	public void decode() {
		
	}
	// compare
	class TreeComparator implements Comparator<TreeClass> {
		@Override
		public int compare(TreeClass o1, TreeClass o2) {
			return o1.getCount().compareTo(o2.getCount());
		}
	}

	public void buildMergerTree(ArrayList<TreeClass> listOfNodes,StringBuffer lineBuffer)
			throws IOException {
		TreeClass node1, node2;
		ArrayList<TreeClass> newListOfTreeNodes = new ArrayList<TreeClass>();

		for (int i = listOfNodes.size() - 1; i >= 0; i = i - 2) {
			if (listOfNodes.size() % 2 == 0) {
				node1 = listOfNodes.get(i);
				node2 = listOfNodes.get(i - 1);

				TreeClass t = new TreeClass(node1.getAlphabet() + ","
						+ node2.getAlphabet(), node1.getCount()
						+ node2.getCount(), node2, node1);
				newListOfTreeNodes.add(t);
			} else if (listOfNodes.size() % 2 != 0) { // Means odd set of
														// elements in the list
				if (i == 0) {
					node1 = (TreeClass) listOfNodes.get(0);
					newListOfTreeNodes.add(node1);
				} else {
					node1 = (TreeClass) listOfNodes.get(i);
					node2 = (TreeClass) listOfNodes.get(i - 1);

					TreeClass t = new TreeClass(node1.getAlphabet() + ","
							+ node2.getAlphabet(), node1.getCount()
							+ node2.getCount(), node2, node1);
					newListOfTreeNodes.add(t);
				}
			}
		}
		if (newListOfTreeNodes.size() > 1) {
			Collections.sort(newListOfTreeNodes,
					new HuffmanEncoder().new TreeComparator());
			buildMergerTree(newListOfTreeNodes,lineBuffer);
		} else {
			if (newListOfTreeNodes.size() == 1) {
				TreeClass finalTree = (TreeClass) newListOfTreeNodes.get(0);
				countOfCharacProcessed ++;
				//writer = new PrintWriter("bitsAndCharacters" + countOfCharacProcessed, "UTF-8");
				mapOfBits = new HashMap<Integer, String>();
				String end_of_tree = new String("33");
				
				traverse(finalTree, null);
				countOfMap++;
				writer_To_Binary_File.write(end_of_tree.getBytes());
				//writer.close();

				mapofReverseBits = new HashMap<Integer, String>();
				encoder(lineBuffer);
			} else {
				System.out.println("Size of the tree is 0");
			}
			// File file = new File("encodedFile.txt");
			// System.out.println("File length for encoded file "+ ((double)
			// file.length() / 8));

			// writer3 = new PrintWriter("decodedFile.txt", "UTF-8");
			// decoder();
			// writer3.close();
			//decoder();

		}

	}

	class ValueComparator<K extends Comparable<? super K>, V extends Comparable<? super V>>
			implements Comparator<Map.Entry<K, V>> {

		public int compare(Map.Entry<K, V> a, Map.Entry<K, V> b) {
			int cmp1 = a.getValue().compareTo(b.getValue());
			if (cmp1 != 0) {
				return cmp1;
			} else {
				return a.getKey().compareTo(b.getKey());
			}
		}
	}

	/*
	 * public void convertToBinary(char c) { String s= String.valueOf(c); String
	 * binary = new BigInteger(s.getBytes()).toString(2); writer4.write(binary);
	 * }
	 */
	/*
	 * public void convertToBinary(StringBuilder text) { String binary = new
	 * BigInteger(text.toString().getBytes()).toString(2);
	 * System.out.println("Binary is " + binary); try { PrintWriter writer4 =
	 * new PrintWriter("InputFileToBinary.txt"); writer4.write(binary);
	 * writer4.close(); File file = new File("InputFileToBinary.txt");
	 * System.out.println("File length " + ((double) file.length() / 8));
	 * 
	 * } catch (FileNotFoundException e) { e.printStackTrace(); }
	 * 
	 * }
	 */

	public void readFromFile(StringBuffer lineBuffer) throws IOException {
		char[] ch = new char[lineBuffer.length()];
		List<int[]> list;
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		String line = lineBuffer.toString();
		int count = 1;
		char c = 0;
		countOfCharac = 0;
		int[] stringOfCharac = new int[255];

		for (int j = 0; j < line.length(); j++) {
			c = line.charAt(j);
			listofTrees = new ArrayList<TreeClass>();
			totalChars++;
			int k = (int) c;
			stringOfCharac[k] += 1;
		}

		for (int i = 0; i < stringOfCharac.length; i++) {
			// build a tree of nodes
			if (stringOfCharac[i] != 0) {
				TreeClass t = (new HuffmanEncoder()).buildInitialTree(
						Character.toString((char) i), stringOfCharac[i]);
				listofTrees.add(t);
			}
		}

		Collections.sort(listofTrees, new TreeComparator());
		(new HuffmanEncoder()).buildMergerTree(listofTrees,lineBuffer);
		listofTrees = null;
		list = null;
		if((totalChars % 16*1024) == 0)
		System.out.println("Total characters processed" + totalChars);
	}

	public static void main(String[] args) throws IOException {
		String filePath = "O220_1.add";
		//String filePath = "readMe.txt";
		countOfCharac = 0;
		countOfMap = 0;

		time1 = System.currentTimeMillis();
		bufferOfNodes = new StringBuilder();
		System.out.println("Started encoding");
		bfReaderForEncodedFile = new BufferedReader(new FileReader(filePath));
	
		writer_To_Encoded_File = new PrintWriter("encodedFile.txt", "UTF-8");
		writer_To_Binary_File = new FileOutputStream("binout.dat");
		writer_To_InputFile = new PrintWriter("InputFileToBinary.txt");
        writer_To_Decoded_File = new PrintWriter("decodedFile.txt","UTF-8");
		try {
			BufferedReader bf = new BufferedReader(new FileReader(filePath));
			String line = "";
			StringBuffer line_of_characters = new StringBuffer();
			while (((line = bf.readLine()) != null)) {
				line_of_characters.append(line);
				if (line_of_characters.length() >= 256*1024) {
					(new HuffmanEncoder()).readFromFile(line_of_characters);
					line_of_characters.setLength(0);
				 }
			}

			bf.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// After converting the characters in the file to the binary sequence
		// close the writer
		writer_To_Encoded_File.close();
		writer_To_Binary_File.close();
		writer_To_InputFile.close();
		writer_To_Decoded_File.close();
		time2 = System.currentTimeMillis();
		System.out.println("Time required to encode and decode file" + (time2 - time1));
	}



	
}
