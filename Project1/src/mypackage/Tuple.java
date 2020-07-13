package mypackage;

/**
 * This class's purpose is to define the Object "Tuple" which includes two 
 * variables; a word and an integer
 * @param word a String storing a word
 * @param lineNum the number of the line where the word was found
 * @author Georgios Frangias
 *
 */
public class Tuple implements Comparable<Tuple>{

	String word;
	int lineNum;
	
	//Constructor
	Tuple(String word, int lineNum){
		this.word = word;
		this.lineNum = lineNum;
	}
	
	//Getters and Setters
	
	public String getWord() {
		return this.word;
	}



	public void setWord(String word) {
		this.word = word;
	}



	public int getLineNum() {
		return lineNum;
	}



	public void setLineNum(int lineNum) {
		this.lineNum = lineNum;
	}
	
	/**
	 * This method implements the method of the class Comparable
	 */
	public int compareTo(Tuple t) {
		return this.word.compareToIgnoreCase(t.word);
	}

}
