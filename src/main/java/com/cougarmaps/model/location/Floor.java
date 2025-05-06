package com.cougarmaps.model.location;

import java.util.*;
import com.cougarmaps.model.graph.Node;

/**
 * Represents a floor within a building.
 * A floor contains a list of rooms, which are linked to nodes in the graph.
 */
public class Floor {
    private final String label;
    private final List<Room> rooms = new ArrayList<>();

    /**
     * Constructs a new Floor with the given label.
     *
     * @param label The label of the floor
     */
    public Floor(String label) {
        this.label = label;
    }

    /**
     * Adds a room to this floor.
     *
     * @param room The room to add.
     */
    public void addRoom(Room room) {
        rooms.add(room);
    }

    /**
     * Returns the label of this floor.
     *
     * @return The floor label.
     */
    public String getLabel() {
        return label;
    }

    /**
     * Returns all rooms located on this floor.
     *
     * @return A list of rooms.
     */
    public List<Room> getRooms() {
        return rooms;
    }
}
