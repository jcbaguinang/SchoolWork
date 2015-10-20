public class DListNode2 {

  /**
   *  item references the item stored in the current node.
   *  prev references the previous node in the DList.
   *  next references the next node in the DList.
   *
   *  DO NOT CHANGE THE FOLLOWING FIELD DECLARATIONS.
   */

  public int red, green, blue;
  public int num;
  public DListNode2 prev;
  public DListNode2 next;

  /**
   *  DListNode2() constructor.
   */
  DListNode2() {
    red = 0;
    green = 0;
    blue = 0;
    prev = null;
    next = null;
  }

  DListNode2(int[] a) {
    red = a[0];
    green = a[1];
    blue = a[2];
    prev = null;
    next = null;
  }
  DListNode2(int[] a, int b) {
    red = a[0];
    green = a[1];
    blue = a[2];
    num = b;
    prev = null;
    next = null;
  }

}