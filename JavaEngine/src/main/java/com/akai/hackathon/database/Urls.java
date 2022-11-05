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

    @Column(name = "occurrences")
    private int occurrences;

    public Urls() {
    }

    public Urls(String url, int rating, int occurrences) {
        this.url = url;
        this.rating = rating;
        this.occurrences=occurrences;
    }

    public String getUrl() {
        return url;
    }

    public int getRating() {
        return rating;
    }

    public int getOccurrences() {
        return occurrences;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setOccurrences(int rating) {
        this.occurrences = occurrences;
    }
}
