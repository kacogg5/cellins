import javax.swing.plaf.synth.Region;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Board {
    private int width, height;
    private String name, difficulty;
    private int[][] cboard;
    private int[][] sboard;

    public Board(File source) throws FileNotFoundException {
        Scanner scan = new Scanner(source);

        while (scan.hasNextLine()) {
            String line = scan.nextLine();
            StringTokenizer contents = new StringTokenizer(line, " ");
            String type = contents.nextToken();
            if (type.equals("i")) {
                this.name = contents.nextToken();
                this.width = Integer.parseInt(contents.nextToken());
                this.height = Integer.parseInt(contents.nextToken());
                this.difficulty = contents.nextToken();
            } else if (type.equals("c") || type.equals("s")) {
                if (name == null) { throw new IllegalArgumentException("File formatted incorrectly"); }

                int[][] tempBoard = new int[width][height];
                int x = 0, y = 0;
                while (contents.hasMoreElements()) {
                    if (y >= height) {
                        throw new IllegalArgumentException("Found more clues than fit within the given dimensions");
                    }
                    tempBoard[y][x++] = Integer.parseInt(contents.nextToken());
                    if (x >= width) {
                        x = 0;
                        y++;
                    }
                }

                if (type.equals("c")) { this.cboard = tempBoard; }
                else { this.sboard = tempBoard; }
            }
        }
    }

    public int getClueAt(int x, int y) {
        if ((x<0||x>=width)||(y<0||y>=height)) {
            throw new IllegalArgumentException("Coordinates out of bounds");
        }
        return cboard[y][x];
    }

    public int[][] getCluesAround(int x, int y) {
        int[][] around = new int[5][5];
        for (int row=y-2; row<=y+2; row++) {
            for (int col=x-2; col<=x+2; col++) {
                if ((col<0||col>=width)||(row<0||row>=height)) { continue; }
                if (cboard[row][col] >= 0) { around[row-(y-2)][col-(x-2)] = cboard[row][col]; }
            }
        }
        return around;
    }

    public int getSolutionAt(int x, int y) {
        if ((x<0||x>=width)||(y<0||y>=height)) {
            throw new IllegalArgumentException("Coordinates out of bounds");
        }
        return sboard[y][x];
    }

    public SolvingBoard startSolution() {
        return new SolvingBoard(this);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return name + ", " + width + "x" + height + ", " + difficulty;
    }

    public static class SolvingBoard {
        private Board parent;
        int width, height;
        private int[][] board;
        int unsure;

        public enum CellStates {;public final static int FILLED = 1, EMPTY = 0, UNSURE = -1;}

        public SolvingBoard(Board parent) {
            this.parent = parent;
            this.width = parent.width+4;
            this.height = parent.height+4;
            this.board = new int[width][height];
            this.unsure = 0;

            for (int i = 0; i<height; i++) {
                for (int j = 0; j < width; j++) {
                    if (i < 2 || i >= width-2 || j < 2 || j >= width-2) { board[i][j] = -2; }
                    else {
                        board[i][j] = -1;
                        unsure++;
                    }
                }
            }
        }

        public void setCell(int state, int x, int y) {
            if (this.board[y+2][x+2] == -1) { unsure--; }
            if (state == -1) { unsure++; }
            this.board[y+2][x+2] = state;
        }

        public int getCell(int x, int y) {
            if ((x<2||x>=width-2)||(y<2||y>=height-2)) {
                throw new IllegalArgumentException("Coordinates out of bounds");
            }
            return this.board[y][x];
        }

        public int[][] getCellGroup(int x, int y){
            if ((x<2||x>=width-2)||(y<2||y>=height-2)) {
                throw new IllegalArgumentException("Coordinates out of bounds");
            }

            int[][] group = new int[3][3];
            for (int i = x-1; i<=x+1; i++) {
                for (int j = y-1; j<=y+1; y++) {
                    group[j-(y-1)][i-(x-1)] = this.board[j][i];
                }
            }
            return group;
        }

        public int checkErrors() {
            int errors = 0;
            for (int i = 2; i < width-2; i++) {
                for (int j = 2; j < height-2; j++) {
                    if (this.board[j][i] != -1 && parent.getSolutionAt(i-2, j-2) != this.board[j][i]) { errors++; }
                }
            }
            return errors;
        }

        public boolean isFinished() {
            return ( this.unsure == 0 && checkErrors() == 0);
        }

        @Override
        public String toString() {
            String s = "";
            for (int i = 1; i<height-1; i++) {
                for (int j = 1; j<width-1; j++) {
                    int cell = this.board[i][j];
                    if (cell == -2) {
                        s += "▓▓";
                    } else if (cell == -1) {
                        if (parent.getClueAt(j-2, i-2) > -1) {
                            s += " " + parent.getClueAt(j-2, i-2);
                        } else {
                            s += "  ";
                        }
                    } else {
                        s += (cell == 1) ? "██" : "░░";
                    }
                }
                s += "\n";
            }
            return s;
        }
    }
}
