package com.example.planetze;

public class ListData {

    int images;
    String description;
    String location;

    public ListData(int img, String des, String loc){
        this.images = img;
        this.description = des;
        this.location = loc;
    }

    public int getImages() {
        return images;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }
}
