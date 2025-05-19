import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Board board = null;
        try {
            board = Parser.parseBoard("puzzle.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Buat root node
        Node root = new Node(board);

        // Jalankan
        Solver s = new Solver(root);
        boolean isFound = s.start(2);

        if (!isFound) {
            System.out.println("Tidak ditemukan");
        } else {
            System.out.println("\nSolution:");
            s.display();
        }
    }
}
