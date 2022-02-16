package com.example.tracing;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Cacheable
public class Item extends PanacheEntity {
    @Column(length = 40, unique = true)
    public String name;

    public Item() {

    }

    public Item(String name) {
        this.name = name;
    }
}
