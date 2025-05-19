import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

public class Solver {
    private PriorityQueue<Node> pq;
    private ArrayList<Node> visitedNodes;
    Set<String> visitedStates;

    public Solver(Node rootNode) { // Selalu minta root nodenya sebagai start
        this.pq = new PriorityQueue<>(Comparator.comparingInt(Node::getCost));
        pq.add(rootNode);
        this.visitedNodes = new ArrayList<>();
        this.visitedStates = new HashSet<>();
    }

    /*
     * 1 = UCS
     * 2 = GBFS
     * 3 = A*
     * jika return true maka ketemu, jika false maka tidak ketemu
     */
    public boolean start(int algo, int heu, int[] visitedNodeCtr) {
        // int ctrV = 0;
        // int ctrN = 0;
        // System.out.println(pq.size());
        while (!pq.isEmpty()) {
            // System.out.print("Masuk woy");
            Node currentNode = pq.poll();
            // System.out.println("Node in pq: " + pq.size());

            int currentId = visitedNodes.size();
            visitedNodes.add(currentNode);
            // ctrV++;
            // System.out.println("Visited node: " + ctrV);

            if (currentNode.isWinning()) {
                visitedNodeCtr[0] = visitedNodes.size();
                return true;
            }

            for (Piece p : currentNode.getState().getPieces()) {
                for (int move : currentNode.getState().calcPossibleMove(p.getId())) {
                    char pieceId = p.getId();
                    Board candidateBoard = currentNode.getState().clone();
                    boolean isNotVisited = true;

                    candidateBoard.movePiece(pieceId, move);
                    String candidateHash = candidateBoard.toHashString();
                    if (!visitedStates.contains(candidateHash)) {
                        visitedStates.add(candidateHash);
                    } else {
                        isNotVisited = false;
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
                                int heuValue;
                                if (heu == 1) {
                                    heuValue = candidateNode.getState().calcH();
                                } else {
                                    heuValue = candidateNode.getState().calcH2();
                                }

                                candidateNode.setCostGn(-1);
                                candidateNode.setCost(heuValue);
                                break;
                            case 3: // A*
                                int heuValue2;
                                if (heu == 1) {
                                    heuValue2 = candidateNode.getState().calcH();
                                } else {
                                    heuValue2 = candidateNode.getState().calcH2();
                                }

                                int currentCostGn2 = candidateNode.getCostGn() + 1;
                                candidateNode.setCostGn(currentCostGn2);
                                candidateNode.setCost(currentCostGn2 + heuValue2);
                                break;

                            default:
                                break;
                        }

                        pq.add(candidateNode);
                    }
                }
            }
        }
        visitedNodeCtr[0] = visitedNodes.size();
        return false;
    }

    public void display() {
        int finishNodeIdx = visitedNodes.size() - 1;
        Node finishNode = visitedNodes.get(finishNodeIdx);
        
        int ctr = 0;
        for (int i : finishNode.getPath()) {
            Node iter = visitedNodes.get(i);
            char id = iter.getWhat().charAt(0);
            if (ctr == 0) {
                System.out.println("Papan Awal");
            } else {
                System.out.println("Gerakan " + ctr + ": " + iter.getWhat());
            }
            System.out.println(iter.getState().toString2(id));
            ctr++;
        }

        char id = finishNode.getWhat().charAt(0);
        System.out.println("Gerakan " + ctr + ": " + finishNode.getWhat());
        System.out.println(finishNode.getState().toString2(id));
    }

    public void display2(PrintWriter pw) {
        int finishNodeIdx = visitedNodes.size() - 1;
        Node finishNode = visitedNodes.get(finishNodeIdx);
        
        int ctr = 0;
        for (int i : finishNode.getPath()) {
            Node iter = visitedNodes.get(i);
            char id = iter.getWhat().charAt(0);
            if (ctr == 0) {
                pw.println("Papan Awal");
            } else {
                pw.println("Gerakan " + ctr + ": " + iter.getWhat());
            }
            pw.println(iter.getState().toString3(id));
            ctr++;
        }

        char id = finishNode.getWhat().charAt(0);
        pw.println("Gerakan " + ctr + ": " + finishNode.getWhat());
        pw.println(finishNode.getState().toString3(id));
    }
}
