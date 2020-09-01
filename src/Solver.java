import java.io.File;
import java.io.FileNotFoundException;

public class Solver {

    public static void main(String[] args) throws FileNotFoundException {
        String filename = "test_00.txt";
        Board test = new Board( new File(filename) );
    }
}
