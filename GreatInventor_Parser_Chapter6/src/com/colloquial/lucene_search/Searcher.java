package com.colloquial.lucene_search;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

public class Searcher {
  String FILES_TO_INDEX_DIRECTORY="data/great-inventors/xml";
  
	 public void searchOnFiles(IndexWriter iWriter) throws IOException {
		 File dir = new File(FILES_TO_INDEX_DIRECTORY);
			
		 File[] f = dir.listFiles();
		 
		 for(File file: f) {
			 Document doc = new Document();
			 String path = file.getCanonicalPath();
			 doc.add(new Field("path", path, Field.Store.YES, Field.Index.ANALYZED));
			 
			 Reader reader = new FileReader(file);
			 doc.add(new Field("contents", reader));
			 iWriter.addDocument(doc);
			 
		 }
		iWriter.close();
	 }
	 
	 public void searchEngine() throws IOException, ParseException {
		Analyzer analyzer  = new StandardAnalyzer(Version.LUCENE_CURRENT); 
	    //Store the index in memory
		Directory directory = new RAMDirectory();
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_CURRENT, analyzer);
		IndexWriter iwriter = new IndexWriter(directory,config);
		/*Document doc = new Document();
		String text = "This is the text to be indexed";
		doc.add(new Field("fieldname", text, TextField.TYPE_STORED));
		doc.add(new Field("contents","This is new",TextField.TYPE_STORED));
		iwriter.addDocument(doc);
		iwriter.close();
		*/
		(new Searcher()).searchOnFiles(iwriter);
		// Now Search the index:
		DirectoryReader ireader = DirectoryReader.open(directory);
		IndexSearcher isearcher = new IndexSearcher(ireader);
		//Parse a simple query that searches for "text";
		QueryParser parser = new QueryParser(Version.LUCENE_CURRENT, "contents", analyzer);
		parser.setLowercaseExpandedTerms(false);
		Query query = parser.parse("ROBERT");
		
		ScoreDoc[] hits = isearcher.search(query, null,1000).scoreDocs;
		//Iterate through the results:
		for(int i= 0 ; i < hits.length; i++) {
			Document hitDoc = isearcher.doc(hits[i].doc);
			System.out.println(hits[i]);
			//assertEquals("This is the text to be indexed", hitDoc.get("fieldname"));
		    //System.out.println("Hits1" + hitDoc.get("fieldname"));
		    System.out.println("Hits2" + hitDoc.get("contents"));
		  
		}
		
		ireader.close();
		directory.close();
	 }
	 
	 public static void main(String[] args) throws IOException, ParseException {
		 new Searcher().searchEngine();
	 }
}
