package tuc.ece.cs202.project3;

/**
 * This class implements the Linear Hashing data structure 
 * 
 * @param maxThreshold the maximum percentage for the split criterion
 * @param minThreshold the minimum percentage for the merge criterion
 * @param bucketSize the size of a bucket 
 * @param keysNum the number of keys already stored in the Linear Hashing data structure 
 * @param keySpace the number of empty spaces in the Linear Hashing data structure
 * @param p points to the next bucket that is going to be split
 * @param n the current number of buckets 
 * @param j the n that is used in the hash function
 * @param minBuckets the minimum and initial number of hash bucket this Linear Hash Table has
 * @param comparisons the number of comparisons performed
 * @author Spyros Argyropoulos
 * @author Stefanos Karasavvidis
 * @author Georgios Frangias
 *
 */

class LinearHashing {

	private HashBucket[] hashBuckets;	// pointer to the hash buckets

	private float maxThreshold;		// max load factor threshold
	private float minThreshold;		// min load factor threshold

	private int bucketSize;		// max number of keys in each bucket
	private int keysNum;			// number of keys currently stored in the table
	private int keySpace;			// total space the hash table has for keys
	private int p;				// pointer to the next bucket to be split
	private int n;				// current number of buckets
	private int j;				// the n used for the hash function
	private int minBuckets;			// minimum number of buckets this hash table can have
	private int comparisons;	//the number of comparisons performed
	
	/**
	 * The constructor of the class LinearHashing
	 * @param itsBucketSize the size of buckets
	 * @param initPages the initial number of buckets
	 * @param threshold the maximum percentage for the split criterion
	 */
	public LinearHashing(int itsBucketSize, int initPages, float threshold) { 	// Constructor.

		int i;

		bucketSize = itsBucketSize;
		keysNum = 0;
		p = 0;
		n = initPages;
		j = initPages;
		minBuckets = initPages;
		keySpace = n*bucketSize;
		maxThreshold = threshold;
		minThreshold = (float)0.5;
		comparisons=0;

		if ((bucketSize == 0) || (n == 0)) {
			System.out.println("error: space for the table cannot be 0");
			System.exit(1);
		}
		hashBuckets = new HashBucket[n];
		for (i = 0; i < n; i++) {
			hashBuckets[i] = new HashBucket(bucketSize);
		}
	}
	
	//Getters and Setters
	public int getBucketSize() {return bucketSize;}
	public int getKeysNum() {return keysNum;}
	public int getKeySpace() {return keySpace;}
	public void setBucketSize(int size) {bucketSize = size;}
	public void setKeysNum(int num) {keysNum = num;}
	public void setKeySpace(int space) {keySpace = space;}
	
	/**
	 * It returns a hash based on the n key
	 * @param key the n key
	 * @return an integer that shows the position of the bucket 
	 */
	private int hashFunction(int key){	// Returns a hash based on the key

		int retval;

		retval = key%this.j;
		this.addComparison();
		if (retval < 0)
			retval *= -1;
		if (retval >= p){
			//System.out.println( "Retval = " + retval);
			return retval;
		}
		else {
			retval = key%(2*this.j);
			this.addComparison();
			if (retval < 0)
				retval *= -1;
			//System.out.println( "Retval = " + retval);
			return retval;
		}
	}

	/**
	 * Get the current number of comparisons
	 * @return the current number of comparisons
	 */
	public int getComparisons() {
		return comparisons;
	}

	/**
	 * Add one more comparison
	 */
	public void addComparison() {
		this.comparisons++;
	}

	/**
	 * Set number of comparisons to zero
	 */
	public void clearComparisons() {
		this.comparisons=0;
	}

	/**
	 *
	 * @return the current load factor of the hash table. 
	 */
	private float loadFactor() {		// Returns the current load factor of the hash table.

		return ((float)this.keysNum)/((float)this.keySpace);
	}

	/**
	 * Splits the bucket with position "p"
	 */
	private void bucketSplit() {		// Splits the bucket pointed by p.

		int i;
		HashBucket[] newHashBuckets;

		newHashBuckets= new HashBucket[n+1];
		for (i = 0; i < this.n; i++){
			newHashBuckets[i] = this.hashBuckets[i];
		}

		hashBuckets = newHashBuckets;
		hashBuckets[this.n] = new HashBucket(this.bucketSize);
		this.keySpace += this.bucketSize;
		this.hashBuckets[this.p].splitBucket(this, 2*this.j, this.p, hashBuckets[this.n]);
		this.n++;
		this.addComparison();
		if (this.n == 2*this.j) {
			this.j = 2*this.j;
			this.p = 0;
		}
		else {
			this.p++;
		}
	}
	
	/**
	 * Merges the last bucket that was split
	 */
	private void bucketMerge() { 		// Merges the last bucket that was split

		int i;

		HashBucket[] newHashBuckets;
		newHashBuckets= new HashBucket[n-1];
		for (i = 0; i < this.n-1; i++) {
			newHashBuckets[i] = this.hashBuckets[i];
		}
		
		this.addComparison();
		if (this.p == 0) {
			this.j = (this.n)/2;
			this.p = this.j-1;
		}
		else {
			this.p--;
		}
		this.n--;
		this.keySpace -= this.bucketSize;
		this.hashBuckets[this.p].mergeBucket(this, hashBuckets[this.n]);
		hashBuckets = newHashBuckets;
	}

	/**
	 * Inserts a key in the Linear Hashing data structure
	 * @param key the key inserted
	 */
	public void insertKey(int key) {	// Insert a new key.

		//System.out.println( "hashBuckets[" + this.hashFunction(key) + "] =  " + key);
		this.hashBuckets[this.hashFunction(key)].insertKey(key, this);
		this.comparisons++;
		if (this.loadFactor() > maxThreshold){
			//System.out.println("loadFactor = " + this.loadFactor() );
			this.bucketSplit();
			//System.out.println("BucketSplit++++++");
		}
	}

	/**
	 * Deletes the key that is was requested from the Linear Hashing data structure
	 * @param key the requested key for deletion
	 */
	public void deleteKey(int key) {	// Delete a key.

		this.hashBuckets[this.hashFunction(key)].deleteKey(key, this);
		this.addComparison();
		if (this.loadFactor() > maxThreshold){
			this.bucketSplit();
		}
		else if ((this.loadFactor() < minThreshold) && (this.n > this.minBuckets)){
			this.addComparison();
			this.bucketMerge();
		}
	}

	/**
	 * Searches for a requested key in the Linear Hashing data structure
	 * @param key the requested key
	 * @return the answer in boolean form
	 */
	public boolean searchKey(int key) {		// Search for a key.

		return this.hashBuckets[this.hashFunction(key)].searchKey(key, this);
	}

	/**
	 *Prints the whole Linear Hashing table 
	 */
	public void printHash() {

		int i;

		for (i = 0; i < this.n; i++) {
			System.out.println("Bucket[" + i + "]");
			this.hashBuckets[i].printBucket(this.bucketSize);
		}
	}


} // LinearHashing class
