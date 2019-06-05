package com.borysenko.multicrypto.db;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static com.borysenko.multicrypto.tools.Constants.DECRYPTED_FILE;

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
    private int encryptionType;

//    private ArrayList<String> usersArray;
//    private ArrayList<String> historyDates;

    public CryptFile(String fileName) {
        this.fileName = fileName;
        this.encryptionType = DECRYPTED_FILE;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getEncryptionType() {
        return encryptionType;
    }

    public void setEncryptionType(int encryptionType) {
        this.encryptionType = encryptionType;
    }
}
