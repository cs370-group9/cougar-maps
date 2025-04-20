package com.cougarmaps.model.graph;

import com.cougarmaps.model.NodeStatus;
import com.cougarmaps.model.NodeType;

public class Node {
    private int id;
    private String campus;
    private String building;
    private String floor;
    private String name;
    private NodeType type;
    private NodeStatus status;

    public Node(int id, String campus, String building, String floor, String name, NodeType type, NodeStatus status) {
        this.id = id;
        this.campus = campus;
        this.building = building;
        this.floor = floor;
        this.name = name;
        this.type = type;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getCampus() {
        return campus;
    }

    public String getBuilding() {
        return building;
    }

    public String getFloor() {
        return floor;
    }

    public String getName() {
        return name;
    }

    public NodeType getType() {
        return type;
    }

    public NodeStatus getStatus() {
        return status;
    }

    public void setId(int id) { this.id = id; }
    public void setCampus(String campus) { this.campus = campus; }
    public void setBuilding(String building) { this.building = building; }
    public void setFloor(String floor) { this.floor = floor; }
    public void setName(String name) { this.name = name; }
    public void setType(NodeType type) { this.type = type; }
    public void setStatus(NodeStatus status) { this.status = status; }

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", campus='" + campus + '\'' +
                ", building='" + building + '\'' +
                ", floor='" + floor + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", status=" + status +
                '}';
    }
}
