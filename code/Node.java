
// class that represents the rooms on campus as nodes. The nodes will be connected on the graph
public class Node {
    private int room_id; // id number for the room
    private String room_name; // string name of room for easier use
    private boolean bathroom; // true if room is a bathroom
    private boolean elevator; // true if room is an elevator

    // Node Constructor: takes four parameters, one for each variable in Node
    public Main(int id, String name, boolean isBathroom, boolean isElevator) {
        room_id = id;
        room_name = name;
        bathroom = isBathroom;
        elevator = isElevator;
    }

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    public boolean isBathroom() {
        return bathroom;
    }

    public void setBathroom(boolean bathroom) {
        this.bathroom = bathroom;
    }

    public boolean isElevator() {
        return elevator;
    }

    public void setElevator(boolean elevator) {
        this.elevator = elevator;
    }
}