package mypackage;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class's purpose is to convert a list of tuples to a byte array of a set size <tt>pageSize</tt> and vice versa 
 *@param pageSize the size of the page given by MyEditor
 *@param minWordSize the minimum size of the word given by MyEditor
 *@param maxWordSize the maximum size of the word given by MyEditor
 *@author Georgios Frangias
 */
public class TupleConverter {
	int pageSize;
	int minWordSize;
	int maxWordSize;

	//Constructor
	public TupleConverter(int pageSize, int minWordSize, int maxWordSize) {
		this.pageSize = pageSize;
		this.minWordSize = minWordSize;
		this.maxWordSize = maxWordSize;
	}

	/**
	 * Converting a list of tuples to a byte array of a set size <tt>pageSize</tt> 
	 * @param tl the list of tuples in the text 
	 * @param curPage the number of the current page
	 * @return a byte array of size <tt>this.pageSize</tt>
	 */
	public byte[] getPageBytes(List<Tuple> tl, int curPage) {
		ByteBuffer bb = ByteBuffer.allocate(this.pageSize);			//Create a byte buffer of size equal to the size of the page

		int tupleLength = this.maxWordSize + 4;						//Calculate the length of a tuple
		int tuplesPerPage = this.pageSize / tupleLength;			//Calculate the number of tuples in a page
		int location = curPage * tuplesPerPage;						//Calculate the location of the current page in the list of tuples

		for(int i=location; i<(location + tuplesPerPage) && i<tl.size(); i++) {
			String curWord = tl.get(i).getWord();					//Get the word of the tuple
			int curLineNum = tl.get(i).getLineNum();				//Get the number of the line	

			bb.put(curWord.getBytes(StandardCharsets.US_ASCII)); 	//Put the word in the byteBuffer
			bb.putInt(curLineNum);									//Put the number of line in the byteBuffer
		}

		byte[] result = bb.array();									//The end result byte array

		return result;
	}

	/**
	 * Converting a byte array of a set size <tt>pageSize</tt> to a list of tuples
	 * @param bytes a byte array of a set size <tt>pageSize</tt>
	 * @return a list of tuples
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public List<Tuple> toTuples(byte[] bytes) throws IOException, ClassNotFoundException{
		List<Tuple> tl = new ArrayList<Tuple>();							//Create a new list of tuples

		int tupleLength = this.maxWordSize + 4;								//Calculate the length of a tuple

		for(int i=0; i<this.pageSize-tupleLength; i+=tupleLength) {
			byte[] word = Arrays.copyOfRange(bytes, i, i+this.maxWordSize);	//Carve the param bytes to get the word in byte array 
			ByteBuffer wordBuffer = ByteBuffer.wrap(word);					
			String curWord = new String(wordBuffer.array(), "US-ASCII");	//Convert the byte array to string
			if (curWord.isEmpty())
				break;			
			int curLineNum = ByteBuffer.wrap(bytes, i+this.maxWordSize, 4).getInt(i+this.maxWordSize);	//Carve again the bytes byte array and get the number of line
			Tuple t = new Tuple(curWord, curLineNum);						//Create the tuple
			tl.add(t);														//Add it in the list of tuples
		}
		return tl;															//Return the list of tuples
	}

}
