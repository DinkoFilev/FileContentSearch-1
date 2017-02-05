package searchLogic;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Search {

	private static ArrayList<File> filesWithSearchText = new ArrayList<>();

	// recursively call findFile method

	/**
	 * @param text
	 *            searchText in files
	 * @param file
	 *            absolute path of the file
	 */
	public void findFile(String text, File file) {
		File[] list = file.listFiles();// make array of all files in directory
		if (list != null)
			for (File fil : list) {// for each files in list

				if (fil.isDirectory()) {// check if file is directory go into
										// directory
					findFile(text, fil);
				} else {
					if (isZipFile(fil.getAbsolutePath())) {// check if file is
															// zip
						readZipFiles(fil.getAbsolutePath(), text);// method for
																	// read zip
																	// files
					} else {
						Scanner scan=null;
						File fileForRead;
						try {
							fileForRead = new File(fil.getAbsolutePath());
							scan = new Scanner(fileForRead);
							while (scan.hasNext()) {// read file with scanner
								String line = scan.nextLine().toString();
								if (line.contains(text)) {
									filesWithSearchText.add(
											fileForRead);/*
															 * if file contains
															 * search text add
															 * file to search
															 * results
															 */
									break;
								}
							}
						} catch (FileNotFoundException e) {
							System.out.println(e.getMessage());
						}
						finally{
							if(scan!=null){
								scan.close();
							}
							
							
						}
					}
				}
			}
	}

	// print result from search
	public void printResultsFromSearch() {
		System.out.println("Results from search : ");
		// sort ArrayList with lambda expression
		filesWithSearchText.sort((o1, o2) -> {
			return o2.compareTo(o1);
		});
		// print message for empty results
		if (filesWithSearchText.size() == 0) {
			System.out.println("No such results !");
			return;
		}
		// print results
		for (int i = 0; i < filesWithSearchText.size(); i++) {
			System.out.println(filesWithSearchText.get(i).getName());
		}
		// print line
		System.out.println("___________________________________");
		System.out.println("The search results are " + filesWithSearchText.size());
	}

	// check if file is zip archive
	private boolean isZipFile(String fileName) {
		if (fileName.endsWith(".zip")) {
			return true;
		}
		return false;
	}

	// method for read zip file

	/**
	 * @param fileName
	 *            absolute path
	 * @param searchText
	 */
	private void readZipFiles(String fileName, String searchText) {
		ZipFile zipFile = null;
		Scanner scan = null;
		try {
			zipFile = new ZipFile(fileName);
			Enumeration<? extends ZipEntry> entries = zipFile.entries();

			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				File f = new File(entry.getName());
				InputStream stream = zipFile.getInputStream(entry);
				scan = new Scanner(stream);
				while (scan.hasNext()) {
					String line = scan.nextLine().toString();
					if (line.contains(searchText)) {
						System.out.println("The text is found in " + entry.getName());
						filesWithSearchText.add(f);
						break;
					}
				}
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		// close open resources
		finally {
			if (scan != null) {
				scan.close();
			}
			if (zipFile != null) {
				try {
					zipFile.close();
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			}
		}
	}
}
