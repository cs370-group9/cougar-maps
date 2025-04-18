package model;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public NodeType getType() {
        return type;
    }

    public void setType(NodeType type) {
        this.type = type;
    }

    public NodeStatus getStatus() {
        return status;
    }

    public void setStatus(NodeStatus status) {
        this.status = status;
    }


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
