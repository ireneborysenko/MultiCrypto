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

import static com.borysenko.multicrypto.tools.Constants.DECRYPTED_FILE;
import static com.borysenko.multicrypto.tools.Constants.ENCRYPTED_FILE;


/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 05/06/19
 * Time: 13:28
 */
public class MainRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static ClickListener clickListener;
    private static List<CryptFile> mListOfFiles;

    static class EncryptedViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.file_title) TextView mTitle;
        @BindView(R.id.users_list) TextView mUsersList;
        @BindView(R.id.decrypt_button) Button mDecryptButton;

        EncryptedViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

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

        switch (viewType) {
            case DECRYPTED_FILE:
                itemView = inflater.inflate(R.layout.item_main_recycler_decrypted, parent, false);
                return new DecryptedViewHolder(itemView);
            case ENCRYPTED_FILE:
                itemView = inflater.inflate(R.layout.item_main_recycler_encrypted, parent, false);
                return new EncryptedViewHolder(itemView);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        switch (getItemViewType(i)) {
            case DECRYPTED_FILE:
                fillDecryptedList(viewHolder, i);
                break;
            case ENCRYPTED_FILE:
                fillEncryptedList(viewHolder, i);
                break;
        }
    }

    public MainRecyclerAdapter(List<CryptFile> listOfFiles) {
        mListOfFiles = listOfFiles;
    }

    private void fillEncryptedList(RecyclerView.ViewHolder viewHolder, int i) {

        EncryptedViewHolder encryptedViewHolder = (EncryptedViewHolder) viewHolder;

        CryptFile file = mListOfFiles.get(i);
        assert file != null;

        encryptedViewHolder.mTitle.setText(file.getFileName());
        encryptedViewHolder.mDecryptButton.setOnClickListener(view -> clickListener.onItemClick(file));
    }

    private void fillDecryptedList(RecyclerView.ViewHolder viewHolder, int i) {

        DecryptedViewHolder decryptedViewHolder = (DecryptedViewHolder) viewHolder;

        CryptFile file = mListOfFiles.get(i);
        assert file != null;

        decryptedViewHolder.mTitle.setText(file.getFileName());
        decryptedViewHolder.mEncryptButton.setOnClickListener(view -> clickListener.onItemClick(file));
    }

    @Override
    public int getItemViewType(int position) {
        return mListOfFiles.get(position).getEncryptionType();
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
