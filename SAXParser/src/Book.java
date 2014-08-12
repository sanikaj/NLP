import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Book {
	@Override
	public String toString() {
		return "Book [lang=" + lang + ", title=" + title + ", id=" + id
				+ ", isbn=" + isbn + ", regDate=" + regDate + ", publisher="
				+ publisher + ", price=" + price + ", authors=" + authors + "]";
	}
	String lang;
	 String title;
	 String id;
	 String isbn;
	 Date regDate;
	 String publisher;
	 int price;
	 List<String> authors;
     public Book() {
    	 authors = new ArrayList<String>();
     }
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public Date getRegDate() {
		return regDate;
	}
	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public List<String> getAuthors() {
		return authors;
	}
	public void setAuthors(List<String> authors) {
		this.authors = authors;
	}
	
}
