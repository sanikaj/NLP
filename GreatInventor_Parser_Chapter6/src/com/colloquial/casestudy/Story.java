package com.colloquial.casestudy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jdom2.Element;
import org.jdom2.Text;

public class Story {
	String authorName;
	String text;
	private final List<String> mParagraphs; // Paragraphs
	int mNumber;

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getmNumber() {
		return mNumber;
	}

	public void setmNumber(int mNumber) {
		this.mNumber = mNumber;
	}

	public List<String> getmParagraphs() {
		return mParagraphs;
	}

	public Story() {
		mParagraphs = new ArrayList<String>();
	}
	
	public int getNumber() {
		return mNumber;
	}

	private Story(Builder builder) {
		mParagraphs = builder.paragraphs;
		mNumber = builder.number;
	}

	public Element toXml() {
		Element root = new Element("doc");
		Element fNumber = new Element("field");
		fNumber.setAttribute("name", "number");
		String number = String.valueOf(mNumber + "");
		fNumber.addContent(number);
		root.addContent(fNumber);

		for (String par : mParagraphs) {
			Element fText = new Element("field");
			fText.setAttribute("name", "text");
			fText.addContent(new Text(par));
			root.addContent(fText);
		}
		return root;

	}

	public static class Builder {
		private int number;
		private String title;
		private String pubName;
		private Date pubDate;
		private String author1;
		private String author2;
		private List<String> paragraphs;

		public Builder(int count) {
			number = -1;
			try {
				number = count;
			} catch (NumberFormatException e) {
				// do nothing
			}
			paragraphs = new ArrayList<String>();
		}

		public boolean isValid() {
			if (number < 1 || paragraphs == null)
				return false;
			return true;
		}

		public Builder paragraphs(List<String> list) {
			paragraphs.addAll(list);
			return this;
		}

		public Story build() {
			if (isValid())
				return new Story(this);
			return null;
		}
	}
}
