import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Solver {

    public static void main(String[] args) throws FileNotFoundException {
        String filename = "test_00.txt";
        Scanner cnsl = new Scanner(System.in);

        Board test = new Board( new File(filename) );
        Board.SolvingBoard solution = test.startSolution();
        while (!solution.isFinished()) {
            System.out.println("Puzzle:\n"+solution+"Please enter a command:\n");
            String command = cnsl.nextLine();
            Scanner scan = new Scanner(command);

            String keyword = scan.next();
            List<String> cargs = new ArrayList<>();
            List<String> cflags = new ArrayList<>();
            while (scan.hasNext()) {
                String word = scan.next();
                if (!word.startsWith( "-" )) { cargs.add(word); }
                else { cflags.add(word); }
            }

            if (keyword.equals("fill")) {

            }
        }
        System.out.println(solution);
    }

    private static class Commands {
        public static void fill(Board toChange, List<String> args, List<String> flags) {

        }
    }
}
