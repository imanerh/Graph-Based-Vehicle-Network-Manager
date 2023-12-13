package classes;

public class Edge {

    public Vertex vertex1;
    public Vertex vertex2;
    public double weight;

    public Edge(Vertex vertex1, Vertex vertex2, double weight) {
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.weight = weight;
    }

    // Getters
    public Vertex getVertex1() {
        return vertex1;
    }
    public Vertex getVertex2() {
        return vertex2;
    }
    public double getweight() {
        return weight;
    }
    // Setters 
    public void setVertex1(Vertex v) {
        this.vertex1 = v;
    }
    public void setVertex2(Vertex v) {
        this.vertex2 = v;
    }
    public void setWeight(double w) {
        this.weight = w;
    }
}