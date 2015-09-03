package com.parse.starter;

/**
 * Created by stevensun on 9/3/15.
 */

import java.util.List;

public class CardMode {
    private String name;
    private int year;
    private List<String> images;
    private String user_id;

    public CardMode(String name, int year, List<String> images,String user_id) {
        this.name = name;
        this.year = year;
        this.images = images;
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public int getYear() {
        return year;
    }

    public String getUserId() {
        return user_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<String> getImages() {
        return images;
    }
}
