package graphFiles;

import java.applet.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Comparator;
import exceptions.LoopException;

public final class StreetSearch {

    private static final double MAX_WEIGHT = 1e8;
    private static final int CHAR_INDEX = 49;

    private static class LoadedVertex {
        Vertex<String> vert;
        double weight;
        boolean visited;

        LoadedVertex(Vertex<String> v) {
            vert = v;
            weight = MAX_WEIGHT;
            visited = false;
        }

        void setVisited(boolean b) {
            this.visited = b;
        }

        void setWeight(double w) {
            this.weight = w;
        }

        boolean getVisited() {
            return this.visited;
        }

        double getWeight() {
            return this.weight;
        }
    }

    private static class StreetComparator implements Comparator<LoadedVertex> {
        @Override
        /**
        * compares the provided loaded verticies based on the weight
        * @param lv1 First loaded vertex to compare
        * @param lv2 Second vertex to compare
        * @return Int value based on comparison
        *   if lv1 < lv2 return a negative value
        *   if lv1 > lv2 returns a positive value
        *   if lv1 == lv2 returns 0
        */
        public int compare(LoadedVertex lv1, LoadedVertex lv2) {
            if (lv1.getWeight() < lv2.getWeight()) {
                return -1;
            }
            else if (lv1.getWeight() > lv2.getWeight()) {
                return 1;
            }
            else {
                return 0;
            }
        }
    }



    private static Map<String, Vertex<String>> vertices = new HashMap<>();
    private static SparseGraph<String, String> graph = new SparseGraph<>();
    private static Map<Vertex<String>, LoadedVertex> pathDiscover = new HashMap<>();
    private static PriorityQueue<LoadedVertex> pq = new PriorityQueue<LoadedVertex>(5, new StreetComparator());

    // arbitrary constructor
    public StreetSearch() {}


    //gets the path
    private static List<Edge<String>> getPath(Vertex<String> end, Vertex<String> start) {
        if (graph.pathInfo(end) != null) {
            List<Edge<String>> path = new ArrayList<>();
            Vertex<String> curr = end;
            Edge<String> road;
            while (curr != start) {
                road = (Edge<String>) graph.pathInfo(curr);
                path.add(road);
                curr = graph.from(road);
            }
            return path;
        }
        return null;
    }

    //print the path
    private static void printPath(List<Edge<String>> path, double distance) {
        System.out.println("Your safest path is: \n");
        for (int i = path.size() - 1; i >= 0; i--) {
            //need to figure out how to give coloquial directions using the latitude and longitude
            System.out.println(path.get(i).get());
        }

    }

    //finds the shortest path
    private static void findShortestPath(String startName, String endName) {
        //get the start and end vertices from the map using names
        Vertex<String> start = vertices.get(startName);
        Vertex<String> end = vertices.get(endName);
        double totalDist = -1;


        LoadedVertex lCurr = new LoadedVertex(start);
        lCurr.setWeight(0);
        lCurr.setVisited(true);
        pathDiscover.put(start, lCurr);
        Vertex<String> vCurr = start;

        while(!vCurr.equals(end) && vCurr != null) {
            Iterable<Edge<String>> neighbors = graph.outgoing(vCurr);
            lCurr = pathDiscover.get(vCurr);
            double currDistance = lCurr.getWeight();

            //iterate through each neighbhor
            for (Edge<String> ed: neighbors) {
                Vertex<String> to = graph.to(ed);
                LoadedVertex tempLCurr = pathDiscover.get(to);

                //goes to next edge if the to node is already getVisited
                if (pathDiscover.containsKey(to) && tempLCurr.getVisited()) {
                    continue;
                }
                double newWeight = currDistance + (double) graph.pathInfo(ed);

                //if the neighbor has not been assigned a weight
                if (tempLCurr == null) {
                    pathDiscover.put(to, new LoadedVertex(to));
                    tempLCurr = pathDiscover.get(to);
                    pq.add(tempLCurr);
                }

                //checks to see if the neighbor weight needs to be updated
                lCurr = tempLCurr;
                if (newWeight < lCurr.getWeight()) {
                    lCurr.setWeight(newWeight);
                    graph.pathInfo(to, ed);
                }
            }
            //takes the LoadedVertex with the lowest weight and checks to see if all options have been exhausted
            lCurr = pq.poll();
            if (lCurr == null) {
                break;
            }
            lCurr.setVisited(true);
            vCurr = lCurr.vert;
        }
        if (vCurr != null) {
            totalDist = pathDiscover.get(end).getWeight();
        }
        //fetches the lowest weighted path detected
        List<Edge<String>> path = getPath(end, start);
        printPath(path, totalDist);
    }

    // adds vertex to HashMap vertices
    private static Vertex<String> addVertex(String name) {
        if (!vertices.containsKey(name)) {
            String[] position = name.split(",");
            Vertex<String> ver = graph.insert(name, position[0], position[1]);
            vertices.put(name, ver);
        }
        return vertices.get(name);
    }

    // loads data from text file into the graphs
    private static void loadData(String file) throws FileNotFoundException {
        Scanner in = new Scanner(new FileInputStream(new File(file)));

        while (in.hasNext()) {
            String[] split = in.nextLine().split(" ");
            String from = split[0];
            String to = split[1];
            double weight = Double.parseDouble(split[2]);
            String edgeName = split[3];

            Vertex<String> f = addVertex(from);
            Vertex<String> t = addVertex(to);

            // add roads
            try {
                Edge<String> fromTo = graph.insert(f, t, edgeName);
                Edge<String> toFrom = graph.insert(t, f, edgeName);

                graph.pathInfo(fromTo, weight);
                graph.pathInfo(toFrom, weight);
            } catch (LoopException e) {
                // do nothing if can't insert edge
            }
        }
    }

    public static void crimeMagUpdate(Edge<String> e) {
        double[] moreWeight = graph.getEdgeCrimeWeights(e);
        double currWeight = (double) graph.pathInfo(e);
        System.out.print(e.get()+ " old weight: " + currWeight + " ");
        for (double nw : moreWeight) {
            currWeight += nw;
        }
        System.out.println(e.get()+ " new weight: " + currWeight);
        graph.pathInfo(e, currWeight);
    }

    public static void updateCrime(List<Crime> crimesList) {
        for (Crime crime : crimesList) {
            Edge<String> closestEdge = graph.getEdges().get(0);
            double closestDistance = Double.MAX_VALUE;
            for (Edge<String> edge : graph.getEdges()) {
                double lat = (Double.parseDouble(graph.getLat(graph.to(edge))) +
                            Double.parseDouble(graph.getLat(graph.from(edge))))/2;
                double lon = (Double.parseDouble(graph.getLon(graph.to(edge))) +
                            Double.parseDouble(graph.getLon(graph.from(edge))))/2;
                double distance = Math.sqrt(Math.pow(lat - crime.getLat(), 2) +
                                            Math.pow(lon - crime.getLon(), 2));
                if (distance < closestDistance) {
                    closestDistance = distance;
                    closestEdge = edge;
                }
            }
            //gets the incident code
            int codeMag = (crime.getCrimeCode()).charAt(0) - CHAR_INDEX;
            graph.incrementCrime(closestEdge, codeMag);
        }
        for (Edge<String> edge : graph.getEdges()) {
            if (graph.getCrimeIncidents(edge) != 0) {

                //changes the frequency according to crimes
                crimeMagUpdate(edge);
            }
        }
    }
    /**
     * Main method
     */
    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Map Name, Start Coordinates, and End Coordinates must be supplied");
            return;
        }
        String fileName = args[0];
        String startName = args[1];
        String endName = args[2];

        // read in street data from data file
        try {
            loadData(fileName);
        } catch (FileNotFoundException e) {
            System.err.println("File not found");
        }
        CrimeAPI capi = new CrimeAPI();
        CrimeCreator creator = new CrimeCreator(capi.getCrimeData());
        List<Crime> crimesList = creator.getCrimesList();

        updateCrime(crimesList);

        findShortestPath(startName, endName);
    }
}
