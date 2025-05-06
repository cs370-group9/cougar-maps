package com.cougarmaps.model.graph;

import com.cougarmaps.model.graph.enums.NodeStatus;
import com.cougarmaps.model.graph.enums.NodeType;

public class Node {
    private final String id;
    private final String campus;
    private final String building;
    private final String floor;
    private final String room;
    private final NodeType type;
    private NodeStatus status;

    /**
     * Constructor to initialize a Node object with the given parameters.
     *
     * @param id The unique identifier for the node.
     * @param campus The campus where the node is located.
     * @param building The building where the node is located.
     * @param floor The floor where the node is located.
     * @param room The room number or name where the node is located.
     * @param type The type of the node (e.g., classroom, restroom, etc.).
     * @param status The status of the node (e.g., active, inactive, retired).
     *
     */
    public Node(String id, String campus, String building, String floor, String room, NodeType type, NodeStatus status) {
        this.id = id;
        this.campus = campus;
        this.building = building;
        this.floor = floor;
        this.room = room;
        this.type = type;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getCampus() {
        return building;
    }

    public String getBuilding() {
        return building;
    }

    public String getFloor() {
        return floor;
    }

    public String getRoom() {
        return room;
    }

    public NodeType getType() {
        return type;
    }

    public NodeStatus getStatus() {
        return status;
    }

    public void setStatus(NodeStatus status) {
        this.status = status;
    }

    /**
     * Returns a string representation of the Node object.
     *
     * @return A string representing the Node object.
     */
    @Override
    public String toString() {
        return String.format(
                "Node[id=%s, campus=%s, building=%s, floor=%s, type=%s, status=%s]",
                id, campus, building, floor, type, status
        );
    }

    /**
     * Compares this node with another object for equality.
     *
     * @param o The object to compare with.
     * @return true if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return id.equals(node.id);
    }

    /**
     * Returns a short label for the node, including its building, floor, and room.
     *
     * @return A string representing the short label of the node.
     */
    public String shortLabel() {
        return String.format("%s, Floor %s, %s", building, floor, room);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}