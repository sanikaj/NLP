import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class SAXParserC extends DefaultHandler {
    String fileName;
    String tmpValue;
    Catalog catalog;
	List<Catalog> catalogL;

	public SAXParserC(String fileName) {
	 this.fileName = fileName;
	 catalog = new Catalog();
	 catalogL = new ArrayList<Catalog>();
	 parseDocument();
	 printData();
	}
	
	public void printData() {
		for(Catalog c: catalogL ) {
			System.out.println(c.toString());
		}
	}
	
	public void parseDocument() {
		SAXParserFactory factory = SAXParserFactory.newInstance() ;
		try {
			SAXParser parser = factory.newSAXParser();
			parser.parse(fileName,this);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		finally {
			
		}
	}
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		
		if(qName.equals("catalog_item")) {
			catalog = new Catalog();
			catalog.setGender(attributes.getValue("gender"));
		}
		if(qName.equals("size")) {
			catalog.setSize_description(attributes.getValue("description"));
		}
		
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		super.endElement(uri, localName, qName);
		if(qName.equals("catalog_item")) {
		  catalogL.add(catalog);	
		}
		if(qName.equals("item_number")) {
			catalog.setItem_number(tmpValue);
		}
		if(qName.equals("price")) {
			catalog.setPrice(Double.parseDouble(tmpValue));
		}
		
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		super.characters(ch, start, length);
		tmpValue = new String(ch,start,length);
	}

	public static void main(String[] args) {
		new SAXParserC("catalogFile.xml");
	}
}
