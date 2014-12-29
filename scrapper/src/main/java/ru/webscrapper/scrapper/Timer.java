package ru.webscrapper.scrapper;

/**
 * 
 * @author Ivan Nesternko
 * This class collecting the time being spent on scraping and processing data.
 *
 */
public class Timer {
	private long initialTime;
	private long endScrapping;
	private long endProcessing;
	
	Timer(){
		initialTime = System.currentTimeMillis();
	}
	
	public long getInitTime(){
		return initialTime;
	}
	
	public void setEndScrapping(){
		endScrapping = System.currentTimeMillis();
	} 
	
	public void setEndProcessing(){
		endProcessing = System.currentTimeMillis();
	}
	
	public long getTimeSpentOnScrapping(){
		return endScrapping - endProcessing;
	}
	
	public long getTimeSpentOnProcessing(){
		return endProcessing - initialTime;
	}
	/**
	 * 
	 * @return string for log
	 */
	public String getMessageForLog(){
		String outputMessage = "";
		if(Settings.getUseTimer()){
			outputMessage = "Time spent on data scraping: " + String.valueOf(getTimeSpentOnScrapping()) + " ms.";
			outputMessage = outputMessage + "Time spent on data processing: " + String.valueOf(getTimeSpentOnProcessing()) + " ms.";
		}
		
		return outputMessage;
	}
}
