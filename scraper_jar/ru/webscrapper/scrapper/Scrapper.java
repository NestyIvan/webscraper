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
 */
public class Scrapper 
{
	public static void main( String[] args )
    {
    	try {
    		//words = "instead;курс;streams;background;resources";	
    		//source = "d:\\workspace\\urlList.txt";
    		//source = "http://www.hireright.com/resources";
    		/*
    		String[] targs = {"d:\\workspace\\urlList.txt"
    						 ,"instead,курс,streams,background,resources,Greece,default"
    						 ,"-v"
    						 ,"-w"
    						 ,"-c"
    						 ,"-e"
    						 };
    		*/
			Settings.loadSettings( args );
	    	List<String> urlList = getURLList();
	    	
	    	for(String url : urlList){
		    	Page pp = new Page(url);
		        pp.parsePage();
	    	}
		} catch (InsufficientResourcesException e) {
			System.out.println("Insufficient number of input parameters!" + e.getMessage());
		}
    }
    
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
