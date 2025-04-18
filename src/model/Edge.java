package model;

public class Edge {
    private final Node fromNode;
    private final Node toNode;
    private final double weight;

    public Edge(Node toNode, Node fromNode, double weight) {
        this.toNode = toNode;
        this.fromNode = fromNode;
        this.weight = weight;
    }

    public Node getFromNode() {
        return fromNode;
    }

    public Node getToNode() {
        return toNode;
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "fromNode=" + fromNode +
                ", toNode=" + toNode +
                ", weight=" + weight +
                '}';
    }
}