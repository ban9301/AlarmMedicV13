package com.example.leunglokyin03a;

public class photo {
    private int _id;
    private byte[] image;
    private String name;

    public photo(int _id, String name, byte[] image){
        this._id = _id;
        this.name = name;
        this.image= image;
    }
    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
