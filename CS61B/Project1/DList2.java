public class DList2 {

  /**
   *  head references the sentinel node.
   *
   *  DO NOT CHANGE THE FOLLOWING FIELD DECLARATIONS.
   */

  protected DListNode2 head;
  protected long size;

  /* DList2 invariants:
   *  1)  head != null.
   *  2)  For any DListNode2 x in a DList2, x.next != null.
   *  3)  For any DListNode2 x in a DList2, x.prev != null.
   *  4)  For any DListNode2 x in a DList2, if x.next == y, then y.prev == x.
   *  5)  For any DListNode2 x in a DList2, if x.prev == y, then y.next == x.
   *  6)  size is the number of DListNode2s, NOT COUNTING the sentinel
   *      (denoted by "head"), that can be accessed from the sentinel by
   *      a sequence of "next" references.
   */

  /**
   *  DList2() constructor for an empty DList2.
   */
  public DList2() {
    head = new DListNode2();
    head.red = Integer.MIN_VALUE;
    head.green = Integer.MIN_VALUE;
    head.blue = Integer.MIN_VALUE;
    head.num = Integer.MIN_VALUE;
    head.next = head;
    head.prev = head;
    size = 0;
  }

  /**
   *  DList2() constructor for a one-node DList2.
   */
  public DList2(int[] a, int num1) {
    head = new DListNode2();
    head.red = Integer.MIN_VALUE;
    head.green = Integer.MIN_VALUE;
    head.blue = Integer.MIN_VALUE;
    head.num = Integer.MIN_VALUE;
    head.next = new DListNode2();
    head.next.red = a[0];
    head.next.green = a[1];
    head.next.blue = a[2];
    head.next.num = num1;
    head.prev = head.next;
    head.next.prev = head;
    head.prev.next = head;
    size = 1;
  }

  /**
   *  DList2() constructor for a two-node DList2.
   */
  public DList2(int[] a, int num1, int[] b, int num2) {
    head = new DListNode2();
    head.red = Integer.MIN_VALUE;
    head.green = Integer.MIN_VALUE;
    head.blue = Integer.MIN_VALUE;
    head.num = Integer.MIN_VALUE;
    head.next = new DListNode2();
    head.next.red = a[0];
    head.next.green = a[1];
    head.next.blue = a[2];
    head.next.num = num1;
    head.prev = new DListNode2();
    head.prev.red = b[0];
    head.prev.green = b[1];
    head.prev.blue = b[2];
    head.prev.num = num2;
    head.next.prev = head;
    head.next.next = head.prev;
    head.prev.next = head;
    head.prev.prev = head.next;
    size = 2;
  }

  /**
   *  insertFront() inserts an item at the front of a DList2.
   */
  public void insertFront(int[] i, int num) {
    // Your solution here.
    DListNode2 front = new DListNode2(i, num);
    head.next.prev = front;
    head.next.prev.prev = head;
    head.next.prev.next = head.next;
    head.next = front;
    size++;


  }

  /**
   *  removeFront() removes the first item (and first non-sentinel node) from
   *  a DList2.  If the list is empty, do nothing.
   */
  public void removeFront() {
    if (size > 0) {
      head.next = head.next.next;
      head.next.prev = head;
      size--;
    }
  }

  /**
   *  toString() returns a String representation of this DList.
   *
   *  DO NOT CHANGE THIS METHOD.
   *
   *  @return a String representation of this DList.
   */
  public String toString() {
    String result = "[  ";
    DListNode2 current = head.next;
    while (current != head) {
      result = result + current.red + current.green + current.blue + current.num + "  ";
      current = current.next;
    }
    return result + "]";
  }
}