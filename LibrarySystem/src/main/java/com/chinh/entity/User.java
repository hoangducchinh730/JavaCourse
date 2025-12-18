package com.chinh.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tbl_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @Embedded
    private Address address;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "card_id")
    private LibraryCard card;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_book_borrowed",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private List<Book> borrowedBooks = new ArrayList<>();

    public User() {};
    public User(String name, Address address)
    {
        this.name = name;
        this.address = address;
    }

    public void borrowBook(Book book)
    {
        this.borrowedBooks.add(book);
        book.getBorrowers();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public LibraryCard getCard() {
        return card;
    }

    public void setCard(LibraryCard card) {
        this.card = card;
    }

    public List<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    public void setBorrowedBooks(List<Book> borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
