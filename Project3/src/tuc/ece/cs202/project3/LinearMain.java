package tuc.ece.cs202.project3;

import java.io.*; 
import java.util.*; 

/**
 * The main class of project 3 that implements everything that is requested for the 3rd project
 * @author Georgios Frangias
 *
 */
public class LinearMain {

	@SuppressWarnings("static-access")
	public static void main(String args[]) throws IOException, ClassNotFoundException {
		//New instance of LinearMain
		LinearMain lm = new LinearMain();
		
		//Used for testing in the Eclipse interface
		//String filename = "C:\\Users\\Γιώργος\\Documents\\Workspace_4th_Semester\\Project3\\testnumbers_10000_BE.bin";
		
		//Get the binary file's path from user input
		String filename = args[0];
		
		//New instance of the class BinaryFileReader
		BinaryFileReader bfr = new BinaryFileReader(filename);
		
		int initPages = 100, pageSize = 10, i;
		float avComparisons;
		
		//Create an instsance of LinearHashing with maximum threshold 0.5 
		LinearHashing Hash1 = new LinearHashing(pageSize, initPages, (float) 0.5);
		
		//Read the numbers from the binary file and add them in an integer array
		int[] nums = bfr.readKeys();
		
		//A float array used to print the results of the project 
		float[][] results = new float[100][8];
		
		//Create an instance of DynamicBST
		DynamicBST dbst = new DynamicBST();

		//Find the results of Linear Hashing with maximum threshold 0.5 
		for(int l=100; l<=10000; l+=100) {
			
			//Set the first column of the results table (Input Size(N))
			results[l/100-1][0]=l;
			
			//Initialize the LinearHashing
			Hash1 = new LinearHashing(pageSize, initPages, (float)0.5);

			//Insert all the data from the binary file to the Linear Hashing table
			for(i=0;i<l;i++) {
				Hash1.insertKey(nums[i]);
			}

			//Compute the comparisons per insertion
			avComparisons=(float)Hash1.getComparisons()/l;
			results[l/100-1][1]=avComparisons;
			Hash1.clearComparisons();

			//Generate 50 random numbers
			int[] randomArray = lm.generate50RandomNumbers(l);

			//Search for the 50 random numbers in the Linear Hashing table
			for(i=0; i<50; i++) {
				Hash1.searchKey(nums[randomArray[i]]);
			}

			//Compute the comparisons per search
			avComparisons=(float)Hash1.getComparisons()/50;
			results[l/100-1][2]=avComparisons;
			Hash1.clearComparisons();
			
			//Delete the same 50 random numbers
			for(i=0; i<50; i++) {
				Hash1.deleteKey(nums[randomArray[i]]);
			}
			
			//Compute the comparisons per deletions
			avComparisons=(float)Hash1.getComparisons()/50;
			results[l/100-1][3]=avComparisons;
			Hash1.clearComparisons();
		}

		//Find the results of Linear Hashing with maximum threshold 0.8
		for(int l=100; l<=10000; l+=100) {
			
			//Initialize the LinearHashing
			Hash1 = new LinearHashing(pageSize, initPages, (float)0.8);

			//Insert all the data from the binary file to the Linear Hashing table
			for(i=0;i<l;i++) {
				Hash1.insertKey(nums[i]);
			}

			//Compute the comparisons per insertion
			avComparisons=(float)Hash1.getComparisons()/l;
			results[l/100-1][4]=avComparisons;
			Hash1.clearComparisons();

			//Generate 50 random numbers
			int[] randomArray = lm.generate50RandomNumbers(l);
			
			//Search for the 50 random numbers in the Linear Hashing table
			for(i=0; i<50; i++) {
				Hash1.searchKey(nums[randomArray[i]]);
			}

			//Compute the comparisons per search
			avComparisons=(float)Hash1.getComparisons()/50;
			results[l/100-1][5]=avComparisons;
			Hash1.clearComparisons();
			
			//Delete the 50 numbers
			for(i=0; i<50; i++) {
				Hash1.deleteKey(nums[randomArray[i]]);
			}
			
			//Compute the comparisons per deletion
			avComparisons=(float)Hash1.getComparisons()/50;
			results[l/100-1][6]=avComparisons;
			Hash1.clearComparisons();
		}

		//Find the results of a Binary Search Tree
		for(int l=100; l<=10000; l+=100) {
			
			//Generate 50 random numbers
			int[] randomArray = lm.generate50RandomNumbers(l);

			//Insert all the data from the binary file to the Binary Search Tree
			for(i=0; i<l; i++) {
				dbst.insertKey(dbst.getRoot(), nums[i]);
			}
			dbst.setComparison(0);
			
			//Search for the 50 random numbers in the Binary Search Tree
			for(i=0; i<50; i++) {
				dbst.keyQuery(dbst.getRoot(), nums[randomArray[i]]);
			}
			
			//Compute the comparisons per search
			avComparisons=(float)dbst.getComparison()/50;
			results[l/100-1][7]=avComparisons;
			dbst.setComparison(0);
		}

		//Print the results
		lm.printResults(results);
		//lm.printResultsExcel(results);
	}
	
	/**
	 * Generates 50 random integers in the range of [0-upperLimit] 
	 * @param upperLimit the upper limit of the range
	 * @return an integer array with the 50 random numbers
	 */
	public int[] generate50RandomNumbers(int upperLimit) {
		Random rand = new Random();
		int[] randomArray = new int[50]; 
		for(int i=0; i<50; i++){
			randomArray[i] = rand.nextInt(upperLimit);
		}
		return randomArray;
	}
	
	/**
	 * Prints the results of the project in a table format
	 * @param results a float array where the results are stored
	 */
	public void printResults(float[][] results) {
		System.out.printf("%-15s%-15s%-15s%-15s%-15s%-15s%-15s%s%n", "Input Size(N)", "LH u>50%",
				"LH u>50%", "LH u<50%", "LH u>80%", "LH u>80%", "LH u<50%", "BST");
		System.out.printf("%-15s%-15s%-15s%-15s%-15s%-15s%-15s%s%n", " ", "avg # of", "avg # of",
				"avg # of", "avg # of", "avg # of", "avg # of", "avg # of");
		System.out.printf("%-15s%-15s%-15s%-15s%-15s%-15s%-15s%s%n"," ", "comparisons", "comparisons"
				, "comparisons", "comparisons", "comparisons", "comparisons", "comparisons");
		System.out.printf("%-15s%-15s%-15s%-15s%-15s%-15s%-15s%s%n", " ", "per insertion",
				"per search", "per deletion", "per intertion", "per search", "per deletion"
				, "per search");
		System.out.println("------------------------------------------------------------------------------"
				+ "--------------------------------------");
		for(int i=0; i<100; i++) {
			System.out.printf("%-15s%-15s%-15s%-15s%-15s%-15s%-15s%s%n", results[i][0],
					results[i][1], results[i][2], results[i][3], results[i][4], results[i][5]
							, results[i][6], results[i][7]);
		}
	}
	
	/**
	 * Prints the results in one row. Useful for transfer on an Microsoft Excel sheet 
	 * @param results a float array where the results are stored
	 */
	public void printResultsExcel(float[][] results) {
		for(int l=0; l<8; l++) {
			for(int i=0; i<100; i++){
				System.out.println(results[i][l]);
			}
			System.out.println("-----------------------");
		}
	}
}


