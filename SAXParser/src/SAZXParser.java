import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SAZXParser extends DefaultHandler {
	private String fileName;
	Book bookTmp;
	String tmpValue;
	List<Book> bookL;
	SimpleDateFormat sdf= new SimpleDateFormat("yy-MM-dd");

	public SAZXParser(String fileName) {
		this.fileName = fileName;
		bookL = new ArrayList<Book>();
		try {
			parseDocument();
			printDatas();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void parseDocument() throws SAXException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser parser = factory.newSAXParser();
			parser.parse(fileName, this);
		} catch (ParserConfigurationException e) {
			System.out.println("Parser Config error");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			System.out.println("IO Error");
		}

	}

	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.endDocument();
	}

	private void printDatas() {
		for (Book tmpB : bookL) {
			System.out.println(tmpB.toString());
		}
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		if (qName.equalsIgnoreCase("book")) {
			bookTmp = new Book();
			bookTmp.setId(attributes.getValue("id"));
			bookTmp.setLang(attributes.getValue("lang"));
		}

		if (qName.equalsIgnoreCase("publisher")) {
			bookTmp.setPublisher(attributes.getValue("country"));
		}

	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		super.endElement(uri, localName, qName);
		if (qName.equals("book")) {
			bookL.add(bookTmp);
		}
		if (qName.equalsIgnoreCase("title")) {
			bookTmp.setTitle(tmpValue);

		}
		if(qName.equalsIgnoreCase("author")){
			bookTmp.getAuthors().add(tmpValue);
		}
		if (qName.equalsIgnoreCase("price")) {
			bookTmp.setPrice(Integer.parseInt(tmpValue));
		}
		if(qName.equalsIgnoreCase("regDate")){
		try {
			bookTmp.setRegDate(sdf.parse(tmpValue));
		} catch (ParseException e) {
			// TODO: handle exception
		}
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		super.characters(ch, start, length);
		tmpValue = new String(ch, start, length);

	}

	public static void main(String[] args) {

		new SAZXParser("book.xml");

	}

}
