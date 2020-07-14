package net.unit8.cohegraph.example.model;

import java.io.Serializable;

public class Publisher implements Serializable {
    private final Long id;
    private final String name;

    public Publisher(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Publisher{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
