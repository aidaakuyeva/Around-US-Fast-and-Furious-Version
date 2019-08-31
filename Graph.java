import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Graph {
    public Graph() {}
    
    //This function reads from the downloaded table and scrapes the proper data returning an edge list
    public static HashMap<Integer, ArrayList<Pair<Integer,Double>>> readingWaitTime() throws IOException {
        Graph g = new Graph();
        File file = new File("Data2.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        HashMap<Integer, ArrayList<Pair<Integer,Double>>> adjList = new HashMap<Integer, ArrayList<Pair<Integer,Double>>>();
        int source;
        int tgt;
        int weight;
        String line = br.readLine();
        while (line != null) {
            String[] temp = line.split(",");
            source = Integer.parseInt(temp[0]);
            tgt = Integer.parseInt(temp[1]);
            weight = Integer.parseInt(temp[6]);
            Pair<Integer, Double> pair = g.new Pair<Integer,Double>(tgt, weight);
            if (adjList.get(source) == null) {
                ArrayList<Pair<Integer, Double>> neighbors = new ArrayList<Pair<Integer, Double>>();
                neighbors.add(pair);
                adjList.put(source, neighbors);
            } else {
                adjList.get(source).add(pair);
            }
             line = br.readLine();
        }
        br.close();
        return adjList;
    }
    
    //This is just the BellmanFord algorithm (basically Dijkstra)
    public static ArrayList<Integer> BellmanFord(HashMap<Integer, ArrayList<Pair<Integer, Double>>> adjList, int src, int tgt) {
        double[] distance = new double[adjList.size()];
        int[] parent = new int[adjList.size()];
        for (int i = 0; i < adjList.size(); i++) {
            distance[i] = Integer.MAX_VALUE;
            parent[i] = -1;
        }
        distance[src] = 0;
        for (int i = 0; i < adjList.size(); i++) {
            for (Integer key : adjList.keySet()) {
                for(Pair<Integer, Double> pair : adjList.get(key)) {
                    if (distance[key] + pair.getWeight() < distance[pair.getVertex()]) {
                        distance[pair.getVertex()] = distance[key] + pair.getWeight();
                        parent[pair.getVertex()] = key;
                    }
                }
            }
        }
        ArrayList<Integer> path = new ArrayList<Integer>();
        int temp = tgt;
        while (temp != src) {
            path.add(temp);
            temp = parent[temp];
        }
        Collections.reverse(path);
        return path;
    }
    
    //This function takes in an adjacency list and a start node and writes all of the shortest paths from the start node
    // to every other node in the graph into a CSV file.
    // This CSV file can be used with our data visualizer to look at the network of airlines from the root node
    public static void outputShortestPaths(HashMap<Integer, ArrayList<Pair<Integer, Double>>> adjList, int root) throws IOException {
        File file = new File("Output.csv");
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        for(int i = 0; i < adjList.size(); i++) {
            ArrayList<Integer> path = BellmanFord(adjList, root, i);
            String temp = path.toString();
            temp = root + "," + temp.substring(1, temp.lastIndexOf(']'));
            temp = temp.replace(" ", "");
            bw.append(temp);
            bw.newLine();
        }
        bw.close();
    }
    
//--------------------------------------------------------------------------------------- //
    // Please    do    not     modify     any     code     above      this     line //  
    public static void main(String[] args) {
      
        try{
            //This creates the adjacency list for the bellford algo
            HashMap<Integer, ArrayList<Pair<Integer, Double>>> adj = readingWaitTime();
            //CHANGE THE 0 to any number between 0 and 353 to change the starting point of our graph
            outputShortestPaths(adj, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //Pair inner class that we use to make our adjacency lists
    // it stores the value of the neighboring node and the weight of the edge
    class Pair<Integer,Double> {

        private int vertex;
        private double weight;

        public Pair(int v, double w) {
            this.weight = w;
            this.vertex = v;
        }

        /**
         * @return  the value stored in the entry
         */
        public double getWeight() {
            return this.weight;
        }

        /**
         * @return  the key stored in the entry
         */
        public int getVertex() {
            return this.vertex;
        }
        

    }
}
