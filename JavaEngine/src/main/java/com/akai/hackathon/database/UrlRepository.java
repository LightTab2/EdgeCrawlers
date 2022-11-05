package com.akai.hackathon.database;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlRepository extends JpaRepository<Urls, String> {
}
