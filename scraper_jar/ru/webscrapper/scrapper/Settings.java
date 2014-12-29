package ru.webscrapper.scrapper;

import javax.naming.InsufficientResourcesException;
/**
 * 
 * @author Ivan Nesterenko
 * This class contains general options and settings for the application.
 * Created with the start of application. All fields represent the 
 * input arguments that came from the command line.
 *
 */
public class Settings {
	private static String words;
	private static String source;
	private static boolean useTimer = false;
	private static boolean countOccurenceNumber = false;
	private static boolean countCharactersNumber = false;
	private static boolean exractSentences = false;	
	/**
	 * Method is launched after the application is being started.
	 * It initializes all settings for the application.
	 * @param args input array of arguments from the command line
	 * @throws InsufficientResourcesException
	 */
	public static void loadSettings(String[] args) throws InsufficientResourcesException{
		if(args.length < 2){
			throw new InsufficientResourcesException();
		}
		//URL or file path
		source = args[0];
		//words to process
		words = args[1];
		
		int i = 2;
		while(i < args.length && args[i].startsWith("-")){
			if(args[i].equalsIgnoreCase("-v")){
				useTimer = true;
			} else
			if(args[i].equalsIgnoreCase("-w")){
				countOccurenceNumber = true;
			} else
			if(args[i].equalsIgnoreCase("-c")){
				countCharactersNumber = true;
			} else
			if(args[i].equalsIgnoreCase("-e")){
				exractSentences = true;
			}
			i++;
		}
	}
	/**
	 * 
	 * @return time setting
	 */
	public static boolean getUseTimer(){
		return useTimer;
	}
	/**
	 * 
	 * @return extract sentence setting
	 */
	public static boolean getExractSentences() {
		return exractSentences;
	}
	/**
	 * 
	 * @return words to find
	 */
	public static String getWords(){
		return words;
	}
	/**
	 * 
	 * @return original source for processing
	 */
	public static String getSource(){
		return source;
	}
	/**
	 * 	
	 * @return occurences of words setting
	 */
	public static boolean getCountOccurenceNumber(){
		return countOccurenceNumber;
	}
	/**
	 * 
	 * @return the number of charcters setting
	 */
	public static boolean getCountCharactersNumber(){
		return countCharactersNumber;
	}
}
