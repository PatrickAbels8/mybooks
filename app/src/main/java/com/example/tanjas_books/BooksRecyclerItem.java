package com.example.tanjas_books;

public class BooksRecyclerItem {
    private Integer id;
    private String author;
    private String title;
    private String isbn;
    private Boolean done;
    private Boolean borrowed;

    @Override
    public String toString() {
        return "BooksRecyclerItem{" +
                "id=" + id +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", isbn='" + isbn + '\'' +
                ", done=" + done +
                ", borrowed=" + borrowed +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public Boolean getBorrowed() {
        return borrowed;
    }

    public void setBorrowed(Boolean borrowed) {
        this.borrowed = borrowed;
    }

    public BooksRecyclerItem(Integer id, String author, String title, String isbn, Boolean done, Boolean borrowed) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.isbn = isbn;
        this.done = done;
        this.borrowed = borrowed;
    }
}
