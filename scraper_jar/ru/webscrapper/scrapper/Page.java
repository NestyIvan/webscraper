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
/**
 * 
 * @author Ivan Nesterenko
 * This class is the main job for the thread which process the URL.
 * The class contains link to the main log so it can synchronize
 * global information at on place. Also it contains its' local log which is
 * printed right after the end of the page processing.
 * 
 */
public class Page extends Thread {
	private final String link;
	private Log pageLog = new Log();
	private String urlContent = null;
	private Timer pageTimer = null;
	private ParsedPage parseContent = null; 
	private Thread logMain;
	/**
	 * Class constructor.
	 * @param link to a URL
	 * @param link to the main thread log
	 */
	Page(String link, Thread mLog){
		pageLog.addLine("Start processing url: " + link);
		this.link = link;
		pageTimer = new Timer();
		logMain = mLog;
	}
	/**
	 * Start thread.
	 */
	public void run(){
		parsePage();
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
	/**
	 * parsing and scraping of the content on page.
	 * Collecting needed statistics.
	 */
	private void parsePage(){		
		loadURLContent();
		
		parseContent = new ParsedPage(urlContent);
		//We need a full stop to see sentences' boundaries.
        String bodyText = parseContent.getNoHTML("."); 
        pageTimer.setEndProcessing();
        
        wordOccurence(bodyText);
        countCharacters();        
        pageTimer.setEndScrapping();

        pageLog.addLine(pageTimer.getMessageForLog());
        pageLog.printLog();
        
        synchronized(logMain){
        	((MainLog) logMain).addScrappingTimeStatistic(pageTimer.getTimeSpentOnScrapping());
        	((MainLog) logMain).addProcessingTimeStatistic(pageTimer.getTimeSpentOnProcessing());
        }
	}
	/**
	 * Collecting statistics of the specified word list occurences on the page.
	 * Won't run if either general parser failed to parse the page or 
	 * the -w setting was set to false.
	 * This is important! The method is looking for separated words only, so no inclusions
	 * will be counted as a match situation.
	 * @param parsed body of the page that contains no HTML info.
	 */
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
			//look for separate words only!
			Pattern wordPattern = Pattern.compile("\\b(" + word.toUpperCase() + ")\\b" );
			Matcher wordMatch = wordPattern.matcher(upperText);
			while (wordMatch.find()){				
				count++;
			}
			logMessage.append("[" + word + ": " + count + "] ");
			if(count > 0){
				extractedSentences.append(extractSentence(word, text, wordMatch));
			}
			synchronized(logMain){
				((MainLog) logMain).addWordStatistic(word, count);
			}
		}
		
		pageLog.addLine(logMessage.toString());
		pageLog.addLine(extractedSentences.toString());
	}
	/**
	 * Extract the sentence which contains the word from the list.
	 * @param word a word to search from the list
	 * @param text the body where the word is contained
	 * @param wordMatch Matcher, which already contain info about word matching in the text
	 * @return sentence which contains the word
	 */
	private String extractSentence(String word, String text, Matcher wordMatch){
		StringBuilder retVal = new StringBuilder("");
		
		if((Settings.getExractSentences()) && (wordMatch != null)){
			wordMatch.reset();
			retVal.append("The word \"" + word + "\" occured in followed sentences:");			
			while (wordMatch.find()){
				int wordPosBegin = wordMatch.start();
				int sentBegin = text.lastIndexOf(".", wordPosBegin);				
				int sentEnd = text.indexOf(".", wordPosBegin);
				retVal.append("\n");
				retVal.append(text.substring(sentBegin, sentEnd));				
			}
		} 
		
		return retVal.toString();
	}
	/**
	 * Method parse body with no additional delimeters, so afterwards
	 * we have a string that contains all characters of the body.
	 */
	private void countCharacters(){
		if(parseContent == null) 
			return;
		
		if(Settings.getCountCharactersNumber()){
	        //Empty character - to get the content from the page without any additional symbols        
	        String noDelimeterText = parseContent.getNoHTML("");
	        int charNumer = noDelimeterText.length();
	        pageLog.addLine("The number of characters on the page: " + charNumer);
			synchronized(logMain){
				((MainLog) logMain).addCharactersStatistic(charNumer);
			}
		}
	}
}
