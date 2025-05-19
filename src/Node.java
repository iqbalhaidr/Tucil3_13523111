import java.util.*;

public class Node {
    private int costGn; // cost g(n)
    private int cost; // total cost f(n)
    private String what; // deskripsi langkah terakhir sebelum sampai ke state ini
    private Board state; // keadaan papan saat ini
    private ArrayList<Integer> path; // jejak langkah berupa ID node sebelumnya

    public Node(Board state) { // By default membuat root node
        this.costGn = 0;
        this.cost = 0;
        this.what = "Papan awal";
        this.state = state.clone();
        this.path = new ArrayList<>();
    }

    public Node clone() { // Deep copy
        Node copy = new Node(this.state.clone());
        copy.costGn = this.costGn;
        copy.cost = this.cost;
        copy.what = this.what;
        copy.path = new ArrayList<>(this.path);
        return copy;
    }

    public int getCostGn() {
        return costGn;
    }

    public void setCostGn(int c) {
        this.costGn = c;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int c) {
        this.cost = c;
    }

    public String getWhat() {
        return what;
    }

    public void setWhat(String w) {
        this.what = w;
    }

    public Board getState() {
        return state;
    }

    public void setState(Board b) {
        this.state = b;
    }

    public ArrayList<Integer> getPath() {
        return path;
    }

    public boolean isWinning() {
        return state.isWin();
    }
}
