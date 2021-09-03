package com.example.gamezen.classes;

import java.io.Serializable;

public class Addresses implements Serializable {

    private int id;
    private String address;
    private int civic;
    private String city;
    private String cap;
    private String province;
    private String phone;

    public Addresses(int id, String address, int civic, String city, String cap, String province, String phone) {
        this.id = id;
        this.address = address;
        this.civic = civic;
        this.city = city;
        this.cap = cap;
        this.province = province;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public int getCivic() {
        return civic;
    }

    public String getCity() {
        return city;
    }

    public String getCap() {
        return cap;
    }

    public String getProvince() {
        return province;
    }

    public String getPhone() {
        return phone;
    }

    private boolean isSelected = false;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean checked) {
        isSelected = checked;
    }
}
