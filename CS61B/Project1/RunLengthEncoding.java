/* RunLengthEncoding.java */

/**
 *  The RunLengthEncoding class defines an object that run-length encodes
 *  a PixImage object.  Descriptions of the methods you must implement appear
 *  below.  They include constructors of the form
 *
 *      public RunLengthEncoding(int width, int height);
 *      public RunLengthEncoding(int width, int height, int[] red, int[] green,
 *                               int[] blue, int[] runLengths) {
 *      public RunLengthEncoding(PixImage image) {
 *
 *  that create a run-length encoding of a PixImage having the specified width
 *  and height.
 *
 *  The first constructor creates a run-length encoding of a PixImage in which
 *  every pixel is black.  The second constructor creates a run-length encoding
 *  for which the runs are provided as parameters.  The third constructor
 *  converts a PixImage object into a run-length encoding of that image.
 *
 *  See the README file accompanying this project for additional details.
 */

import java.util.Iterator;


public class RunLengthEncoding implements Iterable {

  /**
   *  Define any variables associated with a RunLengthEncoding object here.
   *  These variables MUST be private.
   */
  private int height;
  private int width;
  private DList2 pixels;



  /**
   *  The following methods are required for Part II.
   */

  /**
   *  RunLengthEncoding() (with two parameters) constructs a run-length
   *  encoding of a black PixImage of the specified width and height, in which
   *  every pixel has red, green, and blue intensities of zero.
   *
   *  @param width the width of the image.
   *  @param height the height of the image.
   */

  public RunLengthEncoding(int width, int height) {
    this.height = height;
    this.width = width;
    pixels = new DList2(new int[] {0, 0, 0}, width * height);

  }

  /**
   *  RunLengthEncoding() (with six parameters) constructs a run-length
   *  encoding of a PixImage of the specified width and height.  The runs of
   *  the run-length encoding are taken from four input arrays of equal length.
   *  Run i has length runLengths[i] and RGB intensities red[i], green[i], and
   *  blue[i].
   *
   *  @param width the width of the image.
   *  @param height the height of the image.
   *  @param red is an array that specifies the red intensity of each run.
   *  @param green is an array that specifies the green intensity of each run.
   *  @param blue is an array that specifies the blue intensity of each run.
   *  @param runLengths is an array that specifies the length of each run.
   *
   *  NOTE:  All four input arrays should have the same length (not zero).
   *  All pixel intensities in the first three arrays should be in the range
   *  0...255.  The sum of all the elements of the runLengths array should be
   *  width * height.  (Feel free to quit with an error message if any of these
   *  conditions are not met--though we won't be testing that.)
   */

  public RunLengthEncoding(int width, int height, int[] red, int[] green,
                           int[] blue, int[] runLengths) {
    this.height = height;
    this.width = width;
    pixels = new DList2();
    for (int i = red.length - 1; i >= 0; i--) {
      pixels.insertFront(new int[] {red[i], green[i], blue[i]}, runLengths[i]);
    }
  }

  /**
   *  getWidth() returns the width of the image that this run-length encoding
   *  represents.
   *
   *  @return the width of the image that this run-length encoding represents.
   */

  public int getWidth() {
    // Replace the following line with your solution.
    return width;
  }

  /**
   *  getHeight() returns the height of the image that this run-length encoding
   *  represents.
   *
   *  @return the height of the image that this run-length encoding represents.
   */
  public int getHeight() {
    // Replace the following line with your solution.
    return height;
  }

  /**
   *  iterator() returns a newly created RunIterator that can iterate through
   *  the runs of this RunLengthEncoding.
   *
   *  @return a newly created RunIterator object set to the first run of this
   *  RunLengthEncoding.
   */
  public RunIterator iterator() {
    // Replace the following line with your solution.
    RunIterator iterate = new RunIterator(pixels);
    return iterate;
    // You'll want to construct a new RunIterator, but first you'll need to
    // write a constructor in the RunIterator class.
  }

  /**
   *  toPixImage() converts a run-length encoding of an image into a PixImage
   *  object.
   *
   *  @return the PixImage that this RunLengthEncoding encodes.
   */
  public PixImage toPixImage() {
    // Replace the following line with your solution.
    PixImage result = new PixImage(width, height);
    int count = 0; 
    int curr_wid = 0;
    int curr_len = 0;
    DListNode2 curr = pixels.head.next;
    for (int a = 0; a <= pixels.size; a++) {
      count = curr.num;
      while (count > 0) {
        if (curr_wid >= width) {
          curr_wid = 0;
          curr_len++;
        }
        result.setPixel(curr_wid, curr_len, (short) curr.red, (short) curr.green, (short) curr.blue);
        curr_wid++;
        count--;
      }
      curr = curr.next;
    }
    return result;
  }

  /**
   *  toString() returns a String representation of this RunLengthEncoding.
   *
   *  This method isn't required, but it should be very useful to you when
   *  you're debugging your code.  It's up to you how you represent
   *  a RunLengthEncoding as a String.
   *
   *  @return a String representation of this RunLengthEncoding.
   */
  public String toString() {
    // Replace the following line with your solution.
    RunIterator x = iterator();
    String result = "";
    int[] z = {0, 0, 0, 0};
    while (x.hasNext()) {
      z = x.next();
      result += (" num: " + z[0] + " colors: " + z[1] + z[2] + z[3]);
    }
    return result;
  }


  /**
   *  The following methods are required for Part III.
   */

  /**
   *  RunLengthEncoding() (with one parameter) is a constructor that creates
   *  a run-length encoding of a specified PixImage.
   * 
   *  Note that you must encode the image in row-major format, i.e., the second
   *  pixel should be (1, 0) and not (0, 1).
   *
   *  @param image is the PixImage to run-length encode.
   */
  public RunLengthEncoding(PixImage image) {
    // Your solution here, but you should probably leave the following line
    // at the end.
    width = image.getWidth();
    height = image.getHeight();
    short curr_red = image.getRed(width - 1,height - 1);
    short curr_green = image.getGreen(width - 1, height - 1);
    short curr_blue = image.getBlue(width - 1, height - 1);
    int count = 0;
    pixels = new DList2();
    for (int j = height - 1; j >= 0; j--) {
      for (int i = width - 1; i >= 0; i--) {
        if (curr_red == image.getRed(i,j) && curr_green == image.getGreen(i,j) && curr_blue == image.getBlue(i,j)) {
          count++;
        }
        else {
          pixels.insertFront(new int[] {curr_red, curr_green, curr_blue}, count);
          curr_red = image.getRed(i,j);
          curr_green = image.getGreen(i,j);
          curr_blue = image.getBlue(i,j);
          count = 1;
        }
      }
    }
    pixels.insertFront(new int[] {curr_red, curr_green, curr_blue}, count);
    check();
  }

  /**
   *  check() walks through the run-length encoding and prints an error message
   *  if two consecutive runs have the same RGB intensities, or if the sum of
   *  all run lengths does not equal the number of pixels in the image.
   */
  public void check() {

    RunIterator checker = iterator();
    int[] curr = checker.next();
    int[] next = checker.next();
    int sum = curr[0];
    if (curr[0] < 1) {
      System.out.println("Warning: RunLengthEncoding has run with length 0.");
    }
    while (checker.hasNext()) {
      if (curr[1] == next[1] && curr[2] == next[2] && curr[3] == next[3]) {
        System.out.println("Warning: RunLengthEncoding is not fully condensed.");
      }
      else if (next[0] < 1) {
        System.out.println("Warning: RunLengthEncoding has run with length 0.");
      }
      sum += next[0];
      curr = next;
      next = checker.next();
    }
    sum += next[0];
    if (sum != width * height) {
      System.out.println("Warning: RunLengthEncoding has incorrect size.");
    }
  }


  /**
   *  The following method is required for Part IV.
   */

  /**
   *  setPixel() modifies this run-length encoding so that the specified color
   *  is stored at the given (x, y) coordinates.  The old pixel value at that
   *  coordinate should be overwritten and all others should remain the same.
   *  The updated run-length encoding should be compressed as much as possible;
   *  there should not be two consecutive runs with exactly the same RGB color.
   *
   *  @param x the x-coordinate of the pixel to modify.
   *  @param y the y-coordinate of the pixel to modify.
   *  @param red the new red intensity to store at coordinate (x, y).
   *  @param green the new green intensity to store at coordinate (x, y).
   *  @param blue the new blue intensity to store at coordinate (x, y).
   */
  public void setPixel(int x, int y, short red, short green, short blue) {
    // Your solution here, but you should probably leave the following line
    //   at the end.

    int num = y * width + x + 1;
    DListNode2 curr = pixels.head.next;
    if (x == 0 && y == 0 && !(curr.red == red && curr.green == green && curr.blue == blue)) {
      if (curr.num == 1) {
        DListNode2 a = pixels.head.next.next;
        if (red == a.red && green == a.green && blue == a.blue) {
          curr.next.num++;
          pixels.head.next = a;
          a.prev = pixels.head;
          pixels.size--;
        }
        else {
          DListNode2 b = new DListNode2(new int[] {red, green, blue}, 1);
          pixels.head.next = b;
          a.prev = b;
          b.prev = pixels.head;
          b.next = a;
        }
      }
      else {
        curr.num--;
        pixels.insertFront(new int[] {red, green, blue}, 1);
        pixels.size--;
      }
    }
    else if (x == width - 1 && y == height - 1 && !(curr.red == red && curr.green == green && curr.blue == blue)) {
      DListNode2 a = pixels.head.prev;
      if (a.num == 1) {
        DListNode2 b = pixels.head.prev.prev;
        DListNode2 c = new DListNode2(new int[] {red, green, blue}, 1);
        if (b.red == c.red && b.blue == c.blue && b.green == c.green) {
          b.num++;
          b.next = pixels.head;
          pixels.head.prev = b;
        }
        else {
          pixels.head.prev = b;
          c.prev = b;
          b.next = c;
          c.next = pixels.head;
          pixels.head.prev = c;
        }
      }
      else {
        a.num--;
        pixels.head.prev = new DListNode2(new int[] {red, green, blue}, 1);
        pixels.head.prev.prev = a;
        pixels.head.prev.next = pixels.head;
        a.next = pixels.head.prev;
      }
    }
    else {
      while (num - curr.num > 0) {
        num -= curr.num;
        curr = curr.next;
      }
      if (red == curr.red && green == curr.green && blue == curr.blue) {
        return;
      }
      else {
        if (curr.num == 1) {
          if ((curr.next.red == red && curr.next.blue == blue && curr.next.green == green) && (curr.prev.red == red && curr.prev.green == green && curr.prev.blue == blue)) {
            curr.prev.num += curr.next.num + 1;
            curr.prev.next = curr.next.next;
            curr.prev.next.prev = curr.prev;
            pixels.size = pixels.size - 2;
          }     
          else if (curr.next.red == red && curr.next.blue == blue && curr.next.green == green) {
            curr.next.num++;
            curr.prev.next = curr.next;
            curr.next.prev = curr.prev;
            pixels.size--;
          }
          else if (curr.prev.red == red && curr.prev.green == green && curr.prev.blue == blue) {
            curr.prev.num++;
            DListNode2 a = curr.prev;
            DListNode2 b = curr.next;
            a.next = b;
            b.prev = a;
            pixels.size--;
          }
          else {
            DListNode2 a = curr.prev;
            DListNode2 b = curr.next;
            DListNode2 z = new DListNode2(new int[] {red, green, blue}, 1);
            a.next = z;
            z.prev = a;
            b.prev = z;
            z.next = b;
          } 
        }
        else if (num - curr.num == 0) {
          curr.num--;
          DListNode2 a = curr.next;
          if (red == a.red && green == a.green && blue == a.blue) {
            a.num++;
          }
          else {
            DListNode2 z = new DListNode2(new int[] {red, green, blue}, 1);
            curr.next = z;
            z.prev = curr;
            z.next = a;
            a.prev = z;
            pixels.size++;
          }
        }
        else if (num == 1) {
          DListNode2 a = curr.prev;
          curr.num--;
          if (a.red == red && a.green == green && a.blue == blue) {
            a.num++;
          }
          else {
            DListNode2 z = new DListNode2(new int[] {red, green, blue}, 1);
            a.next = z;
            z.prev = a;
            z.next = curr;
            curr.prev = z;
            pixels.size++;
          }
        }
        else {
          DListNode2 a = new DListNode2(new int[] {red, green, blue}, 1);
          DListNode2 b = new DListNode2(new int[] {curr.red, curr.green, curr.blue}, curr.num - num);
          DListNode2 c = curr.next;
          curr.num = num - 1;
          curr.next = a;
          a.prev = curr;
          a.next = b;
          b.prev = a;
          b.next = c;
          c.prev = b;
          pixels.size = pixels.size + 2;
        }
      }
    }
    check();
  }
 


  /**
   * TEST CODE:  YOU DO NOT NEED TO FILL IN ANY METHODS BELOW THIS POINT.
   * You are welcome to add tests, though.  Methods below this point will not
   * be tested.  This is not the autograder, which will be provided separately.
   */


  /**
   * doTest() checks whether the condition is true and prints the given error
   * message if it is not.
   *
   * @param b the condition to check.
   * @param msg the error message to print if the condition is false.
   */
  private static void doTest(boolean b, String msg) {
    if (b) {
      System.out.println("Good.");
    } else {
      System.err.println(msg);
    }
  }

  /**
   * array2PixImage() converts a 2D array of grayscale intensities to
   * a grayscale PixImage.
   *
   * @param pixels a 2D array of grayscale intensities in the range 0...255.
   * @return a new PixImage whose red, green, and blue values are equal to
   * the input grayscale intensities.
   */
  private static PixImage array2PixImage(int[][] pixels) {
    int width = pixels.length;
    int height = pixels[0].length;
    PixImage image = new PixImage(width, height);

    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        image.setPixel(x, y, (short) pixels[x][y], (short) pixels[x][y],
                       (short) pixels[x][y]);
      }
    }

    return image;
  }

  /**
   * setAndCheckRLE() sets the given coordinate in the given run-length
   * encoding to the given value and then checks whether the resulting
   * run-length encoding is correct.
   *
   * @param rle the run-length encoding to modify.
   * @param x the x-coordinate to set.
   * @param y the y-coordinate to set.
   * @param intensity the grayscale intensity to assign to pixel (x, y).
   */
  private static void setAndCheckRLE(RunLengthEncoding rle,
                                     int x, int y, int intensity) {
    rle.setPixel(x, y,
                 (short) intensity, (short) intensity, (short) intensity);
    rle.check();
  }

  /**
   * main() runs a series of tests of the run-length encoding code.
   */
  public static void main(String[] args) {
    // Be forwarned that when you write arrays directly in Java as below,
    // each "row" of text is a column of your image--the numbers get
    // transposed.
    PixImage image1 = array2PixImage(new int[][] { { 0, 3, 6 },
                                                   { 1, 4, 7 },
                                                   { 2, 5, 8 } });

    System.out.println("Testing one-parameter RunLengthEncoding constuctor " +
                       "on a 3x3 image.  Input image:");
    System.out.print(image1);
    RunLengthEncoding rle1 = new RunLengthEncoding(image1);
    rle1.check();
    System.out.println("Testing getWidth/getHeight on a 3x3 encoding.");
    doTest(rle1.getWidth() == 3 && rle1.getHeight() == 3,
           "RLE1 has wrong dimensions");

    System.out.println("Testing toPixImage() on a 3x3 encoding.");
    doTest(image1.equals(rle1.toPixImage()),
           "image1 -> RLE1 -> image does not reconstruct the original image");

    System.out.println("Testing setPixel() on a 3x3 encoding.");
    setAndCheckRLE(rle1, 0, 0, 42);
    image1.setPixel(0, 0, (short) 42, (short) 42, (short) 42);
    doTest(rle1.toPixImage().equals(image1),
           /*
                       array2PixImage(new int[][] { { 42, 3, 6 },
                                                    { 1, 4, 7 },
                                                    { 2, 5, 8 } })),
           */
           "Setting RLE1[0][0] = 42 fails.");

    System.out.println("Testing setPixel() on a 3x3 encoding.");
    setAndCheckRLE(rle1, 1, 0, 42);
    image1.setPixel(1, 0, (short) 42, (short) 42, (short) 42);
    doTest(rle1.toPixImage().equals(image1),
           "Setting RLE1[1][0] = 42 fails.");

    System.out.println("Testing setPixel() on a 3x3 encoding.");
    setAndCheckRLE(rle1, 0, 1, 2);
    image1.setPixel(0, 1, (short) 2, (short) 2, (short) 2);
    doTest(rle1.toPixImage().equals(image1),
           "Setting RLE1[0][1] = 2 fails.");

    System.out.println("Testing setPixel() on a 3x3 encoding.");
    setAndCheckRLE(rle1, 0, 0, 0);
    image1.setPixel(0, 0, (short) 0, (short) 0, (short) 0);
    doTest(rle1.toPixImage().equals(image1),
           "Setting RLE1[0][0] = 0 fails.");

    System.out.println("Testing setPixel() on a 3x3 encoding.");
    setAndCheckRLE(rle1, 2, 2, 7);
    image1.setPixel(2, 2, (short) 7, (short) 7, (short) 7);
    doTest(rle1.toPixImage().equals(image1),
           "Setting RLE1[2][2] = 7 fails.");

    System.out.println("Testing setPixel() on a 3x3 encoding.");
    setAndCheckRLE(rle1, 2, 2, 42);
    image1.setPixel(2, 2, (short) 42, (short) 42, (short) 42);
    doTest(rle1.toPixImage().equals(image1),
           "Setting RLE1[2][2] = 42 fails.");

    System.out.println("Testing setPixel() on a 3x3 encoding.");
    setAndCheckRLE(rle1, 1, 2, 42);
    image1.setPixel(1, 2, (short) 42, (short) 42, (short) 42);
    doTest(rle1.toPixImage().equals(image1),
           "Setting RLE1[1][2] = 42 fails.");


    PixImage image2 = array2PixImage(new int[][] { { 2, 3, 5 },
                                                   { 2, 4, 5 },
                                                   { 3, 4, 6 } });

    System.out.println("Testing one-parameter RunLengthEncoding constuctor " +
                       "on another 3x3 image.  Input image:");
    System.out.print(image2);
    RunLengthEncoding rle2 = new RunLengthEncoding(image2);
    rle2.check();
    System.out.println("Testing getWidth/getHeight on a 3x3 encoding.");
    doTest(rle2.getWidth() == 3 && rle2.getHeight() == 3,
           "RLE2 has wrong dimensions");

    System.out.println("Testing toPixImage() on a 3x3 encoding.");
    doTest(rle2.toPixImage().equals(image2),
           "image2 -> RLE2 -> image does not reconstruct the original image");

    System.out.println("Testing setPixel() on a 3x3 encoding.");
    setAndCheckRLE(rle2, 0, 1, 2);
    image2.setPixel(0, 1, (short) 2, (short) 2, (short) 2);
    doTest(rle2.toPixImage().equals(image2),
           "Setting RLE2[0][1] = 2 fails.");

    System.out.println("Testing setPixel() on a 3x3 encoding.");
    setAndCheckRLE(rle2, 2, 0, 2);
    image2.setPixel(2, 0, (short) 2, (short) 2, (short) 2);
    doTest(rle2.toPixImage().equals(image2),
           "Setting RLE2[2][0] = 2 fails.");


    PixImage image3 = array2PixImage(new int[][] { { 0, 5 },
                                                   { 1, 6 },
                                                   { 2, 7 },
                                                   { 3, 8 },
                                                   { 4, 9 } });

    System.out.println("Testing one-parameter RunLengthEncoding constuctor " +
                       "on a 5x2 image.  Input image:");
    System.out.print(image3);
    RunLengthEncoding rle3 = new RunLengthEncoding(image3);
    rle3.check();
    System.out.println("Testing getWidth/getHeight on a 5x2 encoding.");
    doTest(rle3.getWidth() == 5 && rle3.getHeight() == 2,
           "RLE3 has wrong dimensions");

    System.out.println("Testing toPixImage() on a 5x2 encoding.");
    doTest(rle3.toPixImage().equals(image3),
           "image3 -> RLE3 -> image does not reconstruct the original image");

    System.out.println("Testing setPixel() on a 5x2 encoding.");
    setAndCheckRLE(rle3, 4, 0, 6);
    image3.setPixel(4, 0, (short) 6, (short) 6, (short) 6);
    doTest(rle3.toPixImage().equals(image3),
           "Setting RLE3[4][0] = 6 fails.");

    System.out.println("Testing setPixel() on a 5x2 encoding.");
    setAndCheckRLE(rle3, 0, 1, 6);
    image3.setPixel(0, 1, (short) 6, (short) 6, (short) 6);
    doTest(rle3.toPixImage().equals(image3),
           "Setting RLE3[0][1] = 6 fails.");


    System.out.println("Testing setPixel() on a 5x2 encoding.");
    setAndCheckRLE(rle3, 0, 0, 1);
    image3.setPixel(0, 0, (short) 1, (short) 1, (short) 1);
    doTest(rle3.toPixImage().equals(image3),
           "Setting RLE3[0][0] = 1 fails.");


    PixImage image4 = array2PixImage(new int[][] { { 0, 3 },
                                                   { 1, 4 },
                                                   { 2, 5 } });

    System.out.println("Testing one-parameter RunLengthEncoding constuctor " +
                       "on a 3x2 image.  Input image:");
    System.out.print(image4);
    RunLengthEncoding rle4 = new RunLengthEncoding(image4);
    rle4.check();
    System.out.println("Testing getWidth/getHeight on a 3x2 encoding.");
    doTest(rle4.getWidth() == 3 && rle4.getHeight() == 2,
           "RLE4 has wrong dimensions");

    System.out.println("Testing toPixImage() on a 3x2 encoding.");
    doTest(rle4.toPixImage().equals(image4),
           "image4 -> RLE4 -> image does not reconstruct the original image");

    System.out.println("Testing setPixel() on a 3x2 encoding.");
    setAndCheckRLE(rle4, 2, 0, 0);
    image4.setPixel(2, 0, (short) 0, (short) 0, (short) 0);
    doTest(rle4.toPixImage().equals(image4),
           "Setting RLE4[2][0] = 0 fails.");

    System.out.println("Testing setPixel() on a 3x2 encoding.");
    setAndCheckRLE(rle4, 1, 0, 0);
    image4.setPixel(1, 0, (short) 0, (short) 0, (short) 0);
    doTest(rle4.toPixImage().equals(image4),
           "Setting RLE4[1][0] = 0 fails.");

    System.out.println("Testing setPixel() on a 3x2 encoding.");
    setAndCheckRLE(rle4, 1, 0, 1);
    image4.setPixel(1, 0, (short) 1, (short) 1, (short) 1);
    doTest(rle4.toPixImage().equals(image4),
           "Setting RLE4[1][0] = 1 fails.");
  }
}