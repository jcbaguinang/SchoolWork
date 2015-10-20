/* ListSorts.java */
package graphalg;
import list.*;

public class ListSorts {


  /**
   *  makeQueueOfQueues() makes a queue of queues, each containing one item
   *  of q.  Upon completion of this method, q is empty.
   *  @param q is a LinkedQueue of objects.
   *  @return a LinkedQueue containing LinkedQueue objects, each of which
   *    contains one object from q.
   **/
  public static LinkedQueue makeQueueOfQueues(LinkedQueue q) {
    LinkedQueue queueList = new LinkedQueue();
    while (!q.isEmpty()) {
      LinkedQueue curr = new LinkedQueue();
      try {
        curr.enqueue(q.dequeue());
      }
      catch (QueueEmptyException e) {
        System.out.println(e);
      }
      queueList.enqueue(curr);
    }
    return queueList;
  }

  /**
   *  mergeSortedQueues() merges two sorted queues into a third.  On completion
   *  of this method, q1 and q2 are empty, and their items have been merged
   *  into the returned queue.
   *  @param q1 is LinkedQueue of Comparable objects, sorted from smallest 
   *    to largest.
   *  @param q2 is LinkedQueue of Comparable objects, sorted from smallest 
   *    to largest.
   *  @return a LinkedQueue containing all the Comparable objects from q1 
   *   and q2 (and nothing else), sorted from smallest to largest.
   **/
  public static LinkedQueue mergeSortedQueues(LinkedQueue q1, LinkedQueue q2) {
    LinkedQueue result = new LinkedQueue();
    try {
      while (!q1.isEmpty() && !q2.isEmpty()) {
        if (((Comparable) q1.front()).compareTo((Comparable) q2.front()) < 0) {
          result.enqueue(q1.dequeue());
        }
        else {
          result.enqueue(q2.dequeue());
        }
      }
    }
    catch (QueueEmptyException e) {
      System.out.println(e);
    }
    if (!q1.isEmpty()) {
      result.append(q1);
    }
    else if (!q2.isEmpty()) {
      result.append(q2);
    }
    return result;
  }

  /**
   *  partition() partitions qIn using the pivot item.  On completion of
   *  this method, qIn is empty, and its items have been moved to qSmall,
   *  qEquals, and qLarge, according to their relationship to the pivot.
   *  @param qIn is a LinkedQueue of Comparable objects.
   *  @param pivot is a Comparable item used for partitioning.
   *  @param qSmall is a LinkedQueue, in which all items less than pivot
   *    will be enqueued.
   *  @param qEquals is a LinkedQueue, in which all items equal to the pivot
   *    will be enqueued.
   *  @param qLarge is a LinkedQueue, in which all items greater than pivot
   *    will be enqueued.  
   **/   
  public static void partition(LinkedQueue qIn, Comparable pivot, 
                               LinkedQueue qSmall, LinkedQueue qEquals, 
                               LinkedQueue qLarge) {
    try {
      while (!qIn.isEmpty()) {
        Object item = qIn.dequeue();
        if (((Comparable) item).compareTo(pivot) < 0) {
          qSmall.enqueue(item);
        }
        else if (((Comparable) item).compareTo(pivot) == 0) {
          qEquals.enqueue(item);
        }
        else {
          qLarge.enqueue(item);
        }
      }
    }
    catch (QueueEmptyException e) {
      System.out.println(e);
    }
  }

  /**
   *  mergeSort() sorts q from smallest to largest using mergesort.
   *  @param q is a LinkedQueue of Comparable objects.
   **/
  public static void mergeSort(LinkedQueue q) {
	  //System.out.println("MERGE SORT");
    try {
      LinkedQueue queueofQueues = makeQueueOfQueues(q);
      while (queueofQueues.size() != 1) {
        queueofQueues.enqueue(mergeSortedQueues((LinkedQueue) queueofQueues.dequeue(), (LinkedQueue) queueofQueues.dequeue()));
      }
      LinkedQueue queue = (LinkedQueue) queueofQueues.front();
      while(!queue.isEmpty()) {
        q.enqueue(queue.dequeue());
      }
    }
    catch (QueueEmptyException e) {
      System.out.println(e);
    }
  }

  /**
   *  quickSort() sorts q from smallest to largest using quicksort.
   *  @param q is a LinkedQueue of Comparable objects.
   **/
  public static void quickSort(LinkedQueue q) {
    LinkedQueue equals = new LinkedQueue();
    LinkedQueue smaller = new LinkedQueue();
    LinkedQueue larger = new LinkedQueue();
    partition(q, (Comparable) q.nth((int) (q.size() * Math.random())), smaller, equals, larger);
    if (smaller.size() > 1) {
      quickSort(smaller);
    }
    if (larger.size() > 1) {
      quickSort(larger);
    }
    q.append(smaller);
    q.append(equals);
    q.append(larger);

  }

  /**
   *  makeRandom() builds a LinkedQueue of the indicated size containing
   *  Integer items.  The items are randomly chosen between 0 and size - 1.
   *  @param size is the size of the resulting LinkedQueue.
   **/
  public static LinkedQueue makeRandom(int size) {
    LinkedQueue q = new LinkedQueue();
    for (int i = 0; i < size; i++) {
      q.enqueue(new Integer((int) (size * Math.random())));
    }
    return q;
  }

}
