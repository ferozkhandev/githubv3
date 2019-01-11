package com.githubv3api.meesn.githubv3api.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.githubv3api.meesn.githubv3api.R;
import com.githubv3api.meesn.githubv3api.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserRecyclerAdapter extends RecyclerView.Adapter<UserRecyclerAdapter.UserRecyclerHolder>  {


    private List<User> listUsers = new ArrayList<User>();
    @NonNull
    @Override
    public UserRecyclerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.repo_name, viewGroup, false);
        return new UserRecyclerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserRecyclerHolder userRecyclerHolder, int i) {
        User user = listUsers.get(i);
        Log.d("UserList", user.getName());
        userRecyclerHolder.name.setText(user.getName());
    }

    @Override
    public int getItemCount() {
        return listUsers.size();
    }

    public void setUsers(List<User> listUsers)
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
