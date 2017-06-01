package com.github.robindevilliers.onlinebankingexample.domain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class PersonalDetails {
    private String name;
    private String nationality;
    private String domicile;
    private String address;
    private String mobile;
    private String email;

    public PersonalDetails(){

    }

    public PersonalDetails(PersonalDetails personalDetails) {
        this.name = personalDetails.name;
        this.nationality = personalDetails.nationality;
        this.domicile = personalDetails.domicile;
        this.address = personalDetails.address;
        this.mobile = personalDetails.mobile;
        this.email = personalDetails.email;
    }

    public String getName() {
        return name;
    }

    public PersonalDetails setName(String name) {
        this.name = name;
        return this;
    }

    public String getNationality() {
        return nationality;
    }

    public PersonalDetails setNationality(String nationality) {
        this.nationality = nationality;
        return this;
    }

    public String getDomicile() {
        return domicile;
    }

    public PersonalDetails setDomicile(String domicile) {
        this.domicile = domicile;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public PersonalDetails setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getMobile() {
        return mobile;
    }

    public PersonalDetails setMobile(String mobile) {
        this.mobile = mobile;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public PersonalDetails setEmail(String email) {
        this.email = email;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        PersonalDetails that = (PersonalDetails) o;

        return new EqualsBuilder()
                .append(name, that.name)
                .append(nationality, that.nationality)
                .append(domicile, that.domicile)
                .append(address, that.address)
                .append(mobile, that.mobile)
                .append(email, that.email)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(name)
                .append(nationality)
                .append(domicile)
                .append(address)
                .append(mobile)
                .append(email)
                .toHashCode();
    }
}
