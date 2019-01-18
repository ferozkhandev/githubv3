package com.githubv3api.meesn.githubv3api.Adapter;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.githubv3api.meesn.githubv3api.R;
import com.githubv3api.meesn.githubv3api.model.File;

import java.util.ArrayList;
import java.util.List;

public class FilesRecyclerAdapter extends RecyclerView.Adapter<FilesRecyclerAdapter.FilesRecyclerHolder> {

    private List<File> listFiles = new ArrayList<File>();

    @NonNull
    @Override
    public FilesRecyclerAdapter.FilesRecyclerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.files_list_layout, viewGroup, false);
        return new FilesRecyclerAdapter.FilesRecyclerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FilesRecyclerAdapter.FilesRecyclerHolder filesRecyclerHolder, int i) {
        final File file = listFiles.get(i);
        Log.d("checktypes", file.getType()+":"+file.getName());
        filesRecyclerHolder.name.setText(file.getName());
        float size = file.getSize()/1024;
        filesRecyclerHolder.size.setText(String.valueOf(size)+ "KB");
        if (file.getType() != null && file.getType().equals("file")) {
            filesRecyclerHolder.icon.setImageResource(R.drawable.ic_insert_drive_file_black_24dp);
        } else if (file.getType() != null && file.getType().equals("dir")) {
            filesRecyclerHolder.size.setText("");
            filesRecyclerHolder.icon.setImageResource(R.drawable.ic_folder_black_24dp);
        } else if (file.getType() != null) {
            filesRecyclerHolder.icon.setImageResource(R.drawable.logo);
        }
        else
        {
            filesRecyclerHolder.icon.setImageResource(R.drawable.logo);
        }
        filesRecyclerHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = file.getDownload_url();
                if (url!=null)
                {
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    v.getContext().startActivity(intent);
                }
                else
                {
                    Snackbar snackbar = Snackbar
                            .make(v, "Folder cannot be downloaded!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listFiles.size();
    }

    public void setFiles(List<File> listFiles) {
        this.listFiles = listFiles;
        notifyDataSetChanged();
    }

    public class FilesRecyclerHolder extends RecyclerView.ViewHolder {
        private ImageView icon;
        private TextView name, size;

        public FilesRecyclerHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.fileIcon);
            name = itemView.findViewById(R.id.fileName);
            size = itemView.findViewById(R.id.fileSize);
        }

        public void setIcon(int resid) {
            icon.setImageResource(resid);
        }
    }
}
