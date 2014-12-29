package ru.webscrapper.scrapper;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author Ivan Nesterenko
 *
 */
public class ScraperTest {
	private final static String testWorkingDir = "src\\test\\resources\\ru\\webscrapper\\scrapper";
	
	@BeforeClass
	public static void testBeforeAll(){
		assertTrue("Missing the file with url list", (new File(testWorkingDir + "/urlList.txt")).exists());
	}
	
	@Test
	public void testScraperMain(){
		String[] args = new String[6];
		args[0] = testWorkingDir + "/urlList.txt";
		args[1] = "курс,background,resources,Greece,default";
		args[2] = "-v";
		args[3] = "-w";
		args[4] = "-c";
		args[5] = "-e";
		/**
		 * No assertions. Just to verify that main case works.
		 */
		Scrapper.main(args);
	}
}
