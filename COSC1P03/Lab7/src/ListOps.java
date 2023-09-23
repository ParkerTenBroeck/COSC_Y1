import BasicIO.*;


public class ListOps {
    
    private ASCIIDisplayer  display;
    
    
    public ListOps ( ) {
        
        ASCIIDataFile  dataFile;
        List           aList;
        List           bList;
        
        display = new ASCIIDisplayer();
        dataFile = new ASCIIDataFile();
        aList = load(dataFile);
        dataFile.close();
        display.writeString("List: ");
        print(aList);
        display.newLine();
        display.writeString("Length: ");
        display.writeInt(length(aList));
        display.newLine();
        display.writeString("Reverse: ");
        printReverse(aList);
        display.newLine();
        display.writeString("Sum: ");
        display.writeInt(sum(aList));
        display.newLine();
        display.writeString("Max: ");
        display.writeInt(max(aList));
        display.newLine();
        display.writeString("Contains (3): ");
        display.writeBoolean(contains(aList,3));
        display.newLine();
        display.writeString("Contains (6): ");
        display.writeBoolean(contains(aList,6));
        display.newLine();
        bList = copy(aList);
        display.writeString("List (copy): ");
        print(bList);
        display.newLine();
        display.writeString("Length (copy): ");
        display.writeInt(length(bList));
        display.newLine();
        display.writeString("Equals copy: ");
        display.writeBoolean(equals(aList,bList));
        display.newLine();
        bList = plusOne(aList);
        display.writeString("List (plusOne): ");
        print(bList);
        display.newLine();
        display.writeString("Length (plusOne): ");
        display.writeInt(length(bList));
        display.newLine();
        display.writeString("Equals copy (plusOne): ");
        display.writeBoolean(equals(aList,bList));
        display.newLine();
        bList = append(aList,bList);
        display.writeString("List (append): ");
        print(bList);
        display.newLine();
        display.writeString("Length (append): ");
        display.writeInt(length(bList));
        display.newLine();
        bList = reverse(aList);
        display.writeString("List (reverse): ");
        print(bList);
        display.newLine();
        display.writeString("Length (reverse): ");
        display.writeInt(length(bList));
        display.newLine();
        
        
        display.close();
        
    };  // constructor
    
    
    public List load ( ASCIIDataFile from ) {
        
        int  v;
        
        v = from.readInt();
        if ( from.isEOF() ) {
            return null;
        }
        else {
            return new List(v,load(from));
        }
        
    };  // load
    
    
    private void print ( List aList ) {
        
        if ( aList == null ) {
            return;
        }
        else {
            display.writeInt(aList.head);
            print(aList.tail);
        };
        
    };  // print
    
    
    private void printReverse ( List aList ) {
        
        if ( aList == null ) {
            return;
        }
        else {
            printReverse(aList.tail);
            display.writeInt(aList.head);
        };
        
    };  // printReverse
    
    
    private int length ( List aList ) {
        
        if ( aList == null ) {
            return 0;
        }
        else {
            return 1 + length(aList.tail);
        }
    };  // length
    
    
    private int sum ( List aList ) {
        
        if ( aList == null ) {
            return 0;
        }
        else {
            return aList.head + sum(aList.tail);
        }
        
    };  // sum
    
    
    private int max ( List aList ) {
        
        if ( aList == null ) {
            return Integer.MIN_VALUE;
        }
        else {
            return Math.max(aList.head,max(aList.tail));
        }
        
    };  // max
    
    
    private boolean contains ( List aList, int v ) {
        
        if ( aList == null ) {
            return false;
        }
        else if ( aList.head == v ) {
            return true;
        }
        else {
            return contains(aList.tail,v);
        }
        
    };  // contains
    
    
    private List plusOne ( List aList ) {
        
        if ( aList == null ) {
            return null;
        }
        else {
            return new List(aList.head+1,plusOne(aList.tail));
        }
        
    };  // plusOne
    
    
    private List copy ( List aList ) {
        
        if ( aList == null ) {
            return null;
        }
        else {
            return new List (aList.head,copy(aList.tail));
        }
        
    }; // copy
    
    
    private boolean equals ( List aList, List other ) {
        
        if ( aList == null & other == null ) {
            return true;
        }
        else if ( aList == null | other == null ) {
            return false;
        }
        else if ( aList.head != other.head ) {
            return false;
        }
        else {
            return equals(aList.tail, other.tail);
        }
        
    };
    
    
    private List append ( List aList, List other ) {
        
        if ( aList == null ) {
            return other;
        }
        else {
            return new List(aList.head,append(aList.tail,other));
        }
        
    };  // append
    
    
    private List reverse ( List aList ) {
        
        if ( aList == null ) {
            return null;
        }
        else {
            return append(reverse(aList.tail),new List(aList.head,null));
        }
        
    };  // reverse
    
    
    public static void main ( String[] args ) { ListOps l = new ListOps(); };
    
    
}  // ListOps