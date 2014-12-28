package ru.webscrapper.scrapper;


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
	
	public String getTimeSpentOnScrapping(){
		return String.valueOf(endScrapping - endProcessing);
	}
	
	public String getTimeSpentOnProcessing(){
		return String.valueOf(endProcessing - initialTime);
	}
	
	public String getMessageForLog(){
		String outputMessage = "";
		if(Settings.getUseTimer()){
			outputMessage = "Time spent on data scrapping: " + String.valueOf(getTimeSpentOnScrapping()) + " ms.";
			outputMessage = outputMessage + "Time spent on data processing: " + String.valueOf(getTimeSpentOnProcessing()) + " ms.";
		}
		
		return outputMessage;
	}
}
