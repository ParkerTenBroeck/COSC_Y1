/*Package name, if applicable.*/
import BasicIO.*;
import java.util.LinkedList;
/*COSC 1P03
 *ASSIGNMENT #5
 *Username: pt21zs (Parker TenBroeck)
 *Student #: 7376726
 *
 *This is the starting point of a program for finding one's keys in a 'floor plan' data file.
 *You're provided with the code for loading the data file, and just need to fill in the recursive
 *algorithm. Make sure to include feedback on e.g. unsolveable floor plans!
 *
 *You've also been provided with three sample files, though many more are possible:
 *floor1.txt solves simply
 *floor2.txt requires quite a bit more 'backtracking'
 *floor3.txt is unsolveable
 */

public class Keychain {
    char[][] plans=null;
    int startRow=-1;//Starting row for 'Me'
    int startCol=-1;//Starting column for 'Me'
    //You can add extra stuff here if you like!
    
    public Keychain() {
        
    }
    
    /**
     * Basic code for loading the data file into the array.
     */
    private void loadFloorPlans() {
        int height,width;
        String filename;
        ASCIIDataFile file=new ASCIIDataFile();
        String temp;
        
        height=file.readInt();
        width=file.readInt();
        plans=new char[height][width];
        for (int i=0;i<height;i++) {
            temp=file.readLine();
            plans[i]=temp.toCharArray();
            if (temp.indexOf('M')>=0) {
                startRow=i;startCol=temp.indexOf('M');
                System.out.println("Start at "+startRow+","+startCol+".");
            }
        }
        System.out.println("File transcription complete!\n");
        solve();
    }
    
    private void solve() {
        boolean solved=false;
        System.out.println("Initial State:");
        printFloorPlans();
        
        if (recursiveSolve(this.startCol, this.startRow)) {
            System.out.println("\nFinal Layout:");
            printFloorPlans();
            System.out.print("Findeth yon keys: "); //Directions for getting to my keys

            int x = this.startCol;
            int y = this.startRow;
            // using a for loop so in the case -something- goes wrong
            // we don't run forever. the maximum amount of steps possible at all
            // has to be the total number of spaces so we make that our upper bounds.
            loop:
            for(int i = this.plans.length * this.plans[0].length; i >= 0; i --){
                var direction = this.plans[y][x];
                switch (direction){
                    case '1':
                        y -= 1;
                        System.out.print("U ");
                        break;
                    case '2':
                        System.out.print("R ");
                        x += 1;
                        break;
                    case '3':
                        System.out.print("D ");
                        y += 1;
                        break;
                    case '4':
                        System.out.print("L ");
                        x -= 1;
                        break;
                    case 'K':
                        break loop;
                    default:
                        throw new RuntimeException("Uh oh, Looks like somebody doesn't know how leave a path!!");
                }
            }
            System.out.println();
        }
        else {
            System.out.println("\nOh no! The keys are lost to us!");
            printFloorPlans(); //Displaying anyway, since we presumably modified the floor plans
        }
    }

    /**
     * Determines whether or not there exists a path from 'M' to 'K'<br/><br/>
     *
     * this.plans is modified from this call.
     * if this method returns true a valid path starting at x,y
     * with directions ('1' up, '2' right, '3' down, '4' left) on each node on the path to the key
     * with any node tried at some point filled in with 'X' <br/><br/>
     *
     * If this.plans is null, or either of its entries are 0 long
     * or either x or y is outside the plans (smaller than 0 larger than #row/columns)
     * this will always return false.<br/><br/>
     *
     * if this method returns false all nodes tried are marked with 'X'<br/><br/>
     *
     * @param x the current x(column) position.
     * @param y the current y(row) position
     * @return Whether or not we found a valud path (true for valid)
     * @apiNote x and y should be the position of the starting position ('M') when calling this funciton
     */
    private boolean recursiveSolve(int x, int y) {
        // if we ever encounter anything like this its safe to say
        // we aren't solved if the universe doesn't exist, the universe
        // has zero width, or we our outside the universe
        if (this.plans == null
                || this.plans.length == 0
                || x < 0 || y < 0
                || y >= this.plans.length
                || x >= this.plans[1].length){
            return false;
        }

        // hey look at that, that's pretty cool!
        // we found the key
        if (this.plans[y][x] == 'K'){
            return true;
        }

        // hey wait a second am I inside a wall??
        // oh maybe I've just been here before
        if (this.plans[y][x] != 'S' && this.plans[y][x] != 'M'){
            return false;
        }

        // X for excusssse me
        this.plans[y][x] = 'X';

        // this works, but I decided I wanted marks. Still too funny to not include
        //for(int i = 0; i < 4; i ++){
        //    // I promise you this is actually very funny and entertaining.
        //    // this makes it so [i = (x,y)]
        //    // 0 = (0, -1) up
        //    // 1 = (1, 0)  right
        //    // 2 = (0, 1)  down
        //    // 3 = (-1, 0) left
        //    int xOff = -(-1+(i-1)) >> ((4-i)&4);
        //    int yOff = (-1 + i) >> ((1+i)&4);
        //    if (this.recursiveSolve(x + xOff, y + yOff)){
        //        this.plans[y][x] = (char)('1' + i);
        //        return true;
        //    }
        //}

        // up
        if (this.recursiveSolve(x, y - 1)){
            // hey this is cool, there's a valid path to the key if we go up from here, lets mark that and tell the previous node there's a valid path
            this.plans[y][x] = '1';
            return true;
        }
        // right
        if (this.recursiveSolve(x + 1, y)){
            this.plans[y][x] = '2';
            return true;
        }
        // down
        if (this.recursiveSolve(x, y + 1)){
            this.plans[y][x] = '3';
            return true;
        }
        // left
        if (this.recursiveSolve(x - 1, y)){
            this.plans[y][x] = '4';
            return true;
        }

        // well, this sucks. whatever just let the person above us figure it out
        return false;
    }
    
    private void printFloorPlans() {
        for (char[] row:plans) {
            for (char c:row) System.out.print(c);
            System.out.println();
        }
    }
    
    public static void main(String args[]) {new Keychain().loadFloorPlans();}
}
