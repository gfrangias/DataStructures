package tuc.ece.cs202.project3;

/**
 * Class for the node of a tree
 * @author Mayank Jaiswal
 * @see <a href="https://www.geeksforgeeks.org/print-bst-keys-in-the-given-range/">www.geeksforgeeks.org/print-bst-keys-in-the-given-range</a>
 * 
 */
public class Node { 

	int info; 
	Node left, right; 

	/**
	 * Constructor
	 * @param i the integer stored in the node
	 */
	Node(int i) { 
		info = i; 
		left = right = null; 
	} 
} 
