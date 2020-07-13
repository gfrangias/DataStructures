package tuc.ece.cs202.project3;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Class for reading Binary Files and converting them to integer array
 * @author George Frangias
 * 
 */

public class BinaryFileReader {
	int numberOfKeys;
	String fileName;
	
	/**
	 * Constructor of BinaryFileReader
	 * It creates a new File variable and it computes the file's number of Integers(keys)
	 * @param fileName a file's path in a string
	 * 
	 */
	public BinaryFileReader(String fileName){
		File file = new File(fileName);
		this.numberOfKeys = (int) (file.length()/4);
		this.fileName = fileName;
	};

	/**
	 * A method that converts the file of this instance of BinaryFileReader to an Integer array
	 */
	public int[] readKeys() throws IOException {
		/*Create a new integer array of the same
		size as the numbers of keys in the file*/
		int[] keys = new int[this.numberOfKeys];
		//Create a new file variable 
		File file = new File(this.fileName);
		/*Create a new byte array to write
		the file in it
		*/
		byte[] fileData = new byte[(int) file.length()];
		
		/*Using a DataInputStream write the contents of 
		 * the binary file into fileData
		 */
		DataInputStream dis = new DataInputStream(new FileInputStream(file));
		dis.readFully(fileData);
		dis.close(); //Close the stream	

		
		/*Extract all the integers from 
		the byte array to an integer array*/
		for(int i=0; i<numberOfKeys*4; i+=4) {
			keys[i/4] = ByteBuffer.wrap(fileData, i, 4).getInt(i);
		}
		return keys;
	}
}