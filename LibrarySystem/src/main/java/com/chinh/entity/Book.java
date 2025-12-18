package com.chinh.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Cache; // Nhớ chọn cho đúng package nhé chính ngu
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tbl_book")
@Cacheable // 1. Báo cho JPA biết class này có thể cache
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY) // 2. Chiến lược Cache
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Để DB tự tăng ID
    private int id;

    @Column(name = "book_title")
    private String title;

    private String author;
    private double basePrice;

    @Transient
    private double discountPrice;

    @ManyToMany(mappedBy = "borrowedBooks") // Quan trọng: Không tạo thêm bảng phụ nữa [cite: 339, 345]
    private List<User> borrowers = new ArrayList<>();

    public Book(){};
    public Book(int id, String title, String author, double basePrice)
    {
        this.id = id;
        this.title = title;
        this.author = author;
        this.basePrice = basePrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public List<User> getBorrowers() { return borrowers; }
    public void setBorrowers(List<User> borrowers) { this.borrowers = borrowers; }

    @Override
    public String toString()
    {
        return String.format("Book{id=%d, title=%s, author=%s, basePrice=%.0f}",
                id, title, author, basePrice);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return id == book.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
