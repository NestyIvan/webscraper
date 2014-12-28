package ru.webscrapper.scrapper;

import javax.naming.InsufficientResourcesException;

public class Settings {
	private static String words;
	private static String source;
	private static boolean useTimer = false;
	private static boolean countOccurenceNumber = false;
	private static boolean countCharactersNumber = false;
	private static boolean exractSentences = false;	
	
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
		//words = "instead;курс;streams;background;resources";	
		//source = "d:\\workspace\\urlList.txt";
	}
	
	public static boolean getUseTimer(){
		return useTimer;
	}
	
	public static boolean getExractSentences() {
		return exractSentences;
	}
	
	public static String getWords(){
		return words;
	}
	
	public static String getSource(){
		return source;
	}
		
	public static boolean getCountOccurenceNumber(){
		return countOccurenceNumber;
	}

	public static boolean getCountCharactersNumber(){
		return countCharactersNumber;
	}
}
