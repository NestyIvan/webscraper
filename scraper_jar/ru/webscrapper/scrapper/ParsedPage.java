package ru.webscrapper.scrapper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParsedPage {
	private final String original;
	private String body = null;
	private String noScript = null;
	private String noHTML = null;
	
	public ParsedPage (String input){
		original = input;
	}
	
	public String getBody(){
		if(body == null){
	        Pattern pattern=Pattern.compile(".*?<body.*?>(.*?)</body>.*?");
	        Matcher matcher=pattern.matcher(original);
	        
	        if(matcher.matches()) {
	        	body = matcher.group(1);
	        }
	    }
		
		return body;
	}
	
	public String getNoScript(){
		if(noScript == null){
			String body = (new ParsedPage(original)).getBody();
			noScript = body.replaceAll("\\<script>.*?</script>", "");
		}
		
		return noScript;
	}
	
	public String getNoHTML(String delimeter){
		String noJS = (new ParsedPage(original)).getNoScript();
		noHTML = noJS.replaceAll("\\<.*?>", delimeter);		
		
		return noHTML;
	}
}
