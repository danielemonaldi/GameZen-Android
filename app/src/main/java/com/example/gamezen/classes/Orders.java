package com.example.gamezen.classes;

public class Orders {

    private int id;
    private String date;
    private String delivery;
    private int stateid;
    private String state;
    private String total;

    public Orders(int id, String date, String delivery, int stateid, String state, String total) {
        this.id = id;
        this.date = date;
        this.delivery = delivery;
        this.stateid = stateid;
        this.state = state;
        this.total = total;
    }

    public int getId() {
        return id;
    }

    public String getDate() { return date; }

    public String getDelivery() {
        return delivery;
    }

    public String getState() {
        return state;
    }

    public int getStateid() { return stateid; }

    public String getTotal() { return total; }
}
