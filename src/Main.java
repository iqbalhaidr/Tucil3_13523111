import java.io.PrintWriter;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        PrintWriter pw = null;

        try {
            pw = new PrintWriter("../test/result.txt");
            System.out.print("\nMasukkan path ke file (relative terhadap ./bin/): ");
            String path = sc.nextLine();
            pw.println("File path: " + path);

            System.out.println("\nPilih algoritma pencarian:");
            System.out.println("1. Uniform Cost Search (UCS)");
            System.out.println("2. Greedy Best-First Search (GBFS)");
            System.out.println("3. A*");
            System.out.println("4. Iterative Deepening Searc (IDS) [BONUS]");
            System.out.print("Pilihan (1/2/3/4): ");
            int algo = sc.nextInt();
            if (algo == 1) {
                pw.println("Algoritma: Uniform Cost Search (UCS)");
            } else if (algo == 2) {
                pw.println("Algoritma: Greedy Best-First Search (GBFS)");
            } else if (algo == 3) {
                pw.println("Algoritma: A*");
            } else if (algo == 4) {
                pw.println("Algoritma: Iterative Deepening Search (IDS)");
            } else {
                throw new Exception("Invalid input");
            }

            int heuristic = 0;
            if (algo == 2 || algo == 3) {
                System.out.println("\nPilih heuristic:");
                System.out.println("1. Jumlah blocker ke pintu keluar (Recommended)");
                System.out.println("2. Jarak ke pintu keluar");
                System.out.print("Pilihan heuristic (1/2): ");
                heuristic = sc.nextInt();
                if (heuristic == 1) {
                    pw.println("Heuristik: Jumlah blocker ke pintu keluar");
                } else if (heuristic == 2) {
                    pw.println("Heuristik: Jarak ke pintu keluar");
                }
            }

            int maxDepth = 0;
            if (algo == 4) {
                System.out.print("Masukkan kedalaman maksimum (akan diiterasi 1 - maxDepth): ");
                maxDepth = sc.nextInt();
            }

            Board board = Parser.parseBoard(path);
            Node root = new Node(board);
            Solver s = new Solver(root);
            int[] visitedNodeCtr = new int[1];
            boolean isFound = false;

            long start = System.nanoTime();
            if (algo == 4) {
                isFound = s.startIDS(root, maxDepth, visitedNodeCtr);
            } else {
                isFound = s.start(algo, heuristic, visitedNodeCtr);
            }
            long end = System.nanoTime();
            long durationInMillis = (end - start) / 1_000_000;


            System.out.println("\n========================");
            pw.println("\n========================");
            if (!isFound) {
                System.out.println("\nTidak ditemukan solusi");
                pw.println("Tidak ditemukan solusi");
            } else {
                System.out.println("\nSolusi:\n");
                pw.println("\nSolusi:\n");
                s.display();
                s.display2(pw);
            }
            System.out.println("\nJumlah node dikunjungi: " + visitedNodeCtr[0]);
            pw.println("\nJumlah node dikunjungi: " + visitedNodeCtr[0]);
            System.out.println("Waktu eksekusi: " + durationInMillis + "ms");
            pw.println("Waktu eksekusi: " + durationInMillis + "ms");

            System.out.println("\nHasil disimpan di ../test/result.txt");
        } catch (Exception e) {
            System.out.println("\nTerjadi kesalahan");
            pw.println("Terjadi kesalahan");
        } finally {
            if (pw != null) {
                pw.close();
            }
            sc.close();
        }
    }
}
