package mypackage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mypackage.DLL.Node;
/**
 * This is the main class of the program.
 *<p><i>Some of the operations of this class are:</i>
 *<p><b>1.</b> It handles any user interaction
 *<p><b>2.</b> It creates Tuples using lines
 *<p><b>3.</b> It saves any changes to the file
 *<p><b>4.</b> It creates binary index files
 *<p><b>5.</b> It searches for a word using serial or binary searches
 *@param MIN_WORD_SIZE the minimum word size for it to be saved in the index file. If the word size is smaller than <tt>MIN_WORD_SIZE</tt> it is ignored.
 *@param MAX_WORD_SIZE the maximum word size for it to be saved in the index file. If the word size is bigger it gets cropped to the <tt>MAX_WORD_SIZE</tt>.
 *@param MAX_LINE_SIZE the maximum line size for it to be saved in the index file. If the line size is bigger it gets cropped to the <tt>MAX_LINE_SIZE</tt>. 
 *@param PAGE_SIZE the size of a data page in bytes
 *@author Georgios Frangias
 */

public class MyEditor {

	MyEditor(){};
	private static final int MAX_WORD_SIZE = 20;							//Initializing the basic constants
	private static final int MIN_WORD_SIZE = 5;
	private static final int PAGE_SIZE = 128;
	private static final int MAX_LINE_SIZE = 80;

	public static void main(String[] args) throws IOException, ClassNotFoundException {

		MyEditor myeditor = new MyEditor();									//Creating an instance of the class
		DLL dll = new DLL();												//Creating a new DLL(Doubly Linked List)
		List<Tuple> tl = new ArrayList<Tuple>();							//Creating a new list of tuples
		if (args.length!=1)
			System.out.println("Please use only one parameter!");
		//String fn = "C:\\Users\\Γιώργος\\Documents\\Workspace_4th_Semester\\Project1\\"+args[0];												//Get the argument from the CMD command
		String fn = args[0];
		try {	

			BufferedReader br = new BufferedReader(new FileReader(fn));		//Reading the given file line by line and entering it to the DLL
			String line; 													
			boolean number = true;

			while ((line = br.readLine()) != null) {
				if(line.length()<MAX_LINE_SIZE) {
					dll.push(line);
				}else {
					continue;
				}
			}

			br.close();												//Closing the BufferedReader
			Node curNode = dll.head;								

			//Handle Choices 
			String userInput = " ";
			StandardInputRead reader = new StandardInputRead();

			//Continue the user inputs as long as the input isn't "q"
			while("q".equals(userInput) == false) {
				userInput = reader.readString("CMD> ");
				switch (userInput) {
				case " ":
					continue;
				case "^":
					curNode = dll.goToTheFirst(curNode);		//Go to the first line of the text
					System.out.println("OK");
					continue;
				case "$":
					curNode = dll.head;							//Go to the last line of the text
					System.out.println("OK");
					continue;
				case "-":
					curNode = dll.previousNode(curNode);		//Go to the previous line of the text
					System.out.println("OK");
					continue;
				case "+":
					curNode = dll.nextNode(curNode);			//Go to the next line of the text
					System.out.println("OK");
					continue;
				case "a":
					String newLine1 = reader.readString("Type text for new line: \n");		//Enter a new line after the current line
					dll.InsertAfter(curNode, newLine1);
					System.out.println("OK");
					continue;
				case "t":
					String newLine2 = reader.readString("Type text for new line: \n");		//Enter a new line before the current line
					dll.InsertBefore(curNode, newLine2);
					System.out.println("OK");
					continue;
				case "d":
					dll.deleteNode(dll.head, curNode);			//Delete the current line
					System.out.println("OK");
					continue;
				case "l":	
					dll.print(number);							//Print all the lines of the text
					continue;
				case "n":									
					number = !number;							//Toggle line enumeration
					System.out.println("OK");
					continue;
				case "p":
					dll.printNode(curNode);						//Print the current line
					continue;
				case "w":
					myeditor.saveModifiedFile(dll, fn);			//Save the modified text to the same file
					System.out.println("OK");
					continue;
				case "x":
					myeditor.saveModifiedFile(dll, fn);			//Save the modified text to the same file and exit
					userInput = "q";
					break;
				case "=":
					dll.currentNodeNumber(curNode);				//Show the current line enumeration
					continue;
				case "#":
					dll.numOfLinesAndChars();					//Show the number of lines and characters in the text
					continue;
				case "c":		
					myeditor.addTuplesToTheList(tl, dll);		
					Collections.sort(tl);
					myeditor.createIndexFile(tl, fn);			//Create index file of the text file
					continue;
				case "v":
					myeditor.addTuplesToTheList(tl, dll);		
					Collections.sort(tl);
					myeditor.printTuplesList(tl);				//Print index file
					continue;
				case "s":
					myeditor.addTuplesToTheList(tl, dll);		
					Collections.sort(tl);
					String sSearch = reader.readString("Type word for search: \n");		//Serial search for the word 
					myeditor.serialSearch(fn, sSearch);
					continue;
				case "b":
					myeditor.addTuplesToTheList(tl, dll);		
					Collections.sort(tl);
					String bSearch = reader.readString("Type word for search: \n");		//Bina search for the word
					myeditor.binarySearch(fn, bSearch);
					continue;
				default:
					if ("q".equals(userInput) == true)
						break;
					else {
						System.out.println("Bad command");
						continue;
					}
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found!");
			e.printStackTrace();
		}
	}

	/**
	 * This method is used to save the already modified text from the text editor
	 * overwriting the existing text file
	 * @param dll the doubly linked list used in the text editor
	 * @param fn the path of the text file
	 * @throws IOException 
	 */

	public void saveModifiedFile(DLL dll, String fn) throws IOException {
		Node node = dll.head;
		File fout = new File(fn);
		FileOutputStream fos = new FileOutputStream(fout);

		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		while (node.next != null) 			//Go to the first page 
			node = node.next; 
		while(node != null) {				//Write the file line by line
			bw.write(node.data);
			bw.newLine();
			node = node.prev;
		}
		bw.close();							//Close the BufferedWriter
	}

	/**
	 * This method converts words from <b>one</b> line of the text to tuples and append them in the list of tuples
	 * <p><i>It is called by the method <tt>addTuplesToTheList</tt></i> 
	 * @param tl the list of tuples
	 * @param line the line which needs to be converted to tuples
	 * @param lineNum the number of the line in the text
	 */
	public void createNewTuplesFromLine(List<Tuple> tl, String line, int lineNum) {
		String[] words = line.split("\\W");							//Split the line to words ignoring whitespaces and  

		for(int i=0; i<words.length; i++) {
			if(words[i].length()<MIN_WORD_SIZE)						//If the word is smaller than MIN_WORD_SIZE ignore it
				continue;
			else if(words[i].length()<MAX_WORD_SIZE) {				//Else if the word is smaller than MAX_WORD_SIZE fill it with whitespaces
				while(words[i].length() != MAX_WORD_SIZE) {			
					words[i] = words[i] + " ";
				}
				Tuple t = new Tuple(words[i], lineNum);
				tl.add(t);
			}else {													//Else cut the word down to MAX_WORD_SIZE
				words[i] = words[i].substring(0,MAX_WORD_SIZE);
				Tuple t = new Tuple(words[i], lineNum);
				tl.add(t);
			}
		}
	}

	/**
	 * This method converts all the words of the text to tuples and appends them in the list of tuples
	 * <p><i>For every line of the file it calls the method <tt>createNewTuplesFromLine</tt></i>
	 * @param tl the list of tuples
	 * @param dll the doubly linked list used in the text editor
	 */
	public void addTuplesToTheList(List<Tuple> tl, DLL dll) {
		tl.clear();
		Node node = dll.head;
		int lineNum = 0;
		String line = "";

		while (node.next != null) 							//Go to the first node of the DLL(first line of the text)
			node = node.next; 
		while(node != null) {								//Take every node of the DLL
			lineNum++;
			line = node.data;
			node = node.prev;
			createNewTuplesFromLine(tl, line, lineNum);		//Convert of the words of the (lineNum)th line to tuples
		}
	}

	/**
	 * This method creates (or updates if it already exists) the index file of the text file in the same directory
	 * <p>This is done page by page 
	 * @param tl the list of tuples
	 * @param fn the path of the text file
	 * @throws IOException
	 */
	public void createIndexFile(List<Tuple> tl, String fn) throws IOException {
		TupleConverter tc = new TupleConverter(PAGE_SIZE, MIN_WORD_SIZE, MAX_WORD_SIZE);	//New instance of the class TupleConverter
		String newFileName = fn + ".ndx";													//Create the path of the index file
		Files.deleteIfExists(Paths.get(newFileName));										//Check if the index file already exists, if so delete it
		FilePageAccess fpa = new FilePageAccess(newFileName, PAGE_SIZE);					//New instance of the class FilePageAccess
		int tuplesPerPage = PAGE_SIZE / (MAX_WORD_SIZE+4);									//Calculate the tuples per data page
		double pages =  (double) tl.size() / tuplesPerPage;
		int numOfPages = (int) StrictMath.ceil(pages);										//Calculate the number of pages needed
		System.out.println("OK. Data pages of size "+ PAGE_SIZE+ " bytes: "+ numOfPages +"\n" + tl.size());
		for(int i=0; i<numOfPages; i++) {
			fpa.write(i, tc.getPageBytes(tl, i));											//Convert tuples to byte array of data page size and write it in the index file
		}
	}

	/**
	 * This method prints the whole list of tuples
	 * @param tl the list of tuples
	 */
	public void printTuplesList(List<Tuple> tl) {
		for(int i=0; i<tl.size(); i++)
			System.out.println(tl.get(i).word + "  " + tl.get(i).lineNum);
	}

	/**
	 * This method conducts a non-exhaustive serial search in the text 
	 * @param fn the path of the text file
	 * @param search the given word for search
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void serialSearch(String fn, String search) throws IOException, ClassNotFoundException {

		TupleConverter tc = new TupleConverter(PAGE_SIZE, MIN_WORD_SIZE, MAX_WORD_SIZE);		//New instance of the class TupleConverter
		String newFileName = fn + ".ndx";
		FilePageAccess fpa = new FilePageAccess(newFileName, PAGE_SIZE);						//New instance of the class FilePageAccess
		List<Integer> linesFound = new ArrayList<Integer>();									//New list of integers with the number of lines where the searched word was found
		int discAccesses = 0;

		outer: for(int i=0; fpa.hasNext(); i++) {												//If this is not the last page read it
			List<Tuple> tl = tc.toTuples(fpa.read(i));
			discAccesses++;
			for(int l=0; l<tl.size(); l++) {													
				String word = tl.get(l).getWord().replaceAll(" ", "");							//Read all the words in the page
				if(search.equals(word)) {
					linesFound.add(tl.get(l).getLineNum());
					if(l!=tl.size()-1) 															
						if(!(tl.get(l+1).getWord().replace(" ", "").equalsIgnoreCase(search)))	//If the last word of the page is not the one the user searched break the serial search
							break outer;														//This means that the search is non-exhaustive
				}
			}
		}

		printResult(discAccesses, linesFound, search);											//Print the results of the search
	}

	/**
	 * This method conducts a binary search in the text 
	 * @param fn the path of the text file
	 * @param search the given word for search
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public void binarySearch(String fn, String search) throws ClassNotFoundException, IOException {

		TupleConverter tc = new TupleConverter(PAGE_SIZE, MIN_WORD_SIZE, MAX_WORD_SIZE);		//New instance of the class TupleConverter
		String newFileName = fn + ".ndx";
		FilePageAccess fpa = new FilePageAccess(newFileName, PAGE_SIZE);						//New instance of the class FilePageAccess
		List<Integer> linesFound = new ArrayList<Integer>();									//New list of integers with the number of lines where the searched word was found
		int discAccesses = 0;
		File file = new File(newFileName);
		int fileSize = (int)file.length();
		file = null;
		int numOfPages = fileSize/PAGE_SIZE;			//Calculate the number of pages in the text file												
		numOfPages--;
		boolean recursion = true; 
		int lower = 0;									//Parameters used as pointers in the binary search
		int upper = numOfPages;
		int pointer = 0;

		while(recursion) {
			
			if(upper>=lower) {
				pointer = lower + (upper-lower)/2; 		//Readjust the main pointer to the middle of the search range
				List<Tuple> tl = tc.toTuples(fpa.read(pointer));
				String firstWord = tl.get(0).getWord().replaceAll(" ", "");
				String lastWord = tl.get(tl.size()-1).getWord().replaceAll(" ", "");
				discAccesses++;
				if(!(firstWord.compareToIgnoreCase(search)<=0 && lastWord.compareToIgnoreCase(search)>=0)) {	//If the search is not in the page
					if(firstWord.compareToIgnoreCase(search)>0) {		//If the search is smaller than the first word of the page
						upper=pointer-1;								//Readjust the upper pointer of the range we are searching
						continue;
					}else{												//If the search is bigger than the last word of the page
						lower=pointer+1;								//Readjust the lower pointer of the range we are searching
						continue;
					}
				}else {
					recursion = false;									//If the search is in the page break the recursion
				}
			}else {
				recursion = false;										//If the upper pointer is bigger or equal to the lower pointer break the recursion
			}
		}
		recursion = true;

		List<Tuple> tl = tc.toTuples(fpa.read(pointer));				
		String firstWord = tl.get(0).getWord().replace(" ", "");

		while(search.equalsIgnoreCase(firstWord) && fpa.hasPrevious()) { 	//While there is a previous page and its first word is equal to the search 
			pointer--;														 //Check the previous page
			tl = tc.toTuples(fpa.read(pointer));
			discAccesses++;
			firstWord = tl.get(0).getWord().replace(" ", "");
		}
		String lastWord = tl.get(tl.size()-1).getWord().replace(" ", ""); 
		if(search.compareToIgnoreCase(lastWord)>0) {							//In case the first ever search is the first word of a page 
			pointer++;														//Go to the next page
		}

		while(recursion) {													//Read all the next pages with the searched word in them
			tl = tc.toTuples(fpa.read(pointer));
			discAccesses++;
			lastWord = tl.get(tl.size()-1).getWord().replaceAll(" ", "");

			for(int i=0; i<tl.size(); i++) {								//Add every word equal to the search word in the list of line numbers 
				String word = tl.get(i).getWord().replace(" ", "");
				if(search.equals(word)) {
					linesFound.add(tl.get(i).getLineNum());
				}
			}
			if(search.equalsIgnoreCase(lastWord) && fpa.hasNext()) {		//If the last word of the page equals the search and this is not the last page
				pointer++;													//Check the next page
				continue;
			}else {
				recursion = false;											//Else break the loop
			}
		}

		printResult(discAccesses, linesFound, search);						//Print the results of the search
	}

	/**
	 * This method prints the results of a search conducted by the methods <tt>serialSearch</tt> or <tt>binarySearch</tt>
	 * @param discAccesses the number of disc accesses conducted by the search methods
	 * @param linesFound the list of integers with the number of lines where the searched word was found
	 * @param search the given word for search 
	 */

	public void printResult(int discAccesses, List<Integer> linesFound, String search) {
		if(linesFound.size()==0) {														//If the searched word wasn't found
			System.out.println("Word \""+ search +"\" could not be found.");
		}else if(linesFound.size()==1){													//If the searched word was found only once
			System.out.print("\""+search+"\" is on line "+ linesFound.get(0)+"\n");
			System.out.println("disk accesses: "+discAccesses);
		}else {																			//If the searched word was found multiple times
			System.out.print("\""+search+"\" is on lines ");
			for(int i=0; i<linesFound.size()-1; i++) {
				System.out.print(linesFound.get(i)+",");
			}
			System.out.print(linesFound.get(linesFound.size()-1)+"\n");
			System.out.println("disk accesses: "+discAccesses);
		}
	}
} 

