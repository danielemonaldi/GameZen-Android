package com.example.gamezen.classes;

public class Products {

    private int code;
    private String name;
    private int price;
    private int available;
    private String image;
    private int quantity;

    public Products(int code, String name, int price, int available, String image) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.available = available;
        this.image = image;
    }

    public Products(int code, String name, int price, String image, int quantity) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.image = image;
        this.quantity = quantity;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public float getPrice() {
        return price;
    }

    public int getAvailable() {
        return available;
    }

    public String getImage() {
        return image;
    }

    public int getQuantity() { return quantity; }
}
