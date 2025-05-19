import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.Point;

public class Parser {
    private static Point findK(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        int y = 0;

        while ((line = br.readLine()) != null) {
            for (int x = 0; x < line.length(); x++) {
                if (line.charAt(x) == 'K') {
                    br.close();
                    return new Point(x, y - 2);
                }
            }
            y++;
        }

        br.close();
        return null;
    }

    public static Board parseBoard(String path) throws IOException {
        File file = new File(path);
        List<String> lines = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) {
            lines.add(line);
        }
        br.close();

        Point kPos = findK(file);
        if (kPos == null) {
            throw new IllegalArgumentException("Tidak ditemukan karakter 'K' di file");
        }

        String[] sizeParts = lines.get(0).split(" ");
        int sizeX = Integer.parseInt(sizeParts[0]);
        int sizeY = Integer.parseInt(sizeParts[1]);

        int kSisi;
        char[][] buf = new char[sizeY][sizeX];
        Point exit = null;

        if (kPos.x == 0 && lines.size() == sizeY + 2) { // K di kiri
            exit = new Point(0, kPos.y);
            kSisi = 1;
            // baca buf dari kolom 1 sampai sizeX
            for (int y = 0; y < sizeY; y++) {
                String row = lines.get(y + 2); // asumsi papan mulai baris ke-2
                for (int x = 0; x < sizeX; x++) {
                    buf[y][x] = row.charAt(x + 1); // offset kolom +1 karena kol 0 K/spasi
                }
            }
        } else if (kPos.y == 0 && lines.size() == sizeY + 3) { // K di atas
            exit = new Point(kPos.x, 0);
            kSisi = 2;
            // baca buf dari baris 1 sampai sizeY
            for (int y = 0; y < sizeY; y++) {
                String row = lines.get(y + 3); // asumsi papan mulai baris ke-3 (karena K di baris 0)
                for (int x = 0; x < sizeX; x++) {
                    buf[y][x] = row.charAt(x);
                }
            }
        } else if (kPos.x == sizeX && lines.size() == sizeY + 2) { // K di kanan (asumsi K di kolom sizeX + 1)
            exit = new Point(sizeX - 1, kPos.y);
            kSisi = 3;
            // baca buf biasa
            for (int y = 0; y < sizeY; y++) {
                String row = lines.get(y + 2);
                for (int x = 0; x < sizeX; x++) {
                    buf[y][x] = row.charAt(x);
                }
            }
        } else if (kPos.y == sizeY && lines.size() == sizeY + 3) { // K di bawah (asumsi K di baris sizeY + 2)
            exit = new Point(kPos.x, sizeY - 1);
            kSisi = 4;
            // baca buf biasa
            for (int y = 0; y < sizeY; y++) {
                String row = lines.get(y + 2);
                for (int x = 0; x < sizeX; x++) {
                    buf[y][x] = row.charAt(x);
                }
            }
        } else {
            throw new IllegalArgumentException("Posisi 'K' tidak sesuai asumsi");
        }

        // Buat pieces dari buf
        Map<Character, Piece> pieceMap = new HashMap<>();
        boolean[][] visited = new boolean[sizeY][sizeX];

        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                char c = buf[y][x];
                if (c != '.' && !visited[y][x]) {
                    visited[y][x] = true;

                    // Cek horizontal
                    int len = 1;
                    int nx = x + 1;
                    while (nx < sizeX && buf[y][nx] == c) {
                        visited[y][nx] = true;
                        len++;
                        nx++;
                    }
                    if (len > 1) {
                        pieceMap.put(c, new Piece(c, true, len, new Point(x, y)));
                        continue;
                    }

                    // Cek vertical
                    len = 1;
                    int ny = y + 1;
                    while (ny < sizeY && buf[ny][x] == c) {
                        visited[ny][x] = true;
                        len++;
                        ny++;
                    }
                    if (len > 1) {
                        pieceMap.put(c, new Piece(c, false, len, new Point(x, y)));
                    }
                }
            }
        }

        return new Board(sizeX, sizeY, exit, new ArrayList<>(pieceMap.values()), kSisi);
    }
}
