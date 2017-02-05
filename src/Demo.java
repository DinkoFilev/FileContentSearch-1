

import java.io.File;
import java.util.Scanner;

import searchLogic.Search;

/**
 * @author Kristian
 * program for search text in files
 * Search in java,xml,html,css,javascript,zip and text files
 * Receive two parameters
 * - seach text
 * - directory
 * 
 * After search print results of search
 * 
 * Can not read rar,jar and word files ;(
 */
public class Demo {
	

	public static void main(String[] args) {
		if(args.length!=2){
			System.out.println("Invalid number of parammeter!");
			System.exit(0);
		}
		Search search = new Search();
		Scanner scan = new Scanner(System.in);
		String text = args[0];
		String directory = args[1];
		search.findFile(text, new File(directory));
		search.printResultsFromSearch();
		scan.close();
	}
	
	
	
	

}