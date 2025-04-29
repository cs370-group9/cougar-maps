package com.cougarmaps.model.graph;

public class Edge {
    private final Node fromNode;
    private final Node toNode;
    private final double weight;

    public Edge(Node fromNode, Node toNode, double weight) {
        this.fromNode = fromNode;
        this.toNode = toNode;
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
