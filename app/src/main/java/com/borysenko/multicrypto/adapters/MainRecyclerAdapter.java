package com.borysenko.multicrypto.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.borysenko.multicrypto.R;
import com.borysenko.multicrypto.db.CryptFile;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 05/06/19
 * Time: 13:28
 */
public class MainRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static ClickListener clickListener;
    private static List<CryptFile> mListOfFiles;

//    static class EncryptedViewHolder extends RecyclerView.ViewHolder {
//
//        @BindView(R.id.file_title) TextView mTitle;
//        @BindView(R.id.decrypt_button) Button mDecryptButton;
//
//        EncryptedViewHolder(View v) {
//            super(v);
//            ButterKnife.bind(this, v);
//        }
//    }

    static class DecryptedViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.file_title) TextView mTitle;
        @BindView(R.id.encrypt_button) Button mEncryptButton;

        DecryptedViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        itemView = inflater.inflate(R.layout.item_main_recycler_decrypted, parent, false);
        return new DecryptedViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        fillDecryptedList(viewHolder, i);
    }

    public MainRecyclerAdapter(List<CryptFile> listOfFiles) {
        mListOfFiles = listOfFiles;
    }

//    private void fillEncryptedList(RecyclerView.ViewHolder viewHolder, int i) {
//
//        EncryptedViewHolder encryptedViewHolder = (EncryptedViewHolder) viewHolder;
//
//        String files = mListOfFiles.get(i);
//        assert files != null;
//
//        encryptedViewHolder.mTitle.setText(files);
//    }

    private void fillDecryptedList(RecyclerView.ViewHolder viewHolder, int i) {

        DecryptedViewHolder decryptedViewHolder = (DecryptedViewHolder) viewHolder;

        CryptFile file = mListOfFiles.get(i);
        assert file != null;

        decryptedViewHolder.mTitle.setText(file.getFileName());
        decryptedViewHolder.mEncryptButton.setOnClickListener(view -> clickListener.onItemClick(file));
    }

    @Override
    public int getItemCount() {
        return mListOfFiles.size();
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        MainRecyclerAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(CryptFile files);
    }
}
