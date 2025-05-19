import java.awt.Point;

public class Piece {
    private char id;
    private boolean isOrientationHorizontal;
    private boolean isPrimary;
    private int size;
    private Point head; // representasi koordinat kiri atas (x, y) = (kolom, baris)

    public Piece(char id, boolean isOrientationHorizontal, int size, Point head) {
        this.id = id;
        this.isOrientationHorizontal = isOrientationHorizontal;
        this.size = size;
        this.head = new Point(head); // deep copy untuk keamanan

        if (id == 'P') {
            this.isPrimary = true;
        } else {
            this.isPrimary = false;
        }
    }

    // Deep copy
    public Piece clone() {
        return new Piece(this.id, this.isOrientationHorizontal, this.size, this.head);
    }

    public char getId() {
        return id;
    }

    public boolean isOrientationHorizontal() {
        return isOrientationHorizontal;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public int getSize() {
        return size;
    }

    public Point getHead() {
        return new Point(head); // kembalikan salinan agar aman dari mutasi
    }

    /**
     * Menggerakkan Piece sejauh 'steps' langkah dalam arah orientasinya.
     * steps > 0 artinya ke kanan/bawah, steps < 0 ke kiri/atas
     */
    public void move(int steps) {
        if (isOrientationHorizontal) {
            head.translate(steps, 0); // geser secara horizontal
        } else {
            head.translate(0, steps); // geser secara vertikal
        }
    }
}
