/* Entry.java */

package dict;

/**
 *  A class for dictionary entries.
 *
 *  DO NOT CHANGE THIS FILE.  It is part of the interface of the
 *  Dictionary ADT.
 **/

public class Entry {
	/**
	*  key and value are just the key and value of this entry.
	**/

  protected Object key;
  protected Object value;
  /** 
  *  returns the key of this Entry
  *  @param returns the key object
  **/

  public Object key() {
    return key;
  }
  /** 
  *  returns the value of this Entry
  *  @param returns the value object
  **/

  public Object value() {
    return value;
  }

}
