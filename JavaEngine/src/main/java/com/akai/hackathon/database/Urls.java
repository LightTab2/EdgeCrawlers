package com.akai.hackathon.database;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "urls")
public class Urls {
    @Id
    @Column(name = "url")
    private String url;

    @Column(name = "rating")
    private int rating;

    public Urls() {
    }

    public Urls(String url, int rating) {
        this.url = url;
        this.rating = rating;
    }

    public String getUrl() {
        return url;
    }

    public int getRating() {
        return rating;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
