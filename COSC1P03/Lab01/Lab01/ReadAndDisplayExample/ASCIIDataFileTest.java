package ReadAndDisplayExample;

import BasicIO.*;

/**
 * A minimal and a complete example that shows how to use ASCIIDataFile.
 * It allows the user to select a .txt file and it will read it cell by
 * cell and print out the values read on screen using System.out.println(...);
 * Please select the input.txt file attached in the same folder.
 * 
 * @author Maysara Al Jumaily
 * @since  January 17th, 2021
 * 
 */
public class ASCIIDataFileTest {
  private ASCIIDataFile file;//allows us to select a .txt file to read it. 
  
  public ASCIIDataFileTest() { 
    file = new ASCIIDataFile();//will allow us to select a .txt file
    //To make it easier, we have three columns
    //we will refer to them as left, middle, right
    //left is String, middle is int, right is double
    String left;
    int middle;
    double right;
    for( ; ; ){
      //read first cell (left column)
      left = file.readString();
      if(file.isEOF()){
        break;
      }
      middle = file.readInt();//read the middle cell
      right = file.readDouble();//read the right cell
      //print out the values that we just read. The 
      //\t means it is a tab character
      System.out.println(left + ", \t" + middle + ", \t" + right);
    }
    file.close();//important to close it!
  }
  
  public static void main(String[] args) { 
    ASCIIDataFileTest t = new ASCIIDataFileTest();
  }
}