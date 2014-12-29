package ru.webscrapper.scrapper;

import java.util.ArrayList;
import java.util.List;
/**
 * 
 * @author Ivan Nesterenko
 *
 */
public class Log {
	protected List<String> logInfo;
	
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
		/*Most JVM, actually, don't intersect output. But it would be safer to synchronize output...*/
		//synchronized(System.out){
	        for(String s : logInfo){
	        	System.out.println(s);
	        }	
	        System.out.println();
		//}
	}
}
