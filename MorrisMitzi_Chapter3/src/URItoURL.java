import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

//Experimented with these online set of examples
public class URItoURL {

	URI uri = null;
	URL url = null;
	InputStream is, fis;

	public void convertFromURItoURL() throws URISyntaxException,
			MalformedURLException {

		uri = new URI("http://www.javacodegeeks.com/");
		url = uri.toURL();
		System.out.println("URI is" + uri + "URL is" + url);

	}

	public void getFileDescriptor() {
		FileOutputStream fos = null;
		FileDescriptor fd = null;
		try {
			fos = new FileOutputStream("WriteFile.txt");
			fd = fos.getFD();
			if (fd.valid()) {
				System.out.println("File Exists !!");
			} else {
				System.out.println("File Does not exist!!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void writeToFile() {
		File file = new File("WriteFile.txt");
		char[] cbuf = { 'T', 'h', 'i', 's', ' ', 'i', 's', ' ', 'a', 's', 'e',
				't' };
		try {
			FileWriter fw = new FileWriter(file);
			fw.write('h');
			fw.write(cbuf);
			fw.write("String write!!");
			fw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void markResetImpl(String filePath) throws IOException {
		BufferedInputStream bis;
		try {
			is = new FileInputStream(filePath);
			bis = new BufferedInputStream(is);

			// read and print characters one by one
			System.out.print("Char : " + (char) bis.read());
			System.out.print("Char : " + (char) bis.read());
			System.out.print("Char : " + (char) bis.read());

			// mark is set on the input stream
			bis.mark(3);
			System.out.println("mark() set");

			System.out.print("Char : " + (char) bis.read());
			System.out.println("reset() invoked");

			// reset is called
			bis.reset();

			// read and print characters
			System.out.println("char : " + (char) bis.read());
			System.out.println("char : " + (char) bis.read());
			System.out.println("char : " + (char) bis.read());
			System.out.println("char : " + (char) bis.read());

		} finally {
			is.close();
		}
	}

	public void InputStreamImpl(String filePath) throws IOException {
		try {
			is = new FileInputStream(filePath);
			char c = 0;
			int i = 0;
			System.out.println("Character array is");
			while ((i = is.read()) != -1) {
				c = (char) i;
				System.out.print(c);
			}

			is = new FileInputStream(filePath);
			byte[] bs = new byte[4];
			// byte means Ascii code
			fis = new FileInputStream(filePath);
			i = fis.read(bs);
			// for each byte in the buffer
			for (byte b : bs) {
				System.out.println("Byte is" + b);
				c = (char) b;
				System.out.println("Character is" + c);
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			// releases system resources associated with this stream
			if (is != null)
				is.close();
		}

	}

	public static void main(String[] args) throws IOException {
		// Converting from URI to URL
		try {
			(new URItoURL()).convertFromURItoURL();
			(new URItoURL()).InputStreamImpl("ReadFile.txt");
			(new URItoURL()).markResetImpl("ReadFile.txt");
			(new URItoURL()).writeToFile();
			(new URItoURL()).getFileDescriptor();
		} catch (MalformedURLException | URISyntaxException e) {
			e.printStackTrace();

		}
	}

}
