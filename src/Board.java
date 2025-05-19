import java.util.*;
import java.awt.Point;

public class Board {
    private char[][] buf;
    private int sizeX, sizeY;
    private Point exitGate;
    private List<Piece> pieces;
    private int kPos; // 1 Kiri, 2 Atas, 3 Kanan, 4 Bawah

    public Board(int sizeX, int sizeY, Point exitGate, List<Piece> pieces, int kPos) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.exitGate = new Point(exitGate);
        this.kPos = kPos;
        this.pieces = new ArrayList<>();
        for (Piece p : pieces) {
            this.pieces.add(p.clone()); // Deep copy
        }
        rebuildBuffer();
    }

    // Menyalin papan dan semua komponennya (deep copy)
    public Board clone() {
        return new Board(this.sizeX, this.sizeY, this.exitGate, this.pieces, this.kPos);
    }

    // Mengisi map (buf) dengan piece
    public void rebuildBuffer() {
        buf = new char[sizeY][sizeX];
        for (int y = 0; y < sizeY; y++) {
            Arrays.fill(buf[y], '.');
        }

        for (Piece p : pieces) {
            Point h = p.getHead();
            for (int i = 0; i < p.getSize(); i++) {
                int x = h.x + (p.isOrientationHorizontal() ? i : 0);
                int y = h.y + (p.isOrientationHorizontal() ? 0 : i);
                // System.out.println("y: " + y + " x: " + x + " piece id: " + p.getId());
                buf[y][x] = p.getId();
            }
        }
    }

    public Piece findPiece(char pieceId) { // Ini aman harusnya karena terbatas max 24 element
        for (Piece p : pieces) {
            if (p.getId() == pieceId) {
                return p;
            }
        }
        System.out.println("Piece tidak ditemukan [findPiece()]");
        return null;
    }

    public void movePiece(char pieceId, int step) {
        Piece target = findPiece(pieceId);

        if (target != null) {
            target.move(step);
            rebuildBuffer();
        }
    }

    public List<Integer> calcPossibleMove(char pieceId) {
        List<Integer> possibleMoves = new ArrayList<>();
        Piece target = findPiece(pieceId);

        // Cek ke arah negatif
        for (int i = 1;; i++) {
            if (canMove(target, -i)) {
                possibleMoves.add(-i);
            } else {
                break;
            }
        }

        // Cek ke arah positif
        for (int i = 1;; i++) {
            if (canMove(target, i)) {
                possibleMoves.add(i);
            } else {
                break;
            }
        }

        return possibleMoves;
    }

    private boolean canMove(Piece piece, int step) {
        Point h = piece.getHead();
        int x = h.x, y = h.y;
        int size = piece.getSize();

        for (int i = 0; i < size; i++) {
            int checkX = x + (piece.isOrientationHorizontal() ? i : 0);
            int checkY = y + (piece.isOrientationHorizontal() ? 0 : i);

            int moveX = checkX + (piece.isOrientationHorizontal() ? step : 0);
            int moveY = checkY + (piece.isOrientationHorizontal() ? 0 : step);

            if (moveX < 0 || moveX >= sizeX || moveY < 0 || moveY >= sizeY) {
                return false;
            }

            if (buf[moveY][moveX] != '.' && buf[moveY][moveX] != piece.getId()) {
                return false;
            }
        }

        return true;
    }

    public boolean isWin() {
        for (Piece p : pieces) {
            if (p.isPrimary()) {
                Point tail = p.getHead();
                int size = p.getSize();

                if (p.isOrientationHorizontal()) {
                    tail.translate(size - 1, 0);
                } else {
                    tail.translate(0, size - 1);
                }

                return p.getHead().equals(exitGate) || tail.equals(exitGate);
            }
        }
        return false;
    }

    public List<Piece> getPieces() { // Hanya untuk cari id doang kan
        return this.pieces;
    }

    public int getKPos() {
        return this.kPos;
    }

    public boolean equals(Board b) { // Ini harus di optimisasi sih gila
        // System.out.println("sizeX: " + sizeX + ", sizeY: " + sizeY);
        for (int i = 0; i < sizeY; i++) {
            for (int j = 0; j < sizeX; j++) {
                // System.out.println("(i: " + i + ", j: " + j + ")");
                if (b.buf[i][j] != this.buf[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    public String toString2(char id) {
        final String RESET = "\u001B[0m";
        final String RED = "\u001B[48;5;196m"; // Primary
        final String CYAN = "\u001B[48;5;226m"; // Moveable
        final String GREEN = "\u001B[48;5;46m"; // Exit

        StringBuilder sb = new StringBuilder();
        if (kPos == 1) { // Kiri
            for (int i = 0; i < sizeY; i++) {
                if (i == exitGate.y) {
                    sb.append(GREEN).append('K').append(RESET);
                } else {
                    sb.append(' ');
                }

                for (int j = 0; j < sizeX; j++) {
                    char curChar = buf[i][j];
                    if (curChar == id && curChar != 'P') {
                        sb.append(CYAN).append(curChar).append(RESET);
                    } else if (curChar == 'P') {
                        sb.append(RED).append(curChar).append(RESET);
                    } else {
                        sb.append(curChar);
                    }
                }
                sb.append('\n');
            }
        } else if (kPos == 2) { // Atas
            for (int i = 0; i < sizeX; i++) {
                if (i == exitGate.x) {
                    sb.append(GREEN).append('K').append(RESET);
                } else {
                    sb.append(' ');
                }
            }
            sb.append('\n');

            for (int i = 0; i < sizeY; i++) {
                for (int j = 0; j < sizeX; j++) {
                    char curChar = buf[i][j];
                    if (curChar == id && curChar != 'P') {
                        sb.append(CYAN).append(curChar).append(RESET);
                    } else if (curChar == 'P') {
                        sb.append(RED).append(curChar).append(RESET);
                    } else {
                        sb.append(curChar);
                    }
                }
                sb.append('\n');
            }

        } else if (kPos == 3) { // Kanan
            for (int i = 0; i < sizeY; i++) {
                for (int j = 0; j < sizeX; j++) {
                    char curChar = buf[i][j];
                    if (curChar == id && curChar != 'P') {
                        sb.append(CYAN).append(curChar).append(RESET);
                    } else if (curChar == 'P') {
                        sb.append(RED).append(curChar).append(RESET);
                    } else {
                        sb.append(curChar);
                    }
                }

                if (i == exitGate.y) {
                    sb.append(GREEN).append('K').append(RESET);
                }
                sb.append('\n');
            }

        } else if (kPos == 4) { // Bawah
            for (int i = 0; i < sizeY; i++) {
                for (int j = 0; j < sizeX; j++) {
                    char curChar = buf[i][j];
                    if (curChar == id && curChar != 'P') {
                        sb.append(CYAN).append(curChar).append(RESET);
                    } else if (curChar == 'P') {
                        sb.append(RED).append(curChar).append(RESET);
                    } else {
                        sb.append(curChar);
                    }
                }
                sb.append('\n');
            }

            for (int i = 0; i < sizeX; i++) {
                if (i == exitGate.x) {
                    sb.append(GREEN).append('K').append(RESET);
                } else {
                    sb.append(' ');
                }
            }
            sb.append('\n');
        }

        return sb.toString();
    }

    public String toString3(char id) {
        StringBuilder sb = new StringBuilder();
        if (kPos == 1) { // Kiri
            for (int i = 0; i < sizeY; i++) {
                if (i == exitGate.y) {
                    sb.append('K');
                } else {
                    sb.append(' ');
                }

                for (int j = 0; j < sizeX; j++) {
                    char curChar = buf[i][j];
                    if (curChar == id && curChar != 'P') {
                        sb.append(curChar);
                    } else if (curChar == 'P') {
                        sb.append(curChar);
                    } else {
                        sb.append(curChar);
                    }
                }
                sb.append('\n');
            }
        } else if (kPos == 2) { // Atas
            for (int i = 0; i < sizeX; i++) {
                if (i == exitGate.x) {
                    sb.append('K');
                } else {
                    sb.append(' ');
                }
            }
            sb.append('\n');

            for (int i = 0; i < sizeY; i++) {
                for (int j = 0; j < sizeX; j++) {
                    char curChar = buf[i][j];
                    if (curChar == id && curChar != 'P') {
                        sb.append(curChar);
                    } else if (curChar == 'P') {
                        sb.append(curChar);
                    } else {
                        sb.append(curChar);
                    }
                }
                sb.append('\n');
            }

        } else if (kPos == 3) { // Kanan
            for (int i = 0; i < sizeY; i++) {
                for (int j = 0; j < sizeX; j++) {
                    char curChar = buf[i][j];
                    if (curChar == id && curChar != 'P') {
                        sb.append(curChar);
                    } else if (curChar == 'P') {
                        sb.append(curChar);
                    } else {
                        sb.append(curChar);
                    }
                }

                if (i == exitGate.y) {
                    sb.append('K');
                }
                sb.append('\n');
            }

        } else if (kPos == 4) { // Bawah
            for (int i = 0; i < sizeY; i++) {
                for (int j = 0; j < sizeX; j++) {
                    char curChar = buf[i][j];
                    if (curChar == id && curChar != 'P') {
                        sb.append(curChar);
                    } else if (curChar == 'P') {
                        sb.append(curChar);
                    } else {
                        sb.append(curChar);
                    }
                }
                sb.append('\n');
            }

            for (int i = 0; i < sizeX; i++) {
                if (i == exitGate.x) {
                    sb.append('K');
                } else {
                    sb.append(' ');
                }
            }
            sb.append('\n');
        }

        return sb.toString();
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (char[] row : buf) {
            for (char c : row) {
                sb.append(c);
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    public int calcH() {
        Piece prim = null;
        for (Piece p : pieces) {
            if (p.isPrimary()) {
                prim = p;
            }
        }

        int block = 0;
        int exitX = exitGate.x;
        int exitY = exitGate.y;
        int primX = prim.getHead().x;
        int primY = prim.getHead().y;
        char primId = prim.getId();

        if (prim.isOrientationHorizontal()) {
            int dx = exitX - primX;
            if (dx > 0) {
                for (int i = 0; i < dx; i++) {
                    if (buf[primY][primX + i + 1] != '.' && buf[primY][primX + i + 1] != primId) {
                        block++;
                    }
                }
            } else {
                dx *= -1;
                for (int i = 0; i < dx; i++) {
                    if (buf[primY][primX - i - 1] != '.' && buf[primY][primX - i - 1] != primId) {
                        block++;
                    }
                }
            }
        } else {
            int dy = exitY - primY;
            if (dy > 0) {
                for (int i = 0; i < dy; i++) {
                    if (buf[primY + i + 1][primX] != '.' && buf[primY + i + 1][primX] != primId) {
                        block++;
                    }
                }
            } else {
                dy *= -1;
                for (int i = 0; i < dy; i++) {
                    if (buf[primY - i - 1][primX] != '.' && buf[primY - i - 1][primX] != primId) {
                        block++;
                    }
                }
            }
        }

        return block;
    }

    public int calcH2() {
        Piece prim = null;
        for (Piece p : pieces) {
            if (p.isPrimary()) {
                prim = p;
            }
        }

        int exitX = exitGate.x;
        int exitY = exitGate.y;
        int primX = prim.getHead().x;
        int primY = prim.getHead().y;
        int primSize = prim.getSize();

        if (prim.isOrientationHorizontal()) {
            int dxHead = Math.abs(exitX - primX);
            int dxTail = Math.abs(exitX - (primX + primSize - 1));
            if (dxHead < dxTail) {
                return dxHead;
            } else {
                return dxTail;
            }
        } else {
            int dyHead = Math.abs(exitY - primY);
            int dyTail = Math.abs(exitY - (primY + primSize - 1));
            if (dyHead < dyTail) {
                return dyHead;
            } else {
                return dyTail;
            }
        }
    }

    public String toHashString() {
        StringBuilder sb = new StringBuilder();
        for (char[] row : buf) {
            sb.append(row);
        }
        return sb.toString();
    }

}