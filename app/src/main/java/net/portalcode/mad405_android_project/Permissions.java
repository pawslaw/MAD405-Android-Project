package net.portalcode.mad405_android_project;

/**
 * Created by jamespierce on 2017-04-09.
 */

public class Permissions {

    // These properties are int instead of boolean to simplify the transistion between server(s) and the app
    private int id;
    private int canEdit;
    private int canRead;
    private int canWrite;


    // Constructors
    public Permissions() {
        this.setCanEdit(0);
        this.setCanRead(1);
        this.setCanWrite(1);
    }

    public Permissions(int canEdit, int canRead, int canWrite) {
        this.canEdit = canEdit;
        this.canRead = canRead;
        this.canWrite = canWrite;
    }

    public Permissions(int id, int canEdit, int canRead, int canWrite) {
        this.id = id;
        this.canEdit = canEdit;
        this.canRead = canRead;
        this.canWrite = canWrite;
    }


    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCanEdit() {
        return canEdit;
    }

    public void setCanEdit(int canEdit) {
        this.canEdit = canEdit;
    }

    public int getCanRead() {
        return canRead;
    }

    public void setCanRead(int canRead) {
        this.canRead = canRead;
    }

    public int getCanWrite() {
        return canWrite;
    }

    public void setCanWrite(int canWrite) {
        this.canWrite = canWrite;
    }
}
