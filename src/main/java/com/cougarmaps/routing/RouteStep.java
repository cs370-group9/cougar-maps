package com.cougarmaps.routing;

import com.cougarmaps.model.graph.Node;
import com.cougarmaps.model.graph.enums.NodeType;

import java.util.Objects;

/**
 * Represents a step in a route, including the start and end nodes,
 * the distance, the unit of measurement (e.g., feet or floors),
 * and an optional human-readable instruction.
 */
public class RouteStep {
    private final Node from;
    private final Node to;
    private final double distance;
    private final String unit;
    private final String instruction;

    public RouteStep(Node from, Node to, double distance, String unit) {
        this(from, to, distance, unit, null);
    }

    public RouteStep(Node from, Node to, double distance, String unit, String instruction) {
        this.from = from;
        this.to = to;
        this.distance = distance;
        this.unit = unit;
        this.instruction = instruction;
    }

    public Node getFrom() { return from; }
    public Node getTo() { return to; }
    public double getDistance() { return distance; }
    public String getUnit() { return unit; }
    public String getInstruction() { return instruction; }

    /**
     * Returns a string representation of the route step.
     * The format includes the prefix, building, room, distance, unit,
     * and an optional instruction.
     *
     * @return A formatted string representing the route step.
     */
    @Override
    public String toString() {
        String arrow = "Walk";

        try {
            if (Objects.equals(from.getBuilding(), to.getBuilding())) {
                int fromFloor = Integer.parseInt(from.getFloor());
                int toFloor = Integer.parseInt(to.getFloor());


                if (toFloor > fromFloor) {
                    arrow = "Up️";
                } else if (toFloor < fromFloor) {
                    arrow = "Down️";
                }
            }
        } catch (NumberFormatException e) {
            // Handle the case where floor is not a number
            arrow = "Error";
        }

        if (from.getType() == NodeType.DOOR) {
            arrow = "and Walk";
        }

        if (to.getType() == NodeType.DOOR) {
            arrow = "Walk to";
        }

        String prefix;

        if (from.getType() == NodeType.ELEVATOR && to.getType() == NodeType.ELEVATOR)
            prefix = "Take";
        else if (from.getType() == NodeType.ELEVATOR && to.getType() != NodeType.ELEVATOR)
            prefix = "Exit";
        else if (from.getType() != NodeType.ELEVATOR && to.getType() == NodeType.ELEVATOR)
            prefix = "Enter";
        else if (from.getType() == NodeType.DOOR ^ to.getType() == NodeType.DOOR)
            prefix = "Open";
        else
            prefix = "From";

        return String.format(
                "%s [%s] %s %s %d %s To [%s] %s %s",
                prefix,
                from.getBuilding(),
                from.getRoom(),
                arrow,
                Math.round(distance), unit,
                to.getBuilding(),
                to.getRoom(),
                instruction != null ? " | " + instruction : ""
        );
    }
}