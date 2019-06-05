package com.borysenko.multicrypto.db;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 05/06/19
 * Time: 16:04
 */
@Entity(indices={@Index(value="fileName", unique=true)})
public class CryptFile {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String fileName;
    private Boolean isEncrypted;
//    private ArrayList<String> usersArray;
//    private ArrayList<String> historyDates;

    public CryptFile(String fileName) {
        this.fileName = fileName;
        this.isEncrypted = false;
    }

    public int getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public Boolean getEncrypted() {
        return isEncrypted;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setEncrypted(Boolean encrypted) {
        isEncrypted = encrypted;
    }
}
