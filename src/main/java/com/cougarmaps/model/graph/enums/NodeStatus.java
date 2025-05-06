package com.cougarmaps.model.graph.enums;

/**
 * Enum representing the status of a node in the graph.
 * This is used to determine if a node can be used in pathfinding or not.
 */
public enum NodeStatus {
    ACTIVE,   // The node is active and can be used in pathfinding
    INACTIVE, // The node is inactive and cannot be used in pathfinding
    RETIRED   // The node is retired and should not be used in pathfinding
}