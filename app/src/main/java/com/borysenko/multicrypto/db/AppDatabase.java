package com.borysenko.multicrypto.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 05/06/19
 * Time: 16:02
 */
@Database(entities = {CryptFile.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CryptFileDao cryptFileDao();
}