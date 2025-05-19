import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            System.out.print("Masukkan path ke file (relative terhadap ./bin/): ");
            String path = sc.nextLine();
    
            System.out.println("\nPilih algoritma pencarian:");
            System.out.println("1. UCS");
            System.out.println("2. Greedy Best-First Search");
            System.out.println("3. A*");
            System.out.print("Pilihan (1/2/3): ");
            int algo = sc.nextInt();
    
            int heuristic = 0;
            if (algo == 2 || algo == 3) {
                // 3. Jika heuristic diperlukan
                System.out.println("\nPilih heuristic:");
                System.out.println("1. Jumlah blocker ke pintu keluar (Recommended)");
                System.out.println("2. Jarak ke pintu keluar");
                System.out.print("Pilihan heuristic (1/2): ");
                heuristic = sc.nextInt();
            }

            Board board = Parser.parseBoard(path);
            Node root = new Node(board);
            Solver s = new Solver(root);
            int[] visitedNodeCtr = new int[1];

            long start = System.nanoTime();
            boolean isFound = s.start(algo, heuristic, visitedNodeCtr);
            long end = System.nanoTime();
            long durationInMillis = (end - start) / 1_000_000;

            System.out.println("\n========================");
            if (!isFound) {
                System.out.println("\nTidak ditemukan solusi");
            } else {
                System.out.println("\nSolusi:");
                s.display();
            }
            System.out.println("\nJumlah node dikunjungi: " + visitedNodeCtr[0]);
            System.out.println("Waktu eksekusi: " + durationInMillis + "ms");
        } catch (Exception e) {
            System.out.println("\nTerjadi kesalahan");
        }

        sc.close();
    }
}
