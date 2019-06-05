package com.borysenko.multicrypto.db;

import java.util.List;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 05/06/19
 * Time: 16:16
 */
public interface DataBaseCallBack {

    void onUsersLoaded(List<CryptFile> cryptFiles);

    void onUpdateRecyclerView();

}
