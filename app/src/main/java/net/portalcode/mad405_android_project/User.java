package net.portalcode.mad405_android_project;

import java.security.Permissions;

/**
 * Created by James Pierce on 3/24/2017.
 */

public class User {

    // Declare variables
    private int id;
    private String name;
    private int avatar;
    private Permissions permissions;

    // Constructors
    public User() {

    }

    public User(String name, int avatar) {
        this.name = name;
        this.avatar = avatar;
    }

    public User(int id, String name, int avatar, Permissions permissions) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.permissions = permissions;
    }

    // Getters and Setters
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

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    public Permissions getPermissions() {
        return permissions;
    }

    public void setPermissions(Permissions permissions) {
        this.permissions = permissions;
    }
}
