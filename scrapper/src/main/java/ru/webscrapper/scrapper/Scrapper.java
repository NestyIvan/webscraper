package ru.webscrapper.scrapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InsufficientResourcesException;

/**
 * accepts as command line parameters:
o   (must) web resources URL or path to plain text file containing a list of URLs
o   (must) data command(s)
o   (must) word (or list of words with “,” delimiter)
o   (nice to have) output verbosity flag,  if on then the output should contains information about time spend on data scraping and data processing (-v)
 
-supports the following data processing commands:
o   (must) count number of provided word(s) occurrence on webpage(s). (-w)
o   (must) count number of characters of each web page (-c)
o   (nice to have) extract sentences’ which contain given words (-e)
 *
 *Command line parameters example for Java implementation:
 *java –jar scraper.jar http://www.cnn.com Greece,default –v –w –c –e
 */
public class Scrapper 
{
	public static void main( String[] args )
    {
    	try {    		
    		/*
    		String[] targs = {"d:\\workspace\\urlList.txt"
    						 ,"курс,background,resources,Greece,default"
    						 ,"-v"
    						 ,"-w"
    						 ,"-c"
    						 ,"-e"
    						 };    
    		*/
    		Settings.loadSettings( args );    		
    		runProcessing();
		} catch (InsufficientResourcesException e) {
			System.out.println("Insufficient number of input parameters!");
		}
    }
	/**
	 * Method that run threads for the pages. Wait for all pages were processed
	 * and finally print a summarize report.
	 */
	private static void runProcessing(){
    	List<String> urlList = getURLList();
    	//We need to join threads to wait for the end of all operations
    	Thread[] arrPageThreads = new Thread[urlList.size()];
    	MainLog mLog = new MainLog();
    	int iterate = 0;
    	//create and run threads for every url
    	for(String url : urlList){			    	
    		Thread pageThread = new Page(url, mLog);
    		arrPageThreads[iterate++] = pageThread;
    		pageThread.start();
    	}
    	//stop running main thread until all pages not processed
    	for(int i = 0; i < arrPageThreads.length; i++){
    		try {
				arrPageThreads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();					
			}
    	}
    	//print summarize result
		mLog.printLog();
	}
	
    /**
     * Returns the list of URLs. The number of items will be:
     * 1 - if there is a link in source
     * n - if there is a path to file in source(n - number of links in the file)
     * <p>
     * In case of path to file, function read it line by line and store links in it.
     * Links in the file should be stored on separate lines.
     * @return the list of URLS
     */
	private static List<String> getURLList(){
		String source = Settings.getSource();
		List<String> sourceList = new ArrayList<String>();
		
		if((new File(source)).exists()){
			//work with file of URLs
			BufferedReader sourceBR = null;
			try {
				sourceBR = new BufferedReader(new FileReader(source));
				String line = null;
				while((line = sourceBR.readLine()) != null){
					sourceList.add(line);
				}
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
		} else {
			//work with single URL
			sourceList.add(source);
		}
		
		return sourceList;
	}
}
