package ru.webscrapper.scrapper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * 
 * @author Ivan Nesternko
 * This class supports parsing of a page's body.
 * It uses regular expressions to perform parsing.
 * Important! This method of parsing is not perfect, though
 * on practice it showed good performance and reliability to a certain extent.
 * Apparently, if you need better parser, just use jSoup etc.
 *
 */
public class ParsedPage {
	private final String original;
	private String body = null;
	private String noScript = null;
	private String noHTML = null;
	/**
	 * Constructor of the class
	 * @param input original body of the page to parse
	 */
	public ParsedPage (String input){
		original = input;
	}
	/**
	 * Method parses original body of the page and leaves only body section
	 * @return body section of the original page
	 */
	public String getBody(){
		if(body == null){
	        Pattern pattern=Pattern.compile(".*?<body.*?>(.*?)</body>.*?");
	        Matcher matcher=pattern.matcher(original);
	        
	        if(matcher.matches()) {
	        	body = matcher.group(1);
	        }
			//what if no matches were found
			if(body == null){
				body = original;
			}
	    }
		
		return body;
	}
	/**
	 * Parses body section of the page and eliminates any script sections 
	 * @return body section without script information
	 */
	public String getNoScript(){
		if(noScript == null){
			String body = (new ParsedPage(original)).getBody();
			noScript = body.replaceAll("\\<script>.*?</script>", "");
		}
		
		return noScript;
	}
	/**
	 * Parses the body section of the page and eliminates any HTML tags on it.
	 * Important! Again, this is not perfect way to parse a page.
	 * @param delimeter the character which is inserted instead of any HTML tags
	 * @return the body without any script and HTML tags information.
	 */
	public String getNoHTML(String delimeter){
		String noJS = (new ParsedPage(original)).getNoScript();
		noHTML = noJS.replaceAll("\\<.*?>", delimeter);		
		
		return noHTML;
	}
}
