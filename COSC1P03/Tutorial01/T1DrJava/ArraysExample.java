package T1DrJava;


public class ArraysExample {
  private int[] a;//declared an array called a which stores ints.
  
  public ArraysExample() { 
    a = new int[2];//here is how we initialize it to hold two elements.
  }
  
  public static void main(String[] args) { 
    ArraysExample ae = new ArraysExample();
  }
  
  public static void rowMajor_3(){
    int[][] bruh;
    bruh = new int[100][100];
    for(int y = 0; y < bruh.length; y ++){
      for(int x = 0; x < bruh[y].length; x ++){
        bruh[y][x] = x + y * 100;
      }
    }
  }
  
  public static void columnMajor_4(){
    int[][] bruh;
    bruh = new int[100][100];
    for(int x = 0; x < 100; x ++){
      for(int y = 0; y < 100; y ++){
        bruh[y][x] = x + y * 100;
      }
    }
  }
  
  public static void diagonal_5(){
    int[][] bruh;
    bruh = new int[100][100];
    for(int i = 0; i < bruh.length; i ++){
      //just in case our index would be out of bounds we put it at the end
      int x = Integer.min(i, bruh[i].length - 1);
      bruh[i][x] = i + i * 100;
    }
  }
  
  public static void diagonal2_6(){
      int[][] bruh;
      bruh = new int[100][100];
      for(int i = 0; i < bruh.length; i ++){
        //just in case our index would be out of bounds we put it at the end
      	int x = Integer.min(bruh.length - i, bruh[i].length - 1);
        bruh[i][x] = i + i * 100;
      }
  }
  
  public static void triangle_7(){
    int[][] bruh;
    bruh = new int[100][100];
    
    for(int y = 0; y < bruh.length; y ++){
      for(int x = 0; x =< Integer.min(y, bruh[y].length); x ++){
        bruh[y][x] = x + y * 100;
      }
    }
  }
  
  public static void question8() {
  }
}
