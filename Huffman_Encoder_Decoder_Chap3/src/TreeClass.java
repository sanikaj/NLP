import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class TreeClass {
	TreeClass left,right;
	String alphabet;
	String bitValue;
	
	public String getAlphabet() {
		return alphabet;
	}

	public void setAlphabet(String alphabet) {
		this.alphabet = alphabet;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	Integer count=0;
	public TreeClass() {
	  this.bitValue = new String();
	}
	
	public TreeClass(String alphabet, Integer count, TreeClass left, TreeClass right) {
		this.alphabet = alphabet;
		this.count = count;
		this.left = left;
		this.right = right;
		this.bitValue =  new String();	
	}

	public TreeClass getLeft() {
		return left;
	}

	public void setLeft(TreeClass left) {
		this.left = left;
	}

	public TreeClass getRight() {
		return right;
	}

	public void setRight(TreeClass right) {
		this.right = right;
	}

	
	public void traverseTree() {
		
	}
}
