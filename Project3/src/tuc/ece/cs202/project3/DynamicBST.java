package tuc.ece.cs202.project3;

import java.util.ArrayList;

/**
 * Class for the Dynamic Binary Search Tree
 * <p><i>It contains methods for
 * <p>1. Node Insertion
 * <p>2. Key Search in the tree
 * <p>3. Key Search of a range in the tree
 * <p>4. Creating a list of all the keys using Inorder Traversal</i>
 * @author Mayank Jaiswal
 * @author Georgios Frangias
 * @see <a href="https://www.geeksforgeeks.org/print-bst-keys-in-the-given-range/">www.geeksforgeeks.org/print-bst-keys-in-the-given-range</a> 
 */

public class DynamicBST {
	static Node root;
	public int comparison;
	public ArrayList<Integer> al;
	
	//Constructor
	public DynamicBST() {
		ArrayList<Integer> al = new ArrayList<Integer>();	//Create the ArrayList
		this.al = al;
	}

	//Getters and Setters
	
	public ArrayList<Integer> getAl() {
		return al;
	}

	public void setAl(ArrayList<Integer> al) {
		this.al = al;
	}

	public int getComparison() {
		return comparison;
	}

	public void setComparison(int comparison) {
		this.comparison = comparison;
	}

	public static Node getRoot() {
		return root;
	}

	public static void setRoot(Node root) {
		DynamicBST.root = root;
	}

	
	/**
	 * Method to insert a new node in the tree
	 * @param current the node that the method is currently using during tree traversal
	 * @param key the key of the node that will be inserted
	 * @return
	 */
	@SuppressWarnings("static-access")
	public Node insertKey(Node current, int key) {
		comparison++;
		//If the tree is empty, 
		if(this.root == null) {
			this.root = new Node(key);
		}

		comparison++;
		//If the current node doesn't exist
		if (current == null) {
			return new Node(key);	//Create the new node using the key in this location
		}

		comparison++;
		
		//If the new key is smaller than the key of the current node 
		if (key < current.info){
			current.left = insertKey(current.left, key);	//Search in the left sub-tree
		//Else if the new key is greater than the key of the current node
		} else if (key > current.info) {
			comparison++;
			current.right = insertKey(current.right, key);	
		//Else if the new key is equal to the key of the current node
		} else {
			comparison++;
			return current;	//Just return the current node, as the same key already exists
		}

		return current;
	}

	/**
	 * A method to display if the key in search was found by {@link #keyQuery(Node, int) keyQuery}
	 * @param current the node that the method is currently using during tree traversal
	 * @param key the key in search
	 */
	public void searchKey(Node current, int key) {
		if (keyQuery(current, key)) {
			System.out.println("The key "+ key +" was found in the tree.");
		}else {
			System.out.println("The key "+ key +" wasn't found in the tree.");
		}
	}

	/**
	 * A method that traverses the tree in search of a key
	 * @param current the node that the method is currently using during tree traversal
	 * @param key the key in search
	 * @return a boolean variable that is: 
	 * <p><b>true</b> if the key was found in the tree and 
	 * <p><b>false</b> if it wasn't found
	 */
	public boolean keyQuery(Node current, int key) {
		comparison++;
		//If the current node is null
		if (current == null) {
			return false;
		}

		comparison++;
		//If the key in search is equal to the key of the current node
		if(key == current.info) {
			return true;
		}

		comparison++;
		//If the key in search is smaller than the key of the current node
		if(key < current.info) {
			return keyQuery(current.left, key);	//Search in the left sub-tree
		}

		comparison++;
		//If the key in search is greater than the key of the current node
		if(key > current.info){
			return keyQuery(current.right, key);	//Search in the right sub-tree
		}
		
		return false;
	}

	/**
	 * A method that adds the tree's keys in the ArrayList of the tree using inorder traversal
	 * @param node the node that the method is currently using during tree traversal
	 */
    public void addInorder(Node node) { 
    	//If the current node is null
        if (node == null) 
            return;
  
        //Recurse the left sub-tree
        addInorder(node.left); 
  
        //Add the key of the node
        if(node!=null)
        	this.al.add(node.info);
        
        //Recurse the right sub-tree
        addInorder(node.right); 
    } 

	/**
	 * A method that prints all the nodes that have keys in range [ki,k2]
	 * @param node the node that the method is currently using during tree traversal
	 * @param k1 the lower end of the range in search
	 * @param k2 the upper end of the range in search
	 */
	public void rangeQuery(Node node, int k1, int k2) { 

		
		comparison++;
		//If the current node is null
		if (node == null) { 
			return; 
		} 

		/* Since the desired o/p is sorted, recurse for left subtree first 
	      If root->data is greater than k1, then only we can get o/p keys 
	      in left subtree */
		comparison++;
		if (k1 < node.info) { 
			rangeQuery(node.left, k1, k2); 
		} 

		/* if root's data lies in range, then prints root's data */
		comparison++;
		if (k1 <= node.info && k2 >= node.info) { 
			/*!!!I commented the printing command for the purpose of the project!!!*/
			//System.out.print(node.info + " "); 
		} 

		/* If root->data is smaller than k2, then only we can get o/p keys 
	      in right subtree */
		comparison++;
		if (k2 > node.info) { 
			rangeQuery(node.right, k1, k2); 
		} 
	}
	
	/**
	 * A method that converts the ArrayList to integer array
	 * <p><b> {@link #addInorder(Node) addInorder} method should precede this method</b>
	 * @return An integer array of the same size and elements
	 */
	public int[] getSortedArray() {
		int[] arr = new int[this.al.size()];
		for(int i=0; i<this.al.size(); i++) {
			arr[i]=this.al.get(i);
		}
		return arr;
	}
}