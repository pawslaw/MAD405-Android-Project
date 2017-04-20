package net.portalcode.mad405_android_project;

/**
 * Created by James Pierce on 3/24/2017.
 */

public class User {

    // Declare variables
    private int id;
    private String name;
    private int avatar;
    private int permissions;
    private String email;
    private String password;

    // Constructors
    public User() {

    }

    public User(String name, int avatar) {
        this.name = name;
        this.avatar = avatar;
    }

    public User( String name, int avatar, int permissions) {
        this.name = name;
        this.avatar = avatar;
        this.permissions = permissions;
    }

    public User(int id, String name, int avatar, int permissions) {
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

    public int getPermissions() {
        return permissions;
    }

    public void setPermissions(int permissions) {
        this.permissions = permissions;
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", avatar=" + avatar +
                ", permissions=" + permissions +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
