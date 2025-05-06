package com.cougarmaps.model.location;

import java.util.*;

/**
 * Represents a building in the campus map.
 * A building contains multiple floors, each of which may contain rooms (nodes).
 */
public class Building {
    private final String name;
    private final Map<String, Floor> floors = new HashMap<>();

    /**
     * Constructs a new Building with the given name.
     *
     * @param name The name of the building.
     */
    public Building(String name) {
        this.name = name;
    }

    /**
     * Retrieves a floor by its label, or creates it if it doesn't exist.
     *
     * @param floorLabel The label of the floor
     * @return The Floor object corresponding to the label.
     */
    public Floor getOrCreateFloor(String floorLabel) {
        return floors.computeIfAbsent(floorLabel, Floor::new);
    }

    /**
     * Returns all floors in this building.
     *
     * @return A collection of floors.
     */
    public Collection<Floor> getFloors() {
        return floors.values();
    }

    /**
     * Gets the name of the building.
     *
     * @return The building name.
     */
    public String getName() {
        return name;
    }
}
