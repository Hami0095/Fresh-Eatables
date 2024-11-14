package com.hami.fresheatables;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "vendors")
public class Vendor {
    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo(name = "username")
    private String username;

    private String password;
    private String address;
    private String profilePicture;
    private String phone;

    public Vendor(String username, String password, String address, String profilePicture, String phone) {
        this.username = username;
        this.password = password;
        this.address = address;
        this.profilePicture = profilePicture;
        this.phone = phone;
    }

    // Getters and Setters for each field
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
