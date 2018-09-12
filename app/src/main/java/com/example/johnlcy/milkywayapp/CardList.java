package com.example.johnlcy.milkywayapp;

/**
 * Created by johnlcy on 6/26/2017.
 */

public class CardList {

    private String name;
    //private String desc;
    private String imageURL;

    public CardList(String name, String imageURL) {
        this.name = name;
        //this.desc = desc;
        this.imageURL = imageURL;
    }

    public String getName() {
        return name;
    }

    /*public String getDesc() {
        return desc;
    }*/

    public String getImageURL() {
        return imageURL;
    }

}
