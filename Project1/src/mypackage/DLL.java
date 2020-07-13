package mypackage;

/**
 * Class for Doubly Linked List
 * <p>A complete working Java program to demonstrate all 
 * @author Sumit Ghosh
 * @author<i> With some additions by George Frangias</i>
 *@see <a> href="http://www.geeksforgeeks.org/doubly-linked-list/">www.geeksforgeeks.org/doubly-linked-list</a>
 */

//Class for Doubly Linked List 
public class DLL { 
	Node head; // head of list 

	/* Doubly Linked list Node*/
	class Node { 
		String data; 
		Node prev; 
		Node next; 

		// Constructor to create a new node 
		// next and prev is by default initialized as null 
		Node(String d) { data = d; } 
	} 

	// Adding a node at the front of the list 
	public void push(String new_data) 
	{ 
		/* 1. allocate node  
		 * 2. put in the data */
		Node new_Node = new Node(new_data); 

		/* 3. Make next of new node as head and previous as NULL */
		new_Node.next = head; 
		new_Node.prev = null; 

		/* 4. change prev of head node to new node */
		if (head != null) 
			head.prev = new_Node; 

		/* 5. move the head to point to the new node */
		head = new_Node; 
	} 

	/* Given a node as prev_node, insert a new node after the given node */
	public void InsertAfter(Node prev_Node, String new_data) 
	{ 

		/*1. check if the given prev_node is NULL */
		if (prev_Node == null) { 
			System.out.println("The given previous node cannot be NULL "); 
			return; 
		} 

		/* 2. allocate node  
		 * 3. put in the data */
		Node new_node = new Node(new_data); 

		/* 4. Make next of new node as next of prev_node */
		new_node.prev = prev_Node.prev; 

		/* 5. Make the next of prev_node as new_node */
		prev_Node.prev = new_node; 

		/* 6. Make prev_node as previous of new_node */
		new_node.next = prev_Node; 

		/* 7. Change previous of new_node's next node */
		if (new_node.prev != null) 
			new_node.prev.next = new_node; 
	} 


	/* Given a node as prev_node, insert a new node before the given node */
	public void InsertBefore(Node prev_Node, String new_data) 
	{ 

		/*1. check if the given prev_node is NULL */
		if (prev_Node == null) { 
			System.out.println("The given previous node cannot be NULL "); 
			return; 
		} 

		/* 2. allocate node  
		 * 3. put in the data */
		Node new_node = new Node(new_data); 

		/* 4. Make next of new node as next of prev_node */
		new_node.next = prev_Node.next; 

		/* 5. Make the next of prev_node as new_node */
		prev_Node.next = new_node; 

		/* 6. Make prev_node as previous of new_node */
		new_node.prev = prev_Node; 

		/* 7. Change previous of new_node's next node */
		if (new_node.next != null) 
			new_node.next.prev = new_node; 
	} 

	// Add a node at the end of the list 
	void append(String new_data) 
	{ 
		/* 1. allocate node  
		 * 2. put in the data */
		Node new_node = new Node(new_data); 

		Node last = head; /* used in step 5*/

		/* 3. This new node is going to be the last node, so 
		 * make next of it as NULL*/
		new_node.next = null; 

		/* 4. If the Linked List is empty, then make the new 
		 * node as head */
		if (head == null) { 
			new_node.prev = null; 
			head = new_node; 
			return; 
		} 

		/* 5. Else traverse till the last node */
		while (last.next != null) 
			last = last.next; 

		/* 6. Change the next of last node */
		last.next = new_node; 

		/* 7. Make last node as previous of new node */
		new_node.prev = last; 
	} 

	// Function to delete a node in a Doubly Linked List. 
	// head_ref --> pointer to head node pointer. 
	// del --> data of node to be deleted. 
	void deleteNode(Node head_ref, Node del) 
	{ 

		// Base case 
		if (head == null || del == null) { 
			return; 
		} 

		// If node to be deleted is head node 
		if (head == del) { 
			head = del.next; 
		} 

		// Change next only if node to be deleted 
		// is NOT the last node 
		if (del.next != null) { 
			del.next.prev = del.prev; 
		} 

		// Change prev only if node to be deleted 
		// is NOT the first node 
		if (del.prev != null) { 
			del.prev.next = del.next; 
		} 

		// Finally, free the memory occupied by del 
		return; 
	} 

	// This function prints contents of linked list starting from the given node 
	public void printlist(Node node) 
	{ 
		Node last = null; 
		System.out.println("Traversal in forward Direction"); 
		while (node != null) { 
			System.out.print(node.data + " "); 
			last = node; 
			node = node.next; 
		} 
		System.out.println(); 
		System.out.println("Traversal in reverse direction"); 
		while (last != null) { 
			System.out.print(last.data + " "); 
			last = last.prev; 
		} 
	} 

	public void print(boolean numbers) {
		Node node = head;
		if(numbers) {
			while (node.next != null) 
				node = node.next;  
			int i = 1;
			while(node != null) {
				System.out.print(i+ ") ");
				System.out.println(node.data);
				node = node.prev;
				i++;
			}
		}else {
			while (node.next != null) 
				node = node.next;  
			while(node != null) {
				System.out.println(node.data);
				node = node.prev;
			}
		}
	}

	public void printNode(Node node) {
		System.out.println(node.data);
	}

	public Node goToTheFirst(Node node) {
		node = head;
		while (node.next != null) { 
			node = node.next; 
		}
		return node;
	}

	public Node previousNode(Node node) {
		if(node.next != null) {
			node = node.next;
		}else {
			System.out.println("No previous line! This is the first line!");
		}
		return node;
	}

	public Node nextNode(Node node) {
		if(node.prev != null) {
			node = node.prev;
		}else {
			System.out.println("No next line! This is the last line!");
		}
		return node;
	}
	
	public void currentNodeNumber(Node curNode) {
		int i = 1;
		Node node = head;
		while (node.next != null) 
			node = node.next;  
		while(node != curNode && node.prev != null) {
			i++;
			node = node.prev;
		}
		System.out.println(i + ")");
	}
	
	public void numOfLinesAndChars() {
		int lines = 0;
		int characters = 0;
		char temp;
		Node node = head;
		while (node != null) { 
			lines++;
			for(int i = 0; i < node.data.length(); i++) {
				temp = node.data.charAt(i);
				if(temp != ' ')
					characters++;
			}
			node = node.next;
		}
		System.out.println(lines + " lines, " + characters + " characters");
	}
} 

//This code is contributed by Sumit Ghosh 