package tuc.ece.cs202.project3;

/**
 * This class implements the "bucket" of the Linear Hashing data structure  
 * 
 * @param keysNumber the number of keys in a bucket
 * @param keys the integer array which stores the keys of the bucket
 * @param overflowBucket a bucket that is created and used after the primary bucket fills
 * @author Spyros Argyropoulos
 * @author Stefanos Karasavvidis
 * @author Georgios Frangias
 *
 */

class HashBucket {

	private int keysNumber;
	private int[] keys;
	private HashBucket overflowBucket;

	/**
	 *Constructor of the class HashBucket 
	 * @param bucketSize the size of the bucket
	 */
	public HashBucket(int bucketSize) {	

		keysNumber = 0;				//Set the number of keys to 0
		keys = new int[bucketSize];	//Initialize the integer array with size bucketSize
		overflowBucket = null;		//No overflow bucket yet
	}

	public int numKeys(){return keysNumber;}
	
	/**
	 * Inserts a new key to the bucket
	 * @param key new integer key 
	 * @param lh the Linear Hashing data structure
	 */
	public void insertKey(int key, LinearHashing lh) { 
		int i;
		int bucketSize = lh.getBucketSize();
		int keysNum = lh.getKeysNum();
		int keySpace = lh.getKeySpace();
		for (i = 0; (i < this.keysNumber) && (i < bucketSize); i++){
			lh.addComparison();
			if (this.keys[i] == key){		//key already here. Ignore the new one
				return;
			}
		}
		lh.addComparison();
		if (i < bucketSize){				// bucket not full write the new key
			keys[i] = key;
			this.keysNumber++;
			keysNum++;						// update linear hashing class.
			lh.setKeysNum(keysNum); 			
			//System.out.println("HashBucket.insertKey: KeysNum = " + keysNum );
		}
		else {
			//System.out.println("Overflow.............");
			lh.addComparison();
			if (this.overflowBucket != null){	// pass key to the overflow
				this.overflowBucket.insertKey(key, lh);
			}
			else {						// create a new overflow and write the new key
				this.overflowBucket = new HashBucket(bucketSize);
				keySpace += bucketSize;
				lh.setKeySpace(keySpace);		// update linear hashing class.
				this.overflowBucket.insertKey(key, lh);
			}
		}
	}
	
	/**
	 * Delete the key of the bucket that was requested
	 * @param key the key that was requested for deletion
	 * @param lh the Linear Hashing data structure
	 */
	public void deleteKey(int key, LinearHashing lh) {

		int i;
		int bucketSize = lh.getBucketSize();
		int keysNum = lh.getKeysNum();
		int keySpace = lh.getKeySpace();

		for (i = 0; (i < this.keysNumber) && (i < bucketSize); i++) {
			lh.addComparison();
			if (this.keys[i] == key) {
				lh.addComparison();
				if (this.overflowBucket == null) {		// no overflow
					this.keys[i] = this.keys[this.keysNumber-1];
					this.keysNumber--;
					keysNum--;
					lh.setKeysNum(keysNum);			// update linear hashing class.
				}
				else {	// bucket has an overflow so remove a key from there and bring it here
					this.keys[i] = this.overflowBucket.removeLastKey(lh);
					keysNum--;
					lh.setKeysNum(keysNum);			// update linear hashing class.
					lh.addComparison();
					if (this.overflowBucket.numKeys() == 0) { // overflow empty free it
						this.overflowBucket = null;
						keySpace -= bucketSize;
						lh.setKeySpace(keySpace);			// update linear hashing class.
					}
				}
				return;
			}
		}
		if (this.overflowBucket != null) {			// look at the overflow for the key to be deleted if one exists
			this.overflowBucket.deleteKey(key, lh);
			if (this.overflowBucket.numKeys() == 0) {	// overflow empty free it
				this.overflowBucket = null;
				keySpace -= bucketSize;
				lh.setKeySpace(keySpace);				// update linear hashing class.
			}
		}
	}

	/**
	 * Removes the last key of the bucket
	 * @param lh the Linear Hashing data structure 
	 */
	int removeLastKey(LinearHashing lh) {

		int retval;
		int bucketSize = lh.getBucketSize();
		int keySpace = lh.getKeySpace();
		lh.addComparison();
		if (this.overflowBucket == null) {
			lh.addComparison();
			if (this.keysNumber != 0){
			    this.keysNumber--;
			    return this.keys[this.keysNumber];
			}
			return 0;
		}
		else {
			retval = this.overflowBucket.removeLastKey(lh);
			lh.addComparison();
			if (this.overflowBucket.numKeys() == 0) {	// overflow empty free it
				this.overflowBucket = null;
				keySpace -= bucketSize;
				lh.setKeySpace(keySpace);			// update linear hashing class.
			}
			return retval;
		}
	}

	/**
	 * Search a key in the bucket
	 * @param key the key in search of
	 * @param lh the Linear Hashing data structure  
	 * @return the boolean result of the search
	 */
	public boolean searchKey(int key, LinearHashing lh) {

		int i;
		int bucketSize = lh.getBucketSize();
		for (i = 0; (i < this.keysNumber) && (i < bucketSize); i++) {
			lh.addComparison();
			if (this.keys[i] == key) {	//key found
				return true;
			}
		}
		if (this.overflowBucket != null) {				//look at the overflow for the key if one exists
			return this.overflowBucket.searchKey(key,lh);
		}
		else {
			return false;
		}
	}

	/**
	 * Splits the bucket when the split criterion is met
	 * @param lh the Linear Hashing data structure 
	 * @param n the number used in the modulo calculation
	 * @param bucketPos the position of the bucket in the Linear Hashing data structure 
	 * @param newBucket the new bucket where some of the keys will be moved
	 */
	public void splitBucket(LinearHashing lh, int n, int bucketPos, HashBucket newBucket) {	//splits the current bucket

		int i;
		int bucketSize = lh.getBucketSize();
		int keySpace = lh.getKeySpace();
		int keysNum = lh.getKeysNum();

		for (i = 0; (i < this.keysNumber) && (i < bucketSize);) {
			lh.addComparison();
			if (Math.abs((this.keys[i]%n)) != bucketPos){	//key goes to new bucket
				newBucket.insertKey(this.keys[i], lh);
				this.keysNumber--;
				keysNum = lh.getKeysNum();
				keysNum--;
				lh.setKeysNum(keysNum);		// update linear hashing class.
				//System.out.println("HashBucket.splitBucket.insertKey: KeysNum = " + keysNum );
				this.keys[i] = this.keys[this.keysNumber];
			}
			else {				// key stays here
				i++;
			}
		}
		lh.addComparison();
		if (this.overflowBucket != null) {	// split the overflow too if one exists
			this.overflowBucket.splitBucket(lh, n, bucketPos, newBucket);
		}
		while (this.keysNumber != bucketSize) {
			lh.addComparison();
			if (this.overflowBucket == null) {
				return;
			}
			lh.addComparison();
			if (this.overflowBucket.numKeys() != 0) {
				this.keys[this.keysNumber] = this.overflowBucket.removeLastKey(lh);
				lh.addComparison();
				if (this.overflowBucket.numKeys() == 0) {	// overflow empty free it
					this.overflowBucket = null;
					keySpace -= bucketSize;
					lh.setKeySpace(keySpace);      // update linear hashing class.
				}
				this.keysNumber++;
			}
			else {				// overflow empty free it
				this.overflowBucket = null;
				keySpace -= bucketSize;
				lh.setKeySpace(keySpace);	// update linear hashing class.
			}
		}
	}

	/**
	 * Merges two already existing buckets into one
	 * @param lh the Linear Hashing data structure 
	 * @param oldBucket the bucket that will be deleted
	 */
	public void mergeBucket(LinearHashing lh, HashBucket oldBucket) {	//merges the current bucket

		while (oldBucket.numKeys() != 0) {
			this.insertKey(oldBucket.removeLastKey(lh), lh);
		}
	}

	/**
	 * Prints the contents of the bucket
	 * @param bucketSize the bucket that is printed
	 */
	public void printBucket(int bucketSize) {

		int i;

		for (i = 0; (i < this.keysNumber) && (i < bucketSize); i++) {
			System.out.println(this.keys[i]);
		}
		if (this.overflowBucket != null) {
			System.out.println("|");
			System.out.println("V");
			this.overflowBucket.printBucket(bucketSize);
		}
	}


} // HaskBucket class
