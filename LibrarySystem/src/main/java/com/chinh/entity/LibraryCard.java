package com.chinh.entity;

import jakarta.persistence.*;

@Entity
@Table(name="tbl_library_card")
public class LibraryCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String issuedDate;
    private boolean isActive;

    @OneToOne(mappedBy = "card")
    private User user;

    public LibraryCard() {};
    public LibraryCard(int id, String issuedDate, boolean isActive) {
        this.id = id;
        this.issuedDate = issuedDate;
        this.isActive = isActive;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(String issuedDate) {
        this.issuedDate = issuedDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
