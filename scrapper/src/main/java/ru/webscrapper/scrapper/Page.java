package ru.webscrapper.scrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Page {
	private final String link;
	private Log pageLog = new Log();
	private String urlContent = null;
	private Timer pageTimer = null;
	private ParsedPage parseContent = null; 
	
	Page(String link){
		pageLog.addLine("Start processing url: " + link);
		this.link = link;
		pageTimer = new Timer();
	}
	
	private void loadURLContent(){
		InputStream is = null;
		StringBuilder content = new StringBuilder();
		
		try {
			URL url = new URL(link);
			is = url.openStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
			String line;
	        while ((line = br.readLine()) != null) {	     
	        	content.append(line);
	        }			
	        
	        urlContent = new String(content);
	        
		} catch (MalformedURLException e) {
			pageLog.addLine("Error while opening url. Error message: " + e.getMessage());
		} catch (IOException e) {
			pageLog.addLine("Error while opening url stream. Error message: " + e.getMessage());			
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (IOException ignored) {/*NOP*/}
		}	
	}
	
	public void parsePage(){		
		loadURLContent();
		
		parseContent = new ParsedPage(urlContent);
		//We need a full stop to see sentences' boundaries.
        String bodyText = parseContent.getNoHTML("."); 
        pageTimer.setEndProcessing();
   //System.out.println(urlContent);    
   //System.out.println(parseContent.getNoScript());    
   //System.out.println(bodyText);        
        
        wordOccurence(bodyText);
        countCharacters();        
        pageTimer.setEndScrapping();

        pageLog.addLine(pageTimer.getMessageForLog());
        pageLog.printLog();
	}
	
	private void wordOccurence(String text){
		if(parseContent == null) 
			return;
		
		if(Settings.getCountOccurenceNumber() == false)
			return;
		
		List<String> wordsList = Arrays.asList(Settings.getWords().split("\\s*,\\s*"));
		//most browsers do search with case insensitive option on. why not to do the same...
		String upperText = text.toUpperCase();
		
		StringBuilder logMessage = new StringBuilder("Words occurence on the page: ");
		StringBuilder extractedSentences = new StringBuilder("");
		for(String word : wordsList){
			int count = 0;			
			Pattern wordPattern = Pattern.compile("\\b(" + word.toUpperCase() + ")\\b" );
			Matcher wordMatch = wordPattern.matcher(upperText);
			while (wordMatch.find()){				
				count++;
			}
			logMessage.append("[" + word + ": " + count + "] ");
			if(count > 0){
				extractedSentences.append(extractSentence(word, text, wordMatch));
			}
		}
		
		pageLog.addLine(logMessage.toString());
		pageLog.addLine(extractedSentences.toString());
	}
	
	private String extractSentence(String word, String text, Matcher wordMatch){
		StringBuilder retVal = new StringBuilder("");
		
		if(Settings.getExractSentences()){
			wordMatch.reset();
			retVal.append("The word \"" + word + "\" occured in followed sentences:\n");			
			while (wordMatch.find()){
				int wordPosBegin = wordMatch.start();
				int sentBegin = text.lastIndexOf(".", wordPosBegin);				
				int sentEnd = text.indexOf(".", wordPosBegin);
				retVal.append(text.substring(sentBegin, sentEnd));				
				retVal.append("\n");
			}
		} 
		
		return retVal.toString();
	}
	
	private void countCharacters(){
		if(parseContent == null) 
			return;
		
		if(Settings.getCountCharactersNumber()){
	        //Empty character - to get the content from the page without any additional symbols        
	        String noDelimeterText = parseContent.getNoHTML("");
	        pageLog.addLine("The number of charcters on the page: " + noDelimeterText.length());
		}
	}
}
