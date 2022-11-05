package com.akai.hackathon.database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "specialists_rates")
public class SpecialistsRate {

    @Id
    @Column(name="username")
    private String username;

    @Column(name="url")
    private String url;

    @Column(name="rate_positive")
    private boolean ratePositive;

    public SpecialistsRate(){}

    public SpecialistsRate(String username, String url, boolean rate_positive) {
        this.username = username;
        this.url = url;
        this.ratePositive = rate_positive;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setRatePositive(boolean rate_positive) {
        this.ratePositive = rate_positive;
    }

    public String getUsername() {
        return username;
    }

    public String getUrl() {
        return url;
    }

    public boolean getRatePositive() {
        return ratePositive;
    }

}
