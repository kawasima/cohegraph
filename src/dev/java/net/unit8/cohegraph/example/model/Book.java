package net.unit8.cohegraph.example.model;

import net.unit8.cohegraph.JoinCache;

import java.io.Serializable;

public class Book implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Long id;
    private final String name;
    private final int pageCount;
    private final Long authorId;
    private final Long publisherId;

    @JoinCache(name = "authors", idProperty = "authorId")
    private volatile Author author;

    @JoinCache(name = "publishers", idProperty = "publisherId")
    private volatile Publisher publisher;

    public Book(Long id, String name, int pageCount, Long authorId, Long publisherId) {
        this.id = id;
        this.name = name;
        this.pageCount = pageCount;
        this.authorId = authorId;
        this.publisherId = publisherId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPageCount() {
        return pageCount;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Long getPublisherId() {
        return publisherId;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pageCount=" + pageCount +
                ", authorId=" + authorId +
                ", publisherId=" + publisherId +
                ", author=" + author +
                ", publisher=" + publisher +
                '}';
    }
}
