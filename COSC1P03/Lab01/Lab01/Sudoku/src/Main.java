import BasicIO.ASCIIDataFile;

import java.util.ArrayList;
import java.util.List;

public class Main {

    private byte [][] board;

    private static native boolean bruh();

    public static void main(String[] args) {
        Main main = new Main();
        bruh();

        main.checkPuzzle("../impossible.txt");
        main.checkPuzzle("../valid.txt");
        main.checkPuzzle("../invalid.txt");
        main.checkPuzzle("../puzzle.txt");
    }

    private void checkPuzzle(String filePath){
        System.out.println("Checking puzzle: " + filePath);
        loadPuzzle(filePath);
        displayPuzzle();
        checkRows();
        checkColumns();
        checkQuads();

        if(removeRepeating()){
            System.out.println("Fixed board");
            displayPuzzle();
        }
        if(solveBoard()){
            displayPuzzle();
            checkRows();
            checkColumns();
            checkQuads();
        }else{

            displayPuzzle();
        }
        System.out.println();
    }

    private void loadPuzzle(String filePath){
        ASCIIDataFile file = new ASCIIDataFile(filePath);
        this.board = new byte[9][9];

        for(int y = 0; y < board.length; y ++){
            for(int x = 0; x < board[y].length; x ++){
                board[y][x] = file.readByte();
                if (file.isEOF()){
                    throw new RuntimeException("This Parser API is shit");
                }
            }
        }
    }

    private void displayPuzzle(){
        for(int y = 0; y < board.length; y ++){
            for(int x = 0; x < board[y].length; x ++){
                System.out.print((board[y][x] == 0 ? "_" : board[y][x]) + (board[y].length <= x ? "" : "  "));
            }
            System.out.println();
        }
    }

    private void checkRows() {
        for(int y = 0; y < this.board.length; y ++){
            for(int i = 1; i <= 9; i ++){
                if (!findInRow(i, y)){
                    System.out.println("number: " + i + " not found in row y: " + y);
                }
            }
        }
    }

    private boolean findInRow(int checkFor, int inRow){
        for(int x = 0; x < this.board[inRow].length; x ++){
            if(this.board[inRow][x] == checkFor){
                return true;
            }
        }
        return false;
    }

    private void checkColumns() {
        for(int x = 0; x < this.board[0].length; x ++){
            for(int i = 1; i <= 9; i ++){
                if (!findInCol(i, x)){
                    System.out.println("number: " + i + " not found in column x: " + x);
                }
            }
        }
    }

    private boolean findInCol(int checkFor, int inCol){
        for(int y = 0; y < this.board.length; y ++){
            if(this.board[y][inCol] == checkFor){
                return true;
            }
        }
        return false;
    }

    private void checkQuads() {
        for(int y = 0; y < board.length; y += 3){
            for(int x = 0; x < board[y].length; x += 3){
                for(int i = 1; i <= 9; i ++){
                    if(!findInQuad(i, x, y)){
                        System.out.println("number: " + i + " not found in quad x:" + x + ", y: " + y);
                    }
                }
            }
        }
    }

    private boolean findInQuad(int checkFor, int qx, int qy){
        for(int y = qy; y < qy + 3; y ++) {
            for (int x = qx; x < qx + 3; x++) {
                if (this.board[y][x] == checkFor){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean removeRepeating(){
        boolean changed = false;

        //remove repeating for rows
        for(int y = 0; y < 9; y ++){
            for(int x = 0; x < 9; x ++){
                int tv = this.board[y][x];
                if (tv == 0)
                    continue;
                for(int tx = x+ 1; tx < 9; tx ++) {
                    if(tv == this.board[y][tx]){
                        this.board[y][tx] = 0;
                        this.board[y][x] = 0;
                        changed = true;
                    }
                }
            }
        }

        //remove repeating for columns
        for(int x = 0; x < 9; x ++){
            for(int y = 0; y < 9; y ++){
                int tv = this.board[y][x];
                if (tv == 0)
                    continue;
                for(int ty = y+ 1; ty < 9; ty ++) {
                    if(tv == this.board[ty][x]){
                        this.board[ty][x] = 0;
                        this.board[y][x] = 0;
                        changed = true;
                    }
                }
            }
        }

        //remove repeating for quads
        for(int y = 0; y < 9; y += 3){
            for(int x = 0; x < 9; x += 3){

                for(int i = 0; i < 9; i ++){
                    int tv = this.board[y + i / 3][x + i % 3];
                    if (tv == 0)
                        continue;
                    for(int j = i + 1; j < 9; j ++){
                        if (tv == this.board[y + j / 3][x + j % 3]){
                            this.board[y + j / 3][x + j % 3] = 0;
                            this.board[y + i / 3][x + i % 3] = 0;
                            changed = true;
                        }
                    }
                }
            }
        }
        return changed;
    }

    /**
     * This method assumes that the board is in a valid state
     * if the board is in an invalid state i.e. numbers out of rance [0..9] or
     * multiple numbers of the same value appear in the same row/column/quad
     * it may falsely report it as solved.
     */
    private boolean solveBoard(){
        if(solveBoardHelper(0,0)){
            System.out.println("Solved!");
            return true;
        }else{
            System.out.println("Cannot Solve :(");
            return false;
        }
    }
    private boolean solveBoardHelper(int sx, int sy){

        // find the next two slots that are zero (un decided)
        outer:
        for(; sy < this.board.length; sy ++){
            for(; sx < this.board[sy].length; sx ++){
                if(board[sy][sx] == 0){
                    break outer;
                }
            }
            sx = 0;
        }

        // next slot on the board
        int nx = (sx + 1) % 9;
        int ny = sy + (sx / 9);
        // find second next slot
        outer:
        for(; ny < this.board.length; ny ++){
            for(; nx < this.board[ny].length; nx ++){
                if(board[ny][nx] == 0){
                    break outer;
                }
            }
            nx = 0;
        }
        // we have reached the end of the board so return true
        if(sx >= 9 || sy >= 9) {
            return true;
        }
        var valid = calculateValid(sx, sy);

        // the next open slot doesn't exist
        if(nx >= 9 || ny >= 9){
            if (valid.size() == 1){
                // we reached the end of the board
                // if there is one valid possibility for this slot
                // the board can be solved
                this.board[sy][sx] = valid.get(0);
                return true;
            }else{
                return false;
            }
        }

        for(Byte guess : valid){
            this.board[sy][sx] = guess;
            if(solveBoardHelper(nx, ny)){
                return true;
            }
        }
        this.board[sy][sx] = 0;
        return false;
    }

    /**
     * calculate the valid numbers that can be placed on the board at the given position
     *
     * @param x the x cord on the board [0..9]
     * @param y the y cord on the board [0..9]
     * @return a list of valid numbers that can be put into the slot (x,y)
     */
    private ArrayList<Byte> calculateValid(int x, int y) {
        var list = new ArrayList<>(List.of(new Byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9}));

        // the cast to object on the remove methods is important because we want to remove the
        // item in the list with the same *value* and not remove at the index (would be clearer if emus were used but lol)

        // remove the possibilities occupied by its row
        for (int tx = 0; tx < 9; tx++) {
            list.remove((Object)this.board[y][tx]);
        }
        // remove the possibilities occupied by its column
        for (int ty = 0; ty < 9; ty++) {
            list.remove((Object)this.board[ty][x]);
        }
        // remove the possibilities occupied by its quad
        for (int ty = (y / 3) * 3; ty < (1 + y / 3) * 3; ty++) {
            for (int tx = (x / 3) * 3; tx < (1 + x / 3) * 3; tx++) {
                list.remove((Object)this.board[ty][tx]);
            }
        }
        return list;
    }
}


