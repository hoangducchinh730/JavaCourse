package com.chinh.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public class Address {
    private String street;
    private String zipcode;
    private String city;

    public Address() {};
    public Address(String street, String city, String zipcode)
    {
        this.street = street;
        this.city = city;
        this.zipcode = zipcode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
