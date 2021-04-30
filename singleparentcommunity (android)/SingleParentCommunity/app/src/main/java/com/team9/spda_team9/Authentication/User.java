package com.team9.spda_team9.Authentication;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {
    private String fullName;
    private String username;
    private String email;
    private String password;
    private String selfDescription;
    private String interest;
    private String profession;
    private int numberOfKids;
    private String kidsDescription;
    private String location;
    private List<String> friends;
    private Gender gender;
    private boolean suspended = false;
    public String token;
    public String regDate;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSelfDescription() {
        return selfDescription;
    }

    public void setSelfDescription(String selfDescription) {
        this.selfDescription = selfDescription;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public int getNumberOfKids() {
        return numberOfKids;
    }

    public void setNumberOfKids(int numberOfKids) {
        this.numberOfKids = numberOfKids;
    }

    public String getKidsDescription() {
        return kidsDescription;
    }

    public void setKidsDescription(String kidsDescription) {
        this.kidsDescription = kidsDescription;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public boolean isSuspended() {
        return suspended;
    }

    public void setSuspended(boolean suspended) {
        this.suspended = suspended;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "fullName='" + fullName + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", selfDescription='" + selfDescription + '\'' +
                ", interest='" + interest + '\'' +
                ", profession='" + profession + '\'' +
                ", numberOfKids=" + numberOfKids +
                ", kidsDescription='" + kidsDescription + '\'' +
                ", location='" + location + '\'' +
                ", friends=" + friends +
                ", gender=" + gender +
                ", suspended=" + suspended +
                ", token='" + token + '\'' +
                '}';
    }
}
