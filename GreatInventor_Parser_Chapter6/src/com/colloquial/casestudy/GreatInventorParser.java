package com.colloquial.casestudy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import org.apache.lucene.queryparser.classic.ParseException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.colloquial.lucene_search.Searcher;

public class GreatInventorParser {

	public static final String INPUT_ENCODING = "UTF-8";
	public static final String OUTPUT_ENCODING = "UTF-8";
	public static final String START_STORY = "[Illustration: ";
	public static final String END_OF_BOOK = "End of the Project Gutenberg";
	static ArrayList<String> authors;
	static int count = 0;
	boolean flag2 = true;

	public static void main(String[] args) throws IOException, ParseException {
		if (args.length < 2) {
			System.err.println("usage: GreatInventorParser "
					+ "<etext> <outdir>");
			System.exit(-1);
		}
		// Input file name and output directory
		File etextFile = new File(args[0]);
		File outputDir = new File(args[1]);

		authors = new ArrayList<String>();

		authors.add("FULTON");
		authors.add("ELI WHITNEY");
		authors.add("S.F.B. MORSE");
		authors.add("PETER COOPER");
		authors.add("EDISON");

		GreatInventorParser parser = new GreatInventorParser();

		List<Story> stories = parser.parseStories(etextFile);

		// initialise the authors list with values
		for (Story storiesObj : stories) {
            parser.storyToXml(storiesObj, outputDir);
            System.out.println("All stories processed.");
        }

		Searcher searcher = new Searcher();
		//searcher.searchDoco();
	}

	public void storyToXml(Story story, File outDir) {
		 int number = story.mNumber;
		 String filename = "story_" + number + ".xml";
		 File outFile = new File(outDir,filename);
		 FileOutputStream outStream = null;
	        OutputStreamWriter outWriter = null;
	        BufferedWriter bufWriter = null;
	        try {
	            outStream = new FileOutputStream(outFile);
	            outWriter = new OutputStreamWriter(outStream,
	                                               OUTPUT_ENCODING);
	            bufWriter = new BufferedWriter(outWriter);

	            /*bbf*/Format format = Format.getPrettyFormat();/*ebf*/
	            /*bbf*/format.setEncoding(OUTPUT_ENCODING);/*ebf*/
	            XMLOutputter outputter
	                = new XMLOutputter(format);
	            Element root = new Element("add");
	            Document document = new Document(root);
	            root.addContent(story.toXml());
	            outputter.output(document,bufWriter);
	        } catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
	            close(bufWriter);
	            close(outWriter);
	            close(outStream);
	        }

	}
	 
	public List<Story> parseStories(File inFile) {
		ArrayList<Story> stories = new ArrayList<Story>();

        FileInputStream inStream = null;
        InputStreamReader inReader = null;
        BufferedReader bufReader = null;
        boolean flag = true;
        try {
            /*bbf*/inStream = new FileInputStream(inFile);/*ebf*/
            /*bbf*/inReader = new InputStreamReader(inStream,INPUT_ENCODING);/*ebf*/
            /*bbf*/bufReader = new BufferedReader(inReader);/*ebf*/

            // read from beginning of file to start of paper #1
            String line = null;
            
            while ((line = bufReader.readLine()) != null && flag) {
                //detect the start of the story
            	//System.out.println(line);
            	//for(int i=0; i < authors.size() ; i++) {
            	       if(line.trim().contains(START_STORY)) {
            	    	for(int i=0 ; i < authors.size() ; i++) {   
            	    	String value= START_STORY + authors.get(i) +".]";
                   		if(value.equals(line.trim()))
            	    	   //found the start of the story
            			System.out.println("FOUND : START " + line.trim());
            			flag = false;
                   		break;
            	    	}
            		//}
              }
            }
            if (line == null) 
                throw new IOException("unexpected EOF");
            
            /*bbf*/StringBuilder paragraph = new StringBuilder();/*ebf*/
            /*bbf*/ArrayList<String> parsList = new ArrayList<String>();/*ebf*/
            int ct = 0;
            // process all stories
            
            while (line != null && !(line.startsWith(END_OF_BOOK))) {
                // process previous paper, if any
                if (parsList.size() > 0) { 
                	//add the paragraph to the ArrayList
                	flag2=true;
                	if (paragraph.length() > 0) {
                        parsList.add(paragraph.toString());
                    } 
                	Story story = parseStory(parsList);
                	if (story != null) {
                        stories.add(story);
                    } else {
                        System.err.println("\nparse error story: " 
                                           + ct);
                    }
                }
                parsList.clear();
                paragraph.setLength(0);
                // get this story
                ct++;
                parsList.add(line);  // header line 
                /*bbf*/while/*ebf*/ ((line = bufReader.readLine()) != null && flag2) {
                	if(line.trim().contains(START_STORY)) {
            	    	for(int i=0 ; i < authors.size() ; i++) {   
            	    		String value= START_STORY + authors.get(i) +".]";
            	    		if(value.equals(line.trim())){
            	    			//found the start of the story
            	    			System.out.println("FOUND : START " + line.trim());
            	    			flag2 = false;
            	    			break;
            	    	    }
                	}
                	}
                    if (line.startsWith(END_OF_BOOK)) break;
                    if ("".equals(line)) {
                    	if (paragraph.length() > 0) {
                            parsList.add(paragraph.toString());
                            paragraph.setLength(0);
                        }
                    } else {
                        /*bbf*/paragraph.append(line + " ");/*ebf*/
                    }
                }
                
            }
            // process final story in book
            if (parsList.size() > 0) {
                if (paragraph.length() > 0)
                    parsList.add(paragraph.toString());
                Story story = parseStory(parsList);
                if (story != null) {
                    stories.add(story);
                } else {
                    System.err.println("\nparse error story: " 
                                       + ct);
                }
            }
              } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally  {
            	/*bbf*/close(bufReader);/*ebf*/
                /*bbf*/close(inReader);/*ebf*/
                /*bbf*/close(inStream);/*ebf*/
             
            }
        
        return stories;
	}

	void close(Closeable c) {
		if (c == null)
			return;
		try {
			c.close();
		} catch (IOException e) {
			// ignore
		}
		/* x */
	}

	public Story parseStory(List<String> pars) {
		count++;
		return new Story.Builder(count).paragraphs(pars).build();
	}

}
