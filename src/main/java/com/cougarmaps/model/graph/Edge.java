package com.cougarmaps.model.graph;

public class Edge {
    private final Node from;
    private final Node to;
    private final double weight;

    /**
     * Constructor for a unidirectional edge.
     *
     * @param from The source node.
     * @param to The destination node.
     * @param weight The cost/distance between the two nodes.
     */
    public Edge(Node from, Node to, double weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public Node getFrom() {
        return from;
    }

    public Node getTo() {
        return to;
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return String.format("Edge[%s → %s, %.2f]", from.getId(), to.getId(), weight);
    }
}
