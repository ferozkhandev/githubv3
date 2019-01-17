package com.githubv3api.meesn.githubv3api.Adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.githubv3api.meesn.githubv3api.FileList;
import com.githubv3api.meesn.githubv3api.R;
import com.githubv3api.meesn.githubv3api.database.Repository;

import java.util.ArrayList;
import java.util.List;

public class OtherRecyclerAdapter extends RecyclerView.Adapter<OtherRecyclerAdapter.UserRecyclerHolder>  {


    private List<Repository> listUsers = new ArrayList<Repository>();
    @NonNull
    @Override
    public UserRecyclerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.repo_name, viewGroup, false);
        return new UserRecyclerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserRecyclerHolder userRecyclerHolder, int i) {
        final Repository repo = listUsers.get(i);
        if (repo.getName() != null)
        {
            Log.d("UserList", repo.getName());
            userRecyclerHolder.name.setText(repo.getName());
            userRecyclerHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), FileList.class);
                    intent.putExtra("userLoginName", repo.getUsername());
                    intent.putExtra("repoName", repo.getName());
                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listUsers.size();
    }

    public void setUsers(List<Repository> listUsers)
    {
        this.listUsers = listUsers;
        notifyDataSetChanged();
    }

    public class UserRecyclerHolder extends RecyclerView.ViewHolder
    {
        private TextView name;

        public UserRecyclerHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.repo);
        }
    }
}
