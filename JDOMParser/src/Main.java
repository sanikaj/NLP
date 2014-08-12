import java.io.File;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;


public class Main {

	public void parseFile() {
	  SAXBuilder builder = new SAXBuilder();
	  File xmlFile = new File("sample_xml.txt");
	  try {
		Document document = (Document) builder.build(xmlFile);
		Element rootNode = document.getRootElement();
		List list = rootNode.getChildren("album");
	
		for (int i = 0; i < list.size(); i++) {
			 
			   Element node = (Element) list.get(i);
	 
			   System.out.println("id : " + node.getChildText("id"));
			   System.out.println("Name : " + node.getChildText("name"));
			   System.out.println("Url : " + node.getChildText("url"));
			   System.out.println("Sample URL : " + node.getChildText("sample_url"));
	           System.out.println("upc" +  node.getChildText("upc"));
	           List artist = node.getChildren("artist");
	           if(artist != null) {
	        	   Element artistNode = (Element) list.get(0);
	        	   System.out.println("Artist id " + artistNode.getChildText("id"));
	        	   System.out.println("Artist name " + artistNode.getChildText("name"));
	        	   System.out.println("Artist url " + artistNode.getChildText("url"));
	           }
	           Element nodeChild = node.getChild("category");
	           System.out.println("Category id " + nodeChild.getChildText("id"));
	           System.out.println("Category name " + nodeChild.getChildText("name"));
	           System.out.println("Category url " + nodeChild.getChildText("url"));
	           
	           Element nodeData = node.getChild("data");
	           System.out.println("Data id" + nodeData.getChildText("id"));
	           System.out.println("Data name" + nodeData.getChildText("name"));
	           System.out.println("Data Sample url" + nodeData.getChildText("sample_url"));
	     }
		
	} catch (Exception e) {
		e.printStackTrace();
	}
	}
	
	public static void main(String[] args) {
		(new Main()).parseFile();

	}

}
