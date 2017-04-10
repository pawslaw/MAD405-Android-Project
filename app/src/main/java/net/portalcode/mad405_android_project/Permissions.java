package net.portalcode.mad405_android_project;

/**
 * Created by jamespierce on 2017-04-09.
 */

public class Permissions {

    private boolean canEdit;
    private boolean canRead;
    private boolean canWrite;

    public Permissions() {
        this.setCanEdit(false);
        this.setCanRead(true);
        this.setCanWrite(true);
    }

    public boolean isCanRead() {
        return canRead;
    }

    public void setCanRead(boolean canRead) {
        this.canRead = canRead;
    }

    public boolean isCanWrite() {
        return canWrite;
    }

    public void setCanWrite(boolean canWrite) {
        this.canWrite = canWrite;
    }

    public boolean isCanEdit() {
        return canEdit;
    }

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }
}
