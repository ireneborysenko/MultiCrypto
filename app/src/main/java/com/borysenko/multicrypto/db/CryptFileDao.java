package com.borysenko.multicrypto.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 05/06/19
 * Time: 16:13
 */
@Dao
public interface CryptFileDao {

    @Query("SELECT * FROM cryptfile")
    Single<List<CryptFile>> getAllFiles();

    @Insert
    void insert(CryptFile file);
    @Update
    void update(CryptFile file);

    @Delete
    void delete(CryptFile file);

}
