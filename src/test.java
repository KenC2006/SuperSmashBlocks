import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;


public class test {
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<ArrayList<Node>> graph = new ArrayList<>();
        int nodeNumber = Integer.parseInt(in.readLine());
        int edgeNumber = Integer.parseInt(in.readLine());
        int[] distance = new int[nodeNumber];
        boolean[] travelled = new boolean[nodeNumber];
        Arrays.fill(travelled, false);

        int currentNode = 0;
        int currentWeight = Integer.MAX_VALUE;
        int nextNode = 0;
        int allTravelled = 0;


        distance[0]=0;
        graph.add(new ArrayList<>());
        for (int i = 1; i < nodeNumber; i++) {
            graph.add(new ArrayList<>());
            distance[i] = Integer.MAX_VALUE;
        }
        for (int i = 0; i < edgeNumber; i++) {
            String[] temp = in.readLine().split(" ");
            graph.get(Integer.parseInt(temp[0])).add(Node.addNode(Integer.parseInt(temp[1]), Integer.parseInt(temp[2])));
            graph.get(Integer.parseInt(temp[1])).add(Node.addNode(Integer.parseInt(temp[0]), Integer.parseInt(temp[2])));
        }

        while (true) {
            for (int k = 0; k < graph.get(currentNode).size(); k++) {
                if (!travelled[graph.get(currentNode).get(k).getNodeNumber()]) {
                    if (distance[graph.get(currentNode).get(k).getNodeNumber()] > graph.get(currentNode).get(k).getWeight() + distance[currentNode]) {
                        distance[graph.get(currentNode).get(k).getNodeNumber()] = graph.get(currentNode).get(k).getWeight() + distance[currentNode];
                    }

                }
            }
            travelled[currentNode] = true;
            for (int i = 0; i < nodeNumber; i++) {
                if (!travelled[i] && currentWeight > distance[i]) {
                    currentWeight = distance[i];
                    nextNode = i;
                    allTravelled++;
                }
            }
            if (allTravelled == 0) {
                break;
            } else {
                allTravelled = 0;
            }
            currentNode = nextNode;
            currentWeight=Integer.MAX_VALUE;
        }
        int queries = Integer.parseInt(in.readLine());
        int [] query=new int[queries];
        for (int i = 0; i < queries; i++) {
            query[i]=Integer.parseInt(in.readLine());
        }
        for (int i = 0; i < queries; i++) {
            if (query[i]==Integer.MAX_VALUE) {
                System.out.println(-1);
            } else {
                System.out.println(distance[query[i]]);
            }
        }
    }




    static class Node{
        int weight;
        int nodeNumber;
        Node(int nodeNumber){
            this.nodeNumber=nodeNumber;
        }
        public static Node addNode(int nodeNumber,int weight) {
            Node node = new Node(nodeNumber);
            node.weight = weight;
            return node;
        }
        public int getWeight() {
            return weight;
        }
        public int getNodeNumber() {
            return nodeNumber;
        }
    }
}



