import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class HuffmanEncoder {
	static ArrayList listofTrees;
	private Object listofNodes;
	
	static StringBuilder bufferOfNodes;
	PrintWriter writer, writer2, writer3;
	static HashMap<String, String> mapOfBits, mapofReverseBits;

	public TreeClass buildInitialTree(String listKey, Integer stringValue) {
		TreeClass t = new TreeClass(listKey, stringValue, null, null);
		return t;
	}

	public void traverse(TreeClass root, String currBit) { // Each child of a //
															// tree is a root of
															// its subtree.
		if (root.alphabet.equals("\n") || root.alphabet.equals("\r")) {
			System.out.println("'\\'" + root.alphabet);
		} else {
			System.out.println(root.alphabet);
		}
		if (root.left == null && root.right == null) {
			root.bitValue = bufferOfNodes.toString() + currBit;
			System.out.println("Roots bit value" + root.alphabet
					+ root.bitValue);
			String hex = String.format("%04x", (int) root.alphabet.charAt(0));
			writer.write(hex + "," + root.bitValue + "\n");
			mapOfBits.put(hex, root.bitValue);
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

	//Encode the characters
	public void encoder(String filePath) {
		// Replace all the characters with their equivalent bit values
		int r;
		char c;
		Formatter formatter = new Formatter();
		try {
			BufferedReader bf = new BufferedReader(new FileReader(filePath));
			System.out.println("String of bits");
			while ((r = bf.read()) != -1) {
				c = (char) r;
				byte b = (byte) c;
				String hex = String.format("%04x", b);
				// System.out.println(hex);
				String bitValue = (String) mapOfBits.get(hex);
				System.out.println("BitValue" + bitValue);
				writer2.write(bitValue);
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

	//Decode the string
	public void decoder() {
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

					writer3.write(convertHexToString(characterV));
					buffer.setLength(0);
					count = 0;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

		}
	}

	//compare
	class TreeComparator implements Comparator<TreeClass> {
		@Override
		public int compare(TreeClass o1, TreeClass o2) {
			return o1.getCount().compareTo(o2.getCount());
		}
	}

	public void buildMergerTree(ArrayList listOfNodes)
			throws FileNotFoundException, UnsupportedEncodingException {
		HashMap map = new HashMap();
		TreeClass node1, node2;

		ArrayList newListOfTreeNodes = new ArrayList<TreeClass>();
		for (int i = listOfNodes.size() - 1; i >= 0; i = i - 2) {
			if (listOfNodes.size() % 2 == 0) {
				node1 = (TreeClass) listOfNodes.get(i);
				node2 = (TreeClass) listOfNodes.get(i - 1);

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
			Collections.sort(newListOfTreeNodes,new HuffmanEncoder().new TreeComparator());
			buildMergerTree(newListOfTreeNodes);
		} else {

			TreeClass finalTree = (TreeClass) newListOfTreeNodes.get(0);
			writer = new PrintWriter("bitsAndCharacters", "UTF-8");
			mapOfBits = new HashMap<String, String>();
			traverse(finalTree, null);
			writer.close();
			writer2 = new PrintWriter("encodedFile.txt", "UTF-8");
			mapofReverseBits = new HashMap<String, String>();
			encoder("InputFile.txt");
			writer2.close();

			File file = new File("encodedFile.txt");
			System.out.println("File length for encoded file "
					+ ((double) file.length() / 8));

			writer3 = new PrintWriter("decodedFile.txt", "UTF-8");
			decoder();
			writer3.close();

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

	public void convertToBinary(StringBuilder text) {
		String binary = new BigInteger(text.toString().getBytes()).toString(2);
		System.out.println("Binary is " + binary);
		try {
			PrintWriter writer4 = new PrintWriter("InputFileToBinary.txt");
			writer4.write(binary);
			writer4.close();
			File file = new File("InputFileToBinary.txt");
			System.out.println("File length " + ((double) file.length() / 8));

		} catch (FileNotFoundException e) {
				e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		String filePath = "InputFile.txt";
		char c;
		int r, count = 1;
		StringBuilder text = new StringBuilder();
		bufferOfNodes = new StringBuilder();
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		
		try {
			BufferedReader bf = new BufferedReader(new FileReader(filePath));
			while ((r = bf.read()) != -1) {
				c = (char) r;
				text.append(c);
				if (map.get(c + "") != null) {
					count = map.get(c + "");
					map.remove(c + "");
					count++;
				}
				map.put(c + "", count);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		(new HuffmanEncoder()).convertToBinary(text);
		List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(
				map.entrySet());
		Collections.sort(list,
				new HuffmanEncoder().new ValueComparator<String, Integer>());
		listofTrees = new ArrayList<TreeClass>();
		for (int i = 0; i < list.size(); i++) {
			// build a tree of nodes
			TreeClass t = (new HuffmanEncoder()).buildInitialTree(list.get(i)
					.getKey(), list.get(i).getValue());
			// add each node to a list in order to be able to traverse it.
			listofTrees.add(t);
		}
		try {
			(new HuffmanEncoder()).buildMergerTree(listofTrees);
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
