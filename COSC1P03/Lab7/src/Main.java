import Collections.ConQueue;
import Collections.Queue;

import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    private double[][] map;

    private int from;
    private int to;

    public static void main(String[] args) throws Exception{
        var main = new Main();
        main.readDataFile("map2.txt");
        main.printMatrix();
        System.out.println("Shortest: " + main.distance(main.from, main.to));
    }

    private double distance(int from, int to){

        // idk what this should return if the verticie to self is something
        if (from == to){
            return 0.0;
        }

        Queue<Integer> queue = new ConQueue<Integer>();

        queue.enter(from);

        double[] closest = new double[this.map.length];
        for (int i = 0; i < closest.length; i ++){
            closest[i] = Double.MAX_VALUE;
        }
        closest[from] = 0.0;


        while(!queue.empty()){
            int p = queue.leave();

            for(int i = 0; i < this.map[p].length; i ++) {

                if (this.map[p][i] != Double.MAX_VALUE){
                    var dis = this.map[p][i] + closest[p];
                    if (closest[i] > dis){
                        closest[i] = dis;
                        queue.enter(i);
                    }
                }
            }
        }
        return closest[to];
    }

    private static final class Node{
        double totalDistance;
        int node;

        private Node(double totalDistance, int node){
            this.totalDistance = totalDistance;
            this.node = node;
        }
    }

    public void printMatrix(){
        var matrix = this.map;
        System.out.println();
        int maxWidth = 0;
        for(int y = 0; y < matrix.length; y ++){

            System.out.print((char)('a' + y));
            System.out.print(":\t");
            if (matrix[y].length > maxWidth){
                maxWidth = matrix[y].length;
            }
            for(int x = 0; x < matrix[y].length; x++){
                if (matrix[y][x] != Double.MAX_VALUE){
                    System.out.print(matrix[y][x] + ", ");
                }else{

                    System.out.print("-1,  ");
                }

            }
            System.out.println();
        }
        System.out.print("   ");
        for(int i = 0; i < maxWidth; i ++){

            System.out.print("  ");
            System.out.print((char)('a' + i));
            System.out.print("  ");
        }

        System.out.println("\nFrom: " + (char)('a' + this.from) + " To: " + (char)('a' + this.to));
    }

    public void readDataFile(String file) throws Exception{
        var str = Files.readString(Paths.get(file));
        var lines = str.split("\n");

        var width = Integer.parseInt(lines[0].trim());
        int height = lines.length - 2;

        double [][] arr = new double[lines.length - 2][];
        for(int i = 0; i < height; i ++){
            var nums = lines[i + 1].trim().split("\\s+");
            arr[i] = new double[nums.length];
            for(int j = 0; j < nums.length; j ++){
                if (nums[j].trim().equals("-1")){
                    arr[i][j] = Double.MAX_VALUE;
                }else{
                    arr[i][j] = Double.parseDouble(nums[j]);
                }
            }
        }
        var vals = lines[lines.length - 1].split("\\s+");
        this.from = Integer.parseInt(vals[0].trim());
        this.to = Integer.parseInt(vals[1].trim());

        this.map = arr;
    }
}