/* HashTableChained.java */

package dict;
import list.*;

/**
 *  HashTableChained implements a Dictionary as a hash table with chaining.
 *  All objects used as keys must have a valid hashCode() method, which is
 *  used to determine which bucket of the hash table an entry is stored in.
 *  Each object's hashCode() is presumed to return an int between
 *  Integer.MIN_VALUE and Integer.MAX_VALUE.  The HashTableChained class
 *  implements only the compression function, which maps the hash code to
 *  a bucket in the table's range.
 *
 *  DO NOT CHANGE ANY PROTOTYPES IN THIS FILE.
 **/

public class HashTableChained implements Dictionary {

  /**
   *  Place any data fields here.
   **/
  protected DList[] hashTable;
  protected int size;


  /** 
   *  Construct a new empty hash table intended to hold roughly sizeEstimate
   *  entries.  (The precise number of buckets is up to you, but we recommend
   *  you use a prime number, and shoot for a load factor between 0.5 and 1.)
   **/

  public HashTableChained(int sizeEstimate) {
    boolean prime = false;
    int num = sizeEstimate;
    while (!prime) {
      for (int i = 2; i < num; i++) {
        if (num % i == 0) {
          num++;
          break;
        }
        else if (i == num - 1) {
          prime = true;
          size = num;
        }
      }
    }
    hashTable = new DList[size];
  }

  /** 
   *  Construct a new empty hash table with a default size.  Say, a prime in
   *  the neighborhood of 100.
   **/

  public HashTableChained() {
    hashTable = new DList[103];
    size = 103;
  }

  /**
   *  Converts a hash code in the range Integer.MIN_VALUE...Integer.MAX_VALUE
   *  to a value in the range 0...(size of hash table) - 1.
   *
   *  This function should have package protection (so we can test it), and
   *  should be used by insert, find, and remove.
   **/

  int compFunction(int code) {
    // Replace the following line with your solution.
    int comp = (((37 * code) + 2671) % 9973) % size;
    if (comp < 0) {
      return -comp;
    }
    return comp;
  }

  /** 
   *  Returns the number of entries stored in the dictionary.  Entries with
   *  the same key (or even the same key and value) each still count as
   *  a separate entry.
   *  @return number of entries in the dictionary.
   **/

  public int size() {
    try {
      int total = 0;
      for (int i = 0; i < size; i++) {
        if (hashTable[i] != null) {
          DListNode x = (DListNode) hashTable[i].front();
          while (x.isValidNode()) {
            x = (DListNode) x.next();
            total++;
          }
        }
      }
      return total;
    }
    catch (InvalidNodeException e) {
      System.out.println(e);
    }
    return 0;  
  }

  /** 
   *  Tests if the dictionary is empty.
   *
   *  @return true if the dictionary has no entries; false otherwise.
   **/

  public boolean isEmpty() {
    // Replace the following line with your solution.
    for (int i = 0; i < this.size; i++) {
      if (hashTable[i] != null) {
        return false;
      }
    }
    return true;
  }

  /**
   *  Create a new Entry object referencing the input key and associated value,
   *  and insert the entry into the dictionary.  Return a reference to the new
   *  entry.  Multiple entries with the same key (or even the same key and
   *  value) can coexist in the dictionary.
   *
   *  This method should run in O(1) time if the number of collisions is small.
   *
   *  @param key the key by which the entry can be retrieved.
   *  @param value an arbitrary object.
   *  @return an entry containing the key and value.
   **/

  public Entry insert(Object key, Object value) {
    // Replace the following line with your solution.

    int hashValue = compFunction(key.hashCode());
    Entry val = new Entry();
    val.key = key;
    val.value = value;
    if (hashTable[hashValue] == null) {
      hashTable[hashValue] = new DList();
    }
    hashTable[hashValue].insertFront(val, false);
    return val;
  }

  /** 
   *  Search for an entry with the specified key.  If such an entry is found,
   *  return it; otherwise return null.  If several entries have the specified
   *  key, choose one arbitrarily and return it.
   *
   *  This method should run in O(1) time if the number of collisions is small.
   *
   *  @param key the search key.
   *  @return an entry containing the key and an associated value, or null if
   *          no entry contains the specified key.
   **/

  public Entry find(Object key) {
    try {
      int hashValue = compFunction(key.hashCode());
      if (hashTable[hashValue] == null) {
        return null;
      }
      else {
        DListNode x = (DListNode) hashTable[hashValue].front();
        while (x.isValidNode()) {
          if (((Entry) x.item()).key.equals(key)) {
            return (Entry) x.item();
          }
          x = (DListNode) x.next();
        }
        return null;
      }
    }
    catch (InvalidNodeException e) {
      System.out.println(e);
    }
    return null;
  }

  /** 
   *  Remove an entry with the specified key.  If such an entry is found,
   *  remove it from the table and return it; otherwise return null.
   *  If several entries have the specified key, choose one arbitrarily, then
   *  remove and return it.
   *
   *  This method should run in O(1) time if the number of collisions is small.
   *
   *  @param key the search key.
   *  @return an entry containing the key and an associated value, or null if
   *          no entry contains the specified key.
   */

  public Entry remove(Object key) {
    try {
      int hashValue = compFunction(key.hashCode());
      if (hashTable[hashValue] == null) {
        return null;
      }
      else {
        DListNode x = (DListNode) hashTable[hashValue].front();
        while (x.isValidNode()) {
          if (((Entry) x.item()).key.equals(key) && hashTable[hashValue].length() == 1) {
            hashTable[hashValue] = null;
            return (Entry) x.item();
          }
          else if (((Entry) x.item()).key.equals(key)) {
            Object item = x.item();
            x.remove();
            return (Entry) item;
          }
          x = (DListNode) x.next();
        }
        return null;
      }
    }
    catch (InvalidNodeException e) {
      System.out.println(e);
    }
    return null;
  }
  /* returns the load Factor of this hashTable. */
  public double loadFactor() {
    return size() / this.size;
  }
  /* resizes the hashTable depending on the load factor. If the load factor is less than .5, the hashTable is halved.
  * If the load factor is greater than 1, the hashTable is doubled. All items are unchanged 
  * (though their spots in the hashTab array can change).
  */
  public void resize() {
    try {
      int origSize = this.size;
      DList[] origHashTable = hashTable;
      DList[] replacement = new DList[10];
      if (origSize < 10) {
        return;
      }
      else if (loadFactor() > 1.0) {
        replacement = new DList[this.size * 2];
        this.size = this.size * 2;
      }
      else {
        replacement = new DList[this.size / 2];
        this.size = this.size / 2;
      }
      hashTable = replacement;
      for (int i = 0; i < origSize; i++) {
        if (origHashTable[i] == null) {
          continue;
        }
        else {
          DListNode current = (DListNode) origHashTable[i].front();
          while (current.isValidNode()) {
            this.insert(((Entry) current.item()).key(), ((Entry) current.item()).value());
            current = (DListNode) current.next();
          }
        }
      }
    }
    catch (InvalidNodeException e) {
      System.out.println(e);
    }
  }

  /**
   *  Remove all entries from the dictionary.
   */
  public void makeEmpty() {
    for (int i = 0; i < size; i++) {
      hashTable[i] = null;
    }
  }
  public int hashCollisions() {
    int coll = 0;
    for (int i = 0; i < size; i++) {
      if (hashTable[i] == null) {
        continue;
      }
      else {
        coll += hashTable[i].length() - 1;
      }
    }
    return coll;
  }
}
