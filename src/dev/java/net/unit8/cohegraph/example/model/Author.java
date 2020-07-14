package net.unit8.cohegraph.example.model;

import net.unit8.cohegraph.JoinCache;

import java.io.Serializable;
import java.util.List;

public class Author implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Long id;
    private final String firstName;
    private final String lastName;
    private final List<Long> bookIds;

    @JoinCache(name = "books", idProperty = "bookIds")
    private volatile List<Book> books;

    public Author(Long id, String firstName, String lastName, List<Long> bookIds) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.bookIds = bookIds;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public List<Long> getBookIds() {
        return bookIds;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    @Override
    public String toString() {
        return "Author{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
