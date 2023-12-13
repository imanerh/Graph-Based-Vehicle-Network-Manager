package classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;
import java.lang.Math;

public class Graph {

    // Node of the adjacency list
    static class Node {
        Vertex vertex;
        double weight;

        public Node(Vertex vertex, double weight) {
            this.vertex = vertex;
            this.weight = weight;
        }

        public double getWeight() {
            return weight;
        }

        public Vertex getVertex() {
            return vertex;
        }
    };

    // Define the adjacency list
    public ArrayList<ArrayList<Node>> adjacency_list = new ArrayList<>();
    public int number_vertices;
    public double transmission_range;
    public ArrayList<Vertex> vertices;
    public ArrayList<Edge> edges = new ArrayList<>();

    public Graph(int number_vertices, double transmission_range, ArrayList<Vertex> vertices) {
        this.number_vertices = number_vertices;
        this.transmission_range = transmission_range;
        this.vertices = vertices;

        // Allocate memory in the adjacency list
        for (int i = 0; i < number_vertices; i++) {
            adjacency_list.add(i, new ArrayList<>());
        }

        // Determine the edges and add them to the adjacency list in an increasing order
        // of the “Distance” value
        for (int i = 0; i < number_vertices - 1; i++) {
            for (int j = i + 1; j < number_vertices; j++) {
                // If the Euclidian distance between the two vertices is less than or equal to
                // the transmission range, there is an edge between the two vertices
                Vertex vertex_i = vertices.get(i);
                Vertex vertex_j = vertices.get(j);
                double d = Math.sqrt(
                        Math.pow(vertex_i.getX() - vertex_j.getX(), 2) +
                                Math.pow(vertex_i.getY() - vertex_j.getY(), 2));
                if (d <= transmission_range) {
                    Edge e = new Edge(vertices.get(i), vertices.get(j), d);
                    edges.add(e);
                    insertAdjacencyList(e);
                }

            }
        }

    }

    // Display all the edges of the graph in the format (1, 2, 5) [Third parameter
    // is the weight of the edge (i.e., distance)]
    public void displayEdges() {
        if (edges.size() == 0) {
            System.out.println("\tThere are no edges in this graph.");
        } else {
            System.out.println("\tThe graph has " + edges.size() + " edges:");
            for (Edge e : edges) {
                System.out.println("\t=> (" + e.vertex1.id + ", " + e.vertex2.id + ", " + e.weight + ")");
            }
        }
    }

    // Display the adjacent vehicles for a given vehicle ID
    public void displayAdjacentVehicles(int id) {
        ArrayList<Node> al = adjacency_list.get(id);
        if (al.size() == 0) {
            System.out.println("\tThe vehicle of Id " + id + " has no adjacent vehicles.");
        } else {
            System.out.println("\tThe vehicle of Id " + id + " has " + al.size()
                    + " adjacent vehicle(s) [represented in an ascending order as (adjacent_vehicle_id, distance_between_vehicles)]:");
            for (Node n : al) {
                System.out.println("\t=> (" + n.vertex.id + ", " + n.weight + ")");
            }
        }
    }

    // Move a vehicle
    public void moveVehicle(int id, double x, double y) {
        Vertex v = vertices.get(id);
        v.setX(x);
        v.setY(y);
        // Remove existing edges containing this vertex from the edges list and update
        // the adjacency list
        Iterator<Edge> iterator = edges.iterator();
        while (iterator.hasNext()) {
            Edge e = iterator.next();
            if (e.vertex1.id == id || e.vertex2.id == id) {
                iterator.remove();
                ArrayList<Node> al1 = adjacency_list.get(e.vertex1.id);
                for (int i = 0; i < al1.size(); i++) {
                    if (al1.get(i).vertex.id == e.vertex2.id) {
                        al1.remove(i);
                        break;
                    }
                }
                ArrayList<Node> al2 = adjacency_list.get(e.vertex2.id);
                for (int i = 0; i < al2.size(); i++) {
                    if (al2.get(i).vertex.id == e.vertex1.id) {
                        al2.remove(i);
                        break;
                    }
                }
            }
        }
        // Determine new edges if any
        for (int j = 0; j < number_vertices; j++) {
            if (j == id) {
                continue;
            } else {
                // If the Euclidian distance between the two vertices is less than or equal to
                // the transmission range, there is an edge between the two vertices
                double d = Math.sqrt(
                        Math.pow(x - vertices.get(j).getX(), 2) +
                                Math.pow(y - vertices.get(j).getY(), 2));
                if (d <= transmission_range) {
                    Edge e = new Edge(v, vertices.get(j), d);
                    edges.add(e);
                    insertAdjacencyList(e);
                }
            }
        }
        System.out.println("\tVehicle movement updated successfully.");
    }

    // Insert an edge in the adjacency list
    public void insertAdjacencyList(Edge e) {
        ArrayList<Node> al1 = adjacency_list.get(e.vertex1.id);
        insertHelper(al1, e.vertex2, e.weight);
        ArrayList<Node> al2 = adjacency_list.get(e.vertex2.id);
        insertHelper(al2, e.vertex1, e.weight);
    }

    public void insertHelper(ArrayList<Node> al, Vertex v, double w) {
        if (al.size() == 0) {
            al.add(new Node(v, w));
        } else {
            // Insert the new node in the right position
            boolean flag = true; // Indicates if the edge should be inserted at the end of the list
            for (int i = 0; i < al.size(); i++) {
                if (w < al.get(i).getWeight()) {
                    flag = false;
                    al.add(i, new Node(v, w));
                    break;
                }
            }
            if (flag) {
                al.add(al.size(), new Node(v, w));
            }

        }
    }

    // DFS traversal starting from vehicle of id start_id
    public void DFS(int start_id) {
        boolean visited[] = new boolean[number_vertices];
        DFSHelper(start_id, visited);
    }

    public void DFSHelper(int start_id, boolean visited[]) {
        visited[start_id] = true;
        System.out.print(start_id + " ");

        Iterator<Node> ite = adjacency_list.get(start_id).listIterator();
        while (ite.hasNext()) {
            int next_id = ite.next().vertex.id;
            if (!visited[next_id])
                DFSHelper(next_id, visited);
        }
    }

    // BFS traversal starting from vehicle of id start_id
    public void BFS(int start_id) {
        boolean visited[] = new boolean[number_vertices];
        // Create a queue
        LinkedList<Vertex> queue = new LinkedList<Vertex>();
        visited[start_id] = true;
        queue.add(vertices.get(start_id));

        while (queue.size() != 0) {
            Vertex v = queue.poll();
            System.out.print(v.id + " ");
            Iterator<Node> i = adjacency_list.get(v.id).listIterator();
            while (i.hasNext()) {
                Node n = i.next();
                int next_id = n.vertex.id;
                if (!visited[next_id]) {
                    visited[next_id] = true;
                    queue.add(n.vertex);
                }
            }
        }
    }

    // Find the MST using Prim's algorithm. Display MST Edges in increasing order of
    // distance.
    public void MST() {

        ArrayList<Edge> mstEdges = new ArrayList<>();
        ArrayList<Vertex> mstVertices = new ArrayList<>();
        mstVertices.add(vertices.get(0));

        while (mstVertices.size() != number_vertices) {
            double min_weight = Double.MAX_VALUE;
            Vertex v1 = null;
            Vertex v2 = null;
            for (Vertex v : mstVertices) {
                List<Node> l = adjacency_list.get(v.id);
                for (Node n : l) {
                    if (n.weight < min_weight && !mstVertices.contains(n.vertex)) {
                        min_weight = n.weight;
                        v1 = v;
                        v2 = n.vertex;
                    }
                }
            }
            // Insert the new edge at the right position in mstEdges
            if (mstEdges.size() == 0) {
                mstEdges.add(new Edge(v1, v2, min_weight));
                mstVertices.add(v2);
                continue;
            }
            boolean flag = true; // Indicates if the edge should be inserted at the end of the list
            for (int i = 0; i < mstEdges.size(); i++) {
                if (min_weight < mstEdges.get(i).weight) {
                    flag = false;
                    mstEdges.add(i, new Edge(v1, v2, min_weight));
                    break;
                }
            }
            if (flag) {
                mstEdges.add(mstEdges.size(), new Edge(v1, v2, min_weight));
            }
            // Add the vertex to mstVertices
            mstVertices.add(v2);
        }

        // Display the MST edges
        System.out.println("\tMST edges in increasing order of distance:");
        for (Edge e : mstEdges) {
            System.out.println("\t=> (" + e.vertex1.id + ", " + e.vertex2.id + ", " + e.weight + ")");
        }
    }

    // Find the shortest path from start_id vehicle to dest_id using Djikstra's
    // algorithm
    public void shortest_path(int start_id, int dest_id) {

        double distance[] = new double[number_vertices];
        for (int i = 0; i < number_vertices; i++) {
            distance[i] = Double.MAX_VALUE;
        }
        distance[start_id] = 0;

        Edge previous[] = new Edge[number_vertices]; // To identify the path from start_id to dest_id
        Arrays.fill(previous, null); // Initialize the array to 0s

        boolean[] visited = new boolean[number_vertices];
        Arrays.fill(visited, false); // Initialize the array to 0s

        // Create a priority queue
        PriorityQueue<Node> pq = new PriorityQueue<>((n1, n2) -> Double.compare(n1.weight, n2.weight));
        pq.add(new Node(vertices.get(start_id), 0));

        while (!pq.isEmpty()) {
            Vertex v = pq.remove().vertex;
            visited[v.id] = true; // Mark the vertex as visited

            // If the vertex dequeued is the destination vertex, we found the shortest path
            // from the start vertex to the destination vertex. Therefore, no need to
            // continue.
            if (v.id == dest_id) {
                break;
            }

            double newDistance = 0;

            // For all the adjacent vertices of v that are not yet visited
            for (int i = 0; i < adjacency_list.get(v.id).size(); i++) {
                Node n = adjacency_list.get(v.id).get(i);

                if (!visited[n.vertex.id]) {
                    newDistance = distance[v.id] + n.weight;
                    // Update the distance to this vertex if we find a shorter path
                    if (newDistance < distance[n.vertex.id]) {
                        distance[n.vertex.id] = newDistance;
                        previous[n.vertex.id] = new Edge(v, n.vertex, n.weight);
                    }

                    // Add the current vertex to the PriorityQueue
                    pq.add(new Node(n.vertex, distance[n.vertex.id]));
                }
            }

        }

        // Display the shortest path edge by edge and the minimum distance found
        System.out.println("      The shortest path from " + start_id + " to " + dest_id + " has a distance of "
                + distance[dest_id]);
        System.out.println("      The path is as follows:");

        int next = dest_id;
        Stack<Edge> path_stack = new Stack<>(); // Create a stack to inverse the order of the edges starting from start vertex to destination vertex
        while (true) {
            if (next == start_id) {
                break;
            }
            path_stack.push(previous[next]);
            next = previous[next].vertex1.id;
        }
        while (!path_stack.isEmpty()) {
            Edge e = path_stack.pop();
            System.out.println("      => (" + e.vertex1.id + ", " + e.vertex2.id + ", " + e.weight + ")");
        }
    }

}