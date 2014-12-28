package ru.webscrapper.scrapper;

import java.util.ArrayList;
import java.util.List;

public class Log {
	private List<String> logInfo;
	
	Log(){
		logInfo = new ArrayList<String>();
	}
	
	public List<String> getLog(){
		return logInfo;
	}
	
	public void addLine(String message){
		logInfo.add(message);
	}
	
	public void printLog(){
        for(String s : logInfo){
        	System.out.println(s);
        }	
        System.out.println();
	}
}
