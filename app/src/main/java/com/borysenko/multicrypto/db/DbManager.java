package com.borysenko.multicrypto.db;

import com.borysenko.multicrypto.App;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 05/06/19
 * Time: 16:16
 */
public class DbManager {

    private AppDatabase db = App.getInstance().getDatabase();
    private CryptFileDao cryptFileDao = db.cryptFileDao();

    public void getAllFiles(final DataBaseCallBack databaseCallback) {

        Single<List<CryptFile>> single = cryptFileDao.getAllFiles();
        single.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<CryptFile>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(List<CryptFile> cryptFiles) {
                        databaseCallback.onUsersLoaded(cryptFiles);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    public void insertFile(final CryptFile cryptFile, DataBaseCallBack dataBaseCallBack) {

        Completable.fromAction(() ->
                cryptFileDao.insert(cryptFile)).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onComplete() {
                dataBaseCallBack.onUpdateRecyclerView();
            }

            @Override
            public void onError(Throwable e) {
            }
        });
    }

    public void updateFile(final CryptFile cryptFile, DataBaseCallBack dataBaseCallBack) {

        Completable.fromAction(() ->
                cryptFileDao.update(cryptFile)).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onComplete() {
                dataBaseCallBack.onUpdateRecyclerView();
            }

            @Override
            public void onError(Throwable e) {
            }
        });
    }

    public void deleteFile(final CryptFile cryptFile, DataBaseCallBack dataBaseCallBack) {

        Completable.fromAction(() ->
                cryptFileDao.delete(cryptFile)).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onComplete() {
                dataBaseCallBack.onUpdateRecyclerView();
            }

            @Override
            public void onError(Throwable e) {
            }
        });
    }
}