import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Solver {
    private PriorityQueue<Node> pq;
    private ArrayList<Node> visitedNodes;

    public Solver(Node rootNode) { // Selalu minta root nodenya sebagai start
        this.pq = new PriorityQueue<>(Comparator.comparingInt(Node::getCost));
        pq.add(rootNode);
        this.visitedNodes = new ArrayList<>();
    }

    /*
     * 1 = UCS
     * 2 = GBFS
     * 3 = A*
     * jika return true maka ketemu, jika false maka tidak ketemu
     */
    public boolean start(int algo) {
        // int ctrV = 0;
        // int ctrN = 0;
        // System.out.println(pq.size());
        while (!pq.isEmpty()) {
            // System.out.print("Masuk woy");
            Node currentNode = pq.poll();

            int currentId = visitedNodes.size();
            visitedNodes.add(currentNode);
            // ctrV++;
            // System.out.println("Visited node: " + ctrV);

            if (currentNode.isWinning()) {
                return true;
            }

            for (Piece p : currentNode.getState().getPieces()) {
                for (int move : currentNode.getState().calcPossibleMove(p.getId())) {
                    char pieceId = p.getId();
                    Board candidateBoard = currentNode.getState().clone();
                    boolean isNotVisited = true;

                    candidateBoard.movePiece(pieceId, move);
                    for (Node n : visitedNodes) {
                        if (n.getState().equals(candidateBoard)) {
                            isNotVisited = false;
                            break;
                        }
                    }
                    // ctrN++;
                    // System.out.println("Checked node: " + ctrN);
                    // System.out.println("Queue size: " + pq.size());

                    if (isNotVisited) {
                        // System.out.println(candidateBoard.toString());
                        Node candidateNode = currentNode.clone();
                        candidateNode.getPath().add(currentId);
                        candidateNode.setState(candidateBoard);

                        String arah;
                        if (p.isOrientationHorizontal()) {
                            arah = (move > 0) ? "kanan" : "kiri";
                        } else {
                            arah = (move > 0) ? "bawah" : "atas";
                        }
                        String what = pieceId + "-" + arah;
                        candidateNode.setWhat(what);

                        switch (algo) {
                            case 1: // UCS
                                int currentCostGn = candidateNode.getCostGn() + 1;
                                candidateNode.setCostGn(currentCostGn);
                                candidateNode.setCost(currentCostGn);
                                break;
                            case 2: // GBFS
                                candidateNode.setCostGn(-1);
                                candidateNode.setCost(candidateNode.getState().calcH());
                                break;
                            case 3: // A*
                                int currentCostGn2 = candidateNode.getCostGn() + 1;
                                int currentH = candidateNode.getState().calcH();
                                candidateNode.setCostGn(currentCostGn2);
                                candidateNode.setCost(currentCostGn2 + currentH);
                                break;

                            default:
                                break;
                        }

                        pq.add(candidateNode);
                    }
                }
            }
        }
        return false;
    }

    public void display() {
        int finishNodeIdx = visitedNodes.size() - 1;
        Node finishNode = visitedNodes.get(finishNodeIdx);
        
        int ctr = 1;
        for (int i : finishNode.getPath()) {
            Node iter = visitedNodes.get(i);
            char id = iter.getWhat().charAt(0);
            System.out.println("Gerakan " + ctr + ": " + iter.getWhat());
            System.out.println(iter.getState().toString2(id));
            ctr++;
        }

        char id = finishNode.getWhat().charAt(0);
        System.out.println("Gerakan " + ctr + ": " + finishNode.getWhat());
        System.out.println(finishNode.getState().toString2(id));
    }
}
