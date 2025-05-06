package com.cougarmaps.model.location;

import com.cougarmaps.model.graph.Node;

/**
 * Represents a room within a floor of a building.
 * Each room is associated with a node in the navigation graph.
 */
public class Room {
    private final String name;
    private final Node node;

    /**
     * Constructs a new Room with the given name and associated graph node.
     *
     * @param name The name or number of the room.
     * @param node The associated Node representing this room in the graph.
     */
    public Room(String name, Node node) {
        this.name = name;
        this.node = node;
    }

    /**
     * Gets the name of the room.
     *
     * @return The room name or number.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the node associated with this room.
     *
     * @return The Node object.
     */
    public Node getNode() {
        return node;
    }
}
