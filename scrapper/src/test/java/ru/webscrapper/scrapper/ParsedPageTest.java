package ru.webscrapper.scrapper;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.BeforeClass;
import org.junit.Test;


/**
 * Unit test for ParsedPageTest
 */
public class ParsedPageTest 
{
	private final static String testWorkingDir = "src\\test\\resources\\ru\\webscrapper\\scrapper";
	private static ParsedPage testPP = null;
	private static String input = null;
	
	@BeforeClass
	public static void testBeforeAll(){
		BufferedReader sourceBR = null;
		assertTrue("Source file was not found!", (new File(testWorkingDir + "/origContent.txt")).exists());
		try {
			sourceBR = new BufferedReader(new FileReader(testWorkingDir + "/origContent.txt"));
			String line = null;
			while((line = sourceBR.readLine()) != null){
				input = input + line;
			}
			
			testPP = new ParsedPage(input);
		} catch (FileNotFoundException e) {
			System.out.println("Error occured while opening the source file. Message: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("Error occured while reading the source file. Message: " + e.getMessage());
		} finally {
			try {
				if(sourceBR != null)
					sourceBR.close();
			} catch (IOException ignored) {/*NOP*/}
		}		
	}
	
	@Test
	public void testBody(){
		String body = testPP.getBody();
		assertTrue("Body section contains HTML tag </body>!", body.contains("</body>") == false);
	}
/*	
	@Test
	public void testScript(){
		String noScript = testPP.getNoScript();
		assertTrue("Body section contains HTML tag </script>!", noScript.contains("</script>") == false);		
	}
*/
	@Test
	public void testHTML(){
		String noHTML = testPP.getNoHTML("");
        Pattern pattern=Pattern.compile("\\<.*?>");
        Matcher matcher=pattern.matcher(noHTML);
		
		assertTrue("Result body section contains HTML tags!", matcher.matches() == false);		
	}
}
