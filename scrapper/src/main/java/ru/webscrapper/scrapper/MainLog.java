package ru.webscrapper.scrapper;

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
/**
 * 
 * @author Ivan Nesterenko
 * This class is the main log. It collects total information about all other pages processings.
 *
 */
public class MainLog{
	private Map<String, Integer> wordStatistic =  Collections.synchronizedMap(new TreeMap<String, Integer>());
	volatile private int countCharacters = 0;
	volatile private long timeScrapping = 0;
	volatile private long timeProcessing = 0;

	/**
	 * Method collects total statistic for words
	 * @param word
	 * @param count
	 */
	public synchronized void addWordStatistic(String word, int count){
		if(wordStatistic.containsKey(word)){
			int prevValue = wordStatistic.get(word);
			wordStatistic.put(word, prevValue + count);
		} else {
			wordStatistic.put(word, count);
		}
	}
	/**
	 * Collects statistic for characters number
	 * @param count
	 */
	public void addCharactersStatistic(int count){
		countCharacters += count;
	} 
	/**
	 * Collects statistic for time on scraping
	 * @param scrappingTime
	 */
	public void addScrappingTimeStatistic(long scrappingTime){
		timeScrapping += scrappingTime;
	}
	/**
	 * Collects statistic for time on processing data 
	 * @param processingTime
	 */
	public void addProcessingTimeStatistic(long processingTime){
		timeProcessing += processingTime;
	}
	/**
	 * Print final log with total information.
	 */
	public void printLog(){
		StringBuilder totalInfo = new StringBuilder("\nTotal information.");
		
		if(Settings.getCountOccurenceNumber()){
			totalInfo.append("\nWords occurence on the pages: ");
			for(Entry<String, Integer> entry : wordStatistic.entrySet()){
				totalInfo.append("[" + entry.getKey() + ": " + entry.getValue() + "] ");
			}
		}
		
		if(Settings.getCountCharactersNumber()){
			totalInfo.append("\nTotal number of characters on all pages: " + countCharacters);
		}
		
		if(Settings.getUseTimer()){
			totalInfo.append("\nTotal time spent on scraping: " + timeScrapping + " ms.");
			totalInfo.append(" Total time spent on processing: " + timeProcessing + " ms.");
		}
		
		System.out.println(totalInfo.toString());
	}	
}
